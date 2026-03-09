import "vue-router";

declare module "vue-router" {
  // https://router.vuejs.org/zh/guide/advanced/meta.html#typescript
  // 可以通過擴展 RouteMeta 接口來輸入 meta 字段
  interface RouteMeta {
    /**
     * 菜單名稱
     * @example 'Dashboard'
     */
    title?: string;

    /**
     * 菜單圖標
     * @example 'el-icon-edit'
     */
    icon?: string;

    /**
     * 是否隱藏菜單
     * true 隱藏, false 顯示
     * @default false
     */
    hidden?: boolean;

    /**
     * 始終顯示父級菜單，即使只有一個子菜單
     * true 顯示父級菜單, false 隱藏父級菜單，顯示唯一子節點
     * @default false
     */
    alwaysShow?: boolean;

    /**
     * 是否固定在頁簽上
     * true 固定, false 不固定
     * @default false
     */
    affix?: boolean;

    /**
     * 是否緩存頁面
     * true 緩存, false 不緩存
     * @default false
     */
    keepAlive?: boolean;

    /**
     * 是否在麵包屑導航中隱藏
     * true 隱藏, false 顯示
     * @default false
     */
    breadcrumb?: boolean;
  }
}
