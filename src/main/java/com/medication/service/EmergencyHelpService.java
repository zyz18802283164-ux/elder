package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.EmergencyHelp;
import com.medication.mapper.EmergencyHelpMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyHelpService extends ServiceImpl<EmergencyHelpMapper, EmergencyHelp> {
    
    public List<EmergencyHelp> findByPatientId(Long patientId) {
        LambdaQueryWrapper<EmergencyHelp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmergencyHelp::getPatientId, patientId)
               .orderByDesc(EmergencyHelp::getHelpTime);
        return this.list(wrapper);
    }
    
    public EmergencyHelp createHelp(Long patientId) {
        EmergencyHelp help = new EmergencyHelp();
        help.setPatientId(patientId);
        help.setHelpTime(LocalDateTime.now());
        help.setStatus("PENDING");
        this.save(help);
        return help;
    }
}
