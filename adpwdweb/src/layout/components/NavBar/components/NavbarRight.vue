<template>
  <div class="navbar__right">
    <!-- 非手機設備（窄屏）才顯示 HE:都先取消 -->
    <template v-if="!isMobile">
      <!-- 搜索 
      <MenuSearch />-->

      <!-- 全屏 
      <Fullscreen />-->

      <!-- 佈局大小 
      <SizeSelect />-->

      <!-- 語言選擇 
      <LangSelect />-->

      <!-- 消息通知 
      <Notification />-->
    </template>

    <!-- 用戶頭像（個人中心、註銷登錄等） -->
    <UserProfile />

    <!-- 設置面板 
    <div v-if="defaultSettings.showSettings" @click="settingStore.settingsVisible = true">
      <SvgIcon icon-class="setting" />
    </div>-->
  </div>
</template>
<script setup lang="ts">
import defaultSettings from "@/settings";
import { DeviceEnum } from "@/enums/DeviceEnum";

import { useAppStore, useSettingsStore } from "@/store";

import UserProfile from "./UserProfile.vue";

const appStore = useAppStore();
const settingStore = useSettingsStore();

const isMobile = computed(() => appStore.device === DeviceEnum.MOBILE);
</script>

<style lang="scss" scoped>
.navbar__right {
  display: flex;
  align-items: center;
  justify-content: center;

  & > * {
    display: inline-block;
    min-width: 40px;
    height: $navbar-height;
    line-height: $navbar-height;
    color: var(--el-text-color);
    text-align: center;
    cursor: pointer;

    &:hover {
      background: rgb(0 0 0 / 10%);
    }
  }
}

:deep(.el-divider--horizontal) {
  margin: 10px 0;
}

.dark .navbar__right > *:hover {
  background: rgb(255 255 255 / 20%);
}

.layout-top .navbar__right > *,
.layout-mix .navbar__right > * {
  color: #fff;
}
</style>
