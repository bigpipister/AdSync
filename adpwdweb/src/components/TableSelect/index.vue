<template>
  <div ref="tableSelectRef" :style="'width:' + width">
    <el-popover
      :visible="popoverVisible"
      :width="popoverWidth"
      placement="bottom-end"
      v-bind="selectConfig.popover"
      @show="handleShow"
    >
      <template #reference>
        <div @click="popoverVisible = !popoverVisible">
          <slot>
            <el-input
              class="reference"
              :model-value="text"
              :readonly="true"
              :placeholder="placeholder"
            >
              <template #suffix>
                <el-icon
                  :style="{
                    transform: popoverVisible ? 'rotate(180deg)' : 'rotate(0)',
                    transition: 'transform .5s',
                  }"
                >
                  <ArrowDown />
                </el-icon>
              </template>
            </el-input>
          </slot>
        </div>
      </template>
      <!-- 彈出框內容 -->
      <div ref="popoverContentRef">
        <!-- 表單 -->
        <el-form ref="formRef" :model="queryParams" :inline="true">
          <template v-for="item in selectConfig.formItems" :key="item.prop">
            <el-form-item :label="item.label" :prop="item.prop">
              <!-- Input 輸入框 -->
              <template v-if="item.type === 'input'">
                <template v-if="item.attrs?.type === 'number'">
                  <el-input
                    v-model.number="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
                <template v-else>
                  <el-input
                    v-model="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
              </template>
              <!-- Select 選擇器 -->
              <template v-else-if="item.type === 'select'">
                <el-select v-model="queryParams[item.prop]" v-bind="item.attrs">
                  <template v-for="option in item.options" :key="option.value">
                    <el-option :label="option.label" :value="option.value" />
                  </template>
                </el-select>
              </template>
              <!-- TreeSelect 樹形選擇 -->
              <template v-else-if="item.type === 'tree-select'">
                <el-tree-select v-model="queryParams[item.prop]" v-bind="item.attrs" />
              </template>
              <!-- DatePicker 日期選擇器 -->
              <template v-else-if="item.type === 'date-picker'">
                <el-date-picker v-model="queryParams[item.prop]" v-bind="item.attrs" />
              </template>
              <!-- Input 輸入框 -->
              <template v-else>
                <template v-if="item.attrs?.type === 'number'">
                  <el-input
                    v-model.number="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
                <template v-else>
                  <el-input
                    v-model="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
              </template>
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
            <el-button icon="refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 列表 -->
        <el-table
          ref="tableRef"
          v-loading="loading"
          :data="pageData"
          :border="true"
          :max-height="250"
          :row-key="pk"
          :highlight-current-row="true"
          :class="{ radio: !isMultiple }"
          @select="handleSelect"
          @select-all="handleSelectAll"
        >
          <template v-for="col in selectConfig.tableColumns" :key="col.prop">
            <!-- 自定義 -->
            <template v-if="col.templet === 'custom'">
              <el-table-column v-bind="col">
                <template #default="scope">
                  <slot :name="col.slotName ?? col.prop" :prop="col.prop" v-bind="scope" />
                </template>
              </el-table-column>
            </template>
            <!-- 其他 -->
            <template v-else>
              <el-table-column v-bind="col" />
            </template>
          </template>
        </el-table>
        <!-- 分頁 -->
        <pagination
          v-if="total > 0"
          v-model:total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="handlePagination"
        />
        <div class="feedback">
          <el-button type="primary" size="small" @click="handleConfirm">
            {{ confirmText }}
          </el-button>
          <el-button size="small" @click="handleClear">清 空</el-button>
          <el-button size="small" @click="handleClose">關 閉</el-button>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from "vue";
import { onClickOutside, useResizeObserver } from "@vueuse/core";
import type { FormInstance, PopoverProps, TableInstance } from "element-plus";

// 對象類型
export type IObject = Record<string, any>;
// 定義接收的屬性
export interface ISelectConfig<T = any> {
  // 寬度
  width?: string;
  // 佔位符
  placeholder?: string;
  // popover組件屬性
  popover?: Partial<Omit<PopoverProps, "visible" | "v-model:visible">>;
  // 列表的網絡請求函數(需返回promise)
  indexAction: (queryParams: T) => Promise<any>;
  // 主鍵名(跨頁選擇必填,默認為id)
  pk?: string;
  // 多選
  multiple?: boolean;
  // 表單項
  formItems: Array<{
    // 組件類型(如input,select等)
    type?: "input" | "select" | "tree-select" | "date-picker";
    // 標籤文本
    label: string;
    // 鍵名
    prop: string;
    // 組件屬性
    attrs?: IObject;
    // 初始值
    initialValue?: any;
    // 可選項(適用於select組件)
    options?: { label: string; value: any }[];
  }>;
  // 列選項
  tableColumns: Array<{
    type?: "default" | "selection" | "index" | "expand";
    label?: string;
    prop?: string;
    width?: string | number;
    [key: string]: any;
  }>;
}
const props = withDefaults(
  defineProps<{
    selectConfig: ISelectConfig;
    text?: string;
  }>(),
  {
    text: "",
  }
);

// 自定義事件
const emit = defineEmits<{
  confirmClick: [selection: any[]];
}>();

// 主鍵
const pk = props.selectConfig.pk ?? "id";
// 是否多選
const isMultiple = props.selectConfig.multiple === true;
// 寬度
const width = props.selectConfig.width ?? "100%";
// 佔位符
const placeholder = props.selectConfig.placeholder ?? "請選擇";
// 是否顯示彈出框
const popoverVisible = ref(false);
// 加載狀態
const loading = ref(false);
// 數據總數
const total = ref(0);
// 列表數據
const pageData = ref<IObject[]>([]);
// 每頁條數
const pageSize = 10;
// 搜索參數
const queryParams = reactive<{
  pageNum: number;
  pageSize: number;
  [key: string]: any;
}>({
  pageNum: 1,
  pageSize: pageSize,
});

// 計算popover的寬度
const tableSelectRef = ref();
const popoverWidth = ref(width);
useResizeObserver(tableSelectRef, (entries) => {
  popoverWidth.value = `${entries[0].contentRect.width}px`;
});

// 表單操作
const formRef = ref<FormInstance>();
// 初始化搜索條件
for (const item of props.selectConfig.formItems) {
  queryParams[item.prop] = item.initialValue ?? "";
}
// 重置操作
function handleReset() {
  formRef.value?.resetFields();
  fetchPageData(true);
}
// 查詢操作
function handleQuery() {
  fetchPageData(true);
}

// 獲取分頁數據
function fetchPageData(isRestart = false) {
  loading.value = true;
  if (isRestart) {
    queryParams.pageNum = 1;
    queryParams.pageSize = pageSize;
  }
  props.selectConfig
    .indexAction(queryParams)
    .then((data) => {
      total.value = data.total;
      pageData.value = data.list;
    })
    .finally(() => {
      loading.value = false;
    });
}

// 列表操作
const tableRef = ref<TableInstance>();
// 數據刷新後是否保留選項
for (const item of props.selectConfig.tableColumns) {
  if (item.type === "selection") {
    item.reserveSelection = true;
    break;
  }
}
// 選擇
const selectedItems = ref<IObject[]>([]);
const confirmText = computed(() => {
  return selectedItems.value.length > 0 ? `已選(${selectedItems.value.length})` : "確 定";
});
function handleSelect(selection: any[], row: any) {
  if (isMultiple || selection.length === 0) {
    // 多選
    selectedItems.value = selection;
  } else {
    // 單選
    selectedItems.value = [selection[selection.length - 1]];
    tableRef.value?.clearSelection();
    tableRef.value?.toggleRowSelection(selectedItems.value[0], true);
    tableRef.value?.setCurrentRow(selectedItems.value[0]);
  }
}
function handleSelectAll(selection: any[]) {
  if (isMultiple) {
    selectedItems.value = selection;
  }
}
// 分頁
function handlePagination() {
  fetchPageData();
}

// 彈出框
const isInit = ref(false);
// 顯示
function handleShow() {
  if (isInit.value === false) {
    isInit.value = true;
    fetchPageData();
  }
}
// 確定
function handleConfirm() {
  if (selectedItems.value.length === 0) {
    ElMessage.error("請選擇數據");
    return;
  }
  popoverVisible.value = false;
  emit("confirmClick", selectedItems.value);
}
// 清空
function handleClear() {
  tableRef.value?.clearSelection();
  selectedItems.value = [];
}
// 關閉
function handleClose() {
  popoverVisible.value = false;
}
const popoverContentRef = ref();
/* onClickOutside(tableSelectRef, () => (popoverVisible.value = false), {
  ignore: [popoverContentRef],
}); */
</script>

<style scoped lang="scss">
.reference :deep(.el-input__wrapper),
.reference :deep(.el-input__inner) {
  cursor: pointer;
}

.feedback {
  display: flex;
  justify-content: flex-end;
  margin-top: 6px;
}
// 隱藏全選按鈕
.radio :deep(.el-table__header th.el-table__cell:nth-child(1) .el-checkbox) {
  visibility: hidden;
}
</style>
