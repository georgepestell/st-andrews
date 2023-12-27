<script setup>
import FeedItem from "@/components/Feed/FeedItem.vue";
import PuzzleService from "@/services/PuzzleService.js";
import AdminService from "@/services/AdminService.js"
import "@/sass/components/PuzzleFeed.scss";
import FeedItemPlaceholder from "./FeedItemPlaceholder.vue";
</script>

<template>
  <div class="puzzleFeed">
      <FeedItem
        v-for="puzzle in puzzles"
        :view_mode="view_mode"
        :key="puzzle._id"
        :details="puzzle"
        @remove="removePuzzle(puzzle._id)"
        @getSol="getSolution(puzzle._id)"
        @delSol="deleteSolution(puzzle._id)"
        :solution="puzzleSolutions[puzzle._id]"
      />
      <FeedItemPlaceholder
        v-for="placeholder in limit - puzzles.length"
        :key="placeholder"
      />
  </div>
  <div class="puzzleFeed-pageControls">
    <button @click="previousPage">
      <font-awesome-icon
        alt="previous"
        title="previous"
        icon="fa-solid fa-arrow-left"
      />
    </button>
    <div class="pageControls-currentPage">{{ page }}</div>
    <button @click="nextPage">
      <font-awesome-icon
        alt="previous"
        title="previous"
        icon="fa-solid fa-arrow-right"
      />
    </button>
  </div>
</template>

<script>
export default {
  props:{
    view_mode: String, // MANAGE or PLAY
  },
  data() {
    return {
      page: 1,
      limit: 6,
      puzzles: [],
      puzzleSolutions: {},
    };
  },
  mounted() {
    this.loadPuzzles(this.page);
  },
  methods: {
    async loadPuzzles(page) {
      return PuzzleService.getPuzzles(this.limit, page).then((puzzles) => {
        if (puzzles && puzzles.length > 0) {
          this.puzzles = puzzles;
          return true;
        }
        return false;
      });
    },
    previousPage() {
      if (this.page == 1) {
        return;
      }
      this.loadPuzzles(this.page - 1).then((result) => {
        console.log(result);
        if (result) {
          this.page--;
        }
      });
    },
    async nextPage() {
      this.loadPuzzles(this.page + 1).then((result) => {
        console.log(result);
        if (result) {
          this.page++;
        }
      });
    },
    async removePuzzle(id) {
      if(confirm(`Do you want to remove puzzle (id: ${id}) from the database?`)){
        await AdminService.removePuzzle(id)
          .then((res) => {
            // check response
            // alert(res.data.message)
            this.loadPuzzles(this.page)
          })
          .catch((err) => {
            alert(err.response.data.message)
          })
      }
    },
    async getSolution(id){
      await AdminService.getPuzzleSolution(id)
        .then((res) => {
          this.puzzleSolutions[id] = res.data.solution
        })
        .catch((err) => {
          alert(err.response.data.message)
        })
    },
    async deleteSolution(id){
      delete this.puzzleSolutions[id]
    }
  },
};
</script>
