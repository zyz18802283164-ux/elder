package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.Patient;
import com.medication.entity.User;
import com.medication.service.PatientService;
import com.medication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public Result<List<Map<String, Object>>> findAll() {
        List<Patient> patients = patientService.list();
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        
        for (Patient patient : patients) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", patient.getId());
            map.put("name", patient.getName());
            map.put("age", patient.getAge());
            map.put("gender", patient.getGender());
            map.put("phone", patient.getPhone());
            map.put("address", patient.getAddress());
            map.put("chronicDisease", patient.getChronicDisease());
            map.put("createTime", patient.getCreateTime());
            map.put("updateTime", patient.getUpdateTime());
            
            // 查找关联的用户账号
            User user = userService.findByPatientId(patient.getId());
            if (user != null) {
                map.put("username", user.getUsername());
                map.put("password", user.getPassword());
            }
            
            result.add(map);
        }
        
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<Patient> findById(@PathVariable Long id) {
        return Result.success(patientService.getById(id));
    }
    
    @PostMapping
    public Result<Patient> save(@RequestBody Patient patient) {
        patientService.save(patient);
        return Result.success(patient);
    }
    
    @PutMapping
    public Result<Patient> update(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.success(patient);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/search")
    public Result<List<Patient>> search(@RequestParam String keyword) {
        return Result.success(patientService.search(keyword));
    }
}
