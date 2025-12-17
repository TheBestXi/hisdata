import request from '@/utils/request'

export interface Patient {
  patientId: number // patient_id
  name: string
  gender: number // 1: Male, 2: Female
  age: number
  phone: string
  address: string
  idCard?: string // Not in DB schema but useful for frontend search
  medicalHistory?: string // medical_history
  allergyHistory?: string // allergy_history
  createdAt?: string // created_at
}

export interface Doctor {
  doctorId: number // doctor_id
  name: string
  title: string
  department: string
  phone?: string
  createdAt?: string
}

export interface AppointmentDTO {
  patientId: number // patient_id
  doctorId: number // doctor_id
  department: string
  registrationDate: string // registration_date
  registrationFee: number // registration_fee
  status?: number // status (0: Pending, 1: Waiting, 2: In Progress, 3: Completed)
}

// Extended Appointment for display
export interface Appointment extends AppointmentDTO {
  appointmentId: number // appointment_id
  patientName: string
  doctorName: string
}

export const searchPatient = (keyword: string) => {
  return request({
    url: '/api/patient/search',
    method: 'get',
    params: { keyword }
  })
}

export const createPatient = (data: Partial<Patient>) => {
  return request({
    url: '/api/patient',
    method: 'post',
    data
  })
}

export const updatePatient = (id: number, data: Partial<Patient>) => {
  return request({
    url: `/api/patient/${id}`,
    method: 'put',
    data
  })
}

export const getDoctors = (department?: string) => {
  return request({
    url: '/api/doctor',
    method: 'get',
    params: { department }
  })
}

export const createAppointment = (data: AppointmentDTO) => {
  return request({
    url: '/api/appointment',
    method: 'post',
    data
  })
}

export const getTodayAppointments = () => {
  return request({
    url: '/api/appointment/today',
    method: 'get'
  })
}

export const getRegistrationStats = () => {
  return request({
    url: '/api/statistics/registration',
    method: 'get'
  })
}
