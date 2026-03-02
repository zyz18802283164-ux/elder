package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.EmergencyContact;
import com.medication.mapper.EmergencyContactMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmergencyContactService extends ServiceImpl<EmergencyContactMapper, EmergencyContact> {
    
    public List<EmergencyContact> findByPatientId(Long patientId) {
        LambdaQueryWrapper<EmergencyContact> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmergencyContact::getPatientId, patientId)
               .orderByAsc(EmergencyContact::getPriority);
        return this.list(wrapper);
    }
}
