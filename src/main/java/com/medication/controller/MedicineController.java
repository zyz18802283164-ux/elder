package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.Medicine;
import com.medication.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/medicine")
@CrossOrigin
public class MedicineController {
    @Autowired
    private MedicineService medicineService;
    
    @GetMapping
    public Result<List<Medicine>> findAll() {
        return Result.success(medicineService.list());
    }
    
    @GetMapping("/{id}")
    public Result<Medicine> findById(@PathVariable Long id) {
        return Result.success(medicineService.getById(id));
    }
    
    @PostMapping
    public Result<Medicine> save(@RequestBody Medicine medicine) {
        medicineService.save(medicine);
        return Result.success(medicine);
    }
    
    @PutMapping
    public Result<Medicine> update(@RequestBody Medicine medicine) {
        medicineService.updateById(medicine);
        return Result.success(medicine);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        medicineService.removeById(id);
        return Result.success();
    }
    
    @GetMapping("/search")
    public Result<List<Medicine>> search(@RequestParam String keyword) {
        return Result.success(medicineService.search(keyword));
    }
    
    @PostMapping("/import")
    public Result<List<Medicine>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.endsWith(".xlsx")) {
                return Result.error("只支持 .xlsx 格式的文件");
            }
            
            List<Medicine> medicines = medicineService.importFromExcel(file);
            return Result.success(medicines);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("导入失败: " + e.getMessage());
        }
    }
}
