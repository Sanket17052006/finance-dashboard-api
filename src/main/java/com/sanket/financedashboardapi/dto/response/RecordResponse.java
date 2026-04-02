package com.sanket.financedashboardapi.dto.response;

import com.sanket.financedashboardapi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponse {
    private String id;
    private Double amount;
    private TransactionType type;
    private String category;
    private LocalDate date;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // isDeleted not shown as they are soft deleted (they are not visible to users).
}