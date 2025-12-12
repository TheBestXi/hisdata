package com.his.server.repository;

import com.his.server.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByPid(Integer pid);
    List<Test> findByAppointmentId(Integer appointmentId);
}