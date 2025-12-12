package com.his.server.dto;

import lombok.Data;

@Data
public class PrescriptionDTO {
    private Integer pid;
    private Integer doctorId;
    private Integer appointmentId;
    private Integer medicineId;
    private String dosage;
    private String dosageUnit;
    private String frequency;
    private Integer quantity;
}