import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: 'Dashboard' }
      },
      // Outpatient
      {
        path: 'outpatient/registration',
        name: 'Registration',
        component: () => import('../views/outpatient/registration/index.vue'),
        meta: { title: '挂号办理' }
      },
      {
        path: 'outpatient/history',
        name: 'RegistrationHistory',
        component: () => import('../views/outpatient/history/index.vue'),
        meta: { title: '挂号记录' }
      },
      // Patient
      {
        path: 'patient',
        name: 'Patient',
        component: () => import('../views/patient/index.vue'),
        meta: { title: '患者管理' }
      },
      // Doctor
      {
        path: 'doctor',
        name: 'DoctorStation',
        component: () => import('../views/doctor/index.vue'),
        meta: { title: '医生工作台' }
      },
      // Tech
      {
        path: 'tech',
        name: 'Tech',
        component: () => import('../views/tech/index.vue'),
        meta: { title: '检查检验' }
      },
      // Pharmacy
      {
        path: 'pharmacy/dispensing',
        name: 'Dispensing',
        component: () => import('../views/pharmacy/dispensing/index.vue'),
        meta: { title: '药房发药' }
      },
      {
        path: 'pharmacy/inventory',
        name: 'Inventory',
        component: () => import('../views/pharmacy/inventory/index.vue'),
        meta: { title: '药品库存' }
      },
      // Finance
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('../views/finance/index.vue'),
        meta: { title: '收费管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
