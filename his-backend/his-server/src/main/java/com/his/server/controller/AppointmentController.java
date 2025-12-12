package com.his.server.controller;

import com.his.common.result.GlobalResult;
import com.his.server.dto.AppointmentDTO;
import com.his.server.entity.Appointment;
import com.his.server.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "挂号管理")
@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "创建挂号单")
    @PostMapping
    public GlobalResult<Appointment> create(@RequestBody AppointmentDTO dto) {
        return GlobalResult.success(appointmentService.createAppointment(dto));
    }

    @Operation(summary = "查询患者的挂号记录")
    @GetMapping("/patient/{pid}")
    public GlobalResult<java.util.List<Appointment>> listByPatient(@PathVariable Integer pid) {
        return GlobalResult.success(appointmentService.listByPatient(pid));
    }

    @Operation(summary = "更新挂号状态")
    @PutMapping("/{id}/status")
    public GlobalResult<Appointment> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        return GlobalResult.success(appointmentService.updateStatus(id, status));
    }
}
