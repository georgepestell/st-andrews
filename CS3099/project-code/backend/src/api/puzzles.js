function checkAnswer (answer, solution) {
  for (let i = 0; i < answer.length; i++) {
    for (let j = 0; j < answer[i].length; j++) {
      if (answer[i][j] != solution[i][j]) {
        if (answer[i][j] == "0") {
          // to be completed
          // for full fill check
          // incase of forced submission
          return [false, 'Please complete the sudoku and fill all cells with number.']
        }
        return [false, 'Your answer is incorrect, please try again.']
      }
    }
  }
  return [true, 'Your answer is correct!']
}

function filled (sudoku) {
  for (const row of sudoku) {
    for (const cell of row) {
      if (cell === 0) {
        return false
      }
    }
  }
  return true
}

module.exports = {
  MAX_LIMIT: 50,
  EMPTY_VALUE: "0",
  puzzleDuplicated: async function (client, puzzle) {
    const duplicateSudoku = await client.db('fpf').collection('puzzles').findOne({
      values: puzzle
    })
    return duplicateSudoku
  },
  // fields to add: difficulty, puzzle name, type
  /**
   * 
   * @param {*} client mongo client for connection
   * @param {*} userID for user identification, record the creator 
   * @param {*} puzzleType check whether it is sudoku or wordoku
   * @param {*} inputs get the 9 characters for the puzzle
   * @param {*} values the puzzle for solvers to solve
   * @param {*} solution solution for the puzzle
   * @returns the inserted entry
   */
  createPuzzle: async function (client, userID, puzzleType, values, solution, inputs) {
    
    if (!userID || !puzzleType || !values || !solution || !inputs) {
      throw new Error("Invalid parameters");
    }

    if (!filled(solution)) {
      throw new Error('Please fill all the cells with number for a complete sudoku solution.')
    }
    // if (await this.puzzleDuplicated(client, values)) { // later can do for puzzle symmetric check
    //   throw new Error('An identical sudoku puzzle was created before, please create a new puzzle.')
    // }
    var tmpValues = JSON.parse(JSON.stringify(values))
    var autoSolve = await this.autoSolve(tmpValues, inputs);
    
    if (!autoSolve.solvable) {
      throw new Error("Setup values have no valid solution")
    }
    if (autoSolve.duplicate) {
      throw new Error('Setup values have more than one valid solution');
    }
    if (!(await this.isEqual(solution, autoSolve.solution))) {
      throw new Error("Incorrect solution");
    }

    if (!userID) {
      throw new Error("Invalid user")
    }

    const result = await client.db('fpf').collection('puzzles').insertOne({
      puzzleType: puzzleType,
      values: values,
      solution: solution,
      inputs: inputs,
      creatorID: userID,
      creationDate: new Date()
    })

    if (!result) {
      throw new Error("Failed to create puzzle")
    }

    return result;
  },

  /**
   * this method randomly selects a puzzle from the puzzle database
   * @param {*} client mongo client for the connection
   * @returns the radomly selected puzzle
   */
  getRandomPuzzle: async function (client) {
    const puzzles = await client.db('fpf').collection('puzzles').aggregate(
      [
        { $sample: { size: 1 } },
        { $unset: [ "solution" ] },
      ]
    ).toArray();
  

    if (puzzles.length <= 0) {
      throw new Error("No puzzles in database")
    }

    return puzzles[0];
  },
  /**
 * this method get's a specific puzzle from the puzzle database
 * @param {*} client mongo client for the connection
 * @param puzzleID ID of the puzzle
 * @returns the selected puzzle
 */
  getPuzzle: async function (client, puzzleID) {

    const mongo = require('mongodb')
    
    var puzzleObjectId
    try {
      puzzleObjectId = new mongo.ObjectId(puzzleID)
    } catch (err) {
      console.log("Invalid puzzleID")
      return null;
    }

    const puzzle = await client.db('fpf').collection('puzzles').findOne(
      { _id: puzzleObjectId},
      { $unset: [ "solution" ] },
    )

    if (!puzzle) {
      throw new Error("Puzzle not found")
    }

    // console.log(result);
    // make sure dont return empty entries in future
    return puzzle;
  },
  /**
   * Fetches a consistent page of puzzles, sorted by upload date and _id (for consistency)
   * @param client mongodb database client to connect to
   * @returns array of puzzle objects in that page
   */
  getPuzzles: async function (client, limit, page) {

    if (limit > this.MAX_LIMIT) {
      throw new Error("Requesting too many pages")
    }

    // Calculate the number of entries to skip, pages numbered from 1
    const skip = limit * (page - 1);

    const cursor = client.db('fpf').collection('puzzles').aggregate(
      [
        { $lookup: {
            from: "users",
            localField: "creatorID",
            foreignField: "_id",
            as: "creator"
          }
        }, 
        { $match: { creator: { $ne: [] }}},     
        { $sort: { creationDate: -1, _id: 1 } },  
        { $skip: skip },
        { $limit: limit },
        // Lookup produces an array, 
        // this replaces the creator array with the first creator item
        {
          $unwind: "$creator"
        },
        // Don't include puzzles without a creator
        { $unset: [ "solution", "creator.email", "creatorID" ] },
      ]
    )
    return cursor.toArray();
  },

  checkPuzzleAnswer: async function (client, puzzleID, puzzleAnswer) {
    // temp solution for object id check
    // https://stackoverflow.com/questions/4902569/node-js-mongodb-select-document-by-id-node-mongodb-native
    const mongo = require('mongodb')
    console.log(puzzleID)
    const puzzleIDObj = new mongo.ObjectId(puzzleID)
    const theSudoku = await client.db('fpf').collection('puzzles').findOne({ _id: puzzleIDObj })
    if (!theSudoku) {
      // error sudoku does not exist?!
      return null
    }
    const answerCheck = checkAnswer(puzzleAnswer, theSudoku.solution)
    // for solved db entry
    // if (answerCheck.solved) {
    //   // can insert solved into entry
    // } else {
    //   // can insert wrong attempts
    //   // for future use count attempts
    // }
    return {
      solved: answerCheck[0],
      message: answerCheck[1]
    }
  },

  // check if roleID is needed
  removePuzzle: async function(client, puzzleID) {
    let deleteCheck = false
    const mongo = require('mongodb')
    let removeID = new mongo.ObjectId(puzzleID)
    let msg = 'Removal failed: '
    let foundPuzzle = await client.db('fpf').collection('puzzles').findOne({
      _id: removeID
    })
    if (!foundPuzzle) {
      // puzzle not found
      msg += 'No puzzle found.'
    } else {
      // delete puzzle from collection
      const result = await client.db('fpf').collection('puzzles').deleteOne({
        _id: removeID
      })

      const commentResult = await client.db('fpf').collection('comments').deleteMany({
        puzzleID: removeID
      })
      deleteCheck = true
      msg = `${result.deletedCount} puzzle with ID: ${puzzleID} is deleted. ${commentResult.deletedCount} comment(s) deleted`
    }
    return {
      deleted: deleteCheck,
      message: msg
    }
  },
  /**
   * Checks whether a puzzle is solvable, with a single solution using a backtracking algorithm
   * @param {} values 
   * @returns 
   */
  autoSolve: async function (values, inputs) {
    // Check if values has contradictions 
    var valid = await this.isValid(values);
    if (!valid) {
      return {
        solvable: false,
        duplicate: false
        };
    }


    // Loop over each empty cell, trying all inputs
    for (let row = 0; row < values.length; row++) {
      for (let column = 0; column < values[row].length; column++) {
        var initial = values[row][column];
        if (initial == this.EMPTY_VALUE) {
          var validInputs = [];
          for (let input of inputs) {

            // Put the input into the array, and check if the subsequent value 
            // array is solvable

            values[row][column] = input;
            var result = await this.autoSolve(values, inputs)

            // Check if the subsequent values array had more than one solution
            if (result.duplicate) {
              return {
                solvable: true,
                duplicate: true
              }
            }

            // Check if the tested input was valid 
            if (result.solvable) {
              validInputs.push(input);
            }

            values[row][column] = initial;
          }

          // Check if there are multiple valid inputs
          if (validInputs.length > 1) {
            return {
              solvable: true,
              duplicate: true,
            };
          }

          // Check if there was a unique solution
          if (validInputs.length == 1) {
            // Apply the valid input and return the valid solution
            values[row][column] = validInputs[0];
            return {
              solvable: true,
              duplicate: false,
              solution: values,
            }

          }

          // There are no valid inputs
          return {
            solvable: false,
            duplicate: false,
          }
        }
      }
    }

    // All values are filled, so it's already solvable
    return {
      solvable: true,
      duplicate: false,
      solution: values,
    };
  },
  /**
   * Checks sudoku inputs for contractions
   */
  isValid: async function (values) {

    /*
    * Check if the values in each row are unique 
    */
    var validRows = values.every(row => {
      var rowValues = row.filter(cell => cell != this.EMPTY_VALUE)
      if (new Set(rowValues).size != rowValues.length) {
        return false;
      }
      return true;

    })

    if (!validRows) {
      return false;
    }

    /**
     * Get column and box values
     */

    // Create empty arrays for each box, and column
    var columns = new Array(values.length).fill().map(() => new Array());
    var boxes = new Array(3).fill().map(() => new Array(3).fill().map(() => new Array()));
    
    // Fill the arrays with non-empty values
    for (let row = 0; row < values.length; row++) {
      for (let column = 0; column < values[row].length; column++) {
        
        var value = values[row][column];
        
        // Skip empty values, as they might have a non-conflicting potential value
        if (value == this.EMPTY_VALUE) {
          continue;
        }

        let boxRow = Math.floor(row/3);
        let boxColumn = Math.floor(column/3);

        boxes[boxRow][boxColumn].push(value);
        columns[column].push(value);
      }
    }

    /**
     * Check if the values in each column are unique
     */
    let validColumns = columns.every(column => {
      if (new Set(column).size != column.length) {
        return false;
      }
      return true;

    })

    if (!validColumns) {
      return false;
    }

    /**
     * Check if the values in each box are unique 
     */
    let validBoxes = boxes.every(row => {
      return row.every(box => {
        if (new Set(box).size != box.length) {
          return false;
        }
        return true;

      })
    })

    if (!validBoxes) {
      return false
    }

    return true;

  }, 
  /**
   * Checks whether two sudoku are the same
   */
  isEqual: async function (valuesA, valuesB) {
    if (!valuesA || !valuesB) {
      throw new Error("Equal check values are null")
    }

    if (valuesA.length != valuesB.length) {
      return false;
    }

    return valuesA.every((rowA, row) => {
      if (rowA.length != valuesB[row].length) {
        return false;
      }
      return rowA.every((cellA, column) => {
        if (cellA != valuesB[row][column]) {
          return false;
        }

        return true;
      })
    })
  },

  getSolution: async function (client, puzzleID) {
    const mongo = require('mongodb')
    let puzzleObjectId = new mongo.ObjectId(puzzleID)
    // let msg = 'Removal failed: '
    const puzzleDetail = await client.db('fpf').collection('puzzles').findOne({ _id: puzzleObjectId})
    // console.log(puzzleDetail)
    if (!puzzleDetail) {
      throw new Error("Puzzle not found")
    } else {
      return puzzleDetail.solution
    }
  },

  /**
   * upsert a comment to the comment database if there is actually a user and a puzzle.
   * @param {*} client mongodb client for connection
   * @param {*} puzzleID for puzzle identification, record the puzzle in the comments db
   * @param {*} userID for user identification, record the user in the comments db
   * @param {*} comment the comment posted by the user
   * @returns return the upserted entry
   */
  postComment: async function (client, puzzleID, userID, comment) {
    const mongo = require('mongodb');
    const puzzleObjectId = new mongo.ObjectId(puzzleID);
    const foundPuzzle = await client.db('fpf').collection('puzzles').findOne({ _id: puzzleObjectId });
    const foundUser = await client.db('fpf').collection('users').findOne({ _id: userID })
    if(!foundUser){
      // user not found
      throw new Error(`No user with ID '${userID}' was found.`)
    }
    if (!foundPuzzle) {
      throw new Error(`No puzzle with ID '${puzzleID}' was found.`)
    }

    return client.db('fpf').collection('comments').insertOne(
      {
        puzzleID: puzzleID, 
        userID: userID,
        comment: comment, 
        commentDate: new Date()
      }
    );
  },

  /**
   * Fetches a consistent page of comments, sorted by upload date and _id (for consistency)
   * @param {*} client mongo client for connection
   * @param {*} puzzleID puzzle identification for 
   * @param {*} limit for calculating the entries to skip
   * @param {*} page page number of results
   * @returns array of comment objects in that page
   */
  getComments: async function (client, puzzleID, limit, page) {
    if (limit > this.MAX_LIMIT) {
      throw new Error("Requesting too many pages")
    }
    
    // Calculate the number of entries to skip, pages numbered from 1
    const skip = limit * (page - 1);
    
    const cursor = client.db('fpf').collection('comments').aggregate(
      [
        { $lookup: {
          from: "users",
          localField: "userID",
          foreignField: "_id",
          as: "user"
          }
        },
        // Don't include comments without a creator
        { $match: { user: { $ne: [] }, puzzleID: puzzleID}},
        { $unset: ["userID"] },
        { $sort: { commentDate: -1, _id: 1 } },
        { $skip: skip },
        { $limit: limit },
        { $unwind: "$user"},        
      ]
    )

    return cursor.toArray();
  }
}
