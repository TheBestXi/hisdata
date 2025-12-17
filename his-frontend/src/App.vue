<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const locale = computed(() => zhCn)
</script>

<style>
#app {
  width: 100%;
  height: 100vh;
}
</style>
