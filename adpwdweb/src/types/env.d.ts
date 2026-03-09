// https://cn.vitejs.dev/guide/env-and-mode

// TypeScript 類型提示都為 string： https://github.com/vitejs/vite/issues/6930
interface ImportMetaEnv {
  /** 應用端口 */
  VITE_APP_PORT: number;
  /** API 基礎路徑(代理前綴) */
  VITE_APP_BASE_API: string;
  /** API 地址 */
  VITE_APP_API_URL: string;
  /** 是否開啟 Mock 服務 */
  VITE_MOCK_DEV_SERVER: boolean;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}

/**
 * 平臺的名稱、版本、運行所需的`node`版本、依賴、構建時間的類型提示
 */
declare const __APP_INFO__: {
  pkg: {
    name: string;
    version: string;
    engines: {
      node: string;
    };
    dependencies: Record<string, string>;
    devDependencies: Record<string, string>;
  };
  buildTimestamp: number;
};
