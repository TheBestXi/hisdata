<template>
  <div class="space-y-4">
    <!-- Search Bar -->
    <el-card>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Patient List -->
    <el-card>
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">患者档案列表</span>
          <el-button type="primary" :icon="Plus" @click="handleCreate">新建档案</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="patientId" label="病历号" width="100" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="medicalHistory" label="既往病史" show-overflow-tooltip />
        <el-table-column prop="allergyHistory" label="过敏史" show-overflow-tooltip>
          <template #default="{ row }">
            <span :class="{'text-red-500 font-bold': row.allergyHistory && row.allergyHistory !== '无'}">
              {{ row.allergyHistory }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination background layout="prev, pager, next" :total="100" />
      </div>
    </el-card>

    <!-- Create/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑患者档案' : '新建患者档案'" width="50%">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="0" :max="120" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="家庭地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="既往病史">
          <el-input v-model="form.medicalHistory" type="textarea" :rows="3" placeholder="无特殊病史请填'无'" />
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input v-model="form.allergyHistory" type="textarea" :rows="3" placeholder="无过敏史请填'无'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { searchPatient, createPatient, updatePatient } from '@/api/outpatient'
import type { Patient } from '@/api/outpatient'
import { ElMessage } from 'element-plus'

const searchForm = ref({
  name: '',
  idCard: ''
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const tableData = ref<Patient[]>([])

const form = ref<Partial<Patient>>({
  name: '',
  idCard: '',
  gender: 1,
  age: 30,
  phone: '',
  address: '',
  medicalHistory: '',
  allergyHistory: ''
})

const loadData = async () => {
  const res: any = await searchPatient('')
  tableData.value = res
}

const handleSearch = async () => {
  const res: any = await searchPatient(searchForm.value.name || searchForm.value.idCard)
  tableData.value = res
}

const resetSearch = () => {
  searchForm.value.name = ''
  searchForm.value.idCard = ''
  loadData()
}

const handleCreate = () => {
  isEdit.value = false
  form.value = { gender: 1, age: 30 }
  dialogVisible.value = true
}

const handleEdit = (row: Patient) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (isEdit.value && form.value.patientId) {
    await updatePatient(form.value.patientId, form.value)
    ElMessage.success('Updated successfully')
  } else {
    await createPatient(form.value)
    ElMessage.success('Created successfully')
  }
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
