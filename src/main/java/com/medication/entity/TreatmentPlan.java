package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("treatment_plan")
public class TreatmentPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("patient_id")
    private Long patientId;
    
    @TableField("medicine_id")
    private Long medicineId;
    
    private String dosage;
    private String frequency;
    
    @TableField("start_date")
    private LocalDate startDate;
    
    @TableField("end_date")
    private LocalDate endDate;
    
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime createTime;
    
    @TableField(exist = false)
    private Patient patient;
    
    @TableField(exist = false)
    private Medicine medicine;
}
