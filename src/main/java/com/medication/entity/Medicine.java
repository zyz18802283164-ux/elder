package com.medication.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("medicine")
public class Medicine {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String specification;
    private String dosage;
    private String indication;
    private String contraindication;
    
    @TableField("adverse_reaction")
    private String adverseReaction;
    
    private String interaction;
    
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
