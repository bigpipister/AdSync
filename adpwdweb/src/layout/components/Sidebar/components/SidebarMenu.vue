<!-- 菜單組件 -->
<template>
  <el-menu
    ref="menuRef"
    :default-active="currentRoute.path"
    :collapse="!appStore.sidebar.opened"
    :background-color="variables['menu-background']"
    :text-color="variables['menu-text']"
    :active-text-color="variables['menu-active-text']"
    :unique-opened="false"
    :collapse-transition="false"
    :mode="menuMode"
    @open="onMenuOpen"
    @close="onMenuClose"
  >
    <!-- 菜單項 -->
    <SidebarMenuItem
      v-for="route in menuList"
      :key="route.path"
      :item="route"
      :base-path="resolveFullPath(route.path)"
    />
  </el-menu>
</template>

<script lang="ts" setup>
import path from "path-browserify";
import type { MenuInstance } from "element-plus";

import { LayoutEnum } from "@/enums/LayoutEnum";
import { useSettingsStore, useAppStore } from "@/store";
import { isExternal } from "@/utils/index";

import variables from "@/styles/variables.module.scss";

const props = defineProps({
  menuList: {
    type: Array<any>,
    required: true,
    default: () => [],
  },
  basePath: {
    type: String,
    required: true,
    example: "/system",
  },
});

const menuRef = ref<MenuInstance>();
const settingsStore = useSettingsStore();
const appStore = useAppStore();
const currentRoute = useRoute();

// 存儲已展開的菜單項索引
const expandedMenuIndexes = ref<string[]>([]);

// 根據佈局模式設置菜單的顯示方式：頂部佈局使用水平模式，其他使用垂直模式
const menuMode = computed(() => {
  return settingsStore.layout === LayoutEnum.TOP ? "horizontal" : "vertical";
});

/**
 * 獲取完整路徑
 *
 * @param routePath 當前路由的相對路徑  /user
 * @returns 完整的絕對路徑 D://vue3-element-admin/system/user
 */
function resolveFullPath(routePath: string) {
  if (isExternal(routePath)) {
    return routePath;
  }
  if (isExternal(props.basePath)) {
    return props.basePath;
  }

  // 解析路徑，生成完整的絕對路徑
  return path.resolve(props.basePath, routePath);
}

/**
 * 打開菜單
 *
 * @param index 當前展開的菜單項索引
 */
const onMenuOpen = (index: string) => {
  expandedMenuIndexes.value.push(index);
};

/**
 * 關閉菜單
 *
 * @param index 當前收起的菜單項索引
 */
const onMenuClose = (index: string) => {
  expandedMenuIndexes.value = expandedMenuIndexes.value.filter((item) => item !== index);
};

/**
 * 監聽菜單模式變化：當菜單模式切換為水平模式時，關閉所有展開的菜單項，
 * 避免在水平模式下菜單項顯示錯位。
 *
 * @see https://gitee.com/youlaiorg/vue3-element-admin/issues/IAJ1DR
 */
watch(
  () => menuMode.value,
  () => {
    if (menuMode.value === "horizontal") {
      expandedMenuIndexes.value.forEach((item) => menuRef.value!.close(item));
    }
  }
);
</script>
