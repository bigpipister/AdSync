<template>
  <div class="login">
    <!-- 登錄頁頭部 -->
    <div class="login-header">
      <div class="flex-y-center">
        <!--el-switch
          v-model="isDark"
          inline-prompt
          active-icon="Moon"
          inactive-icon="Sunny"
          @change="toggleTheme"
        />
        <lang-select class="ml-2 cursor-pointer" /-->
      </div>
    </div>

    <!-- 登錄頁內容 -->
    <div class="login-form">
      <el-form ref="loginFormRef" :model="loginFormData" :rules="loginRules">
        <div class="form-title">
          <img :src="logo" class="w446px h113px" />
          <!--h2>{{ defaultSettings.title }}</h2>
          <el-dropdown style="position: absolute; right: 0">
            <div class="cursor-pointer">
              <el-icon>
                <arrow-down />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-tag>{{ defaultSettings.version }}</el-tag>
                </el-dropdown-item>
                <el-dropdown-item @click="setLoginCredentials('root', '123456')">
                  超級管理員：root/123456
                </el-dropdown-item>
                <el-dropdown-item @click="setLoginCredentials('admin', '123456')">
                  系統管理員：admin/123456
                </el-dropdown-item>
                <el-dropdown-item @click="setLoginCredentials('test', '123456')">
                  測試小遊客：test/123456
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown-->
        </div>

        <!-- 用戶名 -->
        <el-form-item prop="username">
          <div class="input-wrapper">
            <el-icon class="mx-2">
              <User />
            </el-icon>
            <el-input
              ref="username"
              v-model="loginFormData.username"
              :placeholder="$t('login.username')"
              name="username"
              size="large"
              class="h-[48px]"
            />
          </div>
        </el-form-item>

        <!-- 密碼 -->
        <el-tooltip :visible="isCapslock" :content="$t('login.capsLock')" placement="right">
          <el-form-item prop="password">
            <div class="input-wrapper">
              <el-icon class="mx-2">
                <Lock />
              </el-icon>
              <el-input
                v-model="loginFormData.password"
                :placeholder="$t('login.password')"
                type="password"
                name="password"
                size="large"
                class="h-[48px] pr-2"
                show-password
                @keyup="checkCapslock"
                @keyup.enter="handleLoginSubmit"
              />
            </div>
          </el-form-item>
        </el-tooltip>

        <!-- 驗證碼 -->
        <!--el-form-item prop="captchaCode">
          <div class="input-wrapper">
            <svg-icon icon-class="captcha" class="mx-2" />
            <el-input
              v-model="loginFormData.captchaCode"
              auto-complete="off"
              size="large"
              class="flex-1"
              :placeholder="$t('login.captchaCode')"
              @keyup.enter="handleLoginSubmit"
            />

            <el-image :src="captchaBase64" class="captcha-img" @click="getCaptcha" />
          </div>
        </el-form-item-->

        <!--/el-row :gutter="20">
          <el-col :span="8"><div class="grid-content ep-bg-purple" />
            <el-checkbox>
              {{ $t("login.rememberMe") }}
            </el-checkbox>
          </el-col>
          <el-col :span="5" :offset="7"><div class="grid-content ep-bg-purple" />
            <el-link type="primary" href="/forget-password">
              {{ $t("login.forgetPassword") }}
            </el-link>
          </el-col>
          <el-col :span="4"><div class="grid-content ep-bg-purple" />
            <el-link type="primary" href="/forget-password">
              {{ $t("login.register") }}
            </el-link>
          </el-col>
        </el-row-->

        <!-- 登錄按鈕 -->
        <el-button
          :loading="loading"
          type="primary"
          size="large"
          class="w-full"
          @click.prevent="handleLoginSubmit"
        >
          {{ $t("login.login") }}
        </el-button>

        <!-- 第三方登錄 -->
        <!--el-divider>
          <el-text size="small">{{ $t("login.otherLoginMethods") }}</el-text>
        </el-divider>
        <div class="third-party-login">
          <svg-icon icon-class="wechat" class="icon" />
          <svg-icon icon-class="qq" class="icon" />
          <svg-icon icon-class="github" class="icon" />
          <svg-icon icon-class="gitee" class="icon" />
        </div-->
      </el-form>
    </div>

    <!-- 登錄頁底部 -->
    <!--div class="login-footer">
      <el-text size="small">
        Copyright © 2021 - 2025 youlai.tech All Rights Reserved.
        <a href="http://beian.miit.gov.cn/" target="_blank">皖ICP備20006496號-2</a>
      </el-text>
    </div-->
  </div>
</template>

<script setup lang="ts">
import { LocationQuery, useRoute } from "vue-router";
import { useI18n } from "vue-i18n";

import AuthAPI, { type LoginFormData } from "@/api/auth";
import router from "@/router";

import type { FormInstance } from "element-plus";

import defaultSettings from "@/settings";
import { ThemeEnum } from "@/enums/ThemeEnum";

import { useSettingsStore, useUserStore } from "@/store";

import logo from "@/assets/login_logo.png";

const userStore = useUserStore();
const settingsStore = useSettingsStore();

const route = useRoute();
const { t } = useI18n();
const loginFormRef = ref<FormInstance>();

const isDark = ref(settingsStore.theme === ThemeEnum.DARK); // 是否暗黑模式
const loading = ref(false); // 按鈕 loading 狀態
const isCapslock = ref(false); // 是否大寫鎖定
const captchaBase64 = ref(); // 驗證碼圖片Base64字符串

const loginFormData = ref<LoginFormData>({
  username: "",
  password: "",
  captchaKey: "",
  captchaCode: "",
});

const loginRules = computed(() => {
  return {
    username: [
      {
        required: true,
        trigger: "blur",
        message: t("login.message.username.required"),
      },
    ],
    password: [
      {
        required: true,
        trigger: "blur",
        message: t("login.message.password.required"),
      },
    ],
    captchaCode: [
      {
        required: true,
        trigger: "blur",
        message: t("login.message.captchaCode.required"),
      },
    ],
  };
});

// 獲取驗證碼
function getCaptcha() {
  AuthAPI.getCaptcha().then((data) => {
    // console.log(data);
    loginFormData.value.captchaKey = data.data.captchaKey;
    captchaBase64.value = data.data.captchaBase64;
  });
}

// 登錄
async function handleLoginSubmit() {
  loginFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true;
      userStore
        .login(loginFormData.value)
        .then(async () => {
          await userStore.getUserInfo();
          // 需要在路由跳轉前加載字典數據，否則會出現字典數據未加載完成導致頁面渲染異常
          // HE: 不載入字典數據
          //await dictStore.loadDictionaries();
          // 跳轉到登錄前的頁面
          const { path, queryParams } = parseRedirect();
          // HE: 不知道為什麼會導致無限路由，先改成go
          //router.push({ path: path, query: queryParams });
          router.go(0);
        })
        .catch(() => {
          ElMessage.error("登入失敗");
          //getCaptcha();
        })
        .finally(() => {
          loading.value = false;
        });
    }
  });
}

/**
 * 解析 redirect 字符串 為 path 和  queryParams
 *
 * @returns { path: string, queryParams: Record<string, string> } 解析後的 path 和 queryParams
 */
function parseRedirect(): {
  path: string;
  queryParams: Record<string, string>;
} {
  const query: LocationQuery = route.query;
  const redirect = (query.redirect as string) ?? "/";

  const url = new URL(redirect, window.location.origin);
  const path = url.pathname;
  const queryParams: Record<string, string> = {};

  url.searchParams.forEach((value, key) => {
    queryParams[key] = value;
  });

  return { path, queryParams };
}

// 主題切換
const toggleTheme = () => {
  const newTheme = settingsStore.theme === ThemeEnum.DARK ? ThemeEnum.LIGHT : ThemeEnum.DARK;
  settingsStore.changeTheme(newTheme);
};

// 檢查輸入大小寫
function checkCapslock(event: KeyboardEvent) {
  // 防止瀏覽器密碼自動填充時報錯
  if (event instanceof KeyboardEvent) {
    isCapslock.value = event.getModifierState("CapsLock");
  }
}

// 設置登錄憑證
const setLoginCredentials = (username: string, password: string) => {
  loginFormData.value.username = username;
  loginFormData.value.password = password;
};

onMounted(() => {
  // 固定深色
  settingsStore.changeTheme(ThemeEnum.LIGHT);
  // getCaptcha();
});
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow-y: auto;
  background: url("@/assets/images/login-bg.jpg") no-repeat center right;

  .login-header {
    position: absolute;
    top: 0;
    display: flex;
    justify-content: right;
    width: 100%;
    padding: 15px;

    .logo {
      width: 26px;
      height: 26px;
    }

    .title {
      margin: auto 5px;
      font-size: 24px;
      font-weight: bold;
      color: #3b82f6;
    }
  }

  .login-form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 460px;
    padding: 40px;
    overflow: hidden;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: var(--el-box-shadow-light);

    @media (width <= 460px) {
      width: 100%;
      padding: 20px;
    }

    .form-title {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 0 20px;
      text-align: center;
    }

    .input-wrapper {
      display: flex;
      align-items: center;
      width: 100%;
    }

    .captcha-img {
      height: 48px;
      cursor: pointer;
      border-top-right-radius: 6px;
      border-bottom-right-radius: 6px;
    }

    .third-party-login {
      display: flex;
      justify-content: center;
      width: 100%;
      color: var(--el-text-color-secondary);

      *:not(:first-child) {
        margin-left: 20px;
      }

      .icon {
        cursor: pointer;
      }
    }
  }

  .login-footer {
    position: fixed;
    bottom: 0;
    width: 100%;
    padding: 10px 0;
    text-align: center;
  }
}

:deep(.el-form-item) {
  background: var(--el-input-bg-color);
  border: 1px solid var(--el-border-color);
  border-radius: 5px;
}

:deep(.el-input) {
  .el-input__wrapper {
    padding: 0;
    background-color: transparent;
    box-shadow: none;

    &.is-focus,
    &:hover {
      box-shadow: none !important;
    }

    input:-webkit-autofill {
      /* 通過延時渲染背景色變相去除背景顏色 */
      transition: background-color 1000s ease-in-out 0s;
    }
  }
}

html.dark {
  .login {
    background: url("@/assets/images/login-bg-dark.jpg") no-repeat center right;

    .login-form {
      background: transparent;
      box-shadow: var(--el-box-shadow);
    }
  }
}
</style>
