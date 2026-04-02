package com.zorvyn.finance.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private Long totalTransactions;
    private Long totalIncomeCount;
    private Long totalExpenseCount;
    private List<CategorySummaryResponse> categoryBreakdown;
    private List<MonthlyTrendResponse> monthlyTrends;
    private List<RecentTransactionResponse> recentTransactions;
}
