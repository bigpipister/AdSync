import defaultSettings from "@/settings";

// 導入 Element Plus 中英文語言包
import zhCn from "element-plus/es/locale/lang/zh-cn";
import en from "element-plus/es/locale/lang/en";
import { store } from "@/store";
import { DeviceEnum } from "@/enums/DeviceEnum";
import { SidebarStatusEnum } from "@/enums/SidebarStatusEnum";

export const useAppStore = defineStore("app", () => {
  // 設備類型
  const device = useStorage("device", DeviceEnum.DESKTOP);
  // 佈局大小
  const size = useStorage("size", defaultSettings.size);
  // 語言
  const language = useStorage("language", defaultSettings.language);
  // 側邊欄狀態
  const sidebarStatus = useStorage("sidebarStatus", SidebarStatusEnum.CLOSED);
  const sidebar = reactive({
    opened: sidebarStatus.value === SidebarStatusEnum.OPENED,
    withoutAnimation: false,
  });

  // 頂部菜單激活路徑
  const activeTopMenuPath = useStorage("activeTopMenuPath", "");

  /**
   * 根據語言標識讀取對應的語言包
   */
  const locale = computed(() => {
    if (language?.value == "en") {
      return en;
    } else {
      return zhCn;
    }
  });

  // 切換側邊欄
  function toggleSidebar() {
    sidebar.opened = !sidebar.opened;
    sidebarStatus.value = sidebar.opened ? SidebarStatusEnum.OPENED : SidebarStatusEnum.CLOSED;
  }

  // 關閉側邊欄
  function closeSideBar() {
    sidebar.opened = false;
    sidebarStatus.value = SidebarStatusEnum.CLOSED;
  }

  // 打開側邊欄
  function openSideBar() {
    sidebar.opened = true;
    sidebarStatus.value = SidebarStatusEnum.OPENED;
  }

  // 切換設備
  function toggleDevice(val: string) {
    device.value = val;
  }

  /**
   * 改變佈局大小
   *
   * @param val 佈局大小 default | small | large
   */
  function changeSize(val: string) {
    size.value = val;
  }
  /**
   * 切換語言
   *
   * @param val
   */
  function changeLanguage(val: string) {
    language.value = val;
  }
  /**
   * 混合模式頂部切換
   */
  function activeTopMenu(val: string) {
    activeTopMenuPath.value = val;
  }
  return {
    device,
    sidebar,
    language,
    locale,
    size,
    activeTopMenu,
    toggleDevice,
    changeSize,
    changeLanguage,
    toggleSidebar,
    closeSideBar,
    openSideBar,
    activeTopMenuPath,
  };
});

/**
 * 用於在組件外部（如在Pinia Store 中）使用 Pinia 提供的 store 實例。
 * 官方文檔解釋瞭如何在組件外部使用 Pinia Store：
 * https://pinia.vuejs.org/core-concepts/outside-component-usage.html#using-a-store-outside-of-a-component
 */
export function useAppStoreHook() {
  return useAppStore(store);
}
