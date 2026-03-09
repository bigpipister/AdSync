import type { NavigationGuardNext, RouteLocationNormalized, RouteRecordRaw } from "vue-router";
import NProgress from "@/utils/nprogress";
import { getToken } from "@/utils/auth";
import router from "@/router";
import { usePermissionStore, useUserStore } from "@/store";

export function setupPermission() {
  // 白名單路由
  const whiteList = ["/login"];

  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const isLogin = !!getToken(); // 判斷是否登錄
    if (isLogin) {
      if (to.path === "/login") {
        // 已登錄，訪問登錄頁，跳轉到首頁
        next({ path: "/" });
      } else {
        const permissionStore = usePermissionStore();
        // 判斷路由是否加載完成
        if (permissionStore.isRoutesLoaded) {
          if (to.matched.length === 0) {
            // 路由未匹配，跳轉到404
            next("/404");
          } else {
            // 動態設置頁面標題
            const title = (to.params.title as string) || (to.query.title as string);
            if (title) {
              to.meta.title = title;
            }
            next();
          }
        } else {
          try {
            // 生成動態路由
            // HE:不用動態路由
            //const dynamicRoutes = await permissionStore.generateRoutes();
            //dynamicRoutes.forEach((route: RouteRecordRaw) => router.addRoute(route));

            //console.log(router.getRoutes());
            next({ ...to, replace: true });
          } catch (error) {
            console.error(error);
            // 路由加載失敗，重置 token 並重定向到登錄頁
            await useUserStore().clearUserData();
            redirectToLogin(to, next);
            NProgress.done();
          }
        }
      }
    } else {
      // 未登錄，判斷是否在白名單中
      if (whiteList.includes(to.path)) {
        next();
      } else {
        // 不在白名單，重定向到登錄頁
        redirectToLogin(to, next);
        NProgress.done();
      }
    }
  });

  // 後置守衛，保證每次路由跳轉結束時關閉進度條
  router.afterEach(() => {
    NProgress.done();
  });
}

// 重定向到登錄頁
function redirectToLogin(to: RouteLocationNormalized, next: NavigationGuardNext) {
  const params = new URLSearchParams(to.query as Record<string, string>);
  const queryString = params.toString();
  const redirect = queryString ? `${to.path}?${queryString}` : to.path;
  next(`/login?redirect=${encodeURIComponent(redirect)}`);
}

/** 判斷是否有權限 */
export function hasAuth(value: string | string[], type: "button" | "role" = "button") {
  const { roles, perms } = useUserStore().userInfo;

  // 超級管理員 擁有所有權限
  if (type === "button" && roles.includes("ROOT")) {
    return true;
  }

  const auths = type === "button" ? perms : roles;
  return typeof value === "string"
    ? auths.includes(value)
    : value.some((perm) => auths.includes(perm));
}
