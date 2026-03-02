package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.MedicationRecord;
import com.medication.service.MedicationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medication-record")
@CrossOrigin
public class MedicationRecordController {
    @Autowired
    private MedicationRecordService medicationRecordService;
    
    @GetMapping
    public Result<List<MedicationRecord>> findAll() {
        return Result.success(medicationRecordService.list());
    }
    
    @GetMapping("/{id}")
    public Result<MedicationRecord> findById(@PathVariable Long id) {
        return Result.success(medicationRecordService.getById(id));
    }
    
    @PostMapping
    public Result<MedicationRecord> save(@RequestBody MedicationRecord record) {
        medicationRecordService.save(record);
        return Result.success(record);
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<MedicationRecord>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(medicationRecordService.findByPatientId(patientId));
    }
    
    @GetMapping("/patient/{patientId}/today")
    public Result<List<MedicationRecord>> findTodayRecords(@PathVariable Long patientId) {
        return Result.success(medicationRecordService.findTodayRecords(patientId));
    }
    
    @GetMapping("/patient/{patientId}/missed-count")
    public Result<Long> countMissed(@PathVariable Long patientId) {
        return Result.success(medicationRecordService.countMissed(patientId));
    }
    
    @PostMapping("/{id}/mark-taken")
    public Result<MedicationRecord> markAsTaken(@PathVariable Long id) {
        return Result.success(medicationRecordService.markAsTaken(id));
    }
}
