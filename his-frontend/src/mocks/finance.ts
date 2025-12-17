import type { MockMethod } from 'vite-plugin-mock'

const bills = [
  { 
    financeId: 2001, 
    appointmentId: 1001, 
    patientName: '张三', 
    registrationFee: 50.00, 
    medicineFee: 51.00, 
    totalFee: 101.00, 
    status: 0,
    createdAt: '2025-12-17 10:00'
  },
  { 
    financeId: 2002, 
    appointmentId: 1002, 
    patientName: '李四', 
    registrationFee: 50.00, 
    medicineFee: 0, 
    totalFee: 50.00, 
    status: 1,
    createdAt: '2025-12-17 10:15'
  }
]

export default [
  {
    url: '/api/finance/bills',
    method: 'get',
    response: () => {
      return {
        code: 200,
        message: 'success',
        data: bills
      }
    }
  },
  {
    url: RegExp('/api/finance/pay/\\d+'),
    method: 'post',
    response: ({ url }: any) => {
      const match = url.match(/\/api\/finance\/pay\/(\d+)/)
      const id = match ? parseInt(match[1]) : 0
      const bill = bills.find(b => b.financeId === id)
      if (bill) {
        bill.status = 1
      }
      return {
        code: 200,
        message: 'success'
      }
    }
  },
  {
    url: '/api/statistics/daily',
    method: 'get',
    response: () => {
      return {
        code: 200,
        message: 'success',
        data: {
          visits: 128,
          registrationIncome: 6400,
          medicineIncome: 15200,
          totalIncome: 21600
        }
      }
    }
  }
] as MockMethod[]
