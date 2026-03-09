declare global {
  /**
   * 響應數據
   */
  interface ResponseData<T = any> {
    code: string;
    data: T;
    msg: string;
  }

  /**
   * 分頁查詢參數
   */
  interface PageQuery {
    pageNum: number;
    pageSize: number;
  }

  /**
   * 分頁響應對象
   */
  interface PageResult<T> {
    /** 數據列表 */
    list: T;
    /** 總數 */
    total: number;
  }

  /**
   * 頁籤對象
   */
  interface TagView {
    /** 頁簽名稱 */
    name: string;
    /** 頁籤標題 */
    title: string;
    /** 頁籤路由路徑 */
    path: string;
    /** 頁籤路由完整路徑 */
    fullPath: string;
    /** 頁籤圖標 */
    icon?: string;
    /** 是否固定頁籤 */
    affix?: boolean;
    /** 是否開啟緩存 */
    keepAlive?: boolean;
    /** 路由查詢參數 */
    query?: any;
  }

  /**
   * 系統設置
   */
  interface AppSettings {
    /** 系統標題 */
    title: string;
    /** 系統版本 */
    version: string;
    /** 是否顯示設置 */
    showSettings: boolean;
    /** 是否顯示多標籤導航 */
    tagsView: boolean;
    /** 是否顯示側邊欄Logo */
    sidebarLogo: boolean;
    /** 導航欄佈局(left|top|mix) */
    layout: string;
    /** 主題顏色 */
    themeColor: string;
    /** 主題模式(dark|light) */
    theme: string;
    /** 佈局大小(default |large |small) */
    size: string;
    /** 語言( zh-cn| en) */
    language: string;
    /** 是否開啟水印 */
    watermarkEnabled: boolean;
    /** 水印內容 */
    watermarkContent: string;
  }

  /**
   * 下拉選項數據類型
   */
  interface OptionType {
    /** 值 */
    value: string | number;
    /** 文本 */
    label: string;
    /** 子列表  */
    children?: OptionType[];
  }
  /**
   * 導入結果
   */
  interface ExcelResult {
    /** 狀態碼 */
    code: string;
    /** 無效數據條數 */
    invalidCount: number;
    /** 有效數據條數 */
    validCount: number;
    /** 錯誤信息 */
    messageList: Array<string>;
  }
}
export {};
