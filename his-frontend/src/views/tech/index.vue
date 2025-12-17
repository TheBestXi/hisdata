<template>
  <div class="space-y-4">
    <!-- Filters -->
    <el-card>
      <div class="flex items-center space-x-4">
        <el-radio-group v-model="activeType" size="large">
          <el-radio-button label="all">全部申请</el-radio-button>
          <el-radio-button label="pending">待检查</el-radio-button>
          <el-radio-button label="completed">已完成</el-radio-button>
        </el-radio-group>
        <el-input
          v-model="searchKey"
          placeholder="搜索患者姓名/申请单号"
          class="w-64"
          :prefix-icon="Search"
        />
      </div>
    </el-card>

    <!-- List -->
    <el-card>
      <el-table :data="techList" border stripe>
        <el-table-column prop="id" label="申请单号" width="120" />
        <el-table-column prop="patientName" label="患者姓名" width="100" />
        <el-table-column prop="type" label="检查类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="item" label="具体项目" />
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="primary" size="small" @click="handleCheck(row)">开始检查</el-button>
            <el-button v-if="row.status === 1" type="success" size="small" @click="handleUpload(row)">上传报告</el-button>
            <el-button v-if="row.status === 2" size="small" @click="viewReport(row)">查看报告</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Upload Report Dialog -->
    <el-dialog v-model="dialogVisible" title="上传检查报告" width="40%">
      <el-form label-width="100px">
        <el-form-item label="检查结果">
          <el-input type="textarea" :rows="4" placeholder="请输入检查结果描述..." />
        </el-form-item>
        <el-form-item label="附件上传">
           <el-upload
            class="upload-demo"
            drag
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
            multiple
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUpload">确认提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const activeType = ref('all')
const searchKey = ref('')
const dialogVisible = ref(false)
const currentItem = ref<any>(null)

// Mock Data
// status: 0-Pending, 1-Processing, 2-Completed
const techList = ref([
  { id: 'T20240101', patientName: '张三', type: '检验', item: '血常规', applyTime: '2025-12-17 09:30', status: 0 },
  { id: 'T20240102', patientName: '李四', type: '检查', item: '胸部CT', applyTime: '2025-12-17 10:00', status: 1 },
  { id: 'T20240103', patientName: '王五', type: '检查', item: '腹部B超', applyTime: '2025-12-16 14:20', status: 2 },
])

const getStatusType = (status: number) => {
  const map: any = { 0: 'info', 1: 'warning', 2: 'success' }
  return map[status]
}

const getStatusText = (status: number) => {
  const map: any = { 0: '待检查', 1: '检查中', 2: '已完成' }
  return map[status]
}

const handleCheck = (row: any) => {
  row.status = 1
  ElMessage.success('开始检查：' + row.item)
}

const handleUpload = (row: any) => {
  currentItem.value = row
  dialogVisible.value = true
}

const confirmUpload = () => {
  if (currentItem.value) {
    currentItem.value.status = 2
    ElMessage.success('报告上传成功')
  }
  dialogVisible.value = false
}

const viewReport = (row: any) => {
  ElMessage.info('查看报告：' + row.id)
}
</script>