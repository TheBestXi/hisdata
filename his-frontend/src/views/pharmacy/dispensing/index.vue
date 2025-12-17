<template>
  <el-card>
    <div class="mb-4 font-bold">待发药列表</div>
    <el-table :data="queue" border stripe>
      <el-table-column prop="id" label="单号" width="100" />
      <el-table-column prop="patientName" label="患者" />
      <el-table-column prop="items" label="药品详情" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'warning' : 'success'">
            {{ row.status === 1 ? '待发药' : '已发药' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" type="primary" size="small" @click="dispense(row)">确认发药</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDispensingQueue } from '@/api/pharmacy'
import { ElMessage } from 'element-plus'

const queue = ref<any[]>([])

const loadData = async () => {
  const res: any = await getDispensingQueue()
  queue.value = res
}

const dispense = (row: any) => {
  row.status = 2
  ElMessage.success('Medicines dispensed successfully')
}

onMounted(() => {
  loadData()
})
</script>
