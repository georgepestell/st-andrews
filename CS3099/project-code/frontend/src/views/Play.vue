<script setup>
import "@/sass/components/SudokuGame.scss";
import SudokuBoard from "@/components/Sudoku/Board/InteractiveBoard.vue";
import SudokuControls from "@/components/Sudoku/SudokuControls.vue";
import Help from "@/components/Help.vue";
import CommentsBar from "@/components/CommentsBar.vue";
import PuzzleService from "@/services/PuzzleService.js";
</script>

<template>
  <div class="game-container">
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
      :note_mode="noteMode"
      @selectInput="selectInput"
      @reset="setupBoard"
      @toggleNoteMode="toggleNoteMode"
      @export="exportPuzzle"
    />
  </div>
  <div id="sidebars">
    <CommentsBar :puzzleID="sudoku_id" />
    <Help title="Play Instructions">
      <ul>
        <li>
          Insert a number into a cell:
          <br />Select the desired letter/number (character in general) from the character palette, then select the
          destinated cell.
          <br />Or type the desired character on the keyboard when the destinated cell is selected.
        </li>
        <br />
        <li>
          Remove number from cell:
          <br />Select the cell when either no characters or the original character from
          the cell is selected from the character palette.
          <br/>Or type the same character on the keyboard when the destinated cell is selected
        </li>
        <br />
        <li>
          Mark notes in a cell: <br />Select the pencil button. Then same controls
          as character insertion.
        </li>
        <br />
        <li>
          Remove a note in a cell: <br />Select the pencil button and the character
          to be removed. Then select the cell.
          <br/>Or type the desired deleted character when the cell and pencil button is selected
        </li>
      </ul>
    </Help>
  </div>
</template>

<script>
export default {
  components: {
    SudokuBoard,
    SudokuControls,
  },
  props: {
    puzzleID: String,
  },
  data() {
    return {
      EMPTY_VALUE: "0",
      inputs: ["1", "2", "3", "4", "5", "6", "7", "8", "9"],
      sudoku_id: undefined,
      // a default setup for user to play in case of cannot connect to server
      values: [
        ["5", "3", "1", "0", "4", "0", "0", "6", "0"],
        ["6", "0", "9", "0", "7", "5", "0", "0", "1"],
        ["0", "0", "7", "0", "0", "1", "4", "5", "9"],
        ["0", "7", "0", "5", "0", "6", "0", "0", "8"],
        ["0", "0", "2", "0", "8", "0", "1", "0", "0"],
        ["1", "8", "0", "0", "9", "2", "0", "0", "0"],
        ["0", "0", "8", "7", "0", "4", "9", "0", "0"],
        ["4", "0", "6", "8", "0", "3", "5", "1", "0"],
        ["0", "0", "0", "0", "0", "0", "0", "2", "4"],
      ],
      board: [],
      selectedCell: undefined,
      selectedInput: "0",
      noteMode: false,
      redirectHomeMessage: "\nYou will be redirected back to our home page.",
    };
  },

  methods: {
    setupBoard() {
      this.board = Array.from(this.values).map((cells, row) => {
        return cells.map((cell, column) => {
          return {
            row: row,
            column: column,
            value: cell,
            fixed: cell != this.EMPTY_VALUE,
            highlighted: false,
            valueIsSelected: false,
            error: false,
            notes: new Array(),
            box: {
              row: Math.floor(row / 3),
              col: Math.floor(column / 3),
            },
          };
        });
      });
    },
    errorHandling(err) {
      if (err.response.status == 404 || err.response.status == 401) {
        alert(err.response.data.message + this.redirectHomeMessage);
        window.location.href = "/";
      }
    },
    async checkAnswer() {
      var answer = [[], [], [], [], [], [], [], [], []];
      for (var i = 0; i < this.board.length; i++) {
        var row = this.board[i];
        for (var j = 0; j < row.length; j++) {
          if (row[j].value == this.EMPTY_VALUE) {
            //if not completed
            return false;
          } else {
            answer[i][j] = row[j].value;
          }
        }
      }
      // if completed
      PuzzleService.solve({
        _id: this.sudoku_id,
        _answer: answer,
        // _session: 0, //session hash(?)
      })
        .then((res) => {
          var response = res.data;
          if (response.solved) {
            alert(response.message + this.redirectHomeMessage);
            window.location.href = "/";
          } else {
            alert(response.message);
          }
        })
        .catch((err) => {
          this.errorHandling(err);
        });
    },
    selectInput(input) {
      if (input == this.selectedInput) {
        this.selectedInput = this.EMPTY_VALUE;
      } else {
        this.selectedInput = input;
      }
    },
    toggleNoteMode() {
      this.noteMode = !this.noteMode;
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
    selectCell(cell) {
      this.selectedCell = cell;

      if (this.noteMode) {
        this.toggleCellNote(cell, this.selectedInput);
      } else {
        this.toggleCellValue(cell, this.selectedInput);
      }
    },
    toggleCellNote(cell, value) {
      if (!cell.fixed && cell.value == this.EMPTY_VALUE) {
        if (cell.notes.includes(value)) {
          cell.notes.splice(cell.notes.indexOf(value), 1);
        } else {
          cell.notes.push(value);
        }
      }
    },
    toggleCellValue(cell, value) {
      if (!cell.fixed) {
        if (cell.value == value) {
          cell.value = this.EMPTY_VALUE;
        } else {
          cell.value = value;
          this.checkAnswer();
        }
      }
    },
    async loadPuzzle(puzzleID) {
      if (!puzzleID) {
        await PuzzleService.getRandom().then((puzzle) => {
          if (!puzzle) {
            return;
          }
          this.sudoku_id = puzzle._id;
          this.values = puzzle.values;
          if(puzzle.inputs !== undefined){
            this.inputs = puzzle.inputs;
          }
          this.setupBoard();
        });
      } else {
        await PuzzleService.getPuzzle(this.puzzleID).then((puzzle) => {
          if (!puzzle) {
            return;
          }
          this.sudoku_id = puzzle._id;
          this.values = puzzle.values;
          if(puzzle.inputs !== undefined){
            this.inputs = puzzle.inputs;
          }
          this.setupBoard();
        });
      }
    },
    async exportPuzzle() {
      window.open("/api/export/" + this.sudoku_id);
    },
  },
  // get from backend done in mounted as page need access to rendered elements
  async mounted() {
    // if(!this.$cookies.isKey("user_id") || this.$cookies.get("user_id")==""){
    //   alert("Please login to an account to play a puzzle.")
    //   window.href.location('/auth/authorize');
    //   return;
    // }
    // should be get everytime 'play' is pressed
    document.addEventListener("keydown", this.typeInput);
    this.loadPuzzle(this.puzzleID);
  },
  beforeDestroy() {
    document.removeEventListener("keydown", this.typeInput);
  },
};
</script>
