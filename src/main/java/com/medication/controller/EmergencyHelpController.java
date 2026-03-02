package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.EmergencyHelp;
import com.medication.service.EmergencyHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emergency-help")
@CrossOrigin
public class EmergencyHelpController {
    @Autowired
    private EmergencyHelpService emergencyHelpService;
    
    @GetMapping
    public Result<List<EmergencyHelp>> findAll() {
        return Result.success(emergencyHelpService.list());
    }
    
    @GetMapping("/{id}")
    public Result<EmergencyHelp> findById(@PathVariable Long id) {
        return Result.success(emergencyHelpService.getById(id));
    }
    
    @PostMapping
    public Result<EmergencyHelp> save(@RequestBody EmergencyHelp help) {
        emergencyHelpService.save(help);
        return Result.success(help);
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<EmergencyHelp>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(emergencyHelpService.findByPatientId(patientId));
    }
    
    @PostMapping("/create/{patientId}")
    public Result<EmergencyHelp> createHelp(@PathVariable Long patientId) {
        return Result.success(emergencyHelpService.createHelp(patientId));
    }
}
