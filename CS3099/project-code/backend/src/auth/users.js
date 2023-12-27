const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')
const fs = require('fs')
const path = require('path')
// const { client } = require('../db/db')
const rounds = 11
// const roles = require("./roles")

const privateKey = fs.readFileSync(path.resolve(__dirname, "../../privateKey.pem"))

module.exports = {
  ROLES: Object.freeze({
    SOLVER: "SOLVER", 
    SETTER: "SETTER", 
    ADMIN: "ADMIN"
  }),
  DEFAULT_FEDERATED_ROLE: "SETTER",
  createUser: async function (client, email, password, displayName, role) {
    const groupID = 24;

    // Check input is valid - userID can be null
    if ( !client || !email || !password || !displayName || !role ) {
      throw new Error("Invalid user parameters");
    }
  
    // Use a regex to check the email formatting
    /**
     * The regular express for email validation was taken from:
     * https://ihateregex.io/expr/email/
     * 
     * 
    */
    /** BEGIN CODE */
    const emailRegex = new RegExp(/[^@ \t\r\n]+@[^@ \t\r\n]+.[^@ \t\r\n]+/);
    /** END CODE */

    if (!emailRegex.test(email)) {
      throw new Error("Invalid email")
    }

    // Supergoup limits display names to 32 characters
    if (displayName.length > 32) {
      return null
    }

    // Check if user is already registered
    if (
      await client.db('fpf').collection('users').findOne(
        {
          email: email
        }
      )
    ) {
      throw new Error("User already exists")
    }

    // Check if the role is valid
    if (role != this.ROLES.SETTER && role != this.ROLES.SOLVER) {
      throw new Error("Invalid role");
    }
   
    // Generate userID
    const counter = await client.db('fpf').collection('counters').findOneAndUpdate({
        _id: 'uid'
      }, {
        $inc: { seq_value: 1 }
      },
      {new: true, upsert: true}
    )

    // If incrementation fails
    if (!counter) {
      throw new Error("UserID counter failed to increment")
    }

    const userID = `G${groupID}-${counter.value.seq_value}`

    // Create the new user
    const userResult = await client.db('fpf').collection('users').insertOne({
      _id: userID,
      groupID: groupID,
      displayName: displayName,
      email: email,
      creationDate: Date.now(),
      role: role,
    })

    // Check if the user was created
    if (!userResult) {
      throw new Error("User was not created")
    }

    const hash = await this.genSaltAndHash(password)

    console.log(hash)
    const passwordResult = await client.db('fpf').collection('passwords').insertOne({
      user_id: userResult.insertedId,
      hash
    })

    // If the password transaction failed, try to remove the user info entry
    if (!passwordResult.insertedId) {
      await client.db('fpf').collection('users').deleteOne({
        _id: userResult.insertedId
      })
      throw new Error("Creating password entry failed")
    }

    return userResult;
  },
  createFederatedUser: async function (client, groupID, userID, displayName) {
  
    // Check input is valid
    if ( !client || !groupID || !userID || !displayName  ) {
      throw new Error("Invalid user parameters");
    }
  
    // Supergoup limits display names to 32 characters
    if (displayName.length > 32) {
      return null
    }

    // Check if user is already registered
    if (
      await client.db('fpf').collection('users').findOne(
        {
          userID: userID
        }
      )
    ) {
      throw new Error("User already exists")
    }

    // Create the new user
    const userResult = await client.db('fpf').collection('users').insertOne({
      _id: userID,
      groupID: groupID,
      displayName: displayName,
      creationDate: Date.now(),
      role: this.DEFAULT_FEDERATED_ROLE,
    })

    // Check if the user was created
    if (!userResult) {
      throw new Error("Failed to create user")
    }

    return userResult;
  },
  generateToken: async function (user) {
    if (user.groupID !== 24) {
      return null
    }

    return jwt.sign({
      userID: user._id,
      groupID: user.groupID,
      email: user.email,
      displayName: user.displayName,
      iat: Date.now(),
      // Set JWT to expire after 2 minutes
      exp: Date.now() + 2 * 60 * 1000,
    },
    privateKey,
    { algorithm: 'RS256' })
  },
  getUserByEmail: async function (client, email) {
    const user = await client.db('fpf').collection('users').findOne({ email })
    return user
  },
  validateUserPassword: async function (client, user, password) {
    if (!user) {
      return false
    }
    const pass = await client.db('fpf').collection('passwords').findOne({ user_id: user._id })

    return new Promise((resolve, reject) => {
      if (pass) {
        bcrypt.compare(password, pass.hash, (error, result) => {
          if (error) {
            console.log(error)
            reject(error)
          }
          if (result) {
            resolve(true)
          } else {
            resolve(false)
          }
        })
      }
    })
  },
  getUserByID: async function (client, userID) {
    const result = await client.db('fpf').collection('users').findOne({ _id: userID })

    if (result) {
      console.log(`Found a user in the collection with ID: '${userID}'`)
      console.log(result)
      return result
    } else {
      console.log(`No listings found with the ID '${userID}'`)
    }
  },
  loginSession: async function (client, userID) {
    const result = await client.db('fpf').collection('sessions').insertOne({ _id: userID })

    console.log('Login session has been created for user with ID: ' + result.insertedIds)
  },
  logoutSession: async function (client, userID) {
    const result = await client.db('fpf').collection('sessions').deleteOne({ _id: userID })

    console.log(result.deletedCount + ' session(s) has ended')
  },
  genSaltAndHash: async function (password) {
    return new Promise((resolve, reject) => {
      bcrypt.genSalt(rounds, (error, salt) => {
        if (error) {
          console.log(error)
          reject(error)
        }
        bcrypt.hash(password, salt, (error, hash) => {
          if (error) {
            console.log(error)
            reject(error)
          }
          resolve(hash)
        })
      })
    })
  },
  // for verifying login and account updates
  checkPassword: async function (client, userID, password) {
    const passwordHash = await client.db('fpf').collection('passwords').findOne({ _id: userID }).password
    bcrypt.compare(password, passwordHash, (error, loginSuccessful) => {
      if (error) {
        console.log(error)
        return
      }
      return loginSuccessful
    })
    // const match = await bcrypt.compare(password, passwordHash);

    // if (match) {
    //     // TODO: add in the way you want to deal with correct pwd
    //     return true;
    // } else {
    //     // TODO: add in the way you want to deal with wrong pwd
    //     return false;
    // }
  },
  removeUserByID: async function (client, /* roleID, permissionID,*/ removeID) {
    let deleteCheck = false
    let msg = 'Removal failed: '
    const foundUser = await client.db('fpf').collection('users').findOne({ _id: removeID })
    if (!foundUser) {
      // user not found
      msg += `No user with ID '${removeID}' was found.`
    } else {
      // delete the account
      const result = await client.db('fpf').collection('users').deleteOne({ _id: removeID })
      const commentResult = await client.db('fpf').collection('comments').deleteMany({ userID: removeID })
      deleteCheck = true
      msg = `${result.deletedCount} account with ID: ${removeID} is deleted. ${commentResult.deletedCount} comment(s) deleted.`
    }
    return {
      deleted: deleteCheck,
      message: msg
    }
  },
  updateUserByID: async function (client, userID, updates) {
    let updateCheck = false
    // need to check if email duplicates if updates has email
    let msg = 'Update failed: '
    let newData = null
    const foundUser = await client.db('fpf').collection('users').findOne({ _id: userID })
    if(!foundUser){
      // user not found
      msg += `No user with ID '${userID}' was found.`
    } else if(Object.keys(updates).includes('_id')){
      // cannot change the ID
      msg += 'Please do not attempt to change the user ID. ID should be fixed.'
    } else if(Object.keys(updates).includes('role') && !Object.values(this.ROLES).includes(updates.role)){
      msg += 'Role invalid.'
    } else { 
      // update
      // console.log(updates)
      const tryUpdate = await client.db('fpf').collection('users')
          .updateOne(
            { _id: userID,
              $and: Object.keys(updates).map(key => ({ [key]: { $exists: true } }))
            }, 
            { $set: updates },
            {
              upsert: false
            })
      // console.log(result)
      if(tryUpdate.modifiedCount == 0){
        msg += 'Invalid fields to update. Please edit user details with the default fields.'
        
      } else {
        updateCheck = true
        msg = `User (ID: ${userID}) information is updated.`
        newData = await client.db('fpf').collection('users').findOne({_id: userID})
      }
    }
    return {
      updated: updateCheck,
      message: msg,
      _id: userID,
      newData: newData
    }
  },
  getUsers: async function (client, pageSize, pageNo) {
    const skip = (pageNo - 1) * pageSize
    const users = await client.db('fpf').collection('users').find({}).sort({creationDate:-1}).skip(skip).limit(pageSize).toArray()
    return users
  },
  countEntries: async function (client,field) {
    const num = await client.db('fpf').collection(field).countDocuments({})
    return num
  }
}
