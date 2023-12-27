<script setup>
import "@/sass/components/Help.scss";
</script>

<template>
  <div id="btn-help" @click="toggleShow" class="btn-icon">
    <font-awesome-icon icon="fa-solid fa-info" />
  </div>
  <div id="help" class="sidebar" :class="{ show: show }">
    <div class="sidebar-header">
      <h1>{{ title }}</h1>
      <div class="btn-exit btn-icon" @click="toggleShow">
        <font-awesome-icon icon="fa-solid fa-xmark" />
      </div>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
  },
  data() {
    return {
      show: false,
    };
  },
  methods: {
    toggleShow() {
      this.show = !this.show;
    },
  },
};
</script>
