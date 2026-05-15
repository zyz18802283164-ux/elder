package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.EmergencyContact;
import com.medication.entity.Patient;
import com.medication.service.EmergencyContactService;
import com.medication.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emergency-contact")
@CrossOrigin
public class EmergencyContactController {
    @Autowired
    private EmergencyContactService emergencyContactService;
    @Autowired
    private PatientService patientService;
    
    @GetMapping
    public Result<List<EmergencyContact>> findAll() {
        List<EmergencyContact> list = emergencyContactService.list();
        for (int i = 0; i < list.size(); i++) {
            Long patientId = list.get(i).getPatientId();
            Patient byId = patientService.getById(patientId);
            list.get(i).setPatient(byId);
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<EmergencyContact> findById(@PathVariable Long id) {
        return Result.success(emergencyContactService.getById(id));
    }
    
    @PostMapping
    public Result<EmergencyContact> save(@RequestBody EmergencyContact contact) {
        if (contact.getPriority() == null) {
            contact.setPriority(1);
        }
        emergencyContactService.save(contact);
        return Result.success(contact);
    }
    
    @PutMapping
    public Result<EmergencyContact> update(@RequestBody EmergencyContact contact) {
        emergencyContactService.updateById(contact);
        return Result.success(contact);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        emergencyContactService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<EmergencyContact>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(emergencyContactService.findByPatientId(patientId));
    }
}
