package com.his.server.repository;

import com.his.server.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPid(Integer pid);
    List<Appointment> findByDoctorIdAndRegistrationDate(Integer doctorId, LocalDate date);
    long countByRegistrationDate(LocalDate date);
}
