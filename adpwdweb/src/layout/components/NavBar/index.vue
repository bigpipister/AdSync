<template>
  <div class="navbar">
    <div class="navbar__left">
      <!-- 展開/收縮菜單  -->
      <Hamburger :is-active="isSidebarOpened" @toggle-click="toggleSideBar" />
      <!-- 麵包屑 -->
      <breadcrumb />
    </div>
    <!-- 導航欄右側 -->
    <NavbarRight />
  </div>
</template>

<script setup lang="ts">
import { useAppStore } from "@/store";

const appStore = useAppStore();

// 側邊欄是否打開
const isSidebarOpened = computed(() => appStore.sidebar.opened);

// 展開/收縮菜單
function toggleSideBar() {
  appStore.toggleSidebar();
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  height: $navbar-height;
  background: var(--el-bg-color);

  &__left {
    display: flex;
    align-items: center;
  }
}
</style>
