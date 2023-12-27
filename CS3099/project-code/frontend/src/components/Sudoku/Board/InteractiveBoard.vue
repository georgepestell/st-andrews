<script setup>
import "@/sass/components/SudokuBoard.scss";
</script>

<template>
  <div class="board-container">
    <div class="board">
      <div v-for="(cells, row) in board" :key="row" class="board-row">
        <div
          v-for="(cell, column) in cells"
          :key="column"
          @click="clickCellHandler(cell)"
          class="board-cell"
          :class="{
            fixed: cell.fixed,
            selected: cell == selected_cell,
            highlighted: cell.highlighted,
            highlightedFixed: cell.highlighted && cell.fixed,
            error: cell.error,
            selectedValue: cell.valueIsSelected,
            selectedValueFixed: cell.valueIsSelected && cell.fixed,
          }"
        >
          <div
            v-if="cell.value == EMPTY_VALUE && cell.notes"
            class="cell-notes"
          >
            <div
              v-for="input in inputs"
              :key="input"
              class="cell-note"
              :class="{
                active: cell.notes.includes(input),
                highlighted: input == cellValue(selected_cell),
              }"
            >
              {{ input }}
            </div>
          </div>
          <div v-else class="cell-value">
            {{ printCell(cell) }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    EMPTY_VALUE: {
      default: "0",
      String,
    },
    board: Array,
    inputs: Array,
    selected_cell: Object,
  },
  methods: {
    clickCellHandler(cell) {
      this.$emit("select-cell", cell);
      this.highlight(cell);
      this.checkForErrors()
    },
    printCell(cell) {
      return cell.value != this.EMPTY_VALUE ? cell.value : " ";
    },
    cellValue(cell) {
      if (cell != undefined) {
        return cell.value;
      }
    },
    highlight(selectedCell) {
      for (var i = 0; i < this.board.length; i++) {
        var row = this.board[i];
        for (var j = 0; j < this.board.length; j++) {
          var cell = row[j];
          if (
            cell.row == selectedCell.row ||
            cell.column == selectedCell.column ||
            (cell.box.row == selectedCell.box.row &&
              cell.box.col == selectedCell.box.col)
          ) {
            cell.valueIsSelected = false;
            cell.highlighted = true;
          } else if (
            selectedCell.value != this.EMPTY_VALUE &&
            selectedCell.value != undefined &&
            selectedCell.value == cell.value
          ) {
            cell.highlighted = false;
            cell.valueIsSelected = true;
          } else {
            cell.highlighted = false;
            cell.valueIsSelected = false;
          }
        }
      }
    },
    checkForErrors() {
      for (var i = 0; i < this.board.length; i++) {
        var row = this.board[i];
        for (var j = 0; j < row.length; j++) {
          var cell = row[j];
          cell.error = false;
          if (cell.value != this.EMPTY_VALUE && cell.value != undefined) {
            this.checkContradictions(cell);
          }
        }
      }
    },
    checkContradictions(checkingCell) {
      for (var i = 0; i < this.board.length; i++) {
        var row = this.board[i];
        for (var j = 0; j < this.board.length; j++) {
          var cell = row[j];
          if (checkingCell != cell && checkingCell.value == cell.value) {
            if (checkingCell.row == cell.row || checkingCell.column == cell.column || (checkingCell.box.row == cell.box.row && checkingCell.box.col == cell.box.col)) {  
              checkingCell.error = true;
            }
          }
        }
      }
    },
    async typeInput(event){
      this.$emit('type-num')
      const cell = this.selected_cell
      if(event.which>=37 && event.which<=40 && this.selected_cell !== undefined) {
        switch (event.which) {
          case 37: //left
            if(cell.column>0){
              this.clickCellHandler(this.board[cell.row][cell.column-1])
            }
            break;
          case 38: //up
            if(cell.row>0){
              this.clickCellHandler(this.board[cell.row-1][cell.column])
            }
            break;

          case 39: //right
            if(cell.column<8){
              this.clickCellHandler(this.selectedCell=this.board[cell.row][cell.column+1])
            }
            break;
          case 40: //down
            if(cell.row<8){
              this.clickCellHandler(this.selectedCell=this.board[cell.row+1][cell.column])
            }
            break;
          default:
            break;
        }
      }
    }
  },
  async mounted() {
    // if(!this.$cookies.isKey("user_id") || this.$cookies.get("user_id")==""){
    //   alert("Please login to an account to play a puzzle.")
    //   window.href.location('/auth/authorize');
    //   return;
    // }
    // should be get everytime 'play' is pressed
    document.addEventListener("keydown", this.typeInput);
    // this.loadPuzzle(this.puzzleID);
  },
  beforeDestroy() {
    document.removeEventListener("keydown", this.typeInput);
  },
};
</script>
