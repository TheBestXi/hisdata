package com.his.server.service;

import com.his.server.entity.Doctor;
import com.his.server.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> list(String department) {
        if (department != null && !department.isEmpty()) {
            return doctorRepository.findByDepartment(department);
        }
        return doctorRepository.findAll();
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    
    public Doctor getById(Integer id) {
        return doctorRepository.findById(id).orElse(null);
    }
}