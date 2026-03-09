// 輔助函數：將十六進制顏色轉換為 RGB
function hexToRgb(hex: string): [number, number, number] {
  const bigint = parseInt(hex.slice(1), 16);
  return [(bigint >> 16) & 255, (bigint >> 8) & 255, bigint & 255];
}

// 輔助函數：將 RGB 轉換為十六進制顏色
function rgbToHex(r: number, g: number, b: number): string {
  return `#${((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1)}`;
}

// 輔助函數：調整顏色亮度
function adjustBrightness(hex: string, factor: number): string {
  const rgb = hexToRgb(hex);
  const newRgb = rgb.map((val) =>
    Math.max(0, Math.min(255, Math.round(val + (255 - val) * factor)))
  ) as [number, number, number];
  return rgbToHex(...newRgb);
}

export function generateThemeColors(primary: string) {
  const colors: Record<string, string> = {
    primary,
  };

  // 生成淺色變體
  for (let i = 1; i <= 9; i++) {
    const factor = i * 0.1;
    colors[`primary-light-${i}`] = adjustBrightness(primary, factor);
  }

  // 生成深色變體
  colors["primary-dark-2"] = adjustBrightness(primary, -0.2);

  return colors;
}

export function applyTheme(colors: Record<string, string>) {
  const el = document.documentElement;

  Object.entries(colors).forEach(([key, value]) => {
    el.style.setProperty(`--el-color-${key}`, value);
  });
}

export function toggleDarkMode(isDark: boolean) {
  if (isDark) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
}
