package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.RiskAlert;
import com.medication.service.RiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/risk-alert")
@CrossOrigin
public class RiskAlertController {
    @Autowired
    private RiskAlertService riskAlertService;
    
    @GetMapping
    public Result<List<RiskAlert>> findAll() {
        return Result.success(riskAlertService.list());
    }
    
    @GetMapping("/{id}")
    public Result<RiskAlert> findById(@PathVariable Long id) {
        return Result.success(riskAlertService.getById(id));
    }
    
    @PostMapping
    public Result<RiskAlert> save(@RequestBody RiskAlert alert) {
        if (alert.getStatus() == null) {
            alert.setStatus("PENDING");
        }
        riskAlertService.save(alert);
        return Result.success(alert);
    }
    
    @PutMapping
    public Result<RiskAlert> update(@RequestBody RiskAlert alert) {
        riskAlertService.updateById(alert);
        return Result.success(alert);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        riskAlertService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/pending")
    public Result<List<RiskAlert>> findPending() {
        return Result.success(riskAlertService.findPending());
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<RiskAlert>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(riskAlertService.findByPatientId(patientId));
    }
}
