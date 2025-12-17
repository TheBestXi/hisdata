<template>
  <div class="space-y-4">
    <el-card>
      <template #header>
        <div class="flex items-center justify-between">
          <span class="font-bold">门诊挂号</span>
          <el-button type="primary" :icon="Plus" @click="handleNewPatient">建档/新患</el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" class="mb-4">
        <el-tab-pane label="挂号办理" name="registration">
          <el-steps :active="activeStep" finish-status="success" simple class="mb-8">
            <el-step title="患者识别" />
            <el-step title="选择医生" />
            <el-step title="确认挂号" />
          </el-steps>

          <!-- Step 1: Patient Search -->
          <div v-if="activeStep === 0" class="flex flex-col items-center space-y-4">
            <div class="w-full max-w-lg">
              <el-autocomplete
                v-model="searchKeyword"
                :fetch-suggestions="querySearchAsync"
                placeholder="搜索姓名或身份证号 (H2: 老患者快速挂号)"
                class="w-full"
                @select="handleSelectPatient"
              >
                <template #default="{ item }">
                  <div class="flex justify-between items-center">
                    <span class="font-bold">{{ item.name }}</span>
                    <span class="text-gray-400 text-xs">{{ item.idCard }}</span>
                  </div>
                </template>
              </el-autocomplete>
            </div>
            
            <div v-if="selectedPatient" class="w-full max-w-lg border p-4 rounded bg-blue-50">
              <h3 class="font-bold text-lg mb-2">{{ selectedPatient.name }}</h3>
              <div class="grid grid-cols-2 gap-2 text-sm">
                <p><span class="text-gray-500">性别:</span> {{ selectedPatient.gender === 1 ? '男' : '女' }}</p>
                <p><span class="text-gray-500">年龄:</span> {{ selectedPatient.age }}</p>
                <p><span class="text-gray-500">身份证:</span> {{ selectedPatient.idCard }}</p>
                <p><span class="text-gray-500">电话:</span> {{ selectedPatient.phone }}</p>
                <p class="col-span-2 text-red-500" v-if="selectedPatient.allergyHistory">
                  <span class="font-bold">过敏史:</span> {{ selectedPatient.allergyHistory }}
                </p>
              </div>
              <div class="mt-4 flex justify-end">
                <el-button type="primary" @click="nextStep">确认并下一步</el-button>
              </div>
            </div>
          </div>

          <!-- Step 2: Select Doctor -->
          <div v-if="activeStep === 1">
            <div class="mb-4">
              <el-radio-group v-model="selectedDept" @change="fetchDoctors">
                <el-radio-button label="内科" />
                <el-radio-button label="外科" />
                <el-radio-button label="儿科" />
              </el-radio-group>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <el-card 
                v-for="doc in doctors" 
                :key="doc.doctorId" 
                class="cursor-pointer hover:shadow-lg transition-all"
                :class="{'border-blue-500 ring-2 ring-blue-200': selectedDoctor?.doctorId === doc.doctorId}"
                @click="selectDoctor(doc)"
              >
                <div class="flex items-center space-x-4">
                  <el-avatar :size="50">{{ doc.name.charAt(0) }}</el-avatar>
                  <div>
                    <p class="font-bold">{{ doc.name }}</p>
                    <p class="text-sm text-gray-500">{{ doc.title }}</p>
                    <p class="text-xs text-blue-500">{{ doc.department }}</p>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="mt-8 flex justify-between">
              <el-button @click="prevStep">上一步</el-button>
              <el-button type="primary" :disabled="!selectedDoctor" @click="nextStep">下一步</el-button>
            </div>
          </div>

          <!-- Step 3: Confirm -->
          <div v-if="activeStep === 2" class="max-w-lg mx-auto">
            <el-descriptions title="挂号信息确认" :column="1" border>
              <el-descriptions-item label="患者">{{ selectedPatient?.name }}</el-descriptions-item>
              <el-descriptions-item label="科室">{{ selectedDoctor?.department }}</el-descriptions-item>
              <el-descriptions-item label="医生">{{ selectedDoctor?.name }}</el-descriptions-item>
              <el-descriptions-item label="日期">{{ new Date().toLocaleDateString() }}</el-descriptions-item>
              <el-descriptions-item label="挂号费">
                <span class="text-xl font-bold text-orange-500">¥ 50.00</span>
              </el-descriptions-item>
            </el-descriptions>
            
            <div class="mt-8 flex justify-between">
              <el-button @click="prevStep">上一步</el-button>
              <el-button type="success" :loading="submitting" @click="submitRegistration">缴费并确认</el-button>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="挂号统计 (H4)" name="stats">
           <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
             <el-card>
               <template #header>每日挂号趋势</template>
               <div class="h-64 flex items-end justify-around p-4 space-x-2">
                  <div v-for="(item, index) in statsData" :key="index" class="flex flex-col items-center w-full">
                    <div 
                      class="w-full bg-blue-500 rounded-t transition-all hover:bg-blue-600" 
                      :style="{height: (item.count / 300 * 100) + '%'}"
                    ></div>
                    <span class="text-xs text-gray-500 mt-1 transform -rotate-45 origin-left">{{ item.date.slice(5) }}</span>
                  </div>
               </div>
             </el-card>
             <el-card>
               <template #header>今日概览</template>
               <div class="text-center py-8">
                 <div class="text-gray-500 mb-2">今日挂号总量</div>
                 <div class="text-5xl font-bold text-primary">128</div>
               </div>
             </el-card>
           </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { searchPatient, getDoctors, createAppointment, getRegistrationStats } from '@/api/outpatient'
import type { Patient, Doctor } from '@/api/outpatient'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref('registration')
const activeStep = ref(0)
const searchKeyword = ref('')
const selectedPatient = ref<Patient | null>(null)
const selectedDept = ref('内科')
const doctors = ref<Doctor[]>([])
const selectedDoctor = ref<Doctor | null>(null)
const submitting = ref(false)
const statsData = ref<any[]>([])

const querySearchAsync = (queryString: string, cb: (arg: any) => void) => {
  if (!queryString) {
    cb([])
    return
  }
  searchPatient(queryString).then((res: any) => {
    cb(res)
  })
}

const handleSelectPatient = (item: Record<string, any>) => {
  selectedPatient.value = item as Patient
}

const handleNewPatient = () => {
  router.push('/patient') // Redirect to patient management
}

const nextStep = () => {
  if (activeStep.value++ > 2) activeStep.value = 0
}

const prevStep = () => {
  if (activeStep.value-- < 0) activeStep.value = 0
}

const fetchDoctors = () => {
  getDoctors(selectedDept.value).then((res: any) => {
    doctors.value = res
  })
}

const selectDoctor = (doc: Doctor) => {
  selectedDoctor.value = doc
}

const submitRegistration = async () => {
  if (!selectedPatient.value || !selectedDoctor.value) return
  
  submitting.value = true
  try {
    await createAppointment({
      patientId: selectedPatient.value.patientId,
      doctorId: selectedDoctor.value.doctorId,
      department: selectedDoctor.value.department,
      registrationDate: new Date().toISOString().slice(0, 10),
      registrationFee: 50.00
    })
    ElMessage.success('Registration successful!')
    router.push('/outpatient/history')
  } catch (e) {
    // Error handled by interceptor
  } finally {
    submitting.value = false
  }
}

const loadStats = async () => {
  const res: any = await getRegistrationStats()
  statsData.value = res
}

onMounted(() => {
  fetchDoctors()
  loadStats()
})
</script>
