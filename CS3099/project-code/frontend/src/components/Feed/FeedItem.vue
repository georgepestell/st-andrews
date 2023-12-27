<script setup>
import SudokuBoard from "@/components/Sudoku/Board/DisplayBoard.vue";
</script>

<template>
  <div class="feedItem">
    <div class="feedItem-thumbnail">
      <!-- v-if="view_mode=='PLAY'" -->
      <router-link
        :to="{
          name: 'Play',
          params: {
            puzzleID: details._id,
          },
        }"
      >
        <SudokuBoard
          :board="details.values"
          :empty="empty"
          :solution="solution"
        />
      </router-link>
    </div>
    <!-- <div class="feedItem-thumbnail" v-if="view_mode=='MANAGE'">
      <SudokuBoard :board="values" :empty="empty" />
    </div> -->
    <div class="feedItem-meta">
      <div class="feedItem-icon"></div>
      <div class="feedItem-info">
        <div>{{ details.puzzleType }}</div>
        <div class="feedItem-user">
          <div class="feedItem-user-pp"></div>
          <div class="feedItem-user-name">
            by {{ details.creator.displayName }}
          </div>
        </div>
      </div>
    </div>
    <div v-if="view_mode == 'MANAGE'" class="feedItem-manage">
      <div class="solution-delete">
        <div 
          class="solution-btn btn"
          @click="getSolution"
        >
          <span v-if="showSolution">hide solution</span>
          <span v-else>show solution</span>
        </div>
        <div class="delete-btn btn" @click="removePuzzle">
          <font-awesome-icon icon="fa-solid fa-trash" />
        </div>
      </div>
      <div v-if="showDetails" class="feedItem-details">
        <div class="row">
          <span class="field">ID:</span>
          <span class="value">{{ details._id }}</span>
        </div>
        <div class="row">
          <span class="field">Puzzle Type:</span>
          <span class="value">{{ details.puzzleType }}</span>
        </div>
        <div class="row">
          <span class="field">Creation Date:</span>
          <span class="value">{{ details.creationDate }}</span>
        </div>
        <div class="row">
          <span class="field">Creator ID:</span>
          <span class="value">{{ details.creator._id }}</span>
        </div>
      </div>
      <div class="detail-btn btn" @click="showPuzzleDetail">
        <font-awesome-icon v-if="showDetails" icon="fa-caret-up" />
        <font-awesome-icon v-else icon="fa-caret-down" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    view_mode: String,
    empty: {
      type: String,
      default: "0",
    },
    details: {
      default: {
        puzzleType: "Sudoku",
        creator: {
          _id: "G24-XX",
          displayName: "John Smith",
          group: 24,
        },
      },
    },
    solution: Array,
  },
  data() {
    return {
      showSolution: false,
      showDetails: false,
    };
  },
  methods: {
    async removePuzzle() {
      this.$emit("remove");
    },
    async showPuzzleDetail() {
      this.showDetails = !this.showDetails;
      // this.$emit('showDetails')
    },
    async getSolution() {
      this.showSolution = !this.showSolution;
      if (this.showSolution) {
        this.$emit("getSol");
      } else {
        this.$emit("delSol");
      }
    },
  },
};
</script>
