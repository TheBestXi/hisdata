package com.his.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "statistics", indexes = {
    @Index(name = "idx_type_date", columnList = "statistic_type, statistic_date", unique = true)
})
public class Statistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Integer statisticId;

    @Column(name = "statistic_type", nullable = false, length = 50)
    private String statisticType;

    @Column(name = "statistic_date", nullable = false)
    private LocalDate statisticDate;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal value;
}
