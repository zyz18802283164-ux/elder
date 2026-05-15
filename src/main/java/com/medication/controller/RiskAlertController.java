package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.RiskAlert;
import com.medication.service.PatientService;
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

    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public Result<List<RiskAlert>> findAll() {
        List<RiskAlert> list = riskAlertService.list();
        for (RiskAlert alert : list) {
            if (alert.getPatientId() != null) {
                alert.setPatient(patientService.getById(alert.getPatientId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<RiskAlert> findById(@PathVariable Long id) {
        RiskAlert alert = riskAlertService.getById(id);
        if (alert != null && alert.getPatientId() != null) {
            alert.setPatient(patientService.getById(alert.getPatientId()));
        }
        return Result.success(alert);
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
        List<RiskAlert> list = riskAlertService.findPending();
        for (RiskAlert alert : list) {
            if (alert.getPatientId() != null) {
                alert.setPatient(patientService.getById(alert.getPatientId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<RiskAlert>> findByPatientId(@PathVariable Long patientId) {
        List<RiskAlert> list = riskAlertService.findByPatientId(patientId);
        for (RiskAlert alert : list) {
            if (alert.getPatientId() != null) {
                alert.setPatient(patientService.getById(alert.getPatientId()));
            }
        }
        return Result.success(list);
    }
}
