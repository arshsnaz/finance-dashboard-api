package com.finance.dashboard.repository;

import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByIsDeletedFalse();

    Page<Transaction> findByIsDeletedFalse(Pageable pageable);

    List<Transaction> findByIsDeletedFalseAndType(TransactionType type);

    List<Transaction> findByIsDeletedFalseAndCategory(String category);

    List<Transaction> findByIsDeletedFalseAndDateBetween(LocalDate startDate, LocalDate endDate);

    List<Transaction> findByIsDeletedFalseAndTypeAndCategory(TransactionType type, String category);

    List<Transaction> findByIsDeletedFalseAndTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByIsDeletedFalseAndCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByIsDeletedFalseAndTypeAndCategoryAndDateBetween(TransactionType type,
                                                                           String category,
                                                                           LocalDate startDate,
                                                                           LocalDate endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.isDeleted = false AND t.type = :type")
    BigDecimal getTotalByType(@Param("type") TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.isDeleted = false AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalByTypeAndDateBetween(@Param("type") TransactionType type,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    @Query("SELECT t.category, t.type, SUM(t.amount) FROM Transaction t WHERE t.isDeleted = false GROUP BY t.category, t.type ORDER BY SUM(t.amount) DESC")
    List<Object[]> getCategoryWiseTotals();

    @Query("SELECT YEAR(t.date), MONTH(t.date), t.type, SUM(t.amount) FROM Transaction t WHERE t.isDeleted = false GROUP BY YEAR(t.date), MONTH(t.date), t.type ORDER BY YEAR(t.date) DESC, MONTH(t.date) DESC")
    List<Object[]> getMonthlyTrends();

    @Query("SELECT t FROM Transaction t WHERE t.isDeleted = false ORDER BY t.createdAt DESC")
    List<Transaction> getRecentTransactions(Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.isDeleted = false AND t.type = :type")
    Long countByType(@Param("type") TransactionType type);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.isDeleted = false")
    Long countAll();

    long countByIsDeletedFalse();

    long countByIsDeletedFalseAndType(TransactionType type);
}
