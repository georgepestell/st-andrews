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
          class="board-cell"
          :class="{ fixed: cell != EMPTY_VALUE }"
        >
          <div class="cell-value">
            {{ printCell(cell, row, column) }}
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
      type: String,
      default: "0",
    },
    board: Array,
    solution: Array,
  },
  methods: {
    printCell(cell, row, column) {
      if(this.solution === undefined){
        return cell != 0 ? cell : "";
      } else {
        return cell != 0 ? cell : this.solution[row][column];
      }
      
    },
  },
};
</script>
