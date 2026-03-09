import vue from "@vitejs/plugin-vue";
import { type UserConfig, type ConfigEnv, loadEnv, defineConfig } from "vite";

import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";

import { createSvgIconsPlugin } from "vite-plugin-svg-icons";
import mockDevServerPlugin from "vite-plugin-mock-dev-server";

import UnoCSS from "unocss/vite";
import { resolve } from "path";
import { name, version, engines, dependencies, devDependencies } from "./package.json";

// 平臺的名稱、版本、運行所需的 node 版本、依賴、構建時間的類型提示
const __APP_INFO__ = {
  pkg: { name, version, engines, dependencies, devDependencies },
  buildTimestamp: Date.now(),
};

const pathSrc = resolve(__dirname, "src");
// Vite配置  https://cn.vitejs.dev/config
export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
  const env = loadEnv(mode, process.cwd());
  return {
    base: '/adws/status/',
    resolve: {
      alias: {
        "@": pathSrc,
      },
    },
    css: {
      preprocessorOptions: {
        // 定義全局 SCSS 變量
        scss: {
          api: "modern-compiler",
          additionalData: `
            @use "@/styles/variables.scss" as *;
          `,
        },
      },
    },
    server: {
      host: "0.0.0.0",
      port: +env.VITE_APP_PORT,
      open: true,
      proxy: {
        // 代理 /dev-api 的請求
        [env.VITE_APP_BASE_API]: {
          changeOrigin: true,
          // 代理目標地址：https://api.youlai.tech
          target: env.VITE_APP_API_URL,
          rewrite: (path) => path.replace(new RegExp("^" + env.VITE_APP_BASE_API), ""),
        },
      },
    },
    plugins: [
      vue(),
      env.VITE_MOCK_DEV_SERVER === "true" ? mockDevServerPlugin() : null,
      UnoCSS({
        hmrTopLevelAwait: false,
      }),
      // 自動導入配置 https://github.com/sxzz/element-plus-best-practices/blob/main/vite.config.ts
      AutoImport({
        // 導入 Vue 函數，如：ref, reactive, toRef 等
        imports: ["vue", "@vueuse/core", "pinia", "vue-router", "vue-i18n"],
        resolvers: [
          // 導入 Element Plus函數，如：ElMessage, ElMessageBox 等
          ElementPlusResolver(),
        ],
        eslintrc: {
          enabled: false,
          filepath: "./.eslintrc-auto-import.json",
          globalsPropValue: true,
        },
        vueTemplate: true,
        // 導入函數類型聲明文件路徑 (false:關閉自動生成)
        dts: false,
        // dts: "src/types/auto-imports.d.ts",
      }),
      Components({
        resolvers: [
          // 導入 Element Plus 組件
          ElementPlusResolver(),
        ],
        // 指定自定義組件位置(默認:src/components)
        dirs: ["src/components", "src/**/components"],
        // 導入組件類型聲明文件路徑 (false:關閉自動生成)
        dts: false,
        // dts: "src/types/components.d.ts",
      }),
      createSvgIconsPlugin({
        // 緩存圖標位置
        iconDirs: [resolve(pathSrc, "assets/icons")],
        symbolId: "icon-[dir]-[name]",
      }),
    ],
    // 預加載項目必需的組件
    optimizeDeps: {
      include: [
        "vue",
        "vue-router",
        "element-plus",
        "pinia",
        "axios",
        "@vueuse/core",
        "sortablejs",
        "exceljs",
        "path-to-regexp",
        "echarts",
        "@wangeditor/editor",
        "@wangeditor/editor-for-vue",
        "vue-i18n",
        "path-browserify",
        "element-plus/es/components/form/style/css",
        "element-plus/es/components/form-item/style/css",
        "element-plus/es/components/button/style/css",
        "element-plus/es/components/input/style/css",
        "element-plus/es/components/input-number/style/css",
        "element-plus/es/components/switch/style/css",
        "element-plus/es/components/upload/style/css",
        "element-plus/es/components/menu/style/css",
        "element-plus/es/components/col/style/css",
        "element-plus/es/components/icon/style/css",
        "element-plus/es/components/row/style/css",
        "element-plus/es/components/tag/style/css",
        "element-plus/es/components/dialog/style/css",
        "element-plus/es/components/loading/style/css",
        "element-plus/es/components/radio/style/css",
        "element-plus/es/components/radio-group/style/css",
        "element-plus/es/components/popover/style/css",
        "element-plus/es/components/scrollbar/style/css",
        "element-plus/es/components/tooltip/style/css",
        "element-plus/es/components/dropdown/style/css",
        "element-plus/es/components/dropdown-menu/style/css",
        "element-plus/es/components/dropdown-item/style/css",
        "element-plus/es/components/sub-menu/style/css",
        "element-plus/es/components/menu-item/style/css",
        "element-plus/es/components/divider/style/css",
        "element-plus/es/components/card/style/css",
        "element-plus/es/components/link/style/css",
        "element-plus/es/components/breadcrumb/style/css",
        "element-plus/es/components/breadcrumb-item/style/css",
        "element-plus/es/components/table/style/css",
        "element-plus/es/components/tree-select/style/css",
        "element-plus/es/components/table-column/style/css",
        "element-plus/es/components/select/style/css",
        "element-plus/es/components/option/style/css",
        "element-plus/es/components/pagination/style/css",
        "element-plus/es/components/tree/style/css",
        "element-plus/es/components/alert/style/css",
        "element-plus/es/components/radio-button/style/css",
        "element-plus/es/components/checkbox-group/style/css",
        "element-plus/es/components/checkbox/style/css",
        "element-plus/es/components/tabs/style/css",
        "element-plus/es/components/tab-pane/style/css",
        "element-plus/es/components/rate/style/css",
        "element-plus/es/components/date-picker/style/css",
        "element-plus/es/components/notification/style/css",
        "element-plus/es/components/image/style/css",
        "element-plus/es/components/statistic/style/css",
        "element-plus/es/components/watermark/style/css",
        "element-plus/es/components/config-provider/style/css",
        "element-plus/es/components/text/style/css",
        "element-plus/es/components/drawer/style/css",
        "element-plus/es/components/color-picker/style/css",
        "element-plus/es/components/backtop/style/css",
        "element-plus/es/components/message-box/style/css",
        "element-plus/es/components/skeleton/style/css",
        "element-plus/es/components/skeleton/style/css",
        "element-plus/es/components/skeleton-item/style/css",
        "element-plus/es/components/badge/style/css",
        "element-plus/es/components/steps/style/css",
        "element-plus/es/components/step/style/css",
        "element-plus/es/components/avatar/style/css",
        "element-plus/es/components/descriptions/style/css",
        "element-plus/es/components/descriptions-item/style/css",
        "element-plus/es/components/checkbox-group/style/css",
        "element-plus/es/components/progress/style/css",
        "element-plus/es/components/image-viewer/style/css",
        "element-plus/es/components/empty/style/css",
      ],
    },
    // 構建配置
    build: {
      chunkSizeWarningLimit: 2000, // 消除打包大小超過500kb警告
      minify: "terser", // Vite 2.6.x 以上需要配置 minify: "terser", terserOptions 才能生效
      terserOptions: {
        compress: {
          keep_infinity: true, // 防止 Infinity 被壓縮成 1/0，這可能會導致 Chrome 上的性能問題
          drop_console: true, // 生產環境去除 console
          drop_debugger: true, // 生產環境去除 debugger
        },
        format: {
          comments: false, // 刪除註釋
        },
      },
      rollupOptions: {
        output: {
          // manualChunks: {
          //   "vue-i18n": ["vue-i18n"],
          // },
          // 用於從入口點創建的塊的打包輸出格式[name]表示文件名,[hash]表示該文件內容hash值
          entryFileNames: "js/[name].[hash].js",
          // 用於命名代碼拆分時創建的共享塊的輸出命名
          chunkFileNames: "js/[name].[hash].js",
          // 用於輸出靜態資源的命名，[ext]表示文件擴展名
          assetFileNames: (assetInfo: any) => {
            const info = assetInfo.name.split(".");
            let extType = info[info.length - 1];
            // console.log('文件信息', assetInfo.name)
            if (/\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/i.test(assetInfo.name)) {
              extType = "media";
            } else if (/\.(png|jpe?g|gif|svg)(\?.*)?$/.test(assetInfo.name)) {
              extType = "img";
            } else if (/\.(woff2?|eot|ttf|otf)(\?.*)?$/i.test(assetInfo.name)) {
              extType = "fonts";
            }
            return `${extType}/[name].[hash].[ext]`;
          },
        },
      },
    },
    define: {
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    },
  };
});
