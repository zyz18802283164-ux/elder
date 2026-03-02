package com.medication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.medication.mapper")
public class MedicationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicationApplication.class, args);
    }
}
