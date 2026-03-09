<template>
  <div class="app-container">
    <div class="search-bar">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="region" label="區域:">
          <el-switch
            v-model="queryParams.region"
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #1316ce"
            active-text="外網"
            inactive-text="內網"
            active-value="external"
            inactive-value="inner"
            @change="handleQuery"
          />
        </el-form-item>

        <el-form-item prop="jobname" label="作業名稱:">
          <el-input
            v-model="queryParams.jobname"
            placeholder=""
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item prop="dn" label="DN:">
          <el-input
            v-model="queryParams.dn"
            placeholder=""
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item prop="keyword" label="事件關鍵字:">
          <el-input
            v-model="queryParams.keyword"
            placeholder=""
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never">
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="eventList"
        highlight-current-row
        border
        empty-text="暫無資料"
      >
        <el-table-column label="作業名稱" prop="jobname" width="100" />
        <el-table-column label="DN" prop="dn" min-width="200" />

        <el-table-column label="事件時間" align="left" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.activitydatetime) }}
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              icon="position"
              @click="handleOpenDialog(scope.row)"
            >
              查看
            </el-button>
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

    <!-- 會員表單彈窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="800px"
      @close="handleCloseDialog"
    >
      <el-form ref="eventFormRef" :model="formData" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24"><div class="grid-content ep-bg-purple" />
            <div style="max-height: 500px; overflow: auto; white-space: pre-wrap; font-family: monospace;">
              {{ formData.exceptioncontent }}
            </div>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Event",
  inheritAttrs: false,
});

import EventAPI, { EventListQuery, EventLogsResult, EventLogs } from "@/api/event";
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
const eventFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

// 將選項陣列提取出來
const regionOptions = ref([
  { label: '內網', value: 'inner' },
  { label: '外網', value: 'external' }
]);

// 畫面上的查詢條件
const queryParams = reactive<EventListQuery>({
  page: 1,
  size: 10,
  region: "inner",
  jobname: '',
  dn: '',
  keyword: ''
});

// 事件表格數據
const eventList = ref<EventLogs[]>([]);

// 彈窗
const dialog = reactive({
  title: "事件詳細內容",
  visible: false,
});

// 事件表單
const formData = ref<EventLogs>({} as EventLogs);

// 查詢
function handleQuery() {
  // console.log(queryParams);
  // console.log(typeof queryParams.region);
  // 組出查詢api query
  const apiRequest: EventListQuery = {
    page: queryParams.page,
    size: queryParams.size,
    region: queryParams.region,
    jobname: queryParams.jobname,
    dn: queryParams.dn,
    keyword: queryParams.keyword
  }

  loading.value = true;
  EventAPI.getPage(apiRequest)
    .then((data) => {
      // console.log(data);
      eventList.value = data.data || [];
      // 根據發生的時間去排序
      eventList.value.sort((a, b) => new Date(a.activitydatetime ?? '').getTime() - new Date(b.activitydatetime ?? '').getTime())
      total.value = Number(data.total);
    })
    .finally(() => {
      loading.value = false;
    });
}

// 打開會員彈窗
function handleOpenDialog(row:EventLogs) {
  dialog.visible = true;
  Object.assign(formData.value, row);
  // console.log(row.dn);
}

// 關閉彈窗
function handleCloseDialog() {
  dialog.visible = false;
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
