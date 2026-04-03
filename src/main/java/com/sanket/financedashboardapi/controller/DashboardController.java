package com.sanket.financedashboardapi.controller;

import com.sanket.financedashboardapi.dto.response.CategoryBreakdownResponse;
import com.sanket.financedashboardapi.dto.response.DashboardSummaryResponse;
import com.sanket.financedashboardapi.dto.response.MonthlyTrendResponse;
import com.sanket.financedashboardapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/breakdown")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<CategoryBreakdownResponse>> getBreakdown() {
        return ResponseEntity.ok(dashboardService.getBreakdown());
    }

    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<MonthlyTrendResponse>> getTrends() {
        return ResponseEntity.ok(dashboardService.getTrends());
    }
}