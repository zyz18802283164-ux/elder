package com.medication.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medication.entity.Patient;
import com.medication.mapper.PatientMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService extends ServiceImpl<PatientMapper, Patient> {
    
    public List<Patient> search(String keyword) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Patient::getName, keyword)
               .or()
               .like(Patient::getPhone, keyword);
        return this.list(wrapper);
    }
}
