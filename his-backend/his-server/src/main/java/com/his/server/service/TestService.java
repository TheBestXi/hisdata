package com.his.server.service;

import com.his.server.dto.TestDTO;
import com.his.server.entity.Test;
import com.his.server.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public List<Test> listByPatient(Integer pid) {
        return testRepository.findByPid(pid);
    }

    public List<Test> listByAppointment(Integer appointmentId) {
        return testRepository.findByAppointmentId(appointmentId);
    }

    @Transactional
    public Test createTest(TestDTO dto) {
        Test test = new Test();
        test.setPid(dto.getPid());
        test.setDoctorId(dto.getDoctorId());
        test.setAppointmentId(dto.getAppointmentId());
        test.setTestType(dto.getTestType());
        test.setTestFee(dto.getTestFee());
        test.setTestDate(LocalDate.now());
        test.setStatus(1); // 1=申请中
        
        return testRepository.save(test);
    }

    @Transactional
    public Test updateStatus(Integer testId, Integer status, String result) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("检查不存在"));
        test.setStatus(status);
        if (result != null) {
            test.setResult(result);
        }
        return testRepository.save(test);
    }
}