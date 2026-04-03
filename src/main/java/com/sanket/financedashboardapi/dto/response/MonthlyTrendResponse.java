package com.sanket.financedashboardapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTrendResponse {
    private int year;
    private int month;
    private Double totalIncome;
    private Double totalExpenses;
    private Double net;
}