<template>
  <el-card>
    <div class="mb-4 font-bold">收费管理 (F1)</div>
    <el-table :data="bills" border stripe>
      <el-table-column prop="financeId" label="账单号" width="100" />
      <el-table-column prop="patientName" label="患者" />
      <el-table-column label="费用明细">
        <template #default="{ row }">
          <div class="text-sm">
            <p>挂号费: ¥{{ row.registrationFee }}</p>
            <p>药费: ¥{{ row.medicineFee }}</p>
            <p v-if="row.testFee">检查费: ¥{{ row.testFee }}</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="totalFee" label="总金额">
        <template #default="{ row }">
          <span class="text-orange-500 font-bold text-lg">¥ {{ row.totalFee.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'danger' : 'success'">
            {{ row.status === 0 ? '未支付' : '已支付' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" type="primary" size="small" @click="handlePay(row)">缴费</el-button>
          <el-button v-else size="small" @click="printReceipt(row)">打印凭证</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPendingBills, payBill } from '@/api/finance'
import type { FinanceRecord } from '@/api/finance'
import { ElMessage } from 'element-plus'

const bills = ref<FinanceRecord[]>([])

const loadData = async () => {
  const res: any = await getPendingBills()
  bills.value = res
}

const handlePay = async (row: FinanceRecord) => {
  await payBill(row.financeId)
  row.status = 1
  ElMessage.success('缴费成功')
}

const printReceipt = (row: FinanceRecord) => {
  ElMessage.info('正在打印凭证 #' + row.financeId)
}

onMounted(() => {
  loadData()
})
</script>
