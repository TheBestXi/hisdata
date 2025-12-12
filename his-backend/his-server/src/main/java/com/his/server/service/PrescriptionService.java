package com.his.server.service;

import com.his.common.exception.BusinessException;
import com.his.server.dto.PrescriptionDTO;
import com.his.server.entity.PharmacyInventory;
import com.his.server.entity.Prescription;
import com.his.server.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PharmacyInventoryService inventoryService;

    public List<Prescription> listByPatient(Integer pid) {
        return prescriptionRepository.findByPid(pid);
    }

    @Transactional
    public Prescription createPrescription(PrescriptionDTO dto) {
        // 1. 检查库存
        inventoryService.checkStock(dto.getMedicineId(), dto.getQuantity());

        // 2. 获取药品信息计算价格
        PharmacyInventory medicine = inventoryService.getById(dto.getMedicineId());
        if (medicine == null) {
            throw new BusinessException("药品不存在");
        }

        // 3. 创建处方
        Prescription prescription = new Prescription();
        prescription.setPid(dto.getPid());
        prescription.setDoctorId(dto.getDoctorId());
        prescription.setAppointmentId(dto.getAppointmentId());
        prescription.setMedicineId(dto.getMedicineId());
        prescription.setMedicineName(medicine.getName());
        prescription.setDosage(dto.getDosage());
        prescription.setDosageUnit(dto.getDosageUnit());
        prescription.setFrequency(dto.getFrequency());
        prescription.setQuantity(dto.getQuantity());
        
        // 计算总价
        BigDecimal totalCost = medicine.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));
        prescription.setTotalCost(totalCost);

        return prescriptionRepository.save(prescription);
    }
}