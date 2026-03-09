import { store } from "@/store";
import { usePermissionStoreHook } from "@/store/modules/permission";

import AuthAPI, { type LoginFormData } from "@/api/auth";
import UserAPI, { type UserInfo } from "@/api/system/user";

import { setToken, setRefreshToken, getRefreshToken, clearToken } from "@/utils/auth";

export const useUserStore = defineStore("user", () => {
  const userInfo = useStorage<UserInfo>("userInfo", {} as UserInfo);

  /**
   * 登錄
   *
   * @param {LoginFormData}
   * @returns
   */
  function login(LoginFormData: LoginFormData) {
    return new Promise<void>((resolve, reject) => {
      if(LoginFormData.username === 'admin' && LoginFormData.password === '1qaz@WSX') {
          setToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImRlcHRJZCI6MSwiZGF0YVNjb3BlIjoxLCJ1c2VySWQiOjIsImlhdCI6MTcyODE5MzA1MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJhZDg3NzlhZDZlYWY0OWY3OTE4M2ZmYmI5OWM4MjExMSJ9.58YHwL3sNNC22jyAmOZeSm-7MITzfHb_epBIz7LvWeA"); // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
          setRefreshToken("");
          resolve();
      } else {
        reject();
      }

      // AuthAPI.login(LoginFormData)
      //   .then((data) => {
      //     const { tokenType, accessToken, refreshToken } = data.data;
      //     setToken(tokenType + " " + accessToken); // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
      //     setRefreshToken(refreshToken);
      //     resolve();
      //   })
      //   .catch((error) => {
      //     reject(error);
      //   });
    });
  }

  /**
   * 獲取用戶信息
   *
   * @returns {UserInfo} 用戶信息
   */
  function getUserInfo() {
    return new Promise<UserInfo>((resolve, reject) => {
      var data = {
        userId: 2,
        username: "admin",
        nickname: "系統管理員",
        avatar: "https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif",
        roles: ["ADMIN"],
        perms: [
          "sys:notice:edit",
          "sys:menu:delete",
          "sys:dict:edit",
          "sys:notice:query",
          "sys:dict:delete",
          "sys:config:add",
          "sys:config:refresh",
          "sys:menu:add",
          "sys:user:add",
          "sys:user:export",
          "sys:role:edit",
          "sys:dept:delete",
          "sys:config:update",
          "sys:user:password:reset",
          "sys:notice:revoke",
          "sys:user:import",
          "sys:user:delete",
          "sys:dict_type:delete",
          "sys:dict:add",
          "sys:role:add",
          "sys:notice:publish",
          "sys:notice:delete",
          "sys:dept:edit",
          "sys:dict_type:edit",
          "sys:user:query",
          "sys:user:edit",
          "sys:config:delete",
          "sys:dept:add",
          "sys:notice:add",
          "sys:role:delete",
          "sys:menu:edit",
          "sys:config:query",
        ],
      }
      Object.assign(userInfo.value, { ...data });
      resolve(data);
      // UserAPI.getInfo()
      //   .then((data) => {
      //     if (!data) {
      //       reject("Verification failed, please Login again.");
      //       return;
      //     }
      //     Object.assign(userInfo.value, { ...data });
      //     resolve(data);
      //   })
      //   .catch((error) => {
      //     reject(error);
      //   });
    });
  }

  /**
   * 登出
   */
  function logout() {
    return new Promise<void>((resolve, reject) => {
      // AuthAPI.logout()
      //   .then(() => {
      //     clearUserData();
      //     resolve();
      //   })
      //   .catch((error) => {
      //     reject(error);
      //   });
      clearUserData();
      resolve();
    });
  }

  /**
   * 清理用戶數據
   *
   * @returns
   */
  function clearUserData() {
    return new Promise<void>((resolve) => {
      clearToken();
      usePermissionStoreHook().resetRouter();
      resolve();
    });
  }

  return {
    userInfo,
    getUserInfo,
    login,
    logout,
    clearUserData,
  };
});

/**
 * 用於在組件外部（如在Pinia Store 中）使用 Pinia 提供的 store 實例。
 * 官方文檔解釋瞭如何在組件外部使用 Pinia Store：
 * https://pinia.vuejs.org/core-concepts/outside-component-usage.html#using-a-store-outside-of-a-component
 */
export function useUserStoreHook() {
  return useUserStore(store);
}
