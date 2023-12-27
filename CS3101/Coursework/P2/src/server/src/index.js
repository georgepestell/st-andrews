const express = require("express");
const morgan = require("morgan");
const cors = require("cors");
const db = require("./db/db");

require("dotenv").config();

/** Get settings from environmental variables */
const PORT = process.env.BACKEND_PORT || 8024;

/** Initialize the app and session store  */
const app = express();

// Allow JSON and URL parameters to be parsed from requests
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Log connection information
app.use(morgan("tiny"));

// Setup CORS connection restrictions
app.use(cors());

// Allow sessions to be created for organiser authentication
app.use(db.session);

/**
 * Endpoint allowing organisers to authenticate
 * using their person_ID and password
 */
app.post("/auth/organisers", async (req, res) => {
  // Get the login parameters from the request
  let organiser_id = req.body.organiser_id;
  let password = req.body.password;

  // Make sure an ID and password are given
  if (!organiser_id || !password) {
    return res.status(401).end();
  }

  // Login if ID and password are correct
  if (await db.validateOrganiser(organiser_id, password)) {
    req.session.isOrganiser = true;
    return res.status(200).send("OK");
  } else {
    return res.status(403).end();
  }
});

/**
 * Endpoint returning whether an organiser is authenticated
 */
app.get("/auth/organisers", async (req, res) => {
  if (req.session.isOrganiser) {
    return res.status(200).send("OK");
  } else {
    return res.status(401).end();
  }
});

/**
 * Endpoint returning guest information given a valid invitation code
 */
app.get("/guests", async (req, res) => {
  const invitation_code = req.query.invitation;

  if (!(await db.validateInvitation(invitation_code))) {
    return res.status(401).end();
  }

  try {
    // Get the list of guests associated with the invitation
    var guests = Array.from(await db.getGuestsByInvite(invitation_code));

    // Get the list of dietary requirements
    var dietary_requirements = await db.getDietaryRequirements();

    // Convert the dietary_requirements array to JSON
    guests.forEach((guest) => {
      guest.dietary_requirements = JSON.parse(guest.dietary_requirements);
    });

    // Return the guests and dietary requirements
    return res.status(200).send({
      guests: guests,
      dietary_requirements: dietary_requirements,
    });
  } catch (err) {
    console.error(err.message);
    return res.status(500).end();
  }
});

/** Endpoint allowing organisers to logout */
app.get("/auth/organisers/logout", async (req, res) => {
  req.session.isOrganiser = false;
  return res.status(200).send("OK");
});

/**
 * Endpoint allowing guests to update responses for users associated with their invitation code
 */
app.post("/guests/response", async (req, res) => {
  const invitation_code = req.body.invitation_code;
  const guest_id = req.body.guest_id;
  var response = req.body.response;

  // The invitation code must be given
  if (!invitation_code) {
    return res.status(401).end();
  }

  // The guest ID must be given
  if (!guest_id) {
    return res.status(401).end();
  }

  // Convert the response to the correct type
  if (response == true) {
    response = true;
  } else if (response == false) {
    response = false;
  } else if (response == "null") {
    response = null;
  } else {
    return res.status(400).end();
  }

  // Validate the guest's invitation code matches the provided one
  const guest = await db.getGuest(guest_id);
  if (!guest || guest.invitation_code != invitation_code) {
    return res.status(403).end();
  }

  // Update the guest response
  try {
    const updated = await db.updateResponse(guest_id, response);

    if (!updated) {
      return res.status(400).end();
    }

    return res.status(200).send("OK");
  } catch (err) {
    console.error(err.message);
    return res.status(500).end();
  }
});

/**
 * Endpoint allowing organisers to get information from all guests
 */
app.get("/organisers/guests", async (req, res) => {
  // Make sure the organiser is logged in
  if (!req.session.isOrganiser) {
    return res.status(403).end();
  }

  try {
    // Get the list of guests
    var guests = Array.from(await db.getGuests());

    // Get the list of dietary requirements
    const dietary_requirements = await db.getDietaryRequirements();

    // Get the list of tables
    const tables = await db.getTables();

    // Get the list of invitations
    const invitations = await db.getInvitations();

    // Convert the dietary_requirements array to JSON
    guests.forEach((guest) => {
      guest.dietary_requirements = JSON.parse(guest.dietary_requirements);
    });

    // Return the guests and dietary requirements
    return res.status(200).send({
      guests: guests,
      dietary_requirements: dietary_requirements,
      tables: tables,
      invitations: invitations,
    });
  } catch (err) {
    console.error(err.message);
    return res.status(500).end();
  }
});

/**
 * Endpoint allowing orgnanisers to delete people from the database
 */
app.post("/organisers/guests/delete", async (req, res) => {
  // Make sure the organiser is authorized
  if (!req.session.isOrganiser) {
    return res.status(403).end();
  }

  const guest_id = req.body.guest_id;

  // Make sure a guest_id is given
  if (!guest_id) {
    return res.status(400).end();
  }

  try {
    // Delete the guest
    const deleted = await db.deleteGuest(guest_id);

    // If there are no errors but the guest wasn't deleted, then the guest didn't exist
    if (!deleted) {
      return res.status(400).end();
    }

    return res.status(200).send("OK");
  } catch (err) {
    console.error(err.code);
    return res.status(500).end();
  }
});

/**
 * Endpoint allowing guests to update dietary requirements for guests on their invitation
 */
app.post("/guests/dietaryRequirements", async (req, res) => {
  const guest_id = req.body.guest_id;
  const dietary_requirement = req.body.dietary_requirement;
  const invitation_code = req.body.invitation_code;
  var value = req.body.value;

  // Make sure an invitation code is given for authentication
  if (!invitation_code) {
    return res.status(401).end();
  }

  // Make sure the value is either true or false
  if (!(value == true || value == false)) {
    return res.status(400).end();
  }

  // Make sure identifier values are given
  if (!guest_id || !dietary_requirement) {
    console.error("Identifiers not given");
    return res.status(400).end();
  }

  // Validate the guest's invitation code matches the provided one
  const guest = await db.getGuest(guest_id);
  if (guest.invitation_code != invitation_code) {
    return res.status(403).end();
  }

  // Validate the dietary requirement exists
  const diet_exists = await db.getDietaryRequirement(dietary_requirement);
  if (!diet_exists) {
    return res.status(400).end();
  }

  // Update the guest's dietary requirement
  try {
    var updated;
    if (value) {
      updated = await db.insertGuestDiet(guest_id, dietary_requirement);
    } else {
      updated = await db.deleteGuestDiet(guest_id, dietary_requirement);
    }

    if (!updated) {
      return res.status(400).end();
    }

    return res.status(200).send("OK");
  } catch (err) {
    console.error(err.message);
    return res.status(500).end();
  }
});

/**
 * Allowing organisers to add guests to the database
 */
app.post("/organisers/guests/add", async (req, res) => {
  // Make sure the organiser is authorized
  if (!req.session.isOrganiser) {
    return res.status(403).end();
  }

  const full_name = req.body.full_name;
  const notes = req.body.notes;
  const invitation_code = req.body.invitation_code;
  const table_no = req.body.table_no;

  // Try to add the guest to the database
  try {
    const added = await db.addGuest(
      full_name,
      notes,
      invitation_code,
      table_no
    );

    if (!added) {
      return res.status(400).json({ message: "Could not add guest" });
    }
    return res.status(200).send("OK");
  } catch (err) {
    if (err.sqlMessage) {
      return res.status(400).json({ message: err.sqlMessage });
    } else {
      return res.status(500).end();
    }
  }
});

/**
 * Endpoint allowing users to add new dietary requirements to the database
 */
app.post("/guests/dietaryRequirements/add", async (req, res) => {
  const short_name = req.body.short_name;
  const description = req.body.description;

  if (!short_name || !description) {
    return res.status(400).json({ message: "Must give name and description" });
  }

  try {
    await db.insertDietaryRequirement(short_name, description);
    return res.status(200).send("OK");
  } catch (err) {
    if (err.sqlMessage) {
      return res.status(400).json({ message: err.sqlMessage });
    } else {
      return res.status(500).end();
    }
  }
});

/**
 * Allow organisers to update the table number associated with a guest
 */
app.post("/organisers/guests/table", async (req, res) => {
  // Make sure the organiser is authorized
  if (!req.session.isOrganiser) {
    return res.status(403).end();
  }

  const guest_id = req.body.guest_id;
  const table_no = req.body.table_no;

  if (!guest_id || !table_no) {
    return req.status(422).end();
  }

  // Try to change the guest table
  try {
    const updated = await db.updateTableNo(guest_id, table_no);

    // If nothing was updated then the guest did not exist
    if (!updated) {
      return res.status(400).json({ message: "Guest does not exist" });
    }

    return res.status(200).send("OK");
  } catch (err) {
    console.log(err);
    if (err.sqlMessage) {
      return res.status(400).json({ message: err.sqlMessage });
    } else {
      console.error(err.message);
      return res.status(500).end();
    }
  }
});

/**
 * Serve the app on localhost:PORT
 */
app.listen(PORT, () => {
  console.log(`Listening at port ${PORT}`);
});
