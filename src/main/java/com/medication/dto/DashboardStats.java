package com.medication.dto;

import lombok.Data;

@Data
public class DashboardStats {
    private Long medicineCount;
    private Long patientCount;
    private Long todayMedicationCount;
    private Long pendingAlertCount;
}
