import request_prod from "@/utils/request_prod";

const USER_BASE_URL = "/api/v1/users";

const UserAPI = {
  /**
   * 獲取當前登錄用戶信息
   *
   * @returns 登錄用戶暱稱、頭像信息，包括角色和權限
   */
  getInfo() {
    // HE: 介接正式api
    return request_prod<any, UserInfo>({
      //url: `${USER_BASE_URL}/me`,
      url: "/api/auth/GetMe",
      method: "get",
    });
  },
}

export default UserAPI;

/** 登錄用戶信息 */
export interface UserInfo {
  /** 用戶ID */
  userId?: number;

  /** 用戶名 */
  username?: string;

  /** 暱稱 */
  nickname?: string;

  /** 頭像URL */
  avatar?: string;

  /** 角色 */
  roles: string[];

  /** 權限 */
  perms: string[];
}

/**
 * 用戶分頁查詢對象
 */
export interface UserPageQuery extends PageQuery {
  /** 搜索關鍵字 */
  keywords?: string;

  /** 用戶狀態 */
  status?: number;

  /** 部門ID */
  deptId?: number;

  /** 開始時間 */
  createTime?: [string, string];
}

/** 用戶分頁對象 */
export interface UserPageVO {
  /** 用戶ID */
  id: number;
  /** 用戶頭像URL */
  avatar?: string;
  /** 創建時間 */
  createTime?: Date;
  /** 部門名稱 */
  deptName?: string;
  /** 用戶郵箱 */
  email?: string;
  /** 性別 */
  gender?: number;
  /** 手機號 */
  mobile?: string;
  /** 用戶暱稱 */
  nickname?: string;
  /** 角色名稱，多個使用英文逗號(,)分割 */
  roleNames?: string;
  /** 用戶狀態(1:啟用;0:禁用) */
  status?: number;
  /** 用戶名 */
  username?: string;
}

/** 用戶表單類型 */
export interface UserForm {
  /** 用戶頭像 */
  avatar?: string;
  /** 部門ID */
  deptId?: number;
  /** 郵箱 */
  email?: string;
  /** 性別 */
  gender?: number;
  /** 用戶ID */
  id?: number;
  /** 手機號 */
  mobile?: string;
  /** 暱稱 */
  nickname?: string;
  /** 角色ID集合 */
  roleIds?: number[];
  /** 用戶狀態(1:正常;0:禁用) */
  status?: number;
  /** 用戶名 */
  username?: string;
}

/** 個人中心用戶信息 */
export interface UserProfileVO {
  /** 用戶ID */
  id?: number;

  /** 用戶名 */
  username?: string;

  /** 暱稱 */
  nickname?: string;

  /** 頭像URL */
  avatar?: string;

  /** 性別 */
  gender?: number;

  /** 手機號 */
  mobile?: string;

  /** 郵箱 */
  email?: string;

  /** 部門名稱 */
  deptName?: string;

  /** 角色名稱，多個使用英文逗號(,)分割 */
  roleNames?: string;

  /** 創建時間 */
  createTime?: Date;
}

/** 個人中心用戶信息表單 */
export interface UserProfileForm {
  /** 用戶ID */
  id?: number;

  /** 用戶名 */
  username?: string;

  /** 暱稱 */
  nickname?: string;

  /** 頭像URL */
  avatar?: string;

  /** 性別 */
  gender?: number;

  /** 手機號 */
  mobile?: string;

  /** 郵箱 */
  email?: string;
}

/** 修改密碼錶單 */
export interface PasswordChangeForm {
  /** 原密碼 */
  oldPassword?: string;
  /** 新密碼 */
  newPassword?: string;
  /** 確認新密碼 */
  confirmPassword?: string;
}

/** 修改手機表單 */
export interface MobileUpdateForm {
  /** 手機號 */
  mobile?: string;
  /** 驗證碼 */
  code?: string;
}

/** 修改郵箱表單 */
export interface EmailUpdateForm {
  /** 郵箱 */
  email?: string;
  /** 驗證碼 */
  code?: string;
}
