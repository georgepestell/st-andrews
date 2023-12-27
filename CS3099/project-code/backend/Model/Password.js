const mongoose = require('mongoose')
const Schema = mongoose.Schema

const passwordSchema = new Schema({
  _id: {
    type: String,
    required: true
  },
  password: {
    type: String,
    required: true
  }
})

mongoose.exports = mongoose.model('Password', passwordSchema)
