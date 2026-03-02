package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.RiskAlert;
import com.medication.mapper.RiskAlertMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskAlertService extends ServiceImpl<RiskAlertMapper, RiskAlert> {
    
    public List<RiskAlert> findPending() {
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RiskAlert::getStatus, "PENDING")
               .orderByDesc(RiskAlert::getCreateTime);
        return this.list(wrapper);
    }
    
    public List<RiskAlert> findByPatientId(Long patientId) {
        LambdaQueryWrapper<RiskAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RiskAlert::getPatientId, patientId)
               .orderByDesc(RiskAlert::getCreateTime);
        return this.list(wrapper);
    }
}
