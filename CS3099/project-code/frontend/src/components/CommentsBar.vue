<script setup>
import "@/sass/components/CommentsBar.scss";
import Comment from "@/components/Comment.vue";
import PuzzleService from "@/services/PuzzleService.js";
</script>

<template>
  <div id="comments-bar-icon" class="btn-icon" @click="toggleShow">
    <font-awesome-icon icon="fa-solid fa-comments" />
  </div>
  <div class="comments-bar sidebar" :class="{ show: show }">
    <div class="sidebar-header">
      <div class="btn-exit btn-icon" @click="toggleShow">
        <font-awesome-icon icon="fa-solid fa-xmark" />
      </div>
      <h1>Comments</h1>
    </div>
    <div class="comments">
      <Comment
        v-for="(comment, key) in comments"
        :key="key"
        :user="comment.user"
        :content="comment.comment"
        :date="comment.commentDate"
        :id="comment._id"
      />
    </div>
    <div class="new-comment">
      <textarea v-model="newComment" />
      <div class="btn-submit" @click="postComment">Post</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    puzzleID: String,
  },
  data() {
    return {
      comments: [],
      show: false,
      page: 1,
      limit: 10,
      newComment: "",
    };
  },
  methods: {
    async toggleShow() {
      if (!this.show && this.comments.length == 0) {
        this.loadComments();
      }

      this.show = !this.show;
    },
    async postComment() {
      if (this.newComment.trim().length < 4) {
        alert("Comment must contain at least 4 characters!");
        return;
      }

      const res = await PuzzleService.postComment(
        this.puzzleID,
        this.newComment
      );

      if (!res) {
        alert("Something went wrong.");
        return;
      }

      this.newComment = "";

      this.page = 1;
      this.loadComments();
    },
    async loadComments() {
      const comments = await PuzzleService.getComments(
        this.puzzleID,
        this.limit,
        this.page
      );

      console.log(comments);

      if (comments) {
        this.comments = comments;
      }
    },
  },
};
</script>
