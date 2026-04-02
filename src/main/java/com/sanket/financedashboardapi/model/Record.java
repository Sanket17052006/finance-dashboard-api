package com.sanket.financedashboardapi.model;

import com.sanket.financedashboardapi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "records")
public class Record {

    @Id
    private String id;

    private Double amount;

    private TransactionType type;

    private String category;

    private LocalDate date;

    private String notes;

    private String createdBy;

    @Builder.Default
    private Boolean isDeleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}