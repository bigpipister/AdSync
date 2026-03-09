import request_prod from "@/utils/request_prod";

const AUTH_BASE_URL = "/api/v1/auth";

const AuthAPI = {
  /** 登錄接口*/
  login(data: LoginFormData) {
    const formData = new FormData();
    formData.append("username", data.username);
    formData.append("password", data.password);
    formData.append("captchaKey", data.captchaKey);
    formData.append("captchaCode", data.captchaCode);
    // HE: 介接正式api
    return request_prod<any, AuthResult>({
      //url: `${AUTH_BASE_URL}/login`,
      url: "/api/auth/SysLogin",
      method: "post",
      data: formData,
      headers: {
        "Content-Type": "application/json",
      },
    });
  },

  /** 獲取驗證碼接口*/
  getCaptcha() {
    // HE: 介接正式api
    return request_prod<any, AuthResult>({
      //url: `${AUTH_BASE_URL}/captcha`,
      url: "/api/auth/GenerateCaptcha",
      method: "get",
    });
  },
};

export default AuthAPI;

/** 登錄表單數據 */
export interface LoginFormData {
  /** 用戶名 */
  username: string;
  /** 密碼 */
  password: string;
  /** 驗證碼緩存key */
  captchaKey: string;
  /** 驗證碼 */
  captchaCode: string;
}

/** 認證查詢結果 */
// HE: 正式的api都是以下的格式
export interface AuthResult {
  result?: string;
  message?: string;
  total?: string;
  data?: any;
}

/** 登錄響應 */
export interface LoginResult {
  /** 訪問令牌 */
  accessToken: string;
  /** 刷新令牌 */
  refreshToken: string;
  /** 令牌類型 */
  tokenType: string;
  /** 過期時間(秒) */
  expiresIn: number;
}

/** 驗證碼信息 */
export interface CaptchaInfo {
  /** 驗證碼緩存key */
  captchaKey: string;
  /** 驗證碼圖片Base64字符串 */
  captchaBase64: string;
}
