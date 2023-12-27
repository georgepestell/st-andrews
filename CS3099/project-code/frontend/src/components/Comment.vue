<template>
  <div class="comment">
    <div class="comment-header">
      <div class="comment-user">
        {{ user.displayName }} @ G{{ user.groupID }}
      </div>
      <div class="comment-meta">{{ printDate }}</div>
    </div>
    <div class="comment-content">{{ content }}</div>
  </div>
</template>

<script>
import moment from "moment";
export default {
  props: {
    id: String,
    date: String,
    user: {
      default: {
        displayName: "John Smith",
        groupID: 24,
      },
    },
    content: {
      type: String,
      default: "Test Comment",
    },
  },
  computed: {
    printDate() {
      return moment(this.date).fromNow();
    },
  },
};
</script>
