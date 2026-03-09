import defaultSettings from "@/settings";
import { ThemeEnum } from "@/enums/ThemeEnum";
import { generateThemeColors, applyTheme, toggleDarkMode } from "@/utils/theme";

type SettingsValue = boolean | string;

export const useSettingsStore = defineStore("setting", () => {
  // 基本設置
  const settingsVisible = ref(false);
  // 標籤
  const tagsView = useStorage<boolean>("tagsView", defaultSettings.tagsView);
  // 側邊欄 Logo
  const sidebarLogo = useStorage<boolean>("sidebarLogo", defaultSettings.sidebarLogo);
  // 佈局
  const layout = useStorage<string>("layout", defaultSettings.layout);
  // 水印
  const watermarkEnabled = useStorage<boolean>(
    "watermarkEnabled",
    defaultSettings.watermarkEnabled
  );

  // 主題
  const themeColor = useStorage<string>("themeColor", defaultSettings.themeColor);
  const theme = useStorage<string>("theme", defaultSettings.theme);

  //  監聽主題變化
  watch(
    [theme, themeColor],
    ([newTheme, newThemeColor]) => {
      toggleDarkMode(newTheme === ThemeEnum.DARK);
      const colors = generateThemeColors(newThemeColor);
      applyTheme(colors);
    },
    { immediate: true }
  );

  // 設置映射
  const settingsMap: Record<string, Ref<SettingsValue>> = {
    tagsView,
    sidebarLogo,
    layout,
    watermarkEnabled,
  };

  function changeSetting({ key, value }: { key: string; value: SettingsValue }) {
    const setting = settingsMap[key];
    if (setting) setting.value = value;
  }

  function changeTheme(val: string) {
    theme.value = val;
  }

  function changeThemeColor(color: string) {
    themeColor.value = color;
  }

  function changeLayout(val: string) {
    layout.value = val;
  }

  return {
    settingsVisible,
    tagsView,
    sidebarLogo,
    layout,
    themeColor,
    theme,
    watermarkEnabled,
    changeSetting,
    changeTheme,
    changeThemeColor,
    changeLayout,
  };
});
