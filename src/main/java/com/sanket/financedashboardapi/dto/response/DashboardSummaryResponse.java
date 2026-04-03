package com.sanket.financedashboardapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private Double totalIncome;
    private Double totalExpenses;
    private Double netBalance;
    private Long totalRecords;
}