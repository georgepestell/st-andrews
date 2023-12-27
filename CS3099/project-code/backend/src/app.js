// Get environmental variables

require("dotenv").config();

const FPF_BACKEND_PORT = process.env.FPF_BACKEND_PORT || 8024;

/** 
 * Package imports 
*/
const BodyParser = require('body-parser')
const Morgan = require('morgan')
const express = require('express')
const session = require('express-session')
const fs = require('fs')
const jwt = require('jsonwebtoken')
const cors = require('cors');
const path = require("path");

/** 
 * Local imports 
*/
const users = require('./auth/users.js')
const puzzles = require('./api/puzzles.js')
const db = require('./db/db.js')
const federation = require("./federation/federation.json")

const PERMISSIONS = {
    Create: [
      users.ROLES.SETTER, 
      users.ROLES.ADMIN
    ],
    Play: [
      users.ROLES.SOLVER, 
      users.ROLES.SETTER, 
      users.ROLES.ADMIN
    ],
    UserManage: [
      users.ROLES.ADMIN
    ],
    PuzzleManage: [
      users.ROLES.ADMIN
    ]
}

/** 
 * Connect to the database
 */
console.log("Connecting: " + db.url)

db.client.connect()
.then(() => {
  console.log("Database connection successful")
})
.catch((err) => {
  console.log("Database connection failed")
  throw err
})

/**
 * Setup express 
 */

const app = express()

app.use(BodyParser.json())
app.use(Morgan('tiny'))
app.use(cors({
  origin: ["https://cs3099user24.host.cs.st-andrews.ac.uk", "127.0.0.1"]
}))

// Allow static files to be send
app.use("/", express.static("public"))

// Only encrypt session cookies for production
if (app.get('env') === 'production') {
  app.set('trust proxy', 1)
  db.session.cookie.secure = true
}
app.use(session(db.session))

/**
 * Allow federated sites to access our public key
 */
app.get('/publickey', cors({ origin: federation.origins }), async (req, res) => {
  res.redirect("/g24-public.pem").send()
})

/**
 * Allow users to register locally
 */
app.post('/auth/register', async (req, res) => {

  // Check input
  if (!req.body.email || !req.body.password || !req.body.displayName || !req.body.role) {
    res.status(422).send({
      message: "Invalid input"
    })
    return;
  }

  const role = req.body.role.toString().toUpperCase()

  await users.createUser(db.client, req.body.email, req.body.password, req.body.displayName, role)
  .then((user) => {
  
    // Check if the user was actually created
    if (!user) {    
      res.status(500).send({ 
        message: 'User failed to be created'
      })
      return

    } else {
      res.send({ 
        message: 'Account created. You may now proceed to log in with your new account.' 
      })
      return
    }
  })
  .catch((err) => {
    res.status(400).send({
      message: err.message
    })
  })
})

/**
 * Allows users registered with G24 to login
 * - req.body.redirect allows JWT creation for authorized federation sites
 */
app.post('/auth/login', async (req, res) => {

  const email = req.body.email || ""
  const password = req.body.password || ""
  const redirect = req.body.redirect || "/"

  // Get the user associated with the given email information (if exists)
  const user = await users.getUserByEmail(db.client, email)

  // Check the user's password is correct
  const validated = await users.validateUserPassword(db.client, user, password)

  if (!validated) {

    res.status(401).send({
      user: null,
      message: "Invalid username or password"
    });
    return;
  }

  // Check if the login is local or federated
  if (redirect === "/") {
    
    /**
     * Local Login 
     */

    // Log the user in the backend
    req.session.user = user
    req.session.login = true

    res.status(200).send({ 
      message: "Login Successfull",
      user: user,
    })
    
    return
  } else {

    /**
     * Federated Login
     */

    // Validate the redirect url
    var valid = federation.tokenUrls.includes(redirect);
    if (!valid) {
      res.status(400).send({
        user: null,
        message: "Invalid redirect url"
      })
      return
    }

    // Create JWT for federated login
    users.generateToken(user)
    .then(
      (token) => {
        res.status(200).send({
          token: token,
          user: null
        })
      },
      () => {
        res.status(401).send({
          user: null,
          message: "Failed to generate token"
        })
        return
      }
    )

    return
  }

})

// Allow logged in users to export puzzles
app.get("/export/*", async (req, res, next) => {
  const puzzleID = req.params[0];

  if (!puzzleID) {
    req.status(404).send("Error: Invalid Puzzle");
  }

  var puzzle = await puzzles.getPuzzle(db.client, puzzleID);

  if (!puzzle) {
    res.redirect(404, "/");
    return;
  }

  var solution = await puzzles.getSolution(db.client, puzzleID);

  if (!solution) {
    res.redirect(404, "/");
    return;
  }

  // Format for federation export
  var exportPuzzle = {
    "puzzle-type": "sudoku",
    "creator-id": puzzle.creatorID,
  }
  if (puzzle.puzzleType == "sudoku") {
    exportPuzzle["values"] = puzzle.values.map((row) => {
      return row.map(cell => {
        return Number(cell);
      })
    })
    exportPuzzle["solution"] = solution.map((row) => {
      return row.map(cell => {
        return Number(cell);
      })
    })
  } else {
    exportPuzzle["values"] = puzzle.values;
    exportPuzzle["solution"] = puzzle.solution;
  }

  res.set('Content-Disposition', 'attachment; filename=' + puzzleID + '.json')
  res.json(exportPuzzle);
  next();

});


app.post('/auth/access', async (req, res) => {
  // permissions for now 
  
  if(req.body.page){
    var pagePermissions = PERMISSIONS[req.body.page]
    if (pagePermissions && pagePermissions.includes(req.session.userRole)) {
      res.send({authorized: true})
      return;
    }
    else {
      res.status(401).send({authorized: false,
                message: req.body.page + ' page forbiddened for ' + req.session.userRole + '!'})
      return;
    }
  }
   else {
    // should not reach
    res.status(404).send({message: 'Wrong Page Name! This is what happens if you mess with the code >:('})
    return;
  }
})

/**
 * Endpoint federated sites redirect to with signed login token
 */
app.get('/auth/token/*', async (req, res) => {

  // Fetch the token from the URL
  const tokenString = req.params[0]

  const token = jwt.decode(tokenString)

  if (!token) {
    console.log("Invalid token");
    res.redirect('/auth/authorize');
    return
  }

  if (!federation.groupIDs.includes(token.groupID)) {
    res.redirect('/auth/authorize');
  }
  
  // Read the public key from the token
  
  fs.readFile(path.resolve(__dirname, "./federation/public-keys/"+token.groupID+".pem"), 'utf8', (fileError, publicKey) => {
   
    // Check if public key was read
    if (fileError) {
      console.log("Faild to read public key")
      res.redirect('/auth/authorize')
      return;
    }

    // Verify the token with the public key
    jwt.verify(tokenString, publicKey, async (verifyError) => {
      if (verifyError) {
        console.log(verifyError)
        console.log(token)
        res.redirect('/auth/authorize')
        return;
      }

      // Check if the user is registered
      var user = await users.getUserByID(db.client, token.userID)
      if (!user) {

        // Add user to the database
        var userResult = await users.createFederatedUser(db.client, token.groupID, token.userID, token.displayName)

        // Check if user was created successfully
        if (!userResult) {
          console.log("Failed to create federated user")
          res.redirect("/auth/authorize")
          return;
        }

        user = {
          _id: token.userID,
          displayName: token.displayName,
          groupID: token.groupID,
          role: users.DEFAULT_FEDERATED_ROLE
        }

      }

      // Log the user into the backend
      req.session.user = user

      // Log the user into the frontend
      // TODO: BUGFIX - User cookie
      res.cookie("user", JSON.stringify(user))

      res.redirect("/")
      return;
    })
  })
})

app.post('/auth/logout', (req, res) => {
  req.session.destroy()
  res.clearCookie('user')
  res.send('Logged Out!')
  return;
})

// API


app.get('/puzzles/random', async (req, res) => {
  const puzzle = await puzzles.getRandomPuzzle(db.client)
  if (puzzle === null) {
    res.status(404).send({ message: 'Error: There are no available sudoku in the database.' })
  } else {
    res.send({ puzzle: puzzle } )
  }
})

app.get('/puzzles', async (req, res) => {

  var page = 1;
  var limit = 10;

  if (req.query) {
    page = Number(req.query.page) || page
    limit = Number(req.query.limit) || limit
  }

  await puzzles.getPuzzles(db.client, limit, page)
  .then((puzzles) => {
    puzzles.map(p => {
      let c_date = new Date(p.creationDate)
      p.creationDate = c_date.toLocaleString('en-GB', { timeZone: 'UTC' }).replace(',','')
    })
    res.status(200).send({
      puzzles: puzzles
    });
    return
  })
  .catch((err) => {
    res.status(500).send({error: err.message})
    return
  })
})

app.post('/puzzles/solve', async (req, res) => {
  const answer = req.body
  const sudokuID = answer._id
  const sudokuAnswer = answer._answer
  const checked = await puzzles.checkPuzzleAnswer(db.client, sudokuID, sudokuAnswer)
  // to be adjusted
  if (!req.session.user) {
    res.status(401).send({message:'user not logged in!'})
    return
  }
  // in future: might wanna keep track of who solve it
  if (checked === null) {
    res.status(404).send({ message: 'Error: The sudoku does not exist.' })
    return
    // then play should refresh
  } else {
    res.status(200).send(checked)
    return
  }
})

app.get('/puzzles/*', async (req, res) => {
  const puzzleID = req.params[0]
  console.log(puzzleID)
  const puzzle = await puzzles.getPuzzle(db.client, puzzleID)
  if (puzzle === null) {
    res.status(404).send({ message: 'Error: There are no available sudoku in the database.' })
  } else {
    res.send({ puzzle: puzzle })
  }
})



app.post('/puzzles/create', async (req, res) => {

  const puzzleType = req.body.puzzleType
  const inputs = req.body.inputs
  const values = req.body.values
  const solution = req.body.solution

  if ( !values || !solution ) {
    res.status(422).send({
      stay: true,
      message: "Could not parse setup or solution"
    })
    return
  }


  if (!req.session.user) {
    res.status(401).send({
      stay: false,
      message: "User not logged in!"
    })
    return
  }
  // untested
  if (!PERMISSIONS.Create.includes(req.session.user.role)) {
    res.status(403).send({
      stay: false,
      message: req.session.user.role + ' is unauthorized to create puzzle!'
    })
  }

  await puzzles.createPuzzle(db.client, req.session.user._id, puzzleType, values, solution, inputs)
  .then(
    () => {
      res.status(200).send()
      return
    },
    (err) => {
      res.status(422).send({
        stay: true,
        message: err.message
      })
      return
    }
  )
})

app.post('/comments/puzzles/*', async (req, res) => {

  const puzzleID = req.params[0]
  const comment = req.body.comment;

  if (!puzzleID || !comment) {
    res.status(422).send({
      stay: true,
      message: "Could not parse puzzle ID or comment."
    })
    return
  }

  if (!req.session.user) {
    res.status(401).send({
      stay: false,
      message: "User not logged in!"
    })
    return
  }

  await puzzles.postComment(db.client, puzzleID, req.session.user._id, comment)
  .then(
    () => {
      res.status(200).send()
      return
    },
    (err) => {
      res.status(422).send({
        stay: true,
        message: err.message
      })
      return
    }
  )
})

app.get('/comments/puzzles/*', async (req, res) => {
  var page = 1;
  var limit = 20;

  const puzzleID = req.params[0];

  if (!puzzleID) {
    res.status(400).send({
      message: "No puzzleID supplied"
    })
    return;
  }

  if(req.query) {
    page = Number(req.query.page) || page;
    limit = Number(req.query.limit) || limit;
  }

  await puzzles.getComments(db.client, puzzleID, limit, page)
  .then((comments) => {
    res.status(200).send({
      comments: comments
    });
    return
  })
  .catch((err) => {
    res.status(500).send({error: err.message})
    return
  })
})

app.post('/manage/puzzles/remove', async (req,res) => {
  if (!PERMISSIONS.PuzzleManage.includes(req.session.user.role)) {
    res.status(401).send({message: req.session.user.role + ' is unauthorized to manage puzzles!'})
    return
  }
  const removeID = req.body.removeID

  const removal = await puzzles.removePuzzle(db.client, removeID)
  if(removal.deleted){
    res.status(200).send(removal)
    return
  } else {
    res.status(422).send(removal)
    return
  }

})

app.get('/manage/puzzles/solution', async (req,res) => {
  if (!PERMISSIONS.PuzzleManage.includes(req.session.user.role)) {
    res.status(401).send({message: req.session.user.role + ' is unauthorized to manage puzzles!'})
    return
  }
  const puzzleID = req.query.puzzleID

  await puzzles.getSolution(db.client, puzzleID)
    .then((sol) => {
      res.status(200).send({
        solution: sol
      })
      return
    })
    .catch((err) => {
      res.status(422).send({
        message: err.message
      })
      return
    })
})

app.get('/manage/user/list', async (req, res) => {
  // tested
  if (!PERMISSIONS.UserManage.includes(req.session.user.role)) {
    res.status(401).send({message: req.session.user.role + ' is unauthorized to manage users!'})
    return
  }
  //check both non-zero
  const pageSize = parseInt(req.query.pageSize,10)
  if(pageSize != 10 && pageSize != 50 && pageSize != 100){
    res.status(422).send({message: 'Invalid page size! Please choose within the given default page sizes.'})
    return
  }
  const pageNo = parseInt(req.query.pageNo,10)
  const userList = await users.getUsers(db.client,pageSize,pageNo);
  if(!userList) {
    res.status(422).send({
      message: "Could not generate user list"
    })
    return
  } else {
    userList.map(user => {
      let c_date = new Date(user.creationDate)
      user.creationDate = c_date.toLocaleString('en-GB', { timeZone: 'UTC' }).replace(',','')
      if(user._id == req.session.user._id){
        user.self=true
      }
      // user.changeRole = user.role
    })
    const noOfUsers = await users.countEntries(db.client, 'users')
    res.send({users: userList,
              noOfUsers: noOfUsers,
              totalPages: Math.ceil(noOfUsers/pageSize),
              roleOptions: Object.values(users.ROLES)
              .map(v => {
                return {value: v, text: v}
              })})
    //total user and total page
    return
  }
})

app.post('/manage/user/remove', async (req, res) => {
  if (!PERMISSIONS.UserManage.includes(req.session.user.role)) {
    res.status(401).send({message: req.session.user.role + ' is unauthorized to remove users!'})
    return
  }
  const removeID = req.body.removeID
  if(removeID == req.session.user._id){
    res.status(401).send({message: 'Please do not attempt to remove your own account.'})
    return
  }
  const removal = await users.removeUserByID(db.client,removeID)
  if(removal.deleted){
    res.status(200).send(removal)
    return
  } else {
    res.status(422).send(removal)
    return
  }
})

app.post('/manage/user/updaterole', async (req, res) => {
  if(!PERMISSIONS.UserManage.includes(req.session.user.role)){
    res.status(401).send({message: req.session.user.role + ' is unauthorized to manage users!'})
    return
  }
  
  const userID = req.body._id
  if(userID == req.session.user._id){
    res.status(401).send({message: 'Update failed: Please do not attempt to change your own role.'})
    return
  }
  const update = await users.updateUserByID(db.client,userID,{role:req.body.newRole})
  if(update.updated) {
    res.status(200).send(update)
    return
  } else {
    res.status(422).send(update)
    return
  }
})


// can add permission for the page of change personal details
// app.post('/manage/user/updatepersonal', async (req, res) => {
  // const userID = req.body._id
  // const toUpdate = req.body.update // cannot update ID & check field accuracy
  // // ADMIN: change users role / their own detail, cannot change their own role
  // // USER: change their own details / cannot change to admin
  // if (PERMISSIONS.UserManage.includes(req.session.user.role)) {
  //   // cannot change own role
  //   if(userID == req.session.user._id) {
  //     if(Object.keys(toUpdate).includes('role')) {
  //       res.status(401).send({message: 'Please do not attempt to change your own role.'})
  //       return
  //     } else {
  //       // update anything
  //     }
  //   } else {
  //     // update only role
  //   }
  // } else if(userID == req.session.user._id){
  //   // cannot update role as admin only
  // } else {
  //   // cannot manage
  //   res.status(401).send({message: req.session.user.role + ' is unauthorized to manage users or alter other account details!'})
  //   return
  // }
// })


app.listen(8024, () => console.log('listening at port: ' + FPF_BACKEND_PORT))
