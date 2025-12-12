package com.his.server.controller;

import com.his.common.result.GlobalResult;
import com.his.server.entity.Finance;
import com.his.server.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "财务管理")
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @Operation(summary = "生成账单")
    @PostMapping("/bill/{appointmentId}")
    public GlobalResult<Finance> generateBill(@PathVariable Integer appointmentId) {
        return GlobalResult.success(financeService.generateBill(appointmentId));
    }

    @Operation(summary = "支付账单")
    @PostMapping("/pay/{financeId}")
    public GlobalResult<Finance> pay(@PathVariable Integer financeId) {
        return GlobalResult.success(financeService.pay(financeId));
    }

    @Operation(summary = "查询挂号单账单")
    @GetMapping("/appointment/{appointmentId}")
    public GlobalResult<List<Finance>> listByAppointment(@PathVariable Integer appointmentId) {
        return GlobalResult.success(financeService.listByAppointment(appointmentId));
    }
}