package com.his.server.repository;

import com.his.server.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    Optional<Statistics> findByStatisticTypeAndStatisticDate(String statisticType, LocalDate statisticDate);
    List<Statistics> findByStatisticDate(LocalDate statisticDate);
}