package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.MedicationRecord;
import com.medication.entity.TreatmentPlan;
import com.medication.mapper.MedicationRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MedicationRecordService extends ServiceImpl<MedicationRecordMapper, MedicationRecord> {
    
    @Autowired
    private TreatmentPlanService treatmentPlanService;
    
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
    
    /**
     * 根据活动疗程自动生成今日服药记录
     * 这个方法应该每天自动执行一次（可以通过定时任务实现）
     */
    public void generateTodayRecords() {
        LocalDate today = LocalDate.now();
        
        // 获取所有活动疗程
        LambdaQueryWrapper<TreatmentPlan> planWrapper = new LambdaQueryWrapper<>();
        planWrapper.eq(TreatmentPlan::getStatus, "ACTIVE");
        List<TreatmentPlan> activePlans = treatmentPlanService.list(planWrapper);
        
        for (TreatmentPlan plan : activePlans) {
            // 检查疗程是否在今天的日期范围内
            if (plan.getStartDate() != null && plan.getEndDate() != null) {
                LocalDate startDate = plan.getStartDate();
                LocalDate endDate = plan.getEndDate();
                
                if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
                    // 检查今天是否已经有该疗程的记录
                    LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
                    LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);
                    
                    LambdaQueryWrapper<MedicationRecord> recordWrapper = new LambdaQueryWrapper<>();
                    recordWrapper.eq(MedicationRecord::getPatientId, plan.getPatientId())
                               .eq(MedicationRecord::getMedicineId, plan.getMedicineId())
                               .eq(MedicationRecord::getPlanId, plan.getId())
                               .between(MedicationRecord::getScheduledTime, startOfDay, endOfDay);
                    
                    long existingCount = this.count(recordWrapper);
                    
                    if (existingCount == 0) {
                        // 根据频率生成服药记录
                        generateRecordsForPlan(plan, today);
                    }
                }
            }
        }
    }
    
    /**
     * 根据疗程计划和日期生成服药记录
     */
    private void generateRecordsForPlan(TreatmentPlan plan, LocalDate date) {
        String frequency = plan.getFrequency();
        if (frequency == null || frequency.isEmpty()) {
            frequency = "每日1次"; // 默认频率
        }
        
        // 解析频率，生成对应的服药时间
        // 简化处理：根据常见频率生成记录
        if (frequency.contains("每日3次") || frequency.contains("一日三次")) {
            createRecord(plan, date, 8, 0);  // 早上8点
            createRecord(plan, date, 12, 0); // 中午12点
            createRecord(plan, date, 18, 0); // 晚上6点
        } else if (frequency.contains("每日2次") || frequency.contains("一日两次")) {
            createRecord(plan, date, 8, 0);  // 早上8点
            createRecord(plan, date, 20, 0); // 晚上8点
        } else if (frequency.contains("每日1次") || frequency.contains("一日一次") || frequency.contains("每天1次")) {
            createRecord(plan, date, 8, 0);  // 早上8点
        } else if (frequency.contains("每日4次") || frequency.contains("一日四次")) {
            createRecord(plan, date, 8, 0);  // 早上8点
            createRecord(plan, date, 12, 0); // 中午12点
            createRecord(plan, date, 16, 0); // 下午4点
            createRecord(plan, date, 20, 0); // 晚上8点
        } else {
            // 默认每日一次
            createRecord(plan, date, 8, 0);
        }
    }
    
    /**
     * 创建单条服药记录
     */
    private void createRecord(TreatmentPlan plan, LocalDate date, int hour, int minute) {
        MedicationRecord record = new MedicationRecord();
        record.setPatientId(plan.getPatientId());
        record.setMedicineId(plan.getMedicineId());
        record.setPlanId(plan.getId());
        record.setScheduledTime(LocalDateTime.of(date, LocalTime.of(hour, minute)));
        record.setStatus("PENDING");
        this.save(record);
    }
}
