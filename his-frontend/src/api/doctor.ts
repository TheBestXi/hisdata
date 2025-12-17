import request from '@/utils/request'

export interface QueueItem {
  appointmentId: number
  patientId: number
  patientName: string
  gender: number
  age: number
  status: number // 1: Waiting, 2: In Progress, 3: Completed
  visitTime: string
  medicalHistory?: string
  allergyHistory?: string
}

export interface MedicalRecord {
  recordId?: number
  patientId: number
  doctorId: number
  chiefComplaint: string // chief_complaint
  presentIllness: string // present_illness
  physicalExamination: string // physical_examination
  preliminaryDiagnosis: string // preliminary_diagnosis
  createdAt?: string
}

export interface PrescriptionItem {
  medicineName: string
  dosage: string
  quantity: number
  price: number
  totalCost: number
}

export interface Prescription {
  prescriptionId?: number
  patientId: number
  doctorId: number
  items: PrescriptionItem[] // Simplified representation for frontend
  totalCost: number
  createdAt?: string
}

export interface LabTest {
  testId?: number
  patientId: number
  testType: string
  testDate: string
  result?: string
  status: number // 0: Pending, 1: Completed
}

export const getPatientQueue = () => {
  return request({
    url: '/api/doctor/queue',
    method: 'get'
  })
}

export const updateStatus = (appointmentId: number, status: number) => {
  return request({
    url: `/api/appointment/${appointmentId}/status`,
    method: 'put',
    params: { status }
  })
}

export const saveMedicalRecord = (data: MedicalRecord) => {
  return request({
    url: '/api/doctor/record',
    method: 'post',
    data
  })
}

export const getMedicalHistory = (patientId: number) => {
  return request({
    url: `/api/doctor/history/${patientId}`,
    method: 'get'
  })
}

export const savePrescription = (data: Prescription) => {
  return request({
    url: '/api/doctor/prescription',
    method: 'post',
    data
  })
}

export const applyLabTest = (data: LabTest) => {
  return request({
    url: '/api/doctor/lab',
    method: 'post',
    data
  })
}

export const getDoctorWorkload = () => {
  return request({
    url: '/api/statistics/doctor/workload',
    method: 'get'
  })
}
