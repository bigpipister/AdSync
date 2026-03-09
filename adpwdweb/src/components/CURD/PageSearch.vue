<template>
  <el-card
    v-show="visible"
    v-hasPerm="[`${searchConfig.pageName}:query`]"
    shadow="never"
    class="mb-[10px]"
  >
    <el-form ref="queryFormRef" :model="queryParams" :inline="true">
      <template v-for="(item, index) in formItems" :key="item.prop">
        <el-form-item
          v-show="isExpand ? true : index < showNumber"
          :label="item.label"
          :prop="item.prop"
        >
          <!-- Label -->
          <template v-if="item.tips" #label>
            <span>
              {{ item.label }}
              <el-tooltip
                placement="bottom"
                effect="light"
                :content="item.tips"
                :raw-content="true"
              >
                <el-icon style="vertical-align: -0.15em" size="16">
                  <QuestionFilled />
                </el-icon>
              </el-tooltip>
            </span>
          </template>
          <!-- Input 輸入框 -->
          <template v-if="item.type === 'input' || item.type === undefined">
            <el-input
              v-model="queryParams[item.prop]"
              v-bind="item.attrs"
              @keyup.enter="handleQuery"
            />
          </template>
          <!-- InputTag 標籤輸入框 -->
          <template v-if="item.type === 'input-tag'">
            <div class="flex-center">
              <el-tag
                v-for="tag in inputTagMap[item.prop].data"
                :key="tag"
                class="mr-2"
                :closable="true"
                v-bind="inputTagMap[item.prop].tagAttrs"
                @close="handleCloseTag(item.prop, tag)"
              >
                {{ tag }}
              </el-tag>
              <template v-if="inputTagMap[item.prop].inputVisible">
                <el-input
                  :ref="(el: HTMLElement) => (inputTagMap[item.prop].inputRef = el)"
                  v-model="inputTagMap[item.prop].inputValue"
                  v-bind="inputTagMap[item.prop].inputAttrs"
                  @keyup.enter="handleInputConfirm(item.prop)"
                  @blur="handleInputConfirm(item.prop)"
                />
              </template>
              <template v-else>
                <el-button
                  v-bind="inputTagMap[item.prop].buttonAttrs"
                  @click="handleShowInput(item.prop)"
                >
                  {{ inputTagMap[item.prop].buttonAttrs.btnText }}
                </el-button>
              </template>
            </div>
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
        </el-form-item>
      </template>
      <el-form-item>
        <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
        <el-button icon="refresh" @click="handleReset">重置</el-button>
        <!-- 展開/收起 -->
        <el-link
          v-if="isExpandable && formItems.length > showNumber"
          class="ml-2"
          type="primary"
          :underline="false"
          @click="isExpand = !isExpand"
        >
          <template v-if="isExpand">
            收起
            <el-icon>
              <ArrowUp />
            </el-icon>
          </template>
          <template v-else>
            展開
            <el-icon>
              <ArrowDown />
            </el-icon>
          </template>
        </el-link>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import type { FormInstance } from "element-plus";
import { reactive, ref } from "vue";
import type { IObject, ISearchConfig } from "./types";

// 定義接收的屬性
const props = defineProps<{
  searchConfig: ISearchConfig;
}>();
// 自定義事件
const emit = defineEmits<{
  queryClick: [queryParams: IObject];
  resetClick: [queryParams: IObject];
}>();

const queryFormRef = ref<FormInstance>();
// 是否顯示
const visible = ref(true);
// 響應式的formItems
const formItems = reactive(props.searchConfig.formItems);
// 是否可展開/收縮
const isExpandable = ref(props.searchConfig.isExpandable ?? true);
// 是否已展開
const isExpand = ref(false);
// 表單項展示數量，若可展開，超出展示數量的表單項隱藏
const showNumber = computed(() => {
  if (isExpandable.value === true) {
    return props.searchConfig.showNumber ?? 3;
  } else {
    return formItems.length;
  }
});
// 搜索表單數據
const queryParams = reactive<IObject>({});
const inputTagMap = reactive<IObject>({});
for (const item of formItems) {
  item.initFn && item.initFn(item);
  if (item.type === "input-tag") {
    inputTagMap[item.prop] = {
      data: Array.isArray(item.initialValue) ? item.initialValue : [],
      inputVisible: false,
      inputValue: "",
      inputRef: null,
      buttonAttrs: {
        size: item.attrs?.size ?? "default",
        btnText: item.attrs?.btnText ?? "+ New Tag",
        style: "color: #b0b2b7",
      },
      inputAttrs: {
        size: item.attrs?.size ?? "default",
        clearable: item.attrs?.clearable ?? false,
        style: "width: 150px",
      },
      tagAttrs: {
        size: item.attrs?.size ?? "default",
      },
    };
    queryParams[item.prop] = computed({
      get() {
        return typeof item.attrs?.join === "string"
          ? inputTagMap[item.prop].data.join(item.attrs.join)
          : inputTagMap[item.prop].data;
      },
      set(value) {
        // resetFields時會被調用
        inputTagMap[item.prop].data =
          typeof item.attrs?.join === "string"
            ? value.split(item.attrs.join).filter((item: any) => item !== "")
            : value;
      },
    });
  } else {
    queryParams[item.prop] = item.initialValue ?? "";
  }
}

// 重置操作
function handleReset() {
  queryFormRef.value?.resetFields();
  emit("resetClick", queryParams);
}

// 查詢操作
function handleQuery() {
  emit("queryClick", queryParams);
}

// 獲取分頁數據
function getQueryParams() {
  return queryParams;
}

// 顯示/隱藏 SearchForm
function toggleVisible() {
  visible.value = !visible.value;
}

// 關閉標籤
function handleCloseTag(prop: string, tag: string) {
  inputTagMap[prop].data.splice(inputTagMap[prop].data.indexOf(tag), 1);
}
// 添加標籤
function handleInputConfirm(prop: string) {
  if (inputTagMap[prop].inputValue) {
    inputTagMap[prop].data.push(inputTagMap[prop].inputValue);
  }
  inputTagMap[prop].inputVisible = false;
  inputTagMap[prop].inputValue = "";
}
// 顯示標籤輸入框
function handleShowInput(prop: string) {
  inputTagMap[prop].inputVisible = true;
  nextTick(() => {
    inputTagMap[prop].inputRef.focus();
  });
}

// 暴露的屬性和方法
defineExpose({ getQueryParams, toggleVisible });
</script>

<style lang="scss" scoped></style>
