const mongoose = require('mongoose')
const Schema = mongoose.Schema

const roleSchema = new Schema({
  role_id: {
    type: Number,
    required: true
  },
  manage_user: {
    type: Boolean,
    required: true
  },
  modified_puzzle: {
    type: Boolean,
    required: true
  },
  solve_puzzle: {
    type: Boolean,
    required: true
  }
})

mongoose.exports = mongoose.model('Role', roleSchema)
