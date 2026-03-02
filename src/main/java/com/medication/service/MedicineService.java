package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.Medicine;
import com.medication.mapper.MedicineMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineService extends ServiceImpl<MedicineMapper, Medicine> {
    
    public List<Medicine> search(String keyword) {
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Medicine::getName, keyword)
               .or()
               .like(Medicine::getSpecification, keyword);
        return this.list(wrapper);
    }
    
    public List<Medicine> importFromExcel(MultipartFile file) throws Exception {
        List<Medicine> medicines = new ArrayList<>();
        InputStream is = null;
        Workbook workbook = null;
        
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                // 检查是否为空行
                String name = getCellValue(row.getCell(0));
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }
                
                Medicine medicine = new Medicine();
                medicine.setName(name);
                medicine.setSpecification(getCellValue(row.getCell(1)));
                medicine.setDosage(getCellValue(row.getCell(2)));
                medicine.setIndication(getCellValue(row.getCell(3)));
                medicine.setContraindication(getCellValue(row.getCell(4)));
                medicine.setAdverseReaction(getCellValue(row.getCell(5)));
                medicine.setInteraction(getCellValue(row.getCell(6)));
                
                this.save(medicine);
                medicines.add(medicine);
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (is != null) {
                is.close();
            }
        }
        
        return medicines;
    }
    
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        try {
            switch (cell.getCellType()) {
                case STRING: 
                    return cell.getStringCellValue().trim();
                case NUMERIC: 
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    }
                    return String.valueOf((long) cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default: 
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
