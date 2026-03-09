<template>
  <el-dropdown trigger="click">
    <div class="flex-center h100% p13px">
      <img :src="logo" class="rounded-full mr-10px w24px h24px" />
      <span>{{ userStore.userInfo.username }}</span>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <!--el-dropdown-item @click="handleOpenUserProfile">
          {{ $t("navbar.profile") }}
        </el-dropdown-item-->
        <el-dropdown-item divided @click="logout">
          {{ $t("navbar.logout") }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
defineOptions({
  name: "UserProfile",
});

import { useTagsViewStore, useUserStore } from "@/store";
import logo from "@/assets/user.png";

const tagsViewStore = useTagsViewStore();
const userStore = useUserStore();

const route = useRoute();
const router = useRouter();

/**
 * 打開個人中心頁面
 */
function handleOpenUserProfile() {
  router.push({ name: "Profile" });
}

/**
 * 註銷登出
 */
function logout() {
  ElMessageBox.confirm("確定註銷並退出系統嗎？", "提示", {
    confirmButtonText: "確定",
    cancelButtonText: "取消",
    type: "warning",
    lockScroll: false,
  }).then(() => {
    userStore
      .logout()
      .then(() => {
        tagsViewStore.delAllViews();
      })
      .then(() => {
        router.push(`/login?redirect=${route.fullPath}`);
      });
  });
}
</script>

<style lang="scss" scoped></style>
