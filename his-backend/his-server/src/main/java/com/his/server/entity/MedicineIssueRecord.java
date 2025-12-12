package com.his.server.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_issue_records", indexes = {
    @Index(name = "idx_pres_med", columnList = "prescription_id, medicine_id"),
    @Index(name = "idx_time_op", columnList = "issue_time, operator")
})
public class MedicineIssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Integer issueId;

    @Column(name = "prescription_id", nullable = false)
    private Integer prescriptionId;

    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Column(name = "issue_quantity", nullable = false)
    private Integer issueQuantity;

    @Column(nullable = false, length = 50)
    private String operator;

    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;
}
