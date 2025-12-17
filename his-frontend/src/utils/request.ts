import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 5000
})

service.interceptors.request.use(
  (config) => {
    // Add token here if needed
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    // If using mock, sometimes it returns data directly or wrapped
    if (res.code === 200) {
      return res.data
    }
    // Allow direct return for some mock scenarios if strict structure isn't followed
    if (import.meta.env.VITE_USE_MOCK === 'true' && res.code === undefined) {
      return res
    }
    
    ElMessage.error(res.message || 'Error')
    return Promise.reject(new Error(res.message || 'Error'))
  },
  (error) => {
    ElMessage.error(error.message || 'Request Failed')
    return Promise.reject(error)
  }
)

export default service
