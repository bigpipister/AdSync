import type { App } from "vue";

import { setupDirective } from "@/directive";
import { setupI18n } from "@/lang";
import { setupRouter } from "@/router";
import { setupStore } from "@/store";
import { setupElIcons } from "./icons";
import { setupPermission } from "./permission";
import webSocketManager from "@/utils/websocket";
import { InstallCodeMirror } from "codemirror-editor-vue3";

export default {
  install(app: App<Element>) {
    // 自定義指令(directive)
    setupDirective(app);
    // 路由(router)
    setupRouter(app);
    // 狀態管理(store)
    setupStore(app);
    // 國際化
    setupI18n(app);
    // Element-plus圖標
    setupElIcons(app);
    // 路由守衛
    setupPermission();
    // 初始化 WebSocket
    //webSocketManager.setupWebSocket();
    // 註冊 CodeMirror
    app.use(InstallCodeMirror);
  },
};
