const mongoose = require('mongoose')
const Schema = mongoose.Schema

const puzzleSchema = new Schema({
  puzzle_type: {
    type: String,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  creator_id: {
    type: String,
    required: true
  },
  checker_id: {
    type: String,
    default: ''
  },
  difficulty: {
    type: Number,
    default: 40,
    validate: [scoreLimit, '{PATH} is not in range 0-100']
  },
  // TODO: please make changes if you have better ways of doing the check
  values_row0: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row1: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row2: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row3: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row4: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row5: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row6: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row7: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  values_row8: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row0: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row1: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row2: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row3: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row4: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row5: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row6: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row7: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  },
  solution_row8: {
    type: Array,
    required: true,
    validate: [arrayLengthLimit, '{PATH} does not have the required length 9']
  }
})

mongoose.exports = mongoose.model('Puzzle', puzzleSchema)
function arrayLengthLimit (val) {
  return val.length === 9
}

function scoreLimit (val) {
  return val >= 0 && val <= 100
}
