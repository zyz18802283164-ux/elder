package com.medication.controller;

import com.medication.dto.Result;
import com.medication.entity.MedicationRecord;
import com.medication.service.MedicationRecordService;
import com.medication.service.MedicineService;
import com.medication.service.PatientService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/medication-record")
@CrossOrigin
public class MedicationRecordController {
    @Autowired
    private MedicationRecordService medicationRecordService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicineService medicineService;
    
    @GetMapping
    public Result<List<MedicationRecord>> findAll() {
        List<MedicationRecord> list = medicationRecordService.list();
        for (MedicationRecord record : list) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
            if (record.getMedicineId() != null) {
                record.setMedicine(medicineService.getById(record.getMedicineId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<MedicationRecord> findById(@PathVariable Long id) {
        MedicationRecord record = medicationRecordService.getById(id);
        if (record != null) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
            if (record.getMedicineId() != null) {
                record.setMedicine(medicineService.getById(record.getMedicineId()));
            }
        }
        return Result.success(record);
    }
    
    @PostMapping
    public Result<MedicationRecord> save(@RequestBody MedicationRecord record) {
        medicationRecordService.save(record);
        return Result.success(record);
    }
    
    @GetMapping("/patient/{patientId}")
    public Result<List<MedicationRecord>> findByPatientId(@PathVariable Long patientId) {
        List<MedicationRecord> list = medicationRecordService.findByPatientId(patientId);
        for (MedicationRecord record : list) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
            if (record.getMedicineId() != null) {
                record.setMedicine(medicineService.getById(record.getMedicineId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/patient/{patientId}/today")
    public Result<List<MedicationRecord>> findTodayRecords(@PathVariable Long patientId) {
        List<MedicationRecord> list = medicationRecordService.findTodayRecords(patientId);
        for (MedicationRecord record : list) {
            if (record.getPatientId() != null) {
                record.setPatient(patientService.getById(record.getPatientId()));
            }
            if (record.getMedicineId() != null) {
                record.setMedicine(medicineService.getById(record.getMedicineId()));
            }
        }
        return Result.success(list);
    }
    
    @GetMapping("/patient/{patientId}/missed-count")
    public Result<Long> countMissed(@PathVariable Long patientId) {
        return Result.success(medicationRecordService.countMissed(patientId));
    }
    
    @PostMapping("/{id}/mark-taken")
    public Result<MedicationRecord> markAsTaken(@PathVariable Long id) {
        return Result.success(medicationRecordService.markAsTaken(id));
    }
    
    @PostMapping("/generate-today-records")
    public Result<String> generateTodayRecords() {
        medicationRecordService.generateTodayRecords();
        return Result.success("今日服药记录生成成功");
    }
    
    @GetMapping("/export")
    public void exportRecords(@RequestParam(required = false) Long patientId, HttpServletResponse response) {
        try {
            // 获取数据
            List<MedicationRecord> list;
            if (patientId != null) {
                list = medicationRecordService.findByPatientId(patientId);
            } else {
                list = medicationRecordService.list();
            }
            
            // 填充关联数据
            for (MedicationRecord record : list) {
                if (record.getPatientId() != null) {
                    record.setPatient(patientService.getById(record.getPatientId()));
                }
                if (record.getMedicineId() != null) {
                    record.setMedicine(medicineService.getById(record.getMedicineId()));
                }
            }
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("服药记录");
            
            // 创建标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            
            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"序号", "患者姓名", "患者年龄", "药品名称", "药品规格", "剂量", 
                               "计划服药时间", "实际服药时间", "服药状态", "备注"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (MedicationRecord record : list) {
                Row row = sheet.createRow(rowNum++);
                
                // 序号
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(rowNum - 1);
                cell0.setCellStyle(dataStyle);
                
                // 患者姓名
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(record.getPatient() != null ? record.getPatient().getName() : "");
                cell1.setCellStyle(dataStyle);
                
                // 患者年龄
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(record.getPatient() != null && record.getPatient().getAge() != null ? 
                                  record.getPatient().getAge() : 0);
                cell2.setCellStyle(dataStyle);
                
                // 药品名称
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(record.getMedicine() != null ? record.getMedicine().getName() : "");
                cell3.setCellStyle(dataStyle);
                
                // 药品规格
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(record.getMedicine() != null ? record.getMedicine().getSpecification() : "");
                cell4.setCellStyle(dataStyle);
                
                // 剂量
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(record.getMedicine() != null ? record.getMedicine().getDosage() : "");
                cell5.setCellStyle(dataStyle);
                
                // 计划服药时间
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(record.getScheduledTime() != null ? 
                                  sdf.format(java.sql.Timestamp.valueOf(record.getScheduledTime())) : "");
                cell6.setCellStyle(dataStyle);
                
                // 实际服药时间
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(record.getActualTime() != null ? 
                                  sdf.format(java.sql.Timestamp.valueOf(record.getActualTime())) : "");
                cell7.setCellStyle(dataStyle);
                
                // 服药状态
                Cell cell8 = row.createCell(8);
                String status = "";
                if ("TAKEN".equals(record.getStatus())) {
                    status = "已服用";
                } else if ("MISSED".equals(record.getStatus())) {
                    status = "漏服";
                } else if ("PENDING".equals(record.getStatus())) {
                    status = "待服用";
                } else {
                    status = record.getStatus();
                }
                cell8.setCellValue(status);
                cell8.setCellStyle(dataStyle);
                
                // 备注
                Cell cell9 = row.createCell(9);
                cell9.setCellValue("");
                cell9.setCellStyle(dataStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最小宽度
                int width = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(width, 3000));
            }
            
            // 设置响应头
            String fileName = "服药记录_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入响应
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}
