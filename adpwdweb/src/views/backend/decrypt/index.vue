<template>
  <div class="app-container">
    <div class="search-bar">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">

        <el-form-item prop="cn" label="帳號:">
          <el-input
            v-model="queryParams.cn"
            placeholder=""
            clearable
          />
        </el-form-item>

        

        <el-form-item>
          <el-button type="primary" icon="search" @click="handleQuery">反解</el-button>
        </el-form-item>
        </el-form>

        <el-form-item prop="encryptPwd" label="加密密碼:" style="width: 100%">
          <el-input
            v-model="queryParams.encryptPwd"
            placeholder=""
            clearable
          />
        </el-form-item>
    </div>

    <el-card shadow="never">
      <template #header>反解結果</template>
      <div v-if="resultPwd">
        <p><strong>原始密碼：</strong> {{ resultPwd }}</p>
      </div>
      <div v-else>
        <p>請輸入帳號與加密密碼後點擊「反解」</p>
      </div>
    </el-card>

  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Event",
  inheritAttrs: false,
});

import DecryptAPI, { DecryptPwdQuery, DecryptPwdResult } from "@/api/decrypt";

const loading = ref(false);

// 畫面上的查詢條件
const queryParams = reactive<DecryptPwdQuery>({
  cn: '',
  encryptPwd: ''
});

const resultPwd = ref<string | null>(null);

// 查詢
function handleQuery() {
  // console.log(queryParams);
  // console.log(typeof queryParams.region);
  // 組出查詢api query
  const apiRequest: DecryptPwdQuery = {
    cn: queryParams.cn,
    encryptPwd: queryParams.encryptPwd
  }

  loading.value = true;
  DecryptAPI.decryptPwd(apiRequest)
    .then((data) => {
      resultPwd.value = data.data || "(空)";
      console.log(resultPwd.value);
    })
    .catch(() => {
      resultPwd.value = "反解失敗或錯誤";
    })
    .finally(() => {
      loading.value = false;
    });
}

onMounted(() => {
  // handleQuery();
});
</script>
