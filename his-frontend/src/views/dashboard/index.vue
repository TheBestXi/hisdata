<template>
  <div class="p-4 space-y-4">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <el-card shadow="hover" class="bg-blue-50">
        <div class="text-gray-500">今日接诊</div>
        <div class="text-3xl font-bold text-blue-600">128</div>
      </el-card>
      <el-card shadow="hover" class="bg-green-50">
        <div class="text-gray-500">今日营收 (¥)</div>
        <div class="text-3xl font-bold text-green-600">45,200</div>
      </el-card>
      <el-card shadow="hover" class="bg-orange-50">
        <div class="text-gray-500">待处理处方</div>
        <div class="text-3xl font-bold text-orange-600">12</div>
      </el-card>
      <el-card shadow="hover" class="bg-purple-50">
        <div class="text-gray-500">在线医生</div>
        <div class="text-3xl font-bold text-purple-600">8</div>
      </el-card>
    </div>

    <el-card header="科室工作量统计">
      <div class="h-64 flex items-end justify-around space-x-2 p-4">
        <!-- Mock Bar Chart -->
        <div class="w-16 bg-blue-500 rounded-t" style="height: 60%"></div>
        <div class="w-16 bg-blue-500 rounded-t" style="height: 80%"></div>
        <div class="w-16 bg-blue-500 rounded-t" style="height: 40%"></div>
        <div class="w-16 bg-blue-500 rounded-t" style="height: 90%"></div>
      </div>
      <div class="flex justify-around text-sm text-gray-500 mt-2">
        <span>内科</span>
        <span>外科</span>
        <span>儿科</span>
        <span>急诊</span>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>
