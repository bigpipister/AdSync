import type { RouteRecordRaw } from "vue-router";
import { constantRoutes } from "@/router";
import { store } from "@/store";
import router from "@/router";

import { type RouteVO } from "@/api/system/menu";
const modules = import.meta.glob("../../views/**/**.vue");
const Layout = () => import("@/layout/index.vue");

export const usePermissionStore = defineStore("permission", () => {
  // 所有路由，包括靜態和動態路由
  const routes = ref<RouteRecordRaw[]>([]);
  // HE:不用動態路由，直接設靜態路由
  routes.value = constantRoutes
  // 混合模式左側菜單
  const mixLeftMenus = ref<RouteRecordRaw[]>([]);
  // 路由是否已加載
  const isRoutesLoaded = ref(false);
  // HE:不用動態路由，直接設載入完成
  isRoutesLoaded.value = true;

  /**
   * 混合模式菜單下根據頂部菜單路徑設置左側菜單
   *
   * @param topMenuPath - 頂部菜單路徑
   */
  const setMixLeftMenus = (topMenuPath: string) => {
    const matchedItem = routes.value.find((item) => item.path === topMenuPath);
    if (matchedItem && matchedItem.children) {
      mixLeftMenus.value = matchedItem.children;
    }
  };

  /**
   * 重置路由
   */
  const resetRouter = () => {
    // 刪除動態路由，保留靜態路由
    routes.value.forEach((route) => {
      if (route.name && !constantRoutes.find((r) => r.name === route.name)) {
        // 從 router 實例中移除動態路由
        router.removeRoute(route.name);
      }
    });

    routes.value = [];
    mixLeftMenus.value = [];
    isRoutesLoaded.value = false;
  };

  return {
    routes,
    mixLeftMenus,
    setMixLeftMenus,
    isRoutesLoaded,
    resetRouter,
  };
});

/**
 * 轉換路由數據為組件
 */
const transformRoutes = (routes: RouteVO[]) => {
  const asyncRoutes: RouteRecordRaw[] = [];
  routes.forEach((route) => {
    const tmpRoute = { ...route } as RouteRecordRaw;
    // 頂級目錄，替換為 Layout 組件
    if (tmpRoute.component?.toString() == "Layout") {
      tmpRoute.component = Layout;
    } else {
      // 其他菜單，根據組件路徑動態加載組件
      const component = modules[`../../views/${tmpRoute.component}.vue`];
      if (component) {
        tmpRoute.component = component;
      } else {
        tmpRoute.component = modules["../../views/error-page/404.vue"];
      }
    }

    if (tmpRoute.children) {
      tmpRoute.children = transformRoutes(route.children);
    }

    asyncRoutes.push(tmpRoute);
  });

  return asyncRoutes;
};

/**
 * 在組件外使用 Pinia store 實例 @see https://pinia.vuejs.org/core-concepts/outside-component-usage.html
 */
export function usePermissionStoreHook() {
  return usePermissionStore(store);
}
