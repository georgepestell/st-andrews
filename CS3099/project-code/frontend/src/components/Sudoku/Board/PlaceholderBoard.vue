<template>
  <div class="board-container">
    <div class="board">
      <div v-for="row in 3" :key="row" class="placeholder-board-row">
        <div
          v-for="column in 3"
          :key="column"
          class="placeholder-board-column"
        ></div>
      </div>
    </div>
  </div>
</template>
