# HIS 前端（Mock-first）

一个可独立运行的 HIS（医院信息系统）前端工程，默认使用 Mock 数据模拟后端接口，同时保留与真实后端对接的接口层与类型定义。

## 功能模块 (Features)

已实现完整的门诊业务流程闭环，包含以下模块：

1.  **首页概览 (Dashboard)**：展示今日接诊量、营收、待办事项及科室统计图表。
2.  **门诊管理 (Outpatient)**
    *   **挂号办理**：患者搜索/建档、科室医生选择、挂号确认。
    *   **挂号记录**：查看历史挂号信息。
3.  **患者管理 (Patient)**
    *   患者档案列表查询、新建、编辑与详情查看。
4.  **医生工作台 (Doctor Station)**
    *   候诊队列管理（叫号/接诊）。
    *   电子病历书写（主诉、诊断）。
    *   处方开立（药品选择、用量）。
    *   检查检验申请。
5.  **检查检验 (Tech/Labs)**
    *   申请单列表（待检查/检查中/已完成）。
    *   检查执行与报告上传/查看。
6.  **药房管理 (Pharmacy)**
    *   **药房发药**：处方单审核与发药操作。
    *   **药品库存**：药品列表、库存预警（有效期/数量）。
7.  **收费管理 (Finance)**
    *   待缴费账单列表、缴费操作与凭证打印。

## 技术栈 (Tech Stack)

*   **框架**: Vue 3 (Composition API + `<script setup>`)
*   **语言**: TypeScript (Strict Mode)
*   **构建**: Vite 5
*   **路由**: Vue Router 4
*   **状态管理**: Pinia
*   **UI 组件库**: Element Plus (按需引入)
*   **样式**: Tailwind CSS + SCSS
*   **HTTP**: Axios (封装统一响应拦截)
*   **Mock**: vite-plugin-mock (开发环境模拟后端接口)

## 快速开始 (Quick Start)

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

启动后访问终端输出的本地地址（通常为 `http://localhost:5173`）。默认开启 Mock 模式，无需后端即可体验完整流程。

### 3. 构建生产版本

```bash
npm run build
```

## 目录结构 (Directory Structure)

```text
src/
├── api/            # 接口定义层 (与后端 DTO 对齐)
├── mocks/          # 本地 Mock 数据 (模拟后端响应)
├── views/          # 页面视图 (按业务模块划分)
│   ├── dashboard/  # 首页
│   ├── outpatient/ # 门诊 (挂号/记录)
│   ├── patient/    # 患者管理
│   ├── doctor/     # 医生工作站
│   ├── tech/       # 检查检验
│   ├── pharmacy/   # 药房 (发药/库存)
│   └── finance/    # 收费
├── layouts/        # 布局组件 (侧边栏/顶栏)
├── router/         # 路由配置
├── stores/         # Pinia 状态管理
├── styles/         # 全局样式 (Tailwind/SCSS)
└── utils/          # 工具库 (Request 封装等)
```

## 后端对接说明 (Backend Integration)

本项目采用 **Mock-first** 策略，开发阶段完全独立。对接真实后端时，只需调整环境变量：

1.  修改 `.env` 或环境变量：
    *   `VITE_USE_MOCK=false`：关闭 Mock 拦截，请求将发送至真实后端。
    *   `VITE_API_URL=http://your-backend-api.com/api`：指定后端地址。

2.  **接口层 (`src/api/*.ts`)**：
    *   所有 API 请求均封装在此目录下。
    *   若后端接口字段有变，仅需在此层做适配或更新 TypeScript 类型定义，无需修改业务页面代码。

## 贡献指南

遵循 `FRONTEND_GUIDE.md` 中的开发规范，保持 UI 风格一致性与代码质量。
