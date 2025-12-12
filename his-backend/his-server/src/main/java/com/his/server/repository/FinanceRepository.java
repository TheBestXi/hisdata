package com.his.server.repository;

import com.his.server.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {
    List<Finance> findByAppointmentId(Integer appointmentId);
    List<Finance> findByPaymentStatus(String paymentStatus);
    
    @Query("SELECT COALESCE(SUM(f.totalFee), 0) FROM Finance f WHERE f.paymentStatus = '已支付' AND DATE(f.paymentTime) = :date")
    BigDecimal sumTotalFeeByDate(@Param("date") LocalDate date);
}