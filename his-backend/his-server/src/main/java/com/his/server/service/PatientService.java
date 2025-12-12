package com.his.server.service;

import com.his.server.entity.Patient;
import com.his.server.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> searchByName(String name) {
        return patientRepository.findByNameContaining(name);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getById(Integer id) {
        return patientRepository.findById(id).orElse(null);
    }
}