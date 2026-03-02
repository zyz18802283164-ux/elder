CREATE DATABASE IF NOT EXISTS medication_system DEFAULT CHARACTER SET utf8mb4;

USE medication_system;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL COMMENT '角色: ADMIN/PATIENT',
    patient_id BIGINT COMMENT '关联患者ID（患者角色时使用）',
    phone VARCHAR(20) COMMENT '电话',
    status INT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除'
);

-- 插入默认管理员账号
INSERT INTO user (username, password, real_name, role, status) VALUES 
('admin', '123456', '系统管理员', 'ADMIN', 1),
('doctor', '123456', '医护人员', 'ADMIN', 1);

-- 药品信息表
CREATE TABLE IF NOT EXISTS medicine (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    specification VARCHAR(100) COMMENT '规格',
    dosage VARCHAR(50) COMMENT '剂量',
    indication TEXT COMMENT '适应症',
    contraindication TEXT COMMENT '禁忌症',
    adverse_reaction TEXT COMMENT '不良反应',
    interaction TEXT COMMENT '相互作用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 患者信息表
CREATE TABLE IF NOT EXISTS patient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    age INT COMMENT '年龄',
    gender VARCHAR(10) COMMENT '性别',
    phone VARCHAR(20) COMMENT '电话',
    address VARCHAR(200) COMMENT '地址',
    chronic_disease TEXT COMMENT '慢性病史',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 疗程计划表
CREATE TABLE IF NOT EXISTS treatment_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    dosage VARCHAR(50) COMMENT '剂量',
    frequency VARCHAR(50) COMMENT '频率',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

-- 服药记录表
CREATE TABLE IF NOT EXISTS medication_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    plan_id BIGINT COMMENT '疗程计划ID',
    scheduled_time DATETIME COMMENT '计划服药时间',
    actual_time DATETIME COMMENT '实际服药时间',
    status VARCHAR(20) COMMENT '状态: TAKEN/MISSED/PARTIAL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (medicine_id) REFERENCES medicine(id)
);

-- 健康档案表
CREATE TABLE IF NOT EXISTS health_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    blood_pressure VARCHAR(20) COMMENT '血压',
    blood_sugar VARCHAR(20) COMMENT '血糖',
    record_date DATE COMMENT '记录日期',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

-- 紧急联系人表
CREATE TABLE IF NOT EXISTS emergency_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    name VARCHAR(50) COMMENT '联系人姓名',
    relationship VARCHAR(20) COMMENT '关系',
    phone VARCHAR(20) COMMENT '电话',
    priority INT DEFAULT 1 COMMENT '优先级',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

-- 风险预警表
CREATE TABLE IF NOT EXISTS risk_alert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    risk_type VARCHAR(50) COMMENT '风险类型',
    risk_level VARCHAR(20) COMMENT '风险等级',
    description TEXT COMMENT '描述',
    intervention TEXT COMMENT '干预建议',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

-- 紧急求助记录表
CREATE TABLE IF NOT EXISTS emergency_help (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    help_time DATETIME COMMENT '求助时间',
    status VARCHAR(20) COMMENT '状态',
    handler VARCHAR(50) COMMENT '处理人',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);
