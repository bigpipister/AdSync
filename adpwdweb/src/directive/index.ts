import type { App } from "vue";

import { hasPerm } from "./permission";

// 全局註冊 directive
export function setupDirective(app: App<Element>) {
  // 使 v-hasPerm 在所有組件中都可用
  app.directive("hasPerm", hasPerm);
}
