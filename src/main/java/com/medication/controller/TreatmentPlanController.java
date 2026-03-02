package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.TreatmentPlan;
import com.medication.service.TreatmentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/treatment-plan")
@CrossOrigin
public class TreatmentPlanController {
    @Autowired
    private TreatmentPlanService treatmentPlanService;
    
    @GetMapping
    public Result<List<TreatmentPlan>> findAll() {
        return Result.success(treatmentPlanService.list());
    }
    
    @GetMapping("/{id}")
    public Result<TreatmentPlan> findById(@PathVariable Long id) {
        return Result.success(treatmentPlanService.getById(id));
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
        return Result.success(treatmentPlanService.findByPatientId(patientId));
    }
    
    @GetMapping("/patient/{patientId}/active")
    public Result<List<TreatmentPlan>> findActiveByPatientId(@PathVariable Long patientId) {
        return Result.success(treatmentPlanService.findActiveByPatientId(patientId));
    }
}
