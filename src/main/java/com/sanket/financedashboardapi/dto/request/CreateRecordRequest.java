package com.sanket.financedashboardapi.dto.request;

import com.sanket.financedashboardapi.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateRecordRequest {

    @NotNull(message = "Enter Amount")
    @Min(value = 0, message = "Amount should be >=0")
    private Double amount;

    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private TransactionType type;

    @NotBlank(message = "Category is compulsory")
    private String category;

    @NotNull(message = "Date is compulsory")
    private LocalDate date;

    private String notes;
}