# HIS系统前端开发规划文档

## 1. 项目概述
本文档旨在为前端开发人员提供 **HIS (医院信息系统)** 的开发规范与技术指引。前端需与后端 (Spring Boot + JPA) 紧密配合，构建高性能、高可用、体验优秀的医疗业务系统。

## 2. 技术栈推荐 (Tech Stack)

| 类别 | 推荐选型 | 说明 |
| :--- | :--- | :--- |
| **框架** | **Vue 3** (Composition API) | 目前国内生态最完善，上手快，性能优 |
| **语言** | **TypeScript** | 强类型，配合后端 DTO 减少联调错误 |
| **构建工具** | **Vite 5.x** | 极速冷启动和热更新 |
| **UI 组件库** | **Element Plus** 或 **Ant Design Vue** | 建议 Element Plus，医疗系统表单表格众多，其组件丰富 |
| **状态管理** | **Pinia** | Vuex 的现代替代品，更轻量，完美支持 TS |
| **路由** | **Vue Router 4.x** | |
| **HTTP客户端** | **Axios** | 需封装拦截器处理 JWT 和统一响应 |
| **CSS 预处理** | **SCSS** / **TailwindCSS** | 建议引入 TailwindCSS 提升样式开发效率 |
| **图表库** | **ECharts** | 用于展示患者生命体征趋势、医院运营报表 |

## 3. 项目结构规范

```
his-frontend-root
├── src
│   ├── api                 // 统一管理 API 请求 (按模块拆分: user.ts, patient.ts)
│   ├── assets              // 静态资源 (images, styles)
│   ├── components          // 通用组件 (BaseTable, BaseDialog)
│   ├── hooks               // 组合式函数 (useLoading, usePagination)
│   ├── layouts             // 布局组件 (Header, Sidebar, MainLayout)
│   ├── router              // 路由配置 (包含权限守卫)
│   ├── stores              // Pinia 状态管理
│   ├── types               // TypeScript 类型定义 (对应后端 DTO)
│   ├── utils               // 工具函数 (request.ts, date.ts, validate.ts)
│   ├── views               // 页面视图 (按业务模块划分)
│   │   ├── login           // 登录页
│   │   ├── dashboard       // 工作台
│   │   ├── outpatient      // 门诊模块
│   │   ├── inpatient       // 住院模块
│   │   └── system          // 系统设置
│   └── App.vue
├── .env.development        // 开发环境变量 (VITE_API_URL=http://localhost:8080)
├── .env.production         // 生产环境变量
└── package.json
```

## 4. 核心功能对接指南

### 4.1 接口交互规范
后端返回格式统一为 `Result<T>`，前端需在 `utils/request.ts` 中统一拦截：

```typescript
// 响应拦截器示例
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code === 200) {
      return res.data; // 直接返回业务数据
    } else {
      ElMessage.error(res.message || '系统错误');
      return Promise.reject(new Error(res.message));
    }
  },
  (error) => {
    if (error.response.status === 401) {
      // Token 过期，重定向到登录页
      useUserStore().logout();
      router.push('/login');
    }
    return Promise.reject(error);
  }
);
```

### 4.2 安全认证 (JWT)
1.  **登录**: 调用 `/auth/login` 获取 `token`。
2.  **存储**: 将 `token` 存储在 `localStorage` 或 `Cookie` 中。
3.  **请求**: 在 Axios 请求拦截器中，将 `token` 放入 Header：
    `config.headers['Authorization'] = 'Bearer ' + token;`

### 4.3 动态路由与权限
*   后端会返回当前用户的菜单列表和权限标识（如 `outpatient:register:add`）。
*   前端需利用 `addRoute` 动态挂载路由。
*   使用自定义指令 `v-permission="'sys:user:edit'"` 控制按钮级别的显示隐藏。

### 4.4 文件处理 (MinIO)
*   **图片/PDF预览**: 后端返回的是 MinIO 的 Presigned URL (临时访问链接) 或 Nginx 反代地址。
*   **上传**: 建议封装 `<UploadFile />` 组件，支持拖拽上传、进度显示。

## 5. 开发协作流程
1.  **接口文档**: 关注后端 Swagger 文档 (通常地址: `http://localhost:8080/swagger-ui.html`)。
2.  **Mock 数据**: 后端接口未完成时，可使用 Mock.js 或 Apifox 进行模拟。
3.  **代码规范**: 启用 ESLint + Prettier，Git 提交前强制校验。

## 6. 必读业务场景
*   **挂号页面**: 需高频刷新号源状态，考虑使用 WebSocket 或短轮询。
*   **医生开单**: 药品列表数据量大，需使用“虚拟滚动”表格组件提升性能。
*   **打印**: 处方单、发票打印需精确控制样式，建议使用 `print-js` 或专门的打印插件。
