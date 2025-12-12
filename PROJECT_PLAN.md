# HIS系统后端项目开发框架计划书

## 1. 项目概述
本项目旨在构建一套现代化、高可用、可扩展的医院信息系统（HIS）。基于用户需求，后端采用 **Spring Boot 单体架构**，使用 **JPA** 作为ORM框架，集成 **MinIO** 对象存储和 **Elasticsearch** 全文检索，并使用 **JWT** 进行安全认证。

## 2. 技术栈选型 (Technology Stack)

| 组件 | 选型 | 说明 |
| :--- | :--- | :--- |
| **开发语言** | Java 17+ | 长期支持版本，性能更优 |
| **核心框架** | Spring Boot 3.2.x | 快速开发，约定优于配置 |
| **ORM框架** | Spring Data JPA (Hibernate) | 开发效率高，支持复杂对象关系映射 |
| **数据库** | MySQL 8.0 | 核心业务数据存储 |
| **对象存储** | MinIO | 存储患者影像(X光/CT)、检验报告、病历PDF等 |
| **搜索引擎** | Elasticsearch 8.x | 用于患者信息模糊搜索、病历全文检索 |
| **安全框架** | Spring Security + JWT | 无状态认证，适合前后端分离 |
| **工具库** | Lombok, Hutool, MapStruct | 简化代码，提升开发效率 |
| **API文档** | SpringDoc (Swagger 3) | 自动生成接口文档 |

## 3. 系统架构设计

采用 **模块化单体架构 (Modular Monolith)**。虽然物理上是一个部署包，但逻辑上严格划分模块边界，便于未来向微服务迁移。

```
his-backend-root
├── his-common          // 公共模块：全局异常、统一响应、工具类
├── his-framework       // 框架核心：Security配置、WebMvc配置、MyBatis/JPA配置
├── his-module-system   // 系统管理：用户、角色、权限、菜单
├── his-module-patient  // 患者服务：档案管理、卡管理
├── his-module-outpatient // 门诊服务：挂号、处方、门诊病历
├── his-module-storage  // 存储服务：MinIO封装
└── his-module-search   // 搜索服务：ES封装
```

## 4. 核心功能设计

### 4.1 全局统一响应 (Global Response)
设计 `Result<T>` 类，统一定义接口返回格式，确保前端处理的一致性。
*   `code`: 状态码 (200成功, 500系统错误, 401未认证等)
*   `message`: 提示信息
*   `data`: 业务数据
*   `timestamp`: 响应时间戳

### 4.2 安全认证 (Security)
*   **登录**: 用户名/密码 -> 校验 -> 生成 JWT Token (包含 userId, roleId)。
*   **鉴权**: Filter 拦截请求 -> 解析 Token -> 放入 SecurityContext -> 接口权限注解 `@PreAuthorize` 控制访问。

### 4.3 文件存储 (MinIO)
*   封装 `FileStorageService` 接口。
*   实现文件上传、下载、预签名URL生成（用于前端直接预览）。
*   桶规划：`his-public` (公开资源), `his-private` (敏感病历数据)。

### 4.4 搜索服务 (Elasticsearch)
*   数据同步：通过监听数据库变更（Canal或Spring Event）同步数据到 ES。
*   场景：输入“张伟” -> ES返回所有匹配患者，支持拼音、模糊匹配。

## 5. 数据库设计 (Database Schema)

*注：由于无法读取您的 `.docx` 文件，以下为基于行业经验设计的核心表结构建议。请您后续补充具体的业务字段。*

### 5.1 基础规范
*   **主键**: `id` (BIGINT, 雪花算法)
*   **审计**: `create_time`, `update_time`, `create_by`, `update_by`
*   **逻辑删除**: `deleted` (BIT/TINYINT)

### 5.2 核心表结构预览

#### (1) 患者表 (`sys_patient`)
| 字段名 | 类型 | 说明 | 索引 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | 主键 | PK |
| name | VARCHAR(64) | 姓名 | MUL (ES同步) |
| id_card | VARCHAR(18) | 身份证号 | UK |
| phone | VARCHAR(11) | 手机号 | MUL |
| gender | TINYINT | 性别 | |
| birth_date | DATE | 出生日期 | |

#### (2) 医生表 (`sys_doctor`)
| 字段名 | 类型 | 说明 | 索引 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | 主键 | PK |
| user_id | BIGINT | 关联系统用户ID | UK |
| dept_id | BIGINT | 所属科室 | MUL |
| title | VARCHAR(32) | 职称 (主任医师/主治医师) | |

#### (3) 挂号记录表 (`op_registration`)
| 字段名 | 类型 | 说明 | 索引 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | 主键 | PK |
| patient_id | BIGINT | 患者ID | MUL |
| doctor_id | BIGINT | 医生ID | MUL |
| visit_date | DATE | 就诊日期 | MUL |
| status | TINYINT | 状态(0待支付 1已支付 2已就诊 3已取消) | |
| reg_fee | DECIMAL(10,2) | 挂号费 | |

## 6. 开发计划 (Development Plan)

1.  **阶段一：框架搭建** (预计 1 天)
    *   初始化 Spring Boot 项目。
    *   集成 JPA, MySQL, MinIO, ES 依赖。
    *   实现 `GlobalResult` 和 `GlobalExceptionHandler`。
2.  **阶段二：基础服务** (预计 2-3 天)
    *   完成用户、科室、医生等基础数据的 CRUD。
    *   实现 JWT 登录认证流程。
3.  **阶段三：核心业务** (预计 5-7 天)
    *   **门诊挂号**: 包含号源管理（并发控制）。
    *   **医生工作站**: 病历录入（存ES），开处方。
    *   **文件服务**: 实现病历附件上传 MinIO。
4.  **阶段四：联调与优化**
    *   接口联调。
    *   SQL 性能分析与索引优化。

## 7. 下一步行动
*   **确认**: 请确认上述架构和计划是否符合您的预期？
*   **补充**: 请将 `.docx` 中的表结构内容以文本形式发给我，以便我精确建模。
*   **执行**: 确认无误后，我将开始搭建项目脚手架。