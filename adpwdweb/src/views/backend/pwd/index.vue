<template>
  <div class="app-container">
    <div class="search-bar">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never">
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="pwdDiffList"
        highlight-current-row
        border
        empty-text="暫無資料"
      >
        <el-table-column label="帳號" prop="samaccountname" width="100" />
        <el-table-column label="姓名" prop="sn" width="100" />
        <el-table-column label="DN" prop="dn" min-width="200" />

        <el-table-column label="內網密碼異動時間" align="left" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.innerTimestamp) }}
          </template>
        </el-table-column>

        <el-table-column label="外網密碼異動時間" align="left" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.externalTimestamp) }}
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.page"
        v-model:limit="queryParams.size"
        @pagination="handleQuery"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Event",
  inheritAttrs: false,
});

import PwdAPI, { PwdDiffListQuery, PwdDiffResult, PwdDiff } from "@/api/pwd";
import zhLocal from 'element-plus/es/locale/lang/zh-cn';
import { useUserStore } from "@/store";

zhLocal.el.pagination = {
  pagesize: '條/頁',
  total: `共 {total} 條`,
  goto: '前往第',
  pageClassifier: '頁',
  page: '頁',
  prev: '上一頁',
  next: '下一頁',
  prevPages: '上',
  nextPages: '下',
  currentPage: '當前頁',
  deprecationWarning: ''
}

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);


// 畫面上的查詢條件
const queryParams = reactive<PwdDiffListQuery>({
  page: 1,
  size: 10
});

// 事件表格數據
const pwdDiffList = ref<PwdDiff[]>([]);

// 查詢
function handleQuery() {
  // console.log(queryParams);
  // console.log(typeof queryParams.region);
  // 組出查詢api query
  const apiRequest: PwdDiffListQuery = {
    page: queryParams.page,
    size: queryParams.size
  }

  loading.value = true;
  PwdAPI.getPage(apiRequest)
    .then((data) => {
      // console.log(data);
      pwdDiffList.value = data.data || [];
      total.value = Number(data.total);
    })
    .finally(() => {
      loading.value = false;
    });
}

const formatDate = (datetime:any) => {
  return datetime
    ? new Date(datetime).toLocaleString("zh-TW", { 
        year: "numeric", 
        month: "2-digit", 
        day: "2-digit", 
        hour: "2-digit", 
        minute: "2-digit", 
        second: "2-digit" 
      })
    : "無資料";
};

onMounted(() => {
  handleQuery();
  // console.log('Initial region value:', queryParams.region);
  // console.log('Region options:', regionOptions.value);
});
</script>
