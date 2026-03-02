package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.TreatmentPlan;
import com.medication.mapper.TreatmentPlanMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TreatmentPlanService extends ServiceImpl<TreatmentPlanMapper, TreatmentPlan> {
    
    public List<TreatmentPlan> findByPatientId(Long patientId) {
        LambdaQueryWrapper<TreatmentPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TreatmentPlan::getPatientId, patientId);
        return this.list(wrapper);
    }
    
    public List<TreatmentPlan> findActiveByPatientId(Long patientId) {
        LambdaQueryWrapper<TreatmentPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TreatmentPlan::getPatientId, patientId)
               .eq(TreatmentPlan::getStatus, "ACTIVE");
        return this.list(wrapper);
    }
}
