<template>
  <el-card>
    <template #header>
      <div class="flex justify-between">
        <span class="font-bold">药品库存管理 (S1, S4, S5)</span>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增药品</el-button>
      </div>
    </template>
    
    <el-table :data="inventory" border stripe style="width: 100%">
      <el-table-column prop="medicineId" label="ID" width="60" />
      <el-table-column prop="name" label="药品名称" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="specification" label="规格" />
      <el-table-column prop="manufacturer" label="生产厂家" />
      <el-table-column prop="price" label="单价 (¥)" width="100" />
      <el-table-column prop="quantity" label="库存" width="100">
        <template #default="{ row }">
          <el-tag :type="row.quantity < 10 ? 'danger' : 'success'">{{ row.quantity }} {{ row.unit }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="expirationDate" label="有效期至" width="120">
        <template #default="{ row }">
          <el-tooltip v-if="getExpiryStatus(row.expirationDate) === 'expired'" content="已过期" placement="top">
            <span class="text-red-600 font-bold bg-red-100 px-2 rounded">{{ row.expirationDate }}</span>
          </el-tooltip>
          <el-tooltip v-else-if="getExpiryStatus(row.expirationDate) === 'warning'" content="临期 (<90天)" placement="top">
            <span class="text-orange-500 font-bold bg-orange-100 px-2 rounded">{{ row.expirationDate }}</span>
          </el-tooltip>
          <span v-else>{{ row.expirationDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑药品' : '新增药品'" width="50%">
      <el-form :model="form" label-width="100px">
        <el-form-item label="药品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" placeholder="如: 抗生素, 解热镇痛" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification" />
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input v-model="form.manufacturer" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="form.quantity" :min="0" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" placeholder="如: 盒, 瓶" />
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker v-model="form.expirationDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getInventory, addMedicine, updateMedicine, deleteMedicine } from '@/api/pharmacy'
import type { InventoryItem } from '@/api/pharmacy'
import { ElMessage, ElMessageBox } from 'element-plus'

const inventory = ref<InventoryItem[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Partial<InventoryItem>>({})

const loadData = async () => {
  const res: any = await getInventory()
  inventory.value = res
}

const getExpiryStatus = (dateStr: string) => {
  const date = new Date(dateStr)
  const now = new Date()
  const diffTime = date.getTime() - now.getTime()
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'expired'
  if (diffDays < 90) return 'warning'
  return 'ok'
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { quantity: 0, price: 0 }
  dialogVisible.value = true
}

const handleEdit = (row: InventoryItem) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row: InventoryItem) => {
  ElMessageBox.confirm(`确认删除 ${row.name} ?`, '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteMedicine(row.medicineId)
    ElMessage.success('删除成功')
    loadData()
  })
}

const submitForm = async () => {
  if (isEdit.value && form.value.medicineId) {
    await updateMedicine(form.value.medicineId, form.value)
    ElMessage.success('更新成功')
  } else {
    await addMedicine(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
