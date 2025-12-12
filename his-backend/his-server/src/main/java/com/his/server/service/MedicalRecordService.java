package com.his.server.service;

import com.his.server.dto.MedicalRecordDTO;
import com.his.server.entity.MedicalRecord;
import com.his.server.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> listByPatient(Integer pid) {
        return medicalRecordRepository.findByPid(pid);
    }

    public List<MedicalRecord> listByAppointment(Integer appointmentId) {
        return medicalRecordRepository.findByAppointmentId(appointmentId);
    }

    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordDTO dto) {
        MedicalRecord record = new MedicalRecord();
        record.setPid(dto.getPid());
        record.setDoctorId(dto.getDoctorId());
        record.setAppointmentId(dto.getAppointmentId());
        record.setChiefComplaint(dto.getChiefComplaint());
        record.setPresentIllness(dto.getPresentIllness());
        record.setPhysicalExamination(dto.getPhysicalExamination());
        record.setPreliminaryDiagnosis(dto.getPreliminaryDiagnosis());
        
        return medicalRecordRepository.save(record);
    }
}