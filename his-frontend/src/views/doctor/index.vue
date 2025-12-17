<template>
  <div class="flex h-full gap-4">
    <!-- Left: Patient Queue -->
    <el-card class="w-1/4 h-full flex flex-col">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">候诊队列</span>
          <el-tag type="primary">{{ queue.length }} 人候诊</el-tag>
        </div>
      </template>
      <div class="mb-2 px-2" v-if="workload">
        <el-alert :title="`今日接诊: ${workload.todayPatients} | 待诊: ${workload.pending} | 已完: ${workload.completed}`" type="info" :closable="false" />
      </div>
      <el-scrollbar>
        <div v-for="patient in queue" :key="patient.appointmentId" 
          class="p-3 mb-2 rounded cursor-pointer border hover:bg-blue-50 transition-colors"
          :class="{'bg-blue-100 border-blue-300': currentPatient?.appointmentId === patient.appointmentId}"
          @click="selectPatient(patient)"
        >
          <div class="flex justify-between">
            <span class="font-bold">{{ patient.patientName }}</span>
            <el-tag size="small" :type="getStatusType(patient.status)">{{ getStatusText(patient.status) }}</el-tag>
          </div>
          <div class="text-xs text-gray-500 mt-1">
            {{ patient.gender === 1 ? '男' : '女' }} | {{ patient.age }} 岁 | {{ patient.visitTime }}
          </div>
        </div>
      </el-scrollbar>
    </el-card>

    <!-- Right: Workstation -->
    <div class="flex-1 flex flex-col gap-4">
      <!-- Patient Info Header -->
      <el-card v-if="currentPatient" class="h-auto">
        <div class="flex justify-between items-start">
          <div>
            <div class="flex items-center mb-2">
              <span class="text-xl font-bold mr-4">{{ currentPatient.patientName }}</span>
              <el-tag class="mr-2">{{ currentPatient.gender === 1 ? '男' : '女' }}</el-tag>
              <el-tag type="info">{{ currentPatient.age }} 岁</el-tag>
            </div>
            <div class="text-sm space-y-1">
              <p><span class="font-bold text-gray-500">既往病史:</span> {{ currentPatient.medicalHistory || '无' }}</p>
              <p><span class="font-bold text-gray-500">过敏史:</span> <span class="text-red-500 font-bold">{{ currentPatient.allergyHistory || '无' }}</span></p>
            </div>
          </div>
          <div class="space-x-2">
            <el-button type="success" @click="completeVisit">完成就诊</el-button>
          </div>
        </div>
      </el-card>
      <el-empty v-else description="请选择患者开始接诊" class="bg-white h-40" />

      <!-- Clinical Tabs -->
      <el-card class="flex-1" v-if="currentPatient">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="病历书写" name="record">
            <el-form label-position="top">
              <el-form-item label="主诉 (Chief Complaint)">
                <el-input v-model="recordForm.chiefComplaint" type="textarea" :rows="2" placeholder="患者主要症状..." />
              </el-form-item>
              <el-form-item label="现病史 (Present Illness)">
                <el-input v-model="recordForm.presentIllness" type="textarea" :rows="3" placeholder="发病经过..." />
              </el-form-item>
              <el-form-item label="体格检查 (Physical Examination)">
                <el-input v-model="recordForm.physicalExamination" type="textarea" :rows="3" placeholder="查体情况..." />
              </el-form-item>
              <el-form-item label="初步诊断 (Preliminary Diagnosis)">
                <el-input v-model="recordForm.preliminaryDiagnosis" type="textarea" :rows="2" placeholder="诊断结果..." />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveRecord">保存病历</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="处方开立" name="prescription">
            <el-table :data="prescriptions" border stripe>
              <el-table-column prop="medicineName" label="药品名称" />
              <el-table-column prop="dosage" label="用法用量" />
              <el-table-column prop="quantity" label="数量" width="80" />
              <el-table-column prop="totalCost" label="金额" width="100" />
              <el-table-column label="操作" width="100">
                <template #default>
                  <el-button link type="danger">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button class="mt-4 w-full" :icon="Plus" border @click="addPrescription">添加药品 (Mock)</el-button>
          </el-tab-pane>

          <el-tab-pane label="检查检验" name="labs">
             <div class="grid grid-cols-4 gap-4">
               <el-button v-for="test in commonTests" :key="test" @click="applyTest(test)">{{ test }}</el-button>
             </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getPatientQueue, updateStatus, getDoctorWorkload, saveMedicalRecord, applyLabTest } from '@/api/doctor'
import type { QueueItem, MedicalRecord } from '@/api/doctor'
import { ElMessage } from 'element-plus'

const queue = ref<QueueItem[]>([])
const currentPatient = ref<QueueItem | null>(null)
const activeTab = ref('record')
const workload = ref<any>(null)

const recordForm = ref<Partial<MedicalRecord>>({
  chiefComplaint: '',
  presentIllness: '',
  physicalExamination: '',
  preliminaryDiagnosis: ''
})

const prescriptions = ref([
  { medicineName: '阿莫西林胶囊', dosage: '0.5g tid', quantity: 2, price: 25.5, totalCost: 51.0 }
])

const commonTests = ['血常规', '尿常规', '肝功能', '肾功能', '心电图', '胸部CT', '腹部B超']

const loadQueue = async () => {
  const res: any = await getPatientQueue()
  queue.value = res
  const stats: any = await getDoctorWorkload()
  workload.value = stats
}

const selectPatient = (patient: QueueItem) => {
  currentPatient.value = patient
  // Reset forms
  recordForm.value = {
    chiefComplaint: '',
    presentIllness: '',
    physicalExamination: '',
    preliminaryDiagnosis: ''
  }
  
  if (patient.status === 1) {
    updateStatus(patient.appointmentId, 2).then(() => {
      patient.status = 2
    })
  }
}

const saveRecord = async () => {
  if (!currentPatient.value) return
  await saveMedicalRecord({
    patientId: currentPatient.value.patientId,
    doctorId: 101, // Mock current doctor
    ...recordForm.value
  } as MedicalRecord)
  ElMessage.success('病历保存成功')
}

const addPrescription = () => {
  prescriptions.value.push({ medicineName: '布洛芬缓释胶囊', dosage: '1粒 prn', quantity: 1, price: 12.0, totalCost: 12.0 })
}

const applyTest = async (testName: string) => {
  if (!currentPatient.value) return
  await applyLabTest({
    patientId: currentPatient.value.patientId,
    testType: testName,
    testDate: new Date().toISOString().slice(0, 10),
    status: 0
  })
  ElMessage.success(`已开具: ${testName}`)
}

const completeVisit = () => {
  if (!currentPatient.value) return
  updateStatus(currentPatient.value.appointmentId, 3).then(() => {
    if (currentPatient.value) currentPatient.value.status = 3
    ElMessage.success('Visit completed')
    currentPatient.value = null
    loadQueue() // Refresh queue and stats
  })
}

const getStatusType = (status: number) => {
  switch (status) {
    case 1: return 'warning'
    case 2: return 'primary'
    case 3: return 'success'
    default: return 'info'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 1: return '候诊'
    case 2: return '就诊中'
    case 3: return '已完成'
    default: return '未知'
  }
}

onMounted(() => {
  loadQueue()
})
</script>
