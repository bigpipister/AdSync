import { ref } from "vue";
import type { IObject, PageContentInstance, PageModalInstance, PageSearchInstance } from "./types";

function usePage() {
  const searchRef = ref<PageSearchInstance>();
  const contentRef = ref<PageContentInstance>();
  const addModalRef = ref<PageModalInstance>();
  const editModalRef = ref<PageModalInstance>();

  // 搜索
  function handleQueryClick(queryParams: IObject) {
    const filterParams = contentRef.value?.getFilterParams();
    contentRef.value?.fetchPageData({ ...queryParams, ...filterParams }, true);
  }
  // 重置
  function handleResetClick(queryParams: IObject) {
    const filterParams = contentRef.value?.getFilterParams();
    contentRef.value?.fetchPageData({ ...queryParams, ...filterParams }, true);
  }
  // 新增
  function handleAddClick() {
    //顯示添加表單
    addModalRef.value?.setModalVisible();
  }
  // 編輯
  function handleEditClick(row: IObject) {
    //顯示編輯表單 根據數據進行填充
    editModalRef.value?.setModalVisible(row);
  }
  // 表單提交
  function handleSubmitClick() {
    //根據檢索條件刷新列表數據
    const queryParams = searchRef.value?.getQueryParams();
    contentRef.value?.fetchPageData(queryParams, true);
  }
  // 導出
  function handleExportClick() {
    // 根據檢索條件導出數據
    const queryParams = searchRef.value?.getQueryParams();
    contentRef.value?.exportPageData(queryParams);
  }
  // 搜索顯隱
  function handleSearchClick() {
    searchRef.value?.toggleVisible();
  }
  // 涮選數據
  function handleFilterChange(filterParams: IObject) {
    const queryParams = searchRef.value?.getQueryParams();
    contentRef.value?.fetchPageData({ ...queryParams, ...filterParams }, true);
  }

  return {
    searchRef,
    contentRef,
    addModalRef,
    editModalRef,
    handleQueryClick,
    handleResetClick,
    handleAddClick,
    handleEditClick,
    handleSubmitClick,
    handleExportClick,
    handleSearchClick,
    handleFilterChange,
  };
}

export default usePage;
