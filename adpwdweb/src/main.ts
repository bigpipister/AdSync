import { createApp } from "vue";
import App from "./App.vue";
import setupPlugins from "@/plugins";

// 本地SVG圖標
import "virtual:svg-icons-register";
// 暗黑主題樣式
import "element-plus/theme-chalk/dark/css-vars.css";
// 暗黑模式自定義變量
import "@/styles/dark/css-vars.css";
import "@/styles/index.scss";
import "uno.css";

const app = createApp(App);
// 註冊插件
app.use(setupPlugins);
app.mount("#app");
