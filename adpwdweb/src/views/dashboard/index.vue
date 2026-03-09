<template>
  <div class="dashboard-container">
    <el-card shadow="never" class="mt-2">
      <el-row class="h-80px">
        <el-col :span="18" :xs="24">
          <div class="flex-x-start">
            <img
              class="wh-80px rounded-full"
              :src="logo"
            />
            <div class="ml-5">
              <p>{{ greetings }}</p>
              <!--p class="text-sm text-gray">今日天氣晴朗，氣溫在15℃至25℃之間，東南風。</p-->
            </div>
          </div>
        </el-col>

        <!--el-col :span="6" :xs="24">
          <el-row class="h-80px flex-y-center" :gutter="10">
            <el-col :span="10">
              <div class="font-bold color-#ff9a2e text-sm flex-y-center">
                <el-icon class="mr-2px"><Folder /></el-icon>
                倉庫
              </div>
              <div class="mt-3">
                <el-link href="https://gitee.com/youlaiorg/vue3-element-admin" target="_blank">
                  <SvgIcon icon-class="gitee" class="text-lg color-#f76560" />
                </el-link>
                <el-divider direction="vertical" />
                <el-link href="https://github.com/youlaitech/vue3-element-admin" target="_blank">
                  <SvgIcon icon-class="github" class="text-lg color-#4080ff" />
                </el-link>
                <el-divider direction="vertical" />
                <el-link href="https://gitcode.com/youlai/vue3-element-admin" target="_blank">
                  <SvgIcon icon-class="gitcode" class="text-lg color-#ff9a2e" />
                </el-link>
              </div>
            </el-col>

            <el-col :span="10">
              <div class="font-bold color-#4080ff text-sm flex-y-center">
                <el-icon class="mr-2px"><Document /></el-icon>
                文檔
              </div>
              <div class="mt-3">
                <el-link href="https://juejin.cn/post/7228990409909108793" target="_blank">
                  <SvgIcon icon-class="juejin" class="text-lg" />
                </el-link>
                <el-divider direction="vertical" />
                <el-link
                  href="https://youlai.blog.csdn.net/article/details/130191394"
                  target="_blank"
                >
                  <SvgIcon icon-class="csdn" class="text-lg" />
                </el-link>
                <el-divider direction="vertical" />
                <el-link href="https://www.cnblogs.com/haoxianrui/p/17331952.html" target="_blank">
                  <SvgIcon icon-class="cnblogs" class="text-lg" />
                </el-link>
              </div>
            </el-col>

            <el-col :span="4">
              <div class="font-bold color-#f76560 text-sm flex-y-center">
                <el-icon class="mr-2px"><VideoCamera /></el-icon>
                視頻
              </div>
              <div class="mt-3">
                <el-link href="https://www.bilibili.com/video/BV1eFUuYyEFj" target="_blank">
                  <SvgIcon icon-class="bilibili" class="text-lg" />
                </el-link>
              </div>
            </el-col>
          </el-row>
        </el-col-->
      </el-row>
    </el-card>

    <el-card shadow="never" class="mt-2">
      <template #header>
        <div class="flex-x-between">
          <span class="text-gray">內網</span>
        </div>
      </template>
      <el-row :gutter="10" class="mt-2">
        <el-col v-for="(item, index) in inStatusLogs" :key="index" :span="8">
          <el-card shadow="never">
            <template #header>
              <div class="flex-x-between">
                <span class="text-gray">{{ item.displayname }}</span>
                <el-tag v-if="item.health==='Y'" type="success" size="small">正常</el-tag>
                <el-tag v-else type="danger" size="small">異常</el-tag>
              </div>
              <span class="text-gray" style="font-size:12px">{{item.dn}}</span>
            </template>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">執行中</span>
              <el-tag v-if="item.running===true" type="primary" size="small">是</el-tag>
              <el-tag v-else type="info" size="small">否</el-tag>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">啟動時間</span>
              <span class="text-gray">{{ formatDate(item.startdatetime) }}</span>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">執行耗時</span>
              <span class="text-gray">{{ item.durationseconds }} 秒</span>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">超時告警</span>
              <span v-if="diffTime(item.startdatetime) > 30" style="color:red">{{ diffTime(item.startdatetime) }} 分</span>
              <span v-else class="text-gray">{{ diffTime(item.startdatetime) }} 分</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never" class="mt-2">
      <template #header>
        <div class="flex-x-between">
          <span class="text-gray">外網</span>
        </div>
      </template>
      <el-row :gutter="10" class="mt-2">
        <el-col v-for="(item, index) in outStatusLogs" :key="index" :span="8">
          <el-card shadow="never">
            <template #header>
              <div class="flex-x-between">
                <span class="text-gray">{{ item.displayname }}</span>
                <el-tag v-if="item.health==='Y'" type="success" size="small">正常</el-tag>
                <el-tag v-else type="danger" size="small">異常</el-tag>
              </div>
              <span class="text-gray" style="font-size:12px">{{item.dn}}</span>
            </template>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">執行中</span>
              <el-tag v-if="item.running===true" type="primary" size="small">是</el-tag>
              <el-tag v-else type="info" size="small">否</el-tag>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">啟動時間</span>
              <span class="text-gray">{{ formatDate(item.startdatetime) }}</span>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">執行耗時</span>
              <span class="text-gray">{{ item.durationseconds }} 秒</span>
            </div>

            <div class="flex-x-between mt-2 text-sm text-gray">
              <span class="text-gray">超時告警</span>
              <span v-if="diffTime(item.startdatetime) > 30" style="color:red">{{ diffTime(item.startdatetime) }} 分</span>
              <span v-else class="text-gray">{{ diffTime(item.startdatetime) }} 分</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Dashboard",
  inheritAttrs: false,
});

import StatusAPI, { StatusLogsResult, StatusLogs } from "@/api/status";
import { useUserStore } from "@/store/modules/user";
import logo from "@/assets/user.png";

const userStore = useUserStore();
const date: Date = new Date();
const greetings = computed(() => {
  const hours = date.getHours();
  if (hours >= 6 && hours < 8) {
    return "晨起披衣出草堂，軒窗已自喜微涼🌅！";
  } else if (hours >= 8 && hours < 12) {
    return "上午好，" + userStore.userInfo.nickname + "！";
  } else if (hours >= 12 && hours < 18) {
    return "下午好，" + userStore.userInfo.nickname + "！";
  } else if (hours >= 18 && hours < 24) {
    return "晚上好，" + userStore.userInfo.nickname + "！";
  } else {
    return "偷偷向銀河要了一把碎星，只等你閉上眼睛撒入你的夢中，晚安🌛！";
  }
});

const statusLogsResult = ref<StatusLogsResult>();
const inStatusLogs = ref<StatusLogs[]>();
const outStatusLogs = ref<StatusLogs[]>();

const loadStatusLogs = () => {
  StatusAPI.getStatusLogs().then((data) => {
    // console.log(data);
    statusLogsResult.value = data;
    inStatusLogs.value = statusLogsResult.value.data?.filter(item => item.region === 'inner');
    outStatusLogs.value = statusLogsResult.value.data?.filter(item => item.region === 'external');
  });
};

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

const diffTime = (datetime:any) => {
  if (!datetime) return 0;

  const now = new Date().getTime();
  const targetTime = new Date(datetime).getTime();
  return Math.floor((now - targetTime) / 60000); // 60000 毫秒 = 1 分鐘
};

let intervalId:any = null;

onMounted(() => {
  loadStatusLogs();
  // intervalId = setInterval(loadStatusLogs, 5000); // 每 5 秒執行一次
});

// onUnmounted(() => {
//   clearInterval(intervalId); // 組件卸載時清除 interval，避免記憶體洩漏
// });
</script>

<style lang="scss" scoped>
.dashboard-container {
  position: relative;
  padding: 24px;

  .github-corner {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 1;
    border: 0;
  }
}
</style>
