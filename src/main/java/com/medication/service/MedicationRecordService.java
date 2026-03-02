package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.MedicationRecord;
import com.medication.mapper.MedicationRecordMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MedicationRecordService extends ServiceImpl<MedicationRecordMapper, MedicationRecord> {
    
    public List<MedicationRecord> findByPatientId(Long patientId) {
        LambdaQueryWrapper<MedicationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicationRecord::getPatientId, patientId)
               .orderByDesc(MedicationRecord::getScheduledTime);
        return this.list(wrapper);
    }
    
    public List<MedicationRecord> findTodayRecords(Long patientId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        LambdaQueryWrapper<MedicationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicationRecord::getPatientId, patientId)
               .between(MedicationRecord::getScheduledTime, startOfDay, endOfDay)
               .orderByAsc(MedicationRecord::getScheduledTime);
        return this.list(wrapper);
    }
    
    public Long countMissed(Long patientId) {
        LambdaQueryWrapper<MedicationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicationRecord::getPatientId, patientId)
               .eq(MedicationRecord::getStatus, "MISSED");
        return this.count(wrapper);
    }
    
    public MedicationRecord markAsTaken(Long recordId) {
        MedicationRecord record = this.getById(recordId);
        if (record != null) {
            record.setStatus("TAKEN");
            record.setActualTime(LocalDateTime.now());
            this.updateById(record);
        }
        return record;
    }
}
