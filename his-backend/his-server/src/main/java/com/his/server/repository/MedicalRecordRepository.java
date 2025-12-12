package com.his.server.repository;

import com.his.server.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByPid(Integer pid);
    List<MedicalRecord> findByAppointmentId(Integer appointmentId);
}
