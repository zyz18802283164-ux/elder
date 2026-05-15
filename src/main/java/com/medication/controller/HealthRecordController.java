package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.HealthRecord;
import com.medication.service.HealthRecordService;
import com.medication.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/health-record")
@CrossOrigin
public class HealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public Result<List<HealthRecord>> findAll() {
        List<HealthRecord> list = healthRecordService.list();
        for (HealthRecord record : list) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<HealthRecord> findById(@PathVariable Long id) {
        HealthRecord record = healthRecordService.getById(id);
        if (record != null && record.getPatientId() != null) {
            record.setPatient(patientService.getById(record.getPatientId()));
        }
        return Result.success(record);
    }
    
    @PostMapping
    public Result<HealthRecord> save(@RequestBody HealthRecord record) {
        healthRecordService.save(record);
        return Result.success(record);
    }
    
    @PutMapping
    public Result<HealthRecord> update(@RequestBody HealthRecord record) {
        healthRecordService.updateById(record);
        return Result.success(record);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        healthRecordService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<HealthRecord>> findByPatientId(@PathVariable Long patientId) {
        List<HealthRecord> list = healthRecordService.findByPatientId(patientId);
        for (HealthRecord record : list) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
        }
        return Result.success(list);
    }
}
