package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("emergency_help")
public class EmergencyHelp {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("patient_id")
    private Long patientId;
    
    @TableField("help_time")
    private LocalDateTime helpTime;
    
    private String status;
    private String handler;
    private String notes;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private Patient patient;
}
