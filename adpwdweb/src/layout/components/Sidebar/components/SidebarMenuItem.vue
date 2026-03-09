<template>
  <div v-if="!item.meta || !item.meta.hidden">
    <!--【葉子節點】顯示葉子節點或唯一子節點且父節點未配置始終顯示 -->
    <template
      v-if="
        // 未配置始終顯示，使用唯一子節點替換父節點顯示為葉子節點
        (!item.meta?.alwaysShow &&
          hasOneShowingChild(item.children, item) &&
          (!onlyOneChild.children || onlyOneChild.noShowingChildren)) ||
        // 即使配置了始終顯示，但無子節點，也顯示為葉子節點
        (item.meta?.alwaysShow && !item.children)
      "
    >
      <AppLink
        v-if="onlyOneChild.meta"
        :to="{
          path: resolvePath(onlyOneChild.path),
          query: onlyOneChild.meta.params,
        }"
      >
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }"
        >
          <SidebarMenuItemTitle
            :icon="onlyOneChild.meta.icon || item.meta?.icon"
            :title="onlyOneChild.meta.title"
          />
        </el-menu-item>
      </AppLink>
    </template>

    <!--【非葉子節點】顯示含多個子節點的父菜單，或始終顯示的單子節點 -->
    <el-sub-menu v-else :index="resolvePath(item.path)" teleported>
      <template #title>
        <SidebarMenuItemTitle v-if="item.meta" :icon="item.meta.icon" :title="item.meta.title" />
      </template>

      <SidebarMenuItem
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "SidebarMenuItem",
  inheritAttrs: false,
});

import path from "path-browserify";
import { RouteRecordRaw } from "vue-router";

import { isExternal } from "@/utils";

const props = defineProps({
  /**
   * 當前路由對象
   */
  item: {
    type: Object as PropType<RouteRecordRaw>,
    required: true,
  },

  /**
   * 父級完整路徑
   */
  basePath: {
    type: String,
    required: true,
  },

  /**
   * 是否為嵌套路由
   */
  isNest: {
    type: Boolean,
    default: false,
  },
});

// 可見的唯一子節點
const onlyOneChild = ref();

/**
 * 檢查是否僅有一個可見子節點
 *
 * @param children 子路由數組
 * @param parent 父級路由
 * @returns 是否僅有一個可見子節點
 */
function hasOneShowingChild(children: RouteRecordRaw[] = [], parent: RouteRecordRaw) {
  // 過濾出可見子節點
  const showingChildren = children.filter((route: RouteRecordRaw) => {
    if (!route.meta?.hidden) {
      onlyOneChild.value = route;
      return true;
    }
    return false;
  });

  // 僅有一個節點
  if (showingChildren.length === 1) {
    return true;
  }

  // 無子節點時
  if (showingChildren.length === 0) {
    // 父節點設置為唯一顯示節點，並標記為無子節點
    onlyOneChild.value = { ...parent, path: "", noShowingChildren: true };
    return true;
  }
  return false;
}

/**
 * 獲取完整路徑，適配外部鏈接
 *
 * @param routePath 路由路徑
 * @returns 絕對路徑
 */
function resolvePath(routePath: string) {
  if (isExternal(routePath)) return routePath;
  if (isExternal(props.basePath)) return props.basePath;

  // 拼接父路徑和當前路徑
  return path.resolve(props.basePath, routePath);
}
</script>

<style lang="scss">
.hideSidebar {
  .submenu-title-noDropdown {
    position: relative;
    padding: 0 !important;

    .el-tooltip {
      padding: 0 !important;

      .sub-el-icon {
        margin-left: 19px;
      }
    }

    & > span {
      display: inline-block;
      width: 0;
      height: 0;
      overflow: hidden;
      visibility: hidden;
    }
  }

  .el-sub-menu {
    overflow: hidden;

    & > .el-sub-menu__title {
      padding: 0 !important;

      .sub-el-icon {
        margin-left: 19px;
      }

      .el-sub-menu__icon-arrow {
        display: none;
      }
    }
  }

  .el-menu--collapse {
    width: $sidebar-width-collapsed;

    .el-sub-menu {
      & > .el-sub-menu__title > span {
        display: inline-block;
        width: 0;
        height: 0;
        overflow: hidden;
        visibility: hidden;
      }
    }
  }
}

.el-menu-item:hover {
  background-color: $menu-hover;
}
</style>
