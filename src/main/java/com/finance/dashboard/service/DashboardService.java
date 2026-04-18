package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.CategorySummaryResponse;
import com.finance.dashboard.dto.response.DashboardSummaryResponse;
import com.finance.dashboard.dto.response.MonthlyTrendResponse;
import com.finance.dashboard.dto.response.RecentTransactionResponse;
import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.model.Transaction;
import com.finance.dashboard.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardSummaryResponse getFullSummary() {
        BigDecimal totalIncome = transactionRepository.getTotalByType(TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.getTotalByType(TransactionType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        Long totalTransactions = transactionRepository.countByIsDeletedFalse();
        Long incomeCount = transactionRepository.countByIsDeletedFalseAndType(TransactionType.INCOME);
        Long expenseCount = transactionRepository.countByIsDeletedFalseAndType(TransactionType.EXPENSE);

        List<Object[]> categoryRaw = transactionRepository.getCategoryWiseTotals();
        List<CategorySummaryResponse> categoryBreakdown = categoryRaw.stream()
                .map(row -> CategorySummaryResponse.builder()
                        .category((String) row[0])
                        .type(((TransactionType) row[1]).name())
                        .total((BigDecimal) row[2])
                        .build())
                .toList();

        List<Object[]> monthlyRaw = transactionRepository.getMonthlyTrends();
        List<MonthlyTrendResponse> monthlyTrends = monthlyRaw.stream()
                .map(row -> {
                    int year = ((Number) row[0]).intValue();
                    int month = ((Number) row[1]).intValue();
                    return MonthlyTrendResponse.builder()
                            .year(year)
                            .month(month)
                            .monthName(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                            .type(((TransactionType) row[2]).name())
                            .total((BigDecimal) row[3])
                            .build();
                })
                .toList();

        Pageable pageable = PageRequest.of(0, 10);
        List<Transaction> recent = transactionRepository.getRecentTransactions(pageable);
        List<RecentTransactionResponse> recentTransactions = recent.stream()
                .map(RecentTransactionResponse::fromEntity)
                .toList();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .totalTransactions(totalTransactions)
                .totalIncomeCount(incomeCount)
                .totalExpenseCount(expenseCount)
                .categoryBreakdown(categoryBreakdown)
                .monthlyTrends(monthlyTrends)
                .recentTransactions(recentTransactions)
                .build();
    }

    public Map<String, Object> getSummaryByDateRange(LocalDate startDate, LocalDate endDate) {
        BigDecimal income = transactionRepository.getTotalByTypeAndDateBetween(TransactionType.INCOME, startDate, endDate);
        BigDecimal expense = transactionRepository.getTotalByTypeAndDateBetween(TransactionType.EXPENSE, startDate, endDate);
        BigDecimal net = income.subtract(expense);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalIncome", income);
        response.put("totalExpense", expense);
        response.put("netBalance", net);

        return response;
    }

    public List<CategorySummaryResponse> getCategoryBreakdown() {
        List<Object[]> categoryRaw = transactionRepository.getCategoryWiseTotals();
        return categoryRaw.stream()
                .map(row -> CategorySummaryResponse.builder()
                        .category((String) row[0])
                        .type(((TransactionType) row[1]).name())
                        .total((BigDecimal) row[2])
                        .build())
                .toList();
    }

    public List<MonthlyTrendResponse> getMonthlyTrends() {
        List<Object[]> monthlyRaw = transactionRepository.getMonthlyTrends();
        return monthlyRaw.stream()
                .map(row -> {
                    int year = ((Number) row[0]).intValue();
                    int month = ((Number) row[1]).intValue();
                    return MonthlyTrendResponse.builder()
                            .year(year)
                            .month(month)
                            .monthName(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                            .type(((TransactionType) row[2]).name())
                            .total((BigDecimal) row[3])
                            .build();
                })
                .toList();
    }

    public List<RecentTransactionResponse> getRecentActivity(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Transaction> recent = transactionRepository.getRecentTransactions(pageable);
        return recent.stream().map(RecentTransactionResponse::fromEntity).toList();
    }
}
