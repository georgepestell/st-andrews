<script setup>
import "@/sass/components/SudokuGame.scss";
import SudokuBoard from "@/components/Sudoku/Board/InteractiveBoard.vue";
import SudokuControls from "@/components/Sudoku/SudokuControls.vue";
import Help from "@/components/Help.vue";
import PuzzleService from "@/services/PuzzleService.js";
</script>

<template>
  <label>Puzzle type: </label>
  <select name="puzzleType" id="puzzleType" v-model="puzzleType">
    <option value="sudoku" selected="selected">sudoku</option>
    <option value="wordoku">wordoku</option>
  </select>
  <button
    class="button"
    id="puzzleTypeButton"
    @click="puzzleTypeCheck"
    v-if="puzzleType == 'sudoku'"
  >
    Confirm
  </button>
  <div class="diagonal-word-input-form" id="diagonalWordInput" v-if="puzzleType == 'wordoku'">
    <p>
      Please ensure that you have a 9-letter word and the repeating letters
      cannot occur in the 3 divisions of the word. (e.g. ealderman <u>e</u
      ><b>a</b>l d<u>e</u>r m<b>a</b>n)
    </p>
    <label>Diagonal word: </label>
    <input
      id="diagonalWord"
      type="text"
      minlength="9"
      maxlength="9"
      @keypress="alphaCheck"
      @keyup="lengthCheck"
      @keydown.backspace="lengthCheck"
      @keydown.delete="lengthCheck"
      oninput="value = value.toUpperCase()"
      required
    />
    <button
      class="button"
      id="diagonalWordButton"
      style="display: none"
      @click="diagonalCheck"
    >
      Confirm
    </button>
    <br />
    <label id="charForPuzzleLabel" style="display: none"
      >Characters for puzzle:
    </label>
    <p id="charactersFromDiagonal"></p>
    <input
      id="charForPuzzleInput"
      style="display: none"
      @keypress="charForPuzzleInputAlphaCheck"
      @keyup="charForPuzzleInputLengthCheck"
      @keydown.backspace="charForPuzzleInputLengthCheck"
      @keydown.delete="charForPuzzleInputLengthCheck"
      oninput="value = value.toUpperCase()"
    />
    <button
      class="button"
      id="charForPuzzleInputButton"
      style="display: none"
      @click="extraCharCheck"
    >
      Confirm
    </button>
    <button
      class="button"
      id="closeDetailsButton"
      @click="closeDetails"
    >
      Close Details
    </button>
  </div>
  <button
    class="button"
    id="showButton"
    style="display: none"
    @click="showDetails"
    v-if="puzzleType == 'wordoku'"
  >
    Show Puzzle Details
  </button>
  <br />

  <div class="game-container" id="gameContainer" style="visibility: hidden">
    <SudokuBoard
      :EMPTY_VALUE="EMPTY_VALUE"
      :board="board"
      :inputs="inputs"
      :selected_cell="selectedCell"
      @select-cell="selectCell"
    />
    <SudokuControls
      :selected_input="selectedInput"
      :inputs="inputs"
      @select-input="selectInput"
      @reset="setupBoard(values)"
      @submit="submit"
      @upload="upload"
    />
  </div>
  <div id="sidebars">
    <Help title="Create Instructions">
      <ul>
        <li>
          Insert a number into a cell:
          <br />Select the desired letter/number (character in general) from the character palette, then select the
          destinated cell.
          <br />Or type the desired character on the keyboard when the destinated cell is selected.
        </li>
        <br />
        <li>
          Remove character from cell:
          <br />Select the cell when the original character from
          the cell is selected from the character palette.
          <br/>Or type the same character on the keyboard when the destinated cell is selected
        </li>
        <br />
        <li>
          Choose a hint cell for sudoku solving:
          <br />Click the cell when no characters is selected from the character palette.
        </li>
      </ul>
      <p>
        Make sure your sudoku is fully filled with numbers and has at least 17
        hint cells. Have fun!
      </p>
    </Help>
  </div>
</template>

<script>
export default {
  components: {
    SudokuBoard,
    SudokuControls,
  },
  data() {
    return {
      puzzleType: "sudoku",
      EMPTY_VALUE: "0",
      inputs: ["1", "2", "3", "4", "5", "6", "7", "8", "9"],
      values: [
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "0", "0"],
      ],
      board: [],
      selectedCell: undefined,
      selectedInput: "0",
      redirectHomeMessage: "You will now be redirected to our home page!",
    };
  },
  methods: {
    puzzleTypeCheck() {
      document.getElementById("puzzleType").disabled = true;
      document.getElementById("puzzleTypeButton").style.display = "none";
      document.getElementById("gameContainer").style.visibility = "visible";
    },
    alphaCheck(keyEntered) {
      if (
        !(keyEntered.charCode >= 65 && keyEntered.charCode <= 90) &&
        !(keyEntered.charCode >= 97 && keyEntered.charCode <= 122)
      ) {
        keyEntered.preventDefault();
      }
    },
    // check the length of the input and check the substrings (index 0-2, 3-5, 6-8) don't contain repeated characters
    lengthCheck() {
      const diagonalWordInput = document.getElementById("diagonalWord").value;
      if (diagonalWordInput.length == 9) {
        const firstSubstring = diagonalWordInput.substring(0, 3);
        const firstSubstringSet = new Set(firstSubstring);
        const secondSubstring = diagonalWordInput.substring(3, 6);
        const secondSubstringSet = new Set(secondSubstring);
        const thirdSubstring = diagonalWordInput.substring(6);
        const thirdSubstringSet = new Set(thirdSubstring);
        if (
          firstSubstringSet.size == 3 &&
          secondSubstringSet.size == 3 &&
          thirdSubstringSet.size == 3
        ) {
          document.getElementById("diagonalWordButton").style.display = "block";
          return true;
        } else {
          return false;
        }
      } else {
        document.getElementById("diagonalWordButton").style.display = "none";
        return false;
      }
    },
    diagonalCheck() {
      if (this.lengthCheck) {
        document.getElementById("diagonalWord").disabled = true;
        document.getElementById("diagonalWordButton").style.display = "none";
        const diagonalWord = document.getElementById("diagonalWord").value;
        const diagonalWordSet = new Set(diagonalWord);
        document.getElementById("charForPuzzleLabel").style.display = "block";
        document.getElementById("charactersFromDiagonal").innerHTML =
          Array.from(diagonalWordSet).join(" ");
        document.getElementById("charactersFromDiagonal").style.display =
          "block";
        if (diagonalWordSet.size == 9) {
          document.getElementById("puzzleType").disabled = true;
          this.inputs = Array.from(diagonalWordSet);
          document.getElementById("gameContainer").style.visibility = "visible";
        } else {
          document.getElementById("charForPuzzleInput").minLength =
            9 - diagonalWordSet.size;
          document.getElementById("charForPuzzleInput").maxLength =
            9 - diagonalWordSet.size;
          document.getElementById("charForPuzzleInput").required = true;
          document.getElementById("charForPuzzleInput").style.display = "block";
        }
      }
    },
    charForPuzzleInputAlphaCheck(keyEntered) {
      const diagonalWord = document.getElementById("diagonalWord").value;
      const diagonalWordSet = new Set(diagonalWord);
      const extraChar = document.getElementById("charForPuzzleInput").value;
      const extraCharSet = new Set(extraChar);
      // check if it is an alphanum and also check if it is in the diagonalWordSet
      if (
        (!(keyEntered.charCode >= 65 && keyEntered.charCode <= 90) &&
          !(keyEntered.charCode >= 97 && keyEntered.charCode <= 122)) ||
        diagonalWordSet.has(keyEntered.key.toUpperCase()) ||
        extraCharSet.has(keyEntered.key.toUpperCase())
      ) {
        keyEntered.preventDefault();
      }
    },
    charForPuzzleInputLengthCheck() {
      const extraChar = document.getElementById("charForPuzzleInput");
      if (extraChar.value.length == extraChar.maxLength) {
        document.getElementById("charForPuzzleInputButton").style.display =
          "block";
        return true;
      } else {
        document.getElementById("charForPuzzleInputButton").style.display =
          "none";
        return false;
      }
    },
    typeInput(event){
      const value = event.key.toUpperCase()
      const cell = this.selectedCell
      if(this.inputs.includes(value) && this.selectCell !== undefined){
        if (this.noteMode) {
          this.toggleCellNote(cell, value);
        } else {
          this.toggleCellValue(cell, value);
        }
      }
    },
    extraCharCheck() {
      if (this.charForPuzzleInputLengthCheck) {
        document.getElementById("puzzleType").disabled = true;
        document.getElementById("charForPuzzleInput").disabled = true;
        document.getElementById("charForPuzzleInputButton").style.display =
          "none";
        const diagonalWord = document.getElementById("diagonalWord").value;
        const inputSet = new Set(diagonalWord);
        const extraChar = document.getElementById("charForPuzzleInput").value;
        const extraCharSet = new Set(extraChar);
        extraCharSet.forEach(inputSet.add, inputSet);
        this.inputs = Array.from(inputSet);
        document.getElementById("gameContainer").style.visibility = "visible";
      }
    },
    closeDetails() {
      document.getElementById("diagonalWordInput").style.display = "none";
      document.getElementById("showButton").style.display = "block";
    },
    showDetails() {
      document.getElementById("diagonalWordInput").style.display = "block";
      document.getElementById("showButton").style.display = "none";
    },
    async submit() {
      var puzzleType = this.puzzleType;
      var inputs = this.inputs;
      var values = this.genValues();
      var solution = this.genSolution();
      PuzzleService.create(puzzleType, values, solution, inputs);
    },
    genValues() {
      return this.board.map((row) => {
        return row.map((cell) => {
          if (cell.fixed) {
            return cell.value;
          } else {
            return this.EMPTY_VALUE;
          }
        });
      });
    },
    genSolution() {
      return this.board.map((row) => {
        return row.map((cell) => {
          return cell.value;
        });
      });
    },
    setupBoard(solution, values) {
      this.board = Array.from(solution).map((cells, row) => {
        return cells.map((cell, column) => {
          return {
            row: row,
            column: column,
            value: cell.toString(),
            fixed: false,
            selected: false,
            highlighted: false,
            error: false,
            box: {
              row: Math.floor(row / 3),
              col: Math.floor(column / 3),
            },
          };
        });
      });

      if (values) {
        this.setFixed(values);
      }

    },
    selectInput(input) {
      if (input == this.selectedInput) {
        this.selectedInput = this.EMPTY_VALUE;
      } else {
        this.selectedInput = input;
      }
    },
    selectCell(cell) {
      this.selectedCell = cell;
      if (cell.value == this.selectedInput) {
        if (cell.value != this.EMPTY_VALUE) {
          this.toggleCellValue(cell, this.selectedInput);
        }
      } else {
        if(this.selectedInput==this.EMPTY_VALUE){
          this.toggleCellFixed(cell);
        } else {
          cell.fixed = false;
          this.toggleCellValue(cell, this.selectedInput);
        }
      }
    },
    toggleCellFixed(cell) {
      cell.fixed = !cell.fixed;
    },
    toggleCellValue(cell, value) {
      // if (!cell.fixed) {
      if (cell.value == value) {
        cell.value = this.EMPTY_VALUE;
      } else {
        cell.value = value;
      }
      // }
    },
    upload(file) {
      var reader = new FileReader();
      var puzzle = {};
      reader.onload = () => {
        try {
          puzzle = JSON.parse(reader.result);

          if (
            !puzzle["values"] ||
            !puzzle["solution"] ||
            !puzzle["puzzle-type"]
          ) {
            throw new Error("Puzzle File Syntax Error");
          }

          // Make sure the puzzle type matches
          if (this.puzzleType != puzzle["puzzle-type"]) {
            throw new Error("Uploaded puzzle type doesn't match!");
          }

          this.setupBoard(puzzle["solution"], puzzle["values"]);
        } catch (err) {
          this.errorHandling(err);
        }
      };

      reader.readAsText(file);
    },
    errorHandling(err) {
      alert(err.message);
    },
    setFixed(values) {
      if (!values) {
        Error("Values undefined");
      }

      if (values.length != this.board.length) {
        Error("Invalid board values");
      }
      this.board.every((row, rowNumber) => {
        if (values[rowNumber].length != row.length) {
          Error("Invalid board values");
        }

        row.every((cell, columnNumber) => {
          console.log(values[rowNumber][columnNumber]);
          if (values[rowNumber][columnNumber] != this.EMPTY_VALUE) {
            if (cell.value != values[rowNumber][columnNumber].toString()) {
              Error("Setup doesn't match solution");
            }
            cell.fixed = true;
          }
          return true;
        });
        return true;
      });
    },
  },
  mounted() {
    document.addEventListener("keydown", this.typeInput);
    this.setupBoard(this.values);
  },
  beforeDestroy() {
    document.removeEventListener("keydown", this.typeInput);
  },
};
</script>
