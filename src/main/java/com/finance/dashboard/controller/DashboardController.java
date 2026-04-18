package com.finance.dashboard.controller;

import com.finance.dashboard.dto.response.ApiResponse;
import com.finance.dashboard.dto.response.CategorySummaryResponse;
import com.finance.dashboard.dto.response.DashboardSummaryResponse;
import com.finance.dashboard.dto.response.MonthlyTrendResponse;
import com.finance.dashboard.dto.response.RecentTransactionResponse;
import com.finance.dashboard.service.DashboardService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEWER','ROLE_ANALYST','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getFullSummary() {
        DashboardSummaryResponse result = dashboardService.getFullSummary();
        return ResponseEntity.ok(new ApiResponse(200, "Dashboard summary fetched", result));
    }

    @GetMapping("/summary/range")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEWER','ROLE_ANALYST','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getSummaryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> result = dashboardService.getSummaryByDateRange(startDate, endDate);
        return ResponseEntity.ok(new ApiResponse(200, "Range summary fetched", result));
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEWER','ROLE_ANALYST','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getCategoryBreakdown() {
        List<CategorySummaryResponse> result = dashboardService.getCategoryBreakdown();
        return ResponseEntity.ok(new ApiResponse(200, "Category breakdown fetched", result));
    }

    @GetMapping("/trends/monthly")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEWER','ROLE_ANALYST','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getMonthlyTrends() {
        List<MonthlyTrendResponse> result = dashboardService.getMonthlyTrends();
        return ResponseEntity.ok(new ApiResponse(200, "Monthly trends fetched", result));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEWER','ROLE_ANALYST','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getRecentActivity(@RequestParam(defaultValue = "10") int limit) {
        List<RecentTransactionResponse> result = dashboardService.getRecentActivity(limit);
        return ResponseEntity.ok(new ApiResponse(200, "Recent activity fetched", result));
    }
}
