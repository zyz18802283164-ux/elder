package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.HealthRecord;
import com.medication.mapper.HealthRecordMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HealthRecordService extends ServiceImpl<HealthRecordMapper, HealthRecord> {
    
    public List<HealthRecord> findByPatientId(Long patientId) {
        LambdaQueryWrapper<HealthRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthRecord::getPatientId, patientId)
               .orderByDesc(HealthRecord::getRecordDate);
        return this.list(wrapper);
    }
}
