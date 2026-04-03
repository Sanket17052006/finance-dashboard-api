package com.sanket.financedashboardapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBreakdownResponse {
    private String category;
    private String type;
    private Double total;
}