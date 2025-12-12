package com.his.server.service;

import com.his.common.exception.BusinessException;
import com.his.server.entity.MedicineIssueRecord;
import com.his.server.entity.PharmacyInventory;
import com.his.server.repository.MedicineIssueRecordRepository;
import com.his.server.repository.PharmacyInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyInventoryService {

    private final PharmacyInventoryRepository inventoryRepository;
    private final MedicineIssueRecordRepository issueRecordRepository;

    public List<PharmacyInventory> list(String name, String category) {
        if (name != null && !name.isEmpty()) {
            return inventoryRepository.findByNameContaining(name);
        }
        if (category != null && !category.isEmpty()) {
            return inventoryRepository.findByCategory(category);
        }
        return inventoryRepository.findAll();
    }

    public List<PharmacyInventory> listExpiringSoon() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        return inventoryRepository.findByExpirationDateBetween(today, thirtyDaysLater);
    }

    public PharmacyInventory save(PharmacyInventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public PharmacyInventory getById(Integer id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public void checkStock(Integer medicineId, Integer quantity) {
        PharmacyInventory inventory = inventoryRepository.findById(medicineId)
                .orElseThrow(() -> new BusinessException("药品不存在"));
        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("库存不足，当前库存: " + inventory.getQuantity());
        }
    }

    @Transactional
    public void dispenseMedicine(Integer prescriptionId, Integer medicineId, Integer quantity, String operator) {
        PharmacyInventory inventory = inventoryRepository.findById(medicineId)
                .orElseThrow(() -> new BusinessException("药品不存在"));
        
        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("库存不足，无法发药");
        }

        // 扣减库存
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        // 记录发药
        MedicineIssueRecord record = new MedicineIssueRecord();
        record.setPrescriptionId(prescriptionId);
        record.setMedicineId(medicineId);
        record.setIssueQuantity(quantity);
        record.setOperator(operator);
        record.setIssueTime(LocalDateTime.now());
        issueRecordRepository.save(record);
    }
}