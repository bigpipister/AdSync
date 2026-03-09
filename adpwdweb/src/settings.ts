import { SizeEnum } from "./enums/SizeEnum";
import { LayoutEnum } from "./enums/LayoutEnum";
import { ThemeEnum } from "./enums/ThemeEnum";
import { LanguageEnum } from "./enums/LanguageEnum";

const { pkg } = __APP_INFO__;

// 檢查用戶的操作系統是否使用深色模式
const mediaQueryList = window.matchMedia("(prefers-color-scheme: dark)");

const defaultSettings: AppSettings = {
  // 系統Title
  title: pkg.name,
  // 系統版本
  version: pkg.version,
  // 是否顯示設置
  showSettings: true,
  // 是否顯示標籤視圖
  tagsView: true,
  // 是否顯示側邊欄Logo
  sidebarLogo: true,
  // 佈局方式，默認為左側佈局
  layout: LayoutEnum.LEFT,
  // 主題，根據操作系統的色彩方案自動選擇
  theme: mediaQueryList.matches ? ThemeEnum.DARK : ThemeEnum.LIGHT,
  // 組件大小 default | medium | small | large
  size: SizeEnum.DEFAULT,
  // 語言
  language: LanguageEnum.ZH_CN,
  // 主題顏色
  themeColor: "#4080FF",
  // 是否開啟水印
  watermarkEnabled: false,
  // 水印內容
  watermarkContent: pkg.name,
};

export default defaultSettings;
