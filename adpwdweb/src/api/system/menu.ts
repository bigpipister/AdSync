import type { MenuTypeEnum } from "@/enums/MenuTypeEnum";

/** 菜單查詢參數 */
export interface MenuQuery {
  /** 搜索關鍵字 */
  keywords?: string;
}

/** 菜單視圖對象 */
export interface MenuVO {
  /** 子菜單 */
  children?: MenuVO[];
  /** 組件路徑 */
  component?: string;
  /** ICON */
  icon?: string;
  /** 菜單ID */
  id?: string;
  /** 菜單名稱 */
  name?: string;
  /** 父菜單ID */
  parentId?: string;
  /** 按鈕權限標識 */
  perm?: string;
  /** 跳轉路徑 */
  redirect?: string;
  /** 路由名稱 */
  routeName?: string;
  /** 路由相對路徑 */
  routePath?: string;
  /** 菜單排序(數字越小排名越靠前) */
  sort?: number;
  /** 菜單 */
  type?: MenuTypeEnum;
  /** 菜單是否可見(1:顯示;0:隱藏) */
  visible?: number;
}

/** 菜單表單對象 */
export interface MenuForm {
  /** 菜單ID */
  id?: string;
  /** 父菜單ID */
  parentId?: string;
  /** 菜單名稱 */
  name?: string;
  /** 菜單是否可見(1-是 0-否) */
  visible: number;
  /** ICON */
  icon?: string;
  /** 排序 */
  sort?: number;
  /** 路由名稱 */
  routeName?: string;
  /** 路由路徑 */
  routePath?: string;
  /** 組件路徑 */
  component?: string;
  /** 跳轉路由路徑 */
  redirect?: string;
  /** 菜單 */
  type?: MenuTypeEnum;
  /** 權限標識 */
  perm?: string;
  /** 【菜單】是否開啟頁面緩存 */
  keepAlive?: number;
  /** 【目錄】只有一個子路由是否始終顯示 */
  alwaysShow?: number;
  /** 參數 */
  params?: KeyValue[];
}

interface KeyValue {
  key: string;
  value: string;
}

/** RouteVO，路由對象 */
export interface RouteVO {
  /** 子路由列表 */
  children: RouteVO[];
  /** 組件路徑 */
  component?: string;
  /** 路由屬性 */
  meta?: Meta;
  /** 路由名稱 */
  name?: string;
  /** 路由路徑 */
  path?: string;
  /** 跳轉鏈接 */
  redirect?: string;
}

/** Meta，路由屬性 */
export interface Meta {
  /** 【目錄】只有一個子路由是否始終顯示 */
  alwaysShow?: boolean;
  /** 是否隱藏(true-是 false-否) */
  hidden?: boolean;
  /** ICON */
  icon?: string;
  /** 【菜單】是否開啟頁面緩存 */
  keepAlive?: boolean;
  /** 路由title */
  title?: string;
}
