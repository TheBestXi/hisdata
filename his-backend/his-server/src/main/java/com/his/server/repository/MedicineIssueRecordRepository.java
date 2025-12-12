package com.his.server.repository;

import com.his.server.entity.MedicineIssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineIssueRecordRepository extends JpaRepository<MedicineIssueRecord, Integer> {
    List<MedicineIssueRecord> findByPrescriptionId(Integer prescriptionId);
}