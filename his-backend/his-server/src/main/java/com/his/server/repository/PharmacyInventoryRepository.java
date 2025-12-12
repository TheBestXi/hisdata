package com.his.server.repository;

import com.his.server.entity.PharmacyInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PharmacyInventoryRepository extends JpaRepository<PharmacyInventory, Integer> {
    List<PharmacyInventory> findByNameContaining(String name);
    List<PharmacyInventory> findByCategory(String category);
    List<PharmacyInventory> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
}