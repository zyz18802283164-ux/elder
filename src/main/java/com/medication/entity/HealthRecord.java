package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("health_record")
public class HealthRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("patient_id")
    private Long patientId;
    
    @TableField("blood_pressure")
    private String bloodPressure;
    
    @TableField("blood_sugar")
    private String bloodSugar;
    
    @TableField("record_date")
    private LocalDate recordDate;
    
    private String notes;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private Patient patient;
}
