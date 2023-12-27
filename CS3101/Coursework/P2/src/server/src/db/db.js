const session = require("express-session");
const KnexSessionStore = require("connect-session-knex")(session);

require("dotenv").config();

// Database settings
const DB_PORT = process.env.DB_PORT || 3306;
const DB_HOST = process.env.DB_HOST || "127.0.0.1";
const DB_NAME = process.env.DB_NAME || "cs3101_p2";
const DB_USER = process.env.DB_USER || "app_user";
const DB_PWD = process.env.DB_PWD || "test123";

// Session settings
const SESSION_SECRET = process.env.SESSION_SECRET || "TestSecret";

const knex = require("knex")({
  client: "mysql",
  connection: {
    host: DB_HOST,
    port: DB_PORT,
    user: DB_USER,
    password: DB_PWD,
    database: DB_NAME,
  },
});

const session_store = new KnexSessionStore({
  knex: knex,
  tablenmame: "sessions",
});

module.exports = {
  session: session({
    secret: SESSION_SECRET,
    resave: false,
    saveUninitialized: false,
    store: session_store,
  }),
  validateOrganiser: async function (organiser_id, password) {
    if (!organiser_id || !password) {
      return false;
    }

    const organiser_password = await knex("organiser")
      .where("person_id", organiser_id)
      .select("password")
      .first();

    // If organiser_id is not in the database, no password will exist
    if (!organiser_password || password !== organiser_password.password) {
      return false;
    }

    return true;
  },
  validateInvitation: async function (code) {
    const invitation = await knex("invitation")
      .where("code", code)
      .select()
      .first();

    if (!invitation) {
      return false;
    }

    return true;
  },
  getGuestsByInvite: async function (code) {
    return knex("person")
      .where("invitation_code", code)
      .select(
        "id",
        "response",
        knex.raw(
          "JSON_ARRAYAGG(dietary_requirement_name) as dietary_requirements"
        ),
        "invitation_code",
        "full_name"
      )
      .leftJoin("guest_diet", "person.id", "guest_diet.person_id")
      .groupBy("id");
  },
  updateResponse: async function (guest_id, response) {
    return knex("person").where("id", guest_id).update("response", response);
  },
  updateTableNo: async function (guest_id, table_no) {
    return knex("person").where("id", guest_id).update("table_no", table_no);
  },
  getGuests: async function () {
    return knex("person")
      .select(
        "id",
        "full_name",
        "response",
        "invitation_code",
        knex.raw(
          "JSON_ARRAYAGG(dietary_requirement_name) as dietary_requirements"
        ),
        "table_no",
        "invitation_code",
        "notes"
      )
      .leftJoin("guest_diet", "person.id", "guest_diet.person_id")
      .groupBy("id");
  },
  getGuest: async function (person_id) {
    return knex("person")
      .where("id", person_id)
      .select("invitation_code")
      .first();
  },
  addGuest: async function (full_name, notes, invitation_code, table_no) {
    return knex("person").insert({
      full_name: full_name,
      notes: notes,
      invitation_code: invitation_code,
      table_no: table_no,
    });
  },
  deleteGuest: async function (guest_id) {
    return knex("person").where("id", guest_id).delete();
  },
  getDietaryRequirements: async function () {
    return knex("dietary_requirement").select("*");
  },
  getTables: async function () {
    return knex("dinner_table").select("*");
  },
  getInvitations: async function () {
    return knex("invitation").select("*");
  },
  getDietaryRequirement: async function (short_name) {
    return knex("dietary_requirement")
      .where("short_name", short_name)
      .select("*");
  },
  insertGuestDiet: async function (guest_id, short_name) {
    return knex("guest_diet").insert({
      dietary_requirement_name: short_name,
      person_id: guest_id,
    });
  },
  deleteGuestDiet: async function (guest_id, short_name) {
    return knex("guest_diet")
      .where({
        person_id: guest_id,
        dietary_requirement_name: short_name,
      })
      .delete();
  },
  insertDietaryRequirement: async function (short_name, description) {
    return knex("dietary_requirement").insert({
      short_name: short_name,
      description: description,
    });
  },
};
