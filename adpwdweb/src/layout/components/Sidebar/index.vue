<template>
  <div :class="{ 'has-logo': sidebarLogo }">
    <!-- 混合佈局頂部  -->
    <div v-if="isMixLayout" class="flex w-full">
      <SidebarLogo v-if="sidebarLogo" :collapse="isSidebarCollapsed" />
      <SidebarMixTopMenu class="flex-1" />
      <NavbarRight />
    </div>

    <!-- 頂部佈局頂部 || 左側佈局左側 -->
    <template v-else>
      <SidebarLogo v-if="sidebarLogo" :collapse="isSidebarCollapsed" />
      <el-scrollbar>
        <SidebarMenu :menu-list="permissionStore.routes" base-path="" />
      </el-scrollbar>

      <!-- 頂部佈局導航 -->
      <NavbarRight v-if="isTopLayout" />
    </template>
  </div>
</template>

<script setup lang="ts">
import { LayoutEnum } from "@/enums/LayoutEnum";
import { useSettingsStore, usePermissionStore, useAppStore } from "@/store";

import NavbarRight from "../NavBar/components/NavbarRight.vue";

const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();
//console.log(permissionStore.routes.length);

const sidebarLogo = computed(() => settingsStore.sidebarLogo);
const layout = computed(() => settingsStore.layout);

const isMixLayout = computed(() => layout.value === LayoutEnum.MIX);
const isTopLayout = computed(() => layout.value === LayoutEnum.TOP);
const isSidebarCollapsed = computed(() => !appStore.sidebar.opened);
</script>

<style lang="scss" scoped>
.has-logo {
  .el-scrollbar {
    height: calc(100vh - $navbar-height);
  }
}
</style>
