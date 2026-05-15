package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("risk_alert")
public class RiskAlert {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("patient_id")
    private Long patientId;
    
    @TableField("risk_type")
    private String riskType;
    
    @TableField("risk_level")
    private String riskLevel;
    
    private String description;
    private String intervention;
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private Patient patient;
}
