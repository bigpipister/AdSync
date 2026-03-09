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

        <el-form-item prop="activitydisplayname" label="異動名稱:">
          <el-input
            v-model="queryParams.activitydisplayname"
            placeholder=""
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item prop="targetresources" label="DN:">
          <el-input
            v-model="queryParams.targetresources"
            placeholder=""
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="開始日期"
            end-placeholder="結束日期"
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
        :data="auditList"
        highlight-current-row
        border
        empty-text="暫無資料"
      >
        <el-table-column label="作業名稱" prop="activitydisplayname" width="100" />
        <el-table-column label="DN" prop="targetresources" min-width="200" />

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
      width="700px"
      @close="handleCloseDialog"
    >
      <el-form ref="auditFormRef" :model="formData" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24"><div class="grid-content ep-bg-purple" />
            <div style="max-height: 500px; overflow: auto; white-space: pre-wrap; font-family: monospace;">
              {{ formData.additionaldetails }}
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

import AuditAPI, { AuditListQuery, AuditLogsResult, AuditLogs } from "@/api/audit";
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
const auditFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

// 將選項陣列提取出來
const regionOptions = ref([
  { label: '內網', value: 'inner' },
  { label: '外網', value: 'external' }
]);

const end = new Date();
end.setHours(23, 59, 59, 999); // 設定為當日 23:59:59

const start = new Date();
start.setTime(start.getTime() - 3600 * 1000 * 24 * 7); // 七天前
start.setHours(0, 0, 0, 0); // 設定為 00:00:00

const dateRange = ref<string[]>([
  start.toISOString(),
  end.toISOString()
]);

// 畫面上的查詢條件
const queryParams = reactive<AuditListQuery>({
  page: 1,
  size: 10,
  region: "inner",
  activitydisplayname: '',
  targetresources: '',
  startTime: '',
  endTime: ''
});

// 事件表格數據
const auditList = ref<AuditLogs[]>();

// 彈窗
const dialog = reactive({
  title: "稽核詳細內容",
  visible: false,
});

// 事件表單
const formData = ref<AuditLogs>({} as AuditLogs);

// 查詢
function handleQuery() {
  // console.log(queryParams);
  // console.log(typeof queryParams.region);
  // 組出查詢api query
  const apiRequest: AuditListQuery = {
    page: queryParams.page,
    size: queryParams.size,
    region: queryParams.region,
    activitydisplayname: queryParams.activitydisplayname,
    targetresources: queryParams.targetresources,
    startTime: dateRange.value[0],
    endTime: dateRange.value[1],
  }

  loading.value = true;
  AuditAPI.getPage(apiRequest)
    .then((data) => {
      // console.log(data);
      auditList.value = data.data || [];
      // 根據發生的時間去排序
      auditList.value.sort((a, b) => new Date(a.activitydatetime ?? '').getTime() - new Date(b.activitydatetime ?? '').getTime())
      total.value = Number(data.total);
    })
    .finally(() => {
      loading.value = false;
    });
}

// 打開會員彈窗
function handleOpenDialog(row:AuditLogs) {
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
