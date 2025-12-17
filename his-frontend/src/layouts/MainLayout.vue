<template>
  <el-container class="h-screen w-full">
    <el-aside :width="isCollapse ? '64px' : '240px'" class="transition-all duration-300 bg-white border-r">
      <div class="h-16 flex items-center justify-center border-b">
        <span class="text-xl font-bold text-primary" v-if="!isCollapse">HIS System</span>
        <span class="text-xl font-bold text-primary" v-else>HIS</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        class="border-none"
        :collapse="isCollapse"
        router
      >
        <el-menu-item index="/">
          <el-icon><DataBoard /></el-icon>
          <template #title>首页概览</template>
        </el-menu-item>

        <el-sub-menu index="/outpatient">
          <template #title>
            <el-icon><FirstAidKit /></el-icon>
            <span>门诊管理</span>
          </template>
          <el-menu-item index="/outpatient/registration">挂号办理</el-menu-item>
          <el-menu-item index="/outpatient/history">挂号记录</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/patient">
          <el-icon><User /></el-icon>
          <template #title>患者管理</template>
        </el-menu-item>

        <el-menu-item index="/doctor">
          <el-icon><Monitor /></el-icon>
          <template #title>医生工作台</template>
        </el-menu-item>
        
        <el-menu-item index="/tech">
          <el-icon><Aim /></el-icon>
          <template #title>检查检验</template>
        </el-menu-item>

        <el-sub-menu index="/pharmacy">
          <template #title>
            <el-icon><Box /></el-icon>
            <span>药房管理</span>
          </template>
          <el-menu-item index="/pharmacy/dispensing">药房发药</el-menu-item>
          <el-menu-item index="/pharmacy/inventory">药品库存</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/finance">
          <el-icon><Money /></el-icon>
          <template #title>收费管理</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="bg-white border-b h-16 flex items-center justify-between px-4">
        <div class="flex items-center">
          <el-button link @click="toggleCollapse">
            <el-icon :size="20"><Fold v-if="!isCollapse"/><Expand v-else/></el-icon>
          </el-button>
          <el-breadcrumb class="ml-4" separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="flex items-center space-x-4">
          <el-avatar :size="32" class="bg-primary text-white">医</el-avatar>
          <span class="text-sm font-medium">管理员医生</span>
        </div>
      </el-header>

      <el-main class="bg-gray-50 p-4">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  DataBoard,
  FirstAidKit,
  Monitor,
  Box,
  Money,
  Fold,
  Expand,
  User,
  Aim
} from '@element-plus/icons-vue'

const route = useRoute()
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)
const currentRouteName = computed(() => route.meta.title || '首页概览')

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
