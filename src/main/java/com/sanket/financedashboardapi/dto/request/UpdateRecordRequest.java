package com.sanket.financedashboardapi.dto.request;

import com.sanket.financedashboardapi.enums.TransactionType;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRecordRequest {

    @Min(value = 1, message = "Amount >0")
    private Double amount;

    private TransactionType type;

    private String category;

    private LocalDate date;

    private String notes;
}