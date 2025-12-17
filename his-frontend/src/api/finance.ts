import request from '@/utils/request'

export interface FinanceRecord {
  financeId: number
  appointmentId: number
  patientName: string
  registrationFee: number
  medicineFee: number
  testFee?: number
  totalFee: number
  status: number // 0: Unpaid, 1: Paid
  createdAt: string
}

export interface DailyStats {
  date: string
  visits: number
  income: number
}

export const getPendingBills = () => {
  return request({
    url: '/api/finance/bills',
    method: 'get'
  })
}

export const payBill = (financeId: number) => {
  return request({
    url: `/api/finance/pay/${financeId}`,
    method: 'post'
  })
}

export const getDailyStats = () => {
  return request({
    url: '/api/statistics/daily',
    method: 'get'
  })
}
