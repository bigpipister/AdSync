import type { App } from "vue";
import { createRouter, createWebHistory, createWebHashHistory, type RouteRecordRaw } from "vue-router";

export const Layout = () => import("@/layout/index.vue");

// 靜態路由
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: Layout,
    meta: { hidden: true },
    children: [
      {
        path: "/redirect/:path(.*)",
        component: () => import("@/views/redirect/index.vue"),
      },
    ],
  },

  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
    meta: { hidden: true },
  },

  {
    path: "/",
    name: "/",
    component: Layout,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        component: () => import("@/views/dashboard/index.vue"),
        // 用於 keep-alive 功能，需要與 SFC 中自動推導或顯式聲明的組件名稱一致
        // 參考文檔: https://cn.vuejs.org/guide/built-ins/keep-alive.html#include-exclude
        name: "Dashboard",
        meta: {
          title: "dashboard",
          icon: "homepage",
          affix: true,
          keepAlive: true,
        },
      },
      {
        path: "401",
        component: () => import("@/views/error/401.vue"),
        meta: { hidden: true },
      },
      {
        path: "404",
        component: () => import("@/views/error/404.vue"),
        meta: { hidden: true },
      },
    ],
  },

  {
    path: "/event",
    component: Layout,
    name: "/event",
    meta: {
      title: "事件管理",
      icon: "menu",
      hidden: false,
      alwaysShow: false,
      params: null,
    },
    children: [
      {
        path: "event",
        component: () => import("@/views/backend/event/index.vue"),
        name: "event",
        meta: {
          title: "事件記錄",
          icon: "menu",
          hidden: false,
          keepAlive: true,
          alwaysShow: false,
          params: null,
        },
      },
    ],
  },

  {
    path: "/audit",
    component: Layout,
    name: "/audit",
    meta: {
      title: "稽核記錄",
      icon: "menu",
      hidden: false,
      alwaysShow: false,
      params: null,
    },
    children: [
      {
        path: "audit",
        component: () => import("@/views/backend/audit/index.vue"),
        name: "audit",
        meta: {
          title: "稽核記錄",
          icon: "menu",
          hidden: false,
          keepAlive: true,
          alwaysShow: false,
          params: null,
        },
      },
    ],
  },

  {
    path: "/pwd",
    component: Layout,
    name: "/pwd",
    meta: {
      title: "密碼不同步",
      icon: "menu",
      hidden: false,
      alwaysShow: false,
      params: null,
    },
    children: [
      {
        path: "pwd",
        component: () => import("@/views/backend/pwd/index.vue"),
        name: "pwd",
        meta: {
          title: "密碼不同步",
          icon: "menu",
          hidden: false,
          keepAlive: true,
          alwaysShow: false,
          params: null,
        },
      },
    ],
  },

  {
    path: "/properties",
    component: Layout,
    name: "/properties",
    meta: {
      title: "屬性不同步",
      icon: "menu",
      hidden: false,
      alwaysShow: false,
      params: null,
    },
    children: [
      {
        path: "properties",
        component: () => import("@/views/backend/properties/index.vue"),
        name: "properties",
        meta: {
          title: "屬性不同步",
          icon: "menu",
          hidden: false,
          keepAlive: true,
          alwaysShow: false,
          params: null,
        },
      },
    ],
  },

  {
    path: "/decrypt",
    component: Layout,
    name: "/decrypt",
    meta: {
      title: "密碼反解",
      icon: "menu",
      hidden: false,
      alwaysShow: false,
      params: null,
    },
    children: [
      {
        path: "decrypt",
        component: () => import("@/views/backend/decrypt/index.vue"),
        name: "decrypt",
        meta: {
          title: "密碼反解",
          icon: "menu",
          hidden: false,
          keepAlive: true,
          alwaysShow: false,
          params: null,
        },
      },
    ],
  },
];

/**
 * 創建路由
 */
const router = createRouter({
  //history: createWebHashHistory(),
  // 改成history mode
  history: createWebHistory('/adws/status'),
  routes: constantRoutes,
  // 刷新時，滾動條位置還原
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// 全局註冊 router
export function setupRouter(app: App<Element>) {
  app.use(router);
}

export default router;
