import request from '@/utils/request'

export interface InventoryItem {
  medicineId: number
  name: string
  specification: string
  manufacturer: string
  category: string
  price: number
  quantity: number
  unit: string
  expirationDate: string // expiration_date
  createdAt?: string
}

export interface DispensingTask {
  prescriptionId: number
  patientName: string
  items: string // JSON or comma separated string for display
  totalCost: number
  status: number // 1: Pending, 2: Dispensed
}

export const getInventory = () => {
  return request({
    url: '/api/pharmacy/inventory',
    method: 'get'
  })
}

export const addMedicine = (data: Partial<InventoryItem>) => {
  return request({
    url: '/api/pharmacy/inventory',
    method: 'post',
    data
  })
}

export const updateMedicine = (id: number, data: Partial<InventoryItem>) => {
  return request({
    url: `/api/pharmacy/inventory/${id}`,
    method: 'put',
    data
  })
}

export const deleteMedicine = (id: number) => {
  return request({
    url: `/api/pharmacy/inventory/${id}`,
    method: 'delete'
  })
}

export const getDispensingQueue = () => {
  return request({
    url: '/api/pharmacy/dispensing/queue',
    method: 'get'
  })
}

export const dispenseMedicine = (taskId: number) => {
  return request({
    url: `/api/pharmacy/dispensing/${taskId}`,
    method: 'post'
  })
}
