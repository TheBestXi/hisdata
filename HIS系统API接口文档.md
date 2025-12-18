# HIS系统API接口文档

## 目录
1. [通用响应格式](#通用响应格式)
2. [患者管理](#患者管理)
3. [医生管理](#医生管理)
4. [挂号管理](#挂号管理)
5. [病历管理](#病历管理)
6. [处方管理](#处方管理)
7. [药房管理](#药房管理)
8. [财务管理](#财务管理)
9. [检查管理](#检查管理)
10. [统计相关](#统计相关)
11. [文件管理](#文件管理)

---

## 通用响应格式

所有接口返回统一的响应格式：

```json
{
  "code": 200,        // 状态码 (200成功, 500系统错误, 401未认证等)
  "message": "成功",   // 提示信息
  "data": {},         // 业务数据
  "timestamp": "2023-12-18T10:30:00"  // 响应时间戳
}
```

---

## 患者管理 (Patient Management)

### 1.1 搜索患者

**接口地址：** `GET /api/patient/search`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 患者姓名 |

**请求示例：**
```
GET /api/patient/search?name=张三
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "patientId": 1,
      "name": "张三",
      "gender": 1,
      "age": 35,
      "phone": "13800138000",
      "address": "北京市朝阳区",
      "idCard": "110101199001011234",
      "medicalHistory": "高血压病史",
      "allergyHistory": "青霉素过敏",
      "createdAt": "2023-12-18T10:30:00"
    }
  ],
  "timestamp": "2023-12-18T10:30:00"
}
```

### 1.2 创建/更新患者

**接口地址：** `POST /api/patient`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "name": "张三",
  "gender": 1,
  "age": 35,
  "phone": "13800138000",
  "address": "北京市朝阳区",
  "idCard": "110101199001011234",
  "medicalHistory": "高血压病史",
  "allergyHistory": "青霉素过敏"
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 患者姓名 |
| gender | Integer | 是 | 性别 (1: 男, 2: 女) |
| age | Integer | 是 | 年龄 |
| phone | String | 是 | 手机号 |
| address | String | 是 | 地址 |
| idCard | String | 否 | 身份证号 |
| medicalHistory | String | 否 | 病史 |
| allergyHistory | String | 否 | 过敏史 |

### 1.3 获取患者详情

**接口地址：** `GET /api/patient/{id}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 患者ID |

**请求示例：**
```
GET /api/patient/1
```

---

## 医生管理 (Doctor Management)

### 2.1 查询医生列表

**接口地址：** `GET /api/doctor`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| department | String | 否 | 科室名称 |

**请求示例：**
```
GET /api/doctor?department=内科
```

### 2.2 添加医生

**接口地址：** `POST /api/doctor`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "name": "李医生",
  "title": "主任医师",
  "department": "内科",
  "phone": "13900139000"
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 医生姓名 |
| title | String | 是 | 职称 |
| department | String | 是 | 所属科室 |
| phone | String | 否 | 联系电话 |

---

## 挂号管理 (Appointment Management)

### 3.1 创建挂号单

**接口地址：** `POST /api/appointment`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "pid": 1,
  "doctorId": 2,
  "department": "内科",
  "registrationDate": "2023-12-18",
  "registrationFee": 10.00
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |
| doctorId | Integer | 是 | 医生ID |
| department | String | 是 | 科室 |
| registrationDate | String | 是 | 挂号日期 (YYYY-MM-DD) |
| registrationFee | BigDecimal | 是 | 挂号费 |

### 3.2 查询患者的挂号记录

**接口地址：** `GET /api/appointment/patient/{pid}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |

**请求示例：**
```
GET /api/appointment/patient/1
```

### 3.3 更新挂号状态

**接口地址：** `PUT /api/appointment/{id}/status`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 挂号ID |
| status | Integer | 是 | 状态值 (0: 待支付, 1: 已支付, 2: 已就诊, 3: 已取消) |

**请求示例：**
```
PUT /api/appointment/1/status?status=2
```

---

## 病历管理 (Medical Record Management)

### 4.1 创建病历

**接口地址：** `POST /api/medical-record`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "pid": 1,
  "doctorId": 2,
  "appointmentId": 3,
  "chiefComplaint": "头痛",
  "presentIllness": "患者自述头痛3天",
  "physicalExamination": "体温正常，血压偏高",
  "preliminaryDiagnosis": "偏头痛"
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |
| doctorId | Integer | 是 | 医生ID |
| appointmentId | Integer | 是 | 挂号ID |
| chiefComplaint | String | 是 | 主诉 |
| presentIllness | String | 是 | 现病史 |
| physicalExamination | String | 是 | 体格检查 |
| preliminaryDiagnosis | String | 是 | 初步诊断 |

### 4.2 查询患者病历

**接口地址：** `GET /api/medical-record/patient/{pid}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |

**请求示例：**
```
GET /api/medical-record/patient/1
```

### 4.3 查询挂号单病历

**接口地址：** `GET /api/medical-record/appointment/{appointmentId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| appointmentId | Integer | 是 | 挂号ID |

**请求示例：**
```
GET /api/medical-record/appointment/1
```

---

## 处方管理 (Prescription Management)

### 5.1 开具处方

**接口地址：** `POST /api/prescription`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "pid": 1,
  "doctorId": 2,
  "appointmentId": 3,
  "medicineId": 101,
  "dosage": "5mg",
  "dosageUnit": "片",
  "frequency": "每日三次",
  "quantity": 21
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |
| doctorId | Integer | 是 | 医生ID |
| appointmentId | Integer | 是 | 挂号ID |
| medicineId | Integer | 是 | 药品ID |
| dosage | String | 是 | 剂量 |
| dosageUnit | String | 是 | 剂量单位 |
| frequency | String | 是 | 用药频率 |
| quantity | Integer | 是 | 数量 |

### 5.2 查询患者处方

**接口地址：** `GET /api/prescription/patient/{pid}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |

**请求示例：**
```
GET /api/prescription/patient/1
```

---

## 药房管理 (Pharmacy Management)

### 6.1 查询药品库存

**接口地址：** `GET /api/pharmacy/inventory`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 否 | 药品名称 |
| category | String | 否 | 药品类别 |

**请求示例：**
```
GET /api/pharmacy/inventory?name=阿莫西林
```

### 6.2 查询临期药品

**接口地址：** `GET /api/pharmacy/inventory/expiring`

**说明：** 查询30天内即将过期的药品

### 6.3 添加/更新药品

**接口地址：** `POST /api/pharmacy/inventory`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "name": "阿莫西林",
  "specification": "0.25g*24粒",
  "manufacturer": "华北制药",
  "category": "抗生素",
  "price": 15.50,
  "quantity": 100,
  "unit": "盒",
  "expirationDate": "2024-12-31"
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 药品名称 |
| specification | String | 是 | 规格 |
| manufacturer | String | 是 | 生产厂家 |
| category | String | 是 | 类别 |
| price | BigDecimal | 是 | 价格 |
| quantity | Integer | 是 | 库存数量 |
| unit | String | 是 | 单位 |
| expirationDate | String | 是 | 有效期 (YYYY-MM-DD) |

### 6.4 发药

**接口地址：** `POST /api/pharmacy/dispense`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| prescriptionId | Integer | 是 | 处方ID |
| medicineId | Integer | 是 | 药品ID |
| quantity | Integer | 是 | 数量 |
| operator | String | 是 | 操作员 |

**请求示例：**
```
POST /api/pharmacy/dispense?prescriptionId=1&medicineId=101&quantity=1&operator=药剂师张三
```

---

## 财务管理 (Finance Management)

### 7.1 生成账单

**接口地址：** `POST /api/finance/bill/{appointmentId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| appointmentId | Integer | 是 | 挂号ID |

**请求示例：**
```
POST /api/finance/bill/1
```

### 7.2 支付账单

**接口地址：** `POST /api/finance/pay/{financeId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| financeId | Integer | 是 | 财务记录ID |

**请求示例：**
```
POST /api/finance/pay/1
```

### 7.3 查询挂号单账单

**接口地址：** `GET /api/finance/appointment/{appointmentId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| appointmentId | Integer | 是 | 挂号ID |

**请求示例：**
```
GET /api/finance/appointment/1
```

---

## 检查管理 (Test Management)

### 8.1 申请检查

**接口地址：** `POST /api/test`

**请求头：**
```
Content-Type: application/json
```

**请求体：**
```json
{
  "pid": 1,
  "doctorId": 2,
  "appointmentId": 3,
  "testType": 1,
  "testFee": 50.00
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |
| doctorId | Integer | 是 | 医生ID |
| appointmentId | Integer | 是 | 挂号ID |
| testType | Integer | 是 | 检查类型 |
| testFee | BigDecimal | 是 | 检查费用 |

### 8.2 查询患者检查

**接口地址：** `GET /api/test/patient/{pid}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pid | Integer | 是 | 患者ID |

**请求示例：**
```
GET /api/test/patient/1
```

### 8.3 查询挂号单检查

**接口地址：** `GET /api/test/appointment/{appointmentId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| appointmentId | Integer | 是 | 挂号ID |

**请求示例：**
```
GET /api/test/appointment/1
```

### 8.4 更新检查状态/结果

**接口地址：** `PUT /api/test/{testId}/status`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| testId | Integer | 是 | 检查ID |
| status | Integer | 是 | 状态值 (0: 待检查, 1: 已完成) |
| result | String | 否 | 检查结果 |

**请求示例：**
```
PUT /api/test/1/status?status=1&result=检查结果正常
```

---

## 统计相关 (Statistics)

### 9.1 获取挂号统计

**接口地址：** `GET /api/statistics/registration`

**说明：** 获取挂号统计数据

### 9.2 获取医生工作量

**接口地址：** `GET /api/statistics/doctor/workload`

**说明：** 获取医生工作量统计数据

### 9.3 获取每日统计

**接口地址：** `GET /api/statistics/daily`

**说明：** 获取每日就诊量和收入统计

---

## 文件管理 (File Management)

### 10.1 文件上传

**接口地址：** `POST /api/file/upload`

**请求头：**
```
Content-Type: multipart/form-data
```

**请求体：** 文件二进制数据

### 10.2 文件下载

**接口地址：** `GET /api/file/download/{fileId}`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fileId | Integer | 是 | 文件ID |

**请求示例：**
```
GET /api/file/download/1
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未认证/未授权 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 测试工具建议

您可以使用以下工具进行接口测试：

1. **Postman** - 图形化界面，功能强大
2. **curl** - 命令行工具，适合脚本化测试
3. **Insomnia** - 轻量级REST客户端
4. **Swagger UI** - 如果项目集成了SpringDoc，可以直接访问 `/swagger-ui.html`

---

## 示例测试脚本

### curl 示例

```bash
# 搜索患者
curl -X GET "http://localhost:8080/api/patient/search?name=张三"

# 创建患者
curl -X POST "http://localhost:8080/api/patient" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试患者",
    "gender": 1,
    "age": 30,
    "phone": "13800138000",
    "address": "测试地址"
  }'

# 创建挂号
curl -X POST "http://localhost:8080/api/appointment" \
  -H "Content-Type: application/json" \
  -d '{
    "pid": 1,
    "doctorId": 2,
    "department": "内科",
    "registrationDate": "2023-12-18",
    "registrationFee": 10.00
  }'
```

---

## 注意事项

1. 所有接口都需要在请求头中添加有效的JWT Token（除了登录接口）
2. 日期格式统一使用 `YYYY-MM-DD` 格式
3. 金额字段使用 `BigDecimal` 类型，避免精度丢失
4. 分页查询接口（如有）使用 `page` 和 `size` 参数
5. 删除操作为逻辑删除，不会物理删除数据

---

*文档版本：v1.0*  
*更新日期：2023-12-18*