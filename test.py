import unittest
import requests
import json
from datetime import datetime

# 配置项 - 请根据实际环境修改
BASE_URL = "http://localhost:8080"  # 接口基础地址
TOKEN = "your_jwt_token_here"      # 替换为有效的JWT令牌

# 通用请求头
headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {TOKEN}"
}

class TestHISSystemAPI(unittest.TestCase):
    """HIS系统API接口测试类"""
    
    # 测试过程中需要用到的临时数据
    test_data = {
        "patient_id": None,
        "doctor_id": None,
        "appointment_id": None,
        "medical_record_id": None,
        "prescription_id": None,
        "medicine_id": None,
        "test_id": None,
        "finance_id": None,
        "file_id": None
    }
    
    def check_response(self, response):
        """检查响应是否符合通用格式"""
        self.assertEqual(response.status_code, 200, "HTTP状态码不为200")
        try:
            json_data = response.json()
        except json.JSONDecodeError:
            self.fail("响应不是有效的JSON格式")
            
        # 检查通用响应字段
        self.assertIn("code", json_data, "响应缺少code字段")
        self.assertIn("message", json_data, "响应缺少message字段")
        self.assertIn("data", json_data, "响应缺少data字段")
        self.assertIn("timestamp", json_data, "响应缺少timestamp字段")
        
        # 检查时间戳格式
        try:
            datetime.fromisoformat(json_data["timestamp"].replace('Z', '+00:00'))
        except ValueError:
            self.fail("timestamp格式不正确")
            
        return json_data
    
    # ------------------------------ 患者管理测试 ------------------------------
    def test_1_1_search_patient(self):
        """测试搜索患者接口"""
        url = f"{BASE_URL}/api/patient/search"
        params = {"name": "张三"}
        response = requests.get(url, params=params, headers=headers)
        self.check_response(response)
    
    def test_1_2_create_patient(self):
        """测试创建患者接口"""
        url = f"{BASE_URL}/api/patient"
        data = {
            "name": "测试患者",
            "gender": 1,
            "age": 30,
            "phone": "13800138000",
            "address": "测试地址",
            "idCard": "110101199001011234",
            "medicalHistory": "无",
            "allergyHistory": "无"
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存创建的患者ID供后续测试使用
        if isinstance(json_data["data"], dict) and "patientId" in json_data["data"]:
            self.test_data["patient_id"] = json_data["data"]["patientId"]
    
    def test_1_3_get_patient_detail(self):
        """测试获取患者详情接口"""
        if not self.test_data["patient_id"]:
            self.skipTest("没有可用的患者ID，跳过测试")
            
        url = f"{BASE_URL}/api/patient/{self.test_data['patient_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 医生管理测试 ------------------------------
    def test_2_1_get_doctor_list(self):
        """测试查询医生列表接口"""
        url = f"{BASE_URL}/api/doctor"
        params = {"department": "内科"}
        response = requests.get(url, params=params, headers=headers)
        self.check_response(response)
    
    def test_2_2_add_doctor(self):
        """测试添加医生接口"""
        url = f"{BASE_URL}/api/doctor"
        data = {
            "name": "测试医生",
            "title": "主治医师",
            "department": "内科",
            "phone": "13900139000"
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存创建的医生ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["doctor_id"] = json_data["data"]["id"]
    
    # ------------------------------ 挂号管理测试 ------------------------------
    def test_3_1_create_appointment(self):
        """测试创建挂号单接口"""
        if not all([self.test_data["patient_id"], self.test_data["doctor_id"]]):
            self.skipTest("缺少患者ID或医生ID，跳过测试")
            
        url = f"{BASE_URL}/api/appointment"
        data = {
            "pid": self.test_data["patient_id"],
            "doctorId": self.test_data["doctor_id"],
            "department": "内科",
            "registrationDate": datetime.now().strftime("%Y-%m-%d"),
            "registrationFee": 10.00
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存创建的挂号ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["appointment_id"] = json_data["data"]["id"]
    
    def test_3_2_get_patient_appointments(self):
        """测试查询患者的挂号记录接口"""
        if not self.test_data["patient_id"]:
            self.skipTest("没有可用的患者ID，跳过测试")
            
        url = f"{BASE_URL}/api/appointment/patient/{self.test_data['patient_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_3_3_update_appointment_status(self):
        """测试更新挂号状态接口"""
        if not self.test_data["appointment_id"]:
            self.skipTest("没有可用的挂号ID，跳过测试")
            
        url = f"{BASE_URL}/api/appointment/{self.test_data['appointment_id']}/status"
        params = {"status": 1}  # 设为已支付状态
        response = requests.put(url, params=params, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 病历管理测试 ------------------------------
    def test_4_1_create_medical_record(self):
        """测试创建病历接口"""
        if not all([self.test_data["patient_id"], self.test_data["doctor_id"], self.test_data["appointment_id"]]):
            self.skipTest("缺少必要ID，跳过测试")
            
        url = f"{BASE_URL}/api/medical-record"
        data = {
            "pid": self.test_data["patient_id"],
            "doctorId": self.test_data["doctor_id"],
            "appointmentId": self.test_data["appointment_id"],
            "chiefComplaint": "头痛",
            "presentIllness": "患者自述头痛3天",
            "physicalExamination": "体温正常，血压偏高",
            "preliminaryDiagnosis": "偏头痛"
        }
        response = requests.post(url, json=data, headers=headers)
        self.check_response(response)
    
    def test_4_2_get_patient_medical_records(self):
        """测试查询患者病历接口"""
        if not self.test_data["patient_id"]:
            self.skipTest("没有可用的患者ID，跳过测试")
            
        url = f"{BASE_URL}/api/medical-record/patient/{self.test_data['patient_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_4_3_get_appointment_medical_record(self):
        """测试查询挂号单病历接口"""
        if not self.test_data["appointment_id"]:
            self.skipTest("没有可用的挂号ID，跳过测试")
            
        url = f"{BASE_URL}/api/medical-record/appointment/{self.test_data['appointment_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 处方管理测试 ------------------------------
    def test_5_1_create_prescription(self):
        """测试开具处方接口"""
        if not all([self.test_data["patient_id"], self.test_data["doctor_id"], self.test_data["appointment_id"]]):
            self.skipTest("缺少必要ID，跳过测试")
            
        url = f"{BASE_URL}/api/prescription"
        data = {
            "pid": self.test_data["patient_id"],
            "doctorId": self.test_data["doctor_id"],
            "appointmentId": self.test_data["appointment_id"],
            "medicineId": 1,  # 假设存在ID为1的药品
            "dosage": "5mg",
            "dosageUnit": "片",
            "frequency": "每日三次",
            "quantity": 21
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存创建的处方ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["prescription_id"] = json_data["data"]["id"]
    
    def test_5_2_get_patient_prescriptions(self):
        """测试查询患者处方接口"""
        if not self.test_data["patient_id"]:
            self.skipTest("没有可用的患者ID，跳过测试")
            
        url = f"{BASE_URL}/api/prescription/patient/{self.test_data['patient_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 药房管理测试 ------------------------------
    def test_6_1_query_medicine_inventory(self):
        """测试查询药品库存接口"""
        url = f"{BASE_URL}/api/pharmacy/inventory"
        params = {"name": "阿莫西林"}
        response = requests.get(url, params=params, headers=headers)
        self.check_response(response)
    
    def test_6_2_query_expiring_medicines(self):
        """测试查询临期药品接口"""
        url = f"{BASE_URL}/api/pharmacy/inventory/expiring"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_6_3_add_medicine(self):
        """测试添加药品接口"""
        url = f"{BASE_URL}/api/pharmacy/inventory"
        data = {
            "name": "测试药品",
            "specification": "0.25g*24粒",
            "manufacturer": "测试药厂",
            "category": "抗生素",
            "price": 15.50,
            "quantity": 100,
            "unit": "盒",
            "expirationDate": "2025-12-31"
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存创建的药品ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["medicine_id"] = json_data["data"]["id"]
    
    def test_6_4_dispense_medicine(self):
        """测试发药接口"""
        if not all([self.test_data["prescription_id"], self.test_data["medicine_id"]]):
            self.skipTest("缺少处方ID或药品ID，跳过测试")
            
        url = f"{BASE_URL}/api/pharmacy/dispense"
        params = {
            "prescriptionId": self.test_data["prescription_id"],
            "medicineId": self.test_data["medicine_id"],
            "quantity": 1,
            "operator": "测试药剂师"
        }
        response = requests.post(url, params=params, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 财务管理测试 ------------------------------
    def test_7_1_generate_bill(self):
        """测试生成账单接口"""
        if not self.test_data["appointment_id"]:
            self.skipTest("没有可用的挂号ID，跳过测试")
            
        url = f"{BASE_URL}/api/finance/bill/{self.test_data['appointment_id']}"
        response = requests.post(url, headers=headers)
        json_data = self.check_response(response)
        # 保存财务记录ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["finance_id"] = json_data["data"]["id"]
    
    def test_7_2_pay_bill(self):
        """测试支付账单接口"""
        if not self.test_data["finance_id"]:
            self.skipTest("没有可用的财务记录ID，跳过测试")
            
        url = f"{BASE_URL}/api/finance/pay/{self.test_data['finance_id']}"
        response = requests.post(url, headers=headers)
        self.check_response(response)
    
    def test_7_3_query_appointment_bill(self):
        """测试查询挂号单账单接口"""
        if not self.test_data["appointment_id"]:
            self.skipTest("没有可用的挂号ID，跳过测试")
            
        url = f"{BASE_URL}/api/finance/appointment/{self.test_data['appointment_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 检查管理测试 ------------------------------
    def test_8_1_apply_test(self):
        """测试申请检查接口"""
        if not all([self.test_data["patient_id"], self.test_data["doctor_id"], self.test_data["appointment_id"]]):
            self.skipTest("缺少必要ID，跳过测试")
            
        url = f"{BASE_URL}/api/test"
        data = {
            "pid": self.test_data["patient_id"],
            "doctorId": self.test_data["doctor_id"],
            "appointmentId": self.test_data["appointment_id"],
            "testType": 1,
            "testFee": 50.00
        }
        response = requests.post(url, json=data, headers=headers)
        json_data = self.check_response(response)
        # 保存检查ID供后续测试使用
        if isinstance(json_data["data"], dict) and "id" in json_data["data"]:
            self.test_data["test_id"] = json_data["data"]["id"]
    
    def test_8_2_query_patient_tests(self):
        """测试查询患者检查接口"""
        if not self.test_data["patient_id"]:
            self.skipTest("没有可用的患者ID，跳过测试")
            
        url = f"{BASE_URL}/api/test/patient/{self.test_data['patient_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_8_3_query_appointment_tests(self):
        """测试查询挂号单检查接口"""
        if not self.test_data["appointment_id"]:
            self.skipTest("没有可用的挂号ID，跳过测试")
            
        url = f"{BASE_URL}/api/test/appointment/{self.test_data['appointment_id']}"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_8_4_update_test_status(self):
        """测试更新检查状态/结果接口"""
        if not self.test_data["test_id"]:
            self.skipTest("没有可用的检查ID，跳过测试")
            
        url = f"{BASE_URL}/api/test/{self.test_data['test_id']}/status"
        params = {
            "status": 1,  # 已完成
            "result": "检查结果正常"
        }
        response = requests.put(url, params=params, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 统计相关测试 ------------------------------
    def test_9_1_get_registration_statistics(self):
        """测试获取挂号统计接口"""
        url = f"{BASE_URL}/api/statistics/registration"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_9_2_get_doctor_workload(self):
        """测试获取医生工作量接口"""
        url = f"{BASE_URL}/api/statistics/doctor/workload"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    def test_9_3_get_daily_statistics(self):
        """测试获取每日统计接口"""
        url = f"{BASE_URL}/api/statistics/daily"
        response = requests.get(url, headers=headers)
        self.check_response(response)
    
    # ------------------------------ 文件管理测试 ------------------------------
    def test_10_1_upload_file(self):
        """测试文件上传接口"""
        url = f"{BASE_URL}/api/file/upload"
        # 用文本文件作为测试文件
        files = {"file": ("test.txt", "这是一个测试文件", "text/plain")}
        # 上传文件不需要JSON头
        upload_headers = {"Authorization": f"Bearer {TOKEN}"}
        response = requests.post(url, files=files, headers=upload_headers)
        json_data = self.check_response(response)
        # 保存文件ID供后续测试使用
        if isinstance(json_data["data"], dict) and "fileId" in json_data["data"]:
            self.test_data["file_id"] = json_data["data"]["fileId"]
    
    def test_10_2_download_file(self):
        """测试文件下载接口"""
        if not self.test_data["file_id"]:
            self.skipTest("没有可用的文件ID，跳过测试")
            
        url = f"{BASE_URL}/api/file/download/{self.test_data['file_id']}"
        response = requests.get(url, headers=headers)
        self.assertEqual(response.status_code, 200, "文件下载失败")


if __name__ == "__main__":
    # 按顺序执行测试用例（因为存在依赖关系）
    test_order = [
        # 患者管理
        "test_1_1_search_patient",
        "test_1_2_create_patient",
        "test_1_3_get_patient_detail",
        # 医生管理
        "test_2_1_get_doctor_list",
        "test_2_2_add_doctor",
        # 挂号管理
        "test_3_1_create_appointment",
        "test_3_2_get_patient_appointments",
        "test_3_3_update_appointment_status",
        # 病历管理
        "test_4_1_create_medical_record",
        "test_4_2_get_patient_medical_records",
        "test_4_3_get_appointment_medical_record",
        # 处方管理
        "test_5_1_create_prescription",
        "test_5_2_get_patient_prescriptions",
        # 药房管理
        "test_6_1_query_medicine_inventory",
        "test_6_2_query_expiring_medicines",
        "test_6_3_add_medicine",
        "test_6_4_dispense_medicine",
        # 财务管理
        "test_7_1_generate_bill",
        "test_7_2_pay_bill",
        "test_7_3_query_appointment_bill",
        # 检查管理
        "test_8_1_apply_test",
        "test_8_2_query_patient_tests",
        "test_8_3_query_appointment_tests",
        "test_8_4_update_test_status",
        # 统计相关
        "test_9_1_get_registration_statistics",
        "test_9_2_get_doctor_workload",
        "test_9_3_get_daily_statistics",
        # 文件管理
        "test_10_1_upload_file",
        "test_10_2_download_file"
    ]
    
    suite = unittest.TestSuite()
    for test_name in test_order:
        suite.addTest(TestHISSystemAPI(test_name))
    
    runner = unittest.TextTestRunner(verbosity=2)
    runner.run(suite)