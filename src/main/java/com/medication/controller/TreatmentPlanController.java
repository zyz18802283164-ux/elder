package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.TreatmentPlan;
import com.medication.service.TreatmentPlanService;
import com.medication.service.PatientService;
import com.medication.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/treatment-plan")
@CrossOrigin
public class TreatmentPlanController {
    @Autowired
    private TreatmentPlanService treatmentPlanService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicineService medicineService;
    
    @GetMapping
    public Result<List<TreatmentPlan>> findAll() {
        List<TreatmentPlan> list = treatmentPlanService.list();
        for (TreatmentPlan plan : list) {
            if (plan.getPatientId() != null) {
                plan.setPatient(patientService.getById(plan.getPatientId()));
            }
            if (plan.getMedicineId() != null) {
                plan.setMedicine(medicineService.getById(plan.getMedicineId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<TreatmentPlan> findById(@PathVariable Long id) {
        TreatmentPlan plan = treatmentPlanService.getById(id);
        if (plan != null) {
            if (plan.getPatientId() != null) {
                plan.setPatient(patientService.getById(plan.getPatientId()));
            }
            if (plan.getMedicineId() != null) {
                plan.setMedicine(medicineService.getById(plan.getMedicineId()));
            }
        }
        return Result.success(plan);
    }
    
    @PostMapping
    public Result<TreatmentPlan> save(@RequestBody TreatmentPlan plan) {
        if (plan.getStatus() == null) {
            plan.setStatus("ACTIVE");
        }
        treatmentPlanService.save(plan);
        return Result.success(plan);
    }
    
    @PutMapping
    public Result<TreatmentPlan> update(@RequestBody TreatmentPlan plan) {
        treatmentPlanService.updateById(plan);
        return Result.success(plan);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        treatmentPlanService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<TreatmentPlan>> findByPatientId(@PathVariable Long patientId) {
        List<TreatmentPlan> list = treatmentPlanService.findByPatientId(patientId);
        for (TreatmentPlan plan : list) {
            if (plan.getPatientId() != null) {
                plan.setPatient(patientService.getById(plan.getPatientId()));
            }
            if (plan.getMedicineId() != null) {
                plan.setMedicine(medicineService.getById(plan.getMedicineId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/patient/{patientId}/active")
    public Result<List<TreatmentPlan>> findActiveByPatientId(@PathVariable Long patientId) {
        List<TreatmentPlan> list = treatmentPlanService.findActiveByPatientId(patientId);
        for (TreatmentPlan plan : list) {
            if (plan.getPatientId() != null) {
                plan.setPatient(patientService.getById(plan.getPatientId()));
            }
            if (plan.getMedicineId() != null) {
                plan.setMedicine(medicineService.getById(plan.getMedicineId()));
            }
        }
        return Result.success(list);
    }
}
