<script setup>
import "@/sass/components/GameControls.scss";
</script>

<template class="controls-container">
  <div class="controls-container">
    <div class="control-container value-controls-container">
      <div class="control-btn-container" v-for="input in inputs" :key="input">
        <div
          @click="inputControlClickHandler(input)"
          class="control-btn"
          :class="{ selected: isSelectedInput(input) }"
        >
          {{ input }}
        </div>
      </div>
    </div>
    <div class="control-container function-controls-container">
      <div class="control-btn-container">
        <div
          class="control-btn reset-btn"
          :class="{ spin: resetAnimation }"
          @click="resetControlClickHandler"
        >
          <font-awesome-icon icon="fa-solid fa-rotate-left" />
        </div>
      </div>
      <div v-if="$attrs.onToggleNoteMode" class="control-btn-container">
        <div
          class="control-btn"
          @click="noteModeControlClickHandler"
          :class="{ selected: note_mode }"
        >
          <font-awesome-icon icon="fa-solid fa-pencil" />
        </div>
      </div>
      <div class="control-btn-container export-btn" v-if="$attrs.onExport">
        <div class="control-btn" @click="exportControlClickHandler">
          <font-awesome-icon icon="fa-solid fa-download" />        
        </div>
      </div>
      <div class="control-btn-container">
        <div v-if="$attrs.onUpload" class="control-btn upload-btn">
          <input
            type="file"
            accept="text/json,.json"
            @change="uploadHandler"
            name="upload"
          />
          <font-awesome-icon alt="upload" icon="fa-solid fa-upload" />
        </div>
      </div>
    </div>
    <div class="control-container">

      <div 
        class="control-btn-container submit-btn" 
        v-if="$attrs.onSubmit"
      >
        <div
          class="control-btn"
          @click="submitControlClickHandler"
        >
          Submit
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    note_mode: Boolean,
    inputs: {
      type: Array[String],
      default: ["1", "2", "3", "4", "5", "6", "7", "8", "9"],
    },
    selected_input: {
      type: String,
      default: "0",
    },
  },
  data() {
    return {
      resetAnimation: false,
      resetAnimationDuration: "0.4s",
    };
  },
  methods: {
    isSelectedInput(input) {
      return input == this.selected_input;
    },
    inputControlClickHandler(input) {
      this.$emit("selectInput", input);
    },
    resetControlClickHandler() {
      if(confirm("Are you sure you want to clear the board and reset the puzzle?")){
        if (this.resetAnimation == false) {
          this.$emit("reset");

          this.resetAnimation = true;

          // Prevent reset animation being interrupted while running
          setTimeout(() => {
            this.resetAnimation = false;
          }, this.resetAnimationDuration.slice(0, this.resetAnimationDuration.length - 1) * 1000);
        }
      }
    },
    noteModeControlClickHandler() {
      this.$emit("toggleNoteMode");
    },
    submitControlClickHandler() {
      this.$emit("submit");
    },
    uploadHandler(event) {
      if (event.target.files.length > 0) {
        this.$emit("upload", event.target.files[0]);
      }
    },
    exportControlClickHandler() {
      this.$emit("export");
    },
  },
};
</script>

<style scoped lang="scss">
@import "@/sass/base/_vars.scss";

.spin > * {
  animation-duration: v-bind("resetAnimationDuration");
}
</style>
