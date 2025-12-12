package com.his.server.service;

import com.his.server.entity.Statistics;
import com.his.server.repository.AppointmentRepository;
import com.his.server.repository.FinanceRepository;
import com.his.server.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final AppointmentRepository appointmentRepository;
    private final FinanceRepository financeRepository;

    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
    @Transactional
    public void calculateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        calculateStatsForDate(yesterday);
    }

    @Transactional
    public void calculateStatsForDate(LocalDate date) {
        // 1. 统计日挂号量
        long registrationCount = appointmentRepository.countByRegistrationDate(date); 
        saveStat("日挂号量", date, BigDecimal.valueOf(registrationCount));

        // 2. 统计日收入
        BigDecimal dailyIncome = financeRepository.sumTotalFeeByDate(date);
        saveStat("日总收入", date, dailyIncome);
    }
    
    private void saveStat(String type, LocalDate date, BigDecimal value) {
        Statistics stat = statisticsRepository.findByStatisticTypeAndStatisticDate(type, date)
                .orElse(new Statistics());
        stat.setStatisticType(type);
        stat.setStatisticDate(date);
        stat.setValue(value);
        statisticsRepository.save(stat);
    }

    public List<Statistics> listByDate(LocalDate date) {
        return statisticsRepository.findByStatisticDate(date);
    }
}