<template>
  <svg aria-hidden="true" class="svg-icon" :style="'width:' + size + ';height:' + size">
    <use :xlink:href="symbolId" :fill="color" />
  </svg>
</template>

<script setup lang="ts">
const props = defineProps({
  prefix: {
    type: String,
    default: "icon",
  },
  iconClass: {
    type: String,
    required: false,
    default: "",
  },
  color: {
    type: String,
    default: "",
  },
  size: {
    type: String,
    default: "1em",
  },
});

const symbolId = computed(() => `#${props.prefix}-${props.iconClass}`);
</script>

<style scoped>
.svg-icon {
  display: inline-block;
  width: 1em;
  height: 1em;
  overflow: hidden;
  vertical-align: -0.15em; /* 因icon大小被設置為和字體大小一致，而span等標籤的下邊緣會和字體的基線對齊，故需設置一個往下的偏移比例，來糾正視覺上的未對齊效果 */
  outline: none;
  fill: currentcolor; /* 定義元素的顏色，currentColor是一個變量，這個變量的值就表示當前元素的color值，如果當前元素未設置color值，則從父元素繼承 */
}
</style>
