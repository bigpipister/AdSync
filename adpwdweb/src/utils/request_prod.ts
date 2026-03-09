import axios, { type InternalAxiosRequestConfig, type AxiosResponse } from "axios";
import qs from "qs";
import { useUserStoreHook } from "@/store/modules/user";
import { ResultEnum } from "@/enums/ResultEnum";
import { getToken } from "@/utils/auth";
import router from "@/router";

// 創建 axios 實例
const service = axios.create({
  // 沒有被proxy代理到又沒匹配到mock url就會傳送出去
  baseURL: import.meta.env.VITE_APP_API_URL,
  timeout: 50000,
  headers: { "Content-Type": "application/json;charset=utf-8" },
  paramsSerializer: (params) => qs.stringify(params),
});

// 請求攔截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // console.log(config);
    const accessToken = getToken();
    // 如果 Authorization 設置為 no-auth，則不攜帶 Token，用於登錄、刷新 Token 等接口
    if (config.headers.Authorization !== "no-auth" && accessToken) {
      config.headers.Authorization = accessToken;
    } else {
      delete config.headers.Authorization;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 響應攔截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 如果響應是二進制流，則直接返回，用於下載文件、Excel 導出等
    if (response.config.responseType === "blob") {
      return response;
    }
    //console.log(response.data);
    const { result, message } = response.data;
    if (result === 'T') {
      return response.data;
    }
    ElMessage.error(message || "系統出錯");
    return Promise.reject(new Error(message || "Error"));
  },
  async (error: any) => {
    // 非 2xx 狀態碼處理 401、403、500 等
    const { config, response } = error;
    if (response) {
      const { Message } = response.data;
      // HE: 先不處理 refresh toekn
      // if (code === ResultEnum.ACCESS_TOKEN_INVALID) {
      //   // Token 過期，刷新 Token
      //   return handleTokenRefresh(config);
      // } else if (code === ResultEnum.REFRESH_TOKEN_INVALID) {
      //   return Promise.reject(new Error(msg || "Error"));
      // } else {
      //   ElMessage.error(msg || "系統出錯");
      // }
      ElMessage.error(Message || "系統出錯");
    }
    return Promise.reject(error.message);
  }
);

export default service;

// 刷新 Token 的鎖
let isRefreshing = false;
// 因 Token 過期導致失敗的請求隊列
let requestsQueue: Array<() => void> = [];
