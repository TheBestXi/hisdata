package com.his.server.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TestDTO {
    private Integer pid;
    private Integer doctorId;
    private Integer appointmentId;
    private Integer testType;
    private BigDecimal testFee;
}