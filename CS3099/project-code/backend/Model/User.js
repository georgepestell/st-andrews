const mongoose = require('mongoose')
const Schema = mongoose.Schema

const userSchema = new Schema({
  _id: {
    type: String,
    required: true
  },
  display_name: {
    type: String,
    required: true
  },
  email: {
    type: String,
    required: true
  },
  creation_date: {
    type: Date,
    required: true
  },
  expiry_time: {
    type: Date,
    required: true
  },
  roles: {
    type: Number,
    default: 1
  }
})

module.exports = mongoose.model('User', userSchema)
