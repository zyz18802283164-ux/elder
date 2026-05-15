package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.EmergencyHelp;
import com.medication.service.EmergencyHelpService;
import com.medication.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emergency-help")
@CrossOrigin
public class EmergencyHelpController {
    @Autowired
    private EmergencyHelpService emergencyHelpService;
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public Result<List<EmergencyHelp>> findAll() {
        List<EmergencyHelp> list = emergencyHelpService.list();
        for (EmergencyHelp help : list) {
            if (help.getPatientId() != null) {
                help.setPatient(patientService.getById(help.getPatientId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<EmergencyHelp> findById(@PathVariable Long id) {
        EmergencyHelp help = emergencyHelpService.getById(id);
        if (help != null && help.getPatientId() != null) {
            help.setPatient(patientService.getById(help.getPatientId()));
        }
        return Result.success(help);
    }
    
    @PostMapping
    public Result<EmergencyHelp> save(@RequestBody EmergencyHelp help) {
        emergencyHelpService.save(help);
        return Result.success(help);
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<EmergencyHelp>> findByPatientId(@PathVariable Long patientId) {
        List<EmergencyHelp> list = emergencyHelpService.findByPatientId(patientId);
        for (EmergencyHelp help : list) {
            if (help.getPatientId() != null) {
                help.setPatient(patientService.getById(help.getPatientId()));
            }
        }
        return Result.success(list);
    }
    
    @PostMapping("/create/{patientId}")
    public Result<EmergencyHelp> createHelp(@PathVariable Long patientId) {
        return Result.success(emergencyHelpService.createHelp(patientId));
    }
}
