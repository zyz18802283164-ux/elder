package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("medication_record")
public class MedicationRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("patient_id")
    private Long patientId;
    
    @TableField("medicine_id")
    private Long medicineId;
    
    @TableField("plan_id")
    private Long planId;
    
    @TableField("scheduled_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime scheduledTime;
    
    @TableField("actual_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime actualTime;
    
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private Patient patient;
    
    @TableField(exist = false)
    private Medicine medicine;
}
