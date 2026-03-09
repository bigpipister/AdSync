import type {
  DialogProps,
  DrawerProps,
  FormItemRule,
  FormProps,
  PaginationProps,
  TableProps,
  ColProps,
} from "element-plus";
import type PageContent from "./PageContent.vue";
import type PageForm from "./PageForm.vue";
import type PageModal from "./PageModal.vue";
import type PageSearch from "./PageSearch.vue";

export type PageSearchInstance = InstanceType<typeof PageSearch>;
export type PageContentInstance = InstanceType<typeof PageContent>;
export type PageModalInstance = InstanceType<typeof PageModal>;
export type PageFormInstance = InstanceType<typeof PageForm>;

export type IObject = Record<string, any>;

export interface IOperatData {
  name: string;
  row: IObject;
  column: IObject;
  $index: number;
}

export interface ISearchConfig {
  // 頁面名稱(參與組成權限標識,如sys:user:xxx)
  pageName: string;
  // 表單項
  formItems: Array<{
    // 組件類型(如input,select等)
    type?: "input" | "select" | "tree-select" | "date-picker" | "input-tag";
    // 標籤文本
    label: string;
    // 標籤提示
    tips?: string;
    // 鍵名
    prop: string;
    // 組件屬性(input-tag組件支持join,btnText,size屬性)
    attrs?: IObject;
    // 初始值
    initialValue?: any;
    // 可選項(適用於select組件)
    options?: { label: string; value: any }[];
    // 初始化數據函數擴展
    initFn?: (formItem: IObject) => void;
  }>;
  // 是否開啟展開和收縮
  isExpandable?: boolean;
  // 默認展示的表單項數量
  showNumber?: number;
}

export interface IContentConfig<T = any> {
  // 頁面名稱(參與組成權限標識,如sys:user:xxx)
  pageName: string;
  // table組件屬性
  table?: Omit<TableProps<any>, "data">;
  // pagination組件屬性
  pagination?:
    | boolean
    | Partial<
        Omit<
          PaginationProps,
          "v-model:page-size" | "v-model:current-page" | "total" | "currentPage"
        >
      >;
  // 列表的網絡請求函數(需返回promise)
  indexAction: (queryParams: T) => Promise<any>;
  // 默認的分頁相關的請求參數
  request?: {
    pageName: string;
    limitName: string;
  };
  // 數據格式解析的回調函數
  parseData?: (res: any) => {
    total: number;
    list: IObject[];
    [key: string]: any;
  };
  // 修改屬性的網絡請求函數(需返回promise)
  modifyAction?: (data: {
    [key: string]: any;
    field: string;
    value: boolean | string | number;
  }) => Promise<any>;
  // 刪除的網絡請求函數(需返回promise)
  deleteAction?: (ids: string) => Promise<any>;
  // 後端導出的網絡請求函數(需返回promise)
  exportAction?: (queryParams: T) => Promise<any>;
  // 前端全量導出的網絡請求函數(需返回promise)
  exportsAction?: (queryParams: T) => Promise<IObject[]>;
  // 導入模板
  importTemplate?: string | (() => Promise<any>);
  // 後端導入的網絡請求函數(需返回promise)
  importAction?: (file: File) => Promise<any>;
  // 前端導入的網絡請求函數(需返回promise)
  importsAction?: (data: IObject[]) => Promise<any>;
  // 主鍵名(默認為id)
  pk?: string;
  // 表格工具欄(默認支持add,delete,export,也可自定義)
  toolbar?: Array<
    | "add"
    | "delete"
    | "import"
    | "export"
    | {
        auth: string;
        icon?: string;
        name: string;
        text: string;
        type?: "primary" | "success" | "warning" | "danger" | "info";
      }
  >;
  // 表格工具欄右側圖標
  defaultToolbar?: Array<
    | "refresh"
    | "filter"
    | "imports"
    | "exports"
    | "search"
    | {
        name: string;
        icon: string;
        title?: string;
        auth?: string;
      }
  >;
  // table組件列屬性(額外的屬性templet,operat,slotName)
  cols: Array<{
    type?: "default" | "selection" | "index" | "expand";
    label?: string;
    prop?: string;
    width?: string | number;
    align?: "left" | "center" | "right";
    columnKey?: string;
    reserveSelection?: boolean;
    // 列是否顯示
    show?: boolean;
    // 模板
    templet?:
      | "image"
      | "list"
      | "url"
      | "switch"
      | "input"
      | "price"
      | "percent"
      | "icon"
      | "date"
      | "tool"
      | "custom";
    // image模板相關參數
    imageWidth?: number;
    imageHeight?: number;
    // list模板相關參數
    selectList?: IObject;
    // switch模板相關參數
    activeValue?: boolean | string | number;
    inactiveValue?: boolean | string | number;
    activeText?: string;
    inactiveText?: string;
    // input模板相關參數
    inputType?: string;
    // price模板相關參數
    priceFormat?: string;
    // date模板相關參數
    dateFormat?: string;
    // tool模板相關參數
    operat?: Array<
      | "edit"
      | "delete"
      | {
          auth?: string;
          icon?: string;
          name: string;
          text: string;
          type?: "primary" | "success" | "warning" | "danger" | "info";
          render?: (row: IObject) => boolean;
        }
    >;
    // filter值拼接符
    filterJoin?: string;
    [key: string]: any;
    // 初始化數據函數
    initFn?: (item: IObject) => void;
  }>;
}

export interface IModalConfig<T = any> {
  // 頁面名稱
  pageName?: string;
  // 主鍵名(主要用於編輯數據,默認為id)
  pk?: string;
  // 組件類型
  component?: "dialog" | "drawer";
  // dialog組件屬性
  dialog?: Partial<Omit<DialogProps, "modelValue">>;
  // drawer組件屬性
  drawer?: Partial<Omit<DrawerProps, "modelValue">>;
  // form組件屬性
  form?: IForm;
  // 表單項
  formItems: IFormItems<T>;
  // 提交之前處理
  beforeSubmit?: (data: T) => void;
  // 提交的網絡請求函數(需返回promise)
  formAction: (data: T) => Promise<any>;
}

export type IForm = Partial<Omit<FormProps, "model" | "rules">>;

// 表單項
export type IFormItems<T = any> = Array<{
  // 組件類型(如input,select,radio,custom等，默認input)
  type?:
    | "input"
    | "select"
    | "radio"
    | "switch"
    | "checkbox"
    | "tree-select"
    | "date-picker"
    | "input-number"
    | "text"
    | "custom";
  // 組件屬性
  attrs?: IObject;
  // 組件可選項(適用於select,radio,checkbox組件)
  options?: Array<{
    label: string;
    value: any;
    disabled?: boolean;
    [key: string]: any;
  }>;
  // 插槽名(適用於組件類型為custom)
  slotName?: string;
  // 標籤文本
  label: string;
  // 標籤提示
  tips?: string;
  // 鍵名
  prop: string;
  // 驗證規則
  rules?: FormItemRule[];
  // 初始值
  initialValue?: any;
  // 是否隱藏
  hidden?: boolean;
  // layout組件Col屬性
  col?: Partial<ColProps>;
  // 監聽函數
  watch?: (newValue: any, oldValue: any, data: T, items: IObject[]) => void;
  // 計算屬性函數
  computed?: (data: T) => any;
  // 監聽收集函數
  watchEffect?: (data: T) => void;
  // 初始化數據函數擴展
  initFn?: (item: IObject) => void;
}>;

export interface IPageForm {
  // 主鍵名(主要用於編輯數據,默認為id)
  pk?: string;
  // form組件屬性
  form?: IForm;
  // 表單項
  formItems: IFormItems;
}
