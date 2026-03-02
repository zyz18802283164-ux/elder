package com.medication.service;

import com.medication.dto.DashboardStats;
import com.medication.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medication.entity.MedicationRecord;
import com.medication.entity.RiskAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DashboardService {
    @Autowired
    private MedicineMapper medicineMapper;
    
    @Autowired
    private PatientMapper patientMapper;
    
    @Autowired
    private MedicationRecordMapper medicationRecordMapper;
    
    @Autowired
    private RiskAlertMapper riskAlertMapper;
    
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setMedicineCount(medicineMapper.selectCount(null));
        stats.setPatientCount(patientMapper.selectCount(null));
        
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        LambdaQueryWrapper<MedicationRecord> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.between(MedicationRecord::getScheduledTime, startOfDay, endOfDay);
        stats.setTodayMedicationCount(medicationRecordMapper.selectCount(recordWrapper));
        
        LambdaQueryWrapper<RiskAlert> alertWrapper = new LambdaQueryWrapper<>();
        alertWrapper.eq(RiskAlert::getStatus, "PENDING");
        stats.setPendingAlertCount(riskAlertMapper.selectCount(alertWrapper));
        
        return stats;
    }
}
