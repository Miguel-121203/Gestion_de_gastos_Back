package com.Corhuila.ms_expense.infrastructure.adapters.output.persistence;

import com.Corhuila.ms_expense.domain.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    Page<Expense> findByUserId(Long userId, Pageable pageable);

    List<Expense> findByUserIdAndExpenseCategoryId(Long userId, Long expenseCategoryId);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findByUserIdAndExpenseDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.tags LIKE %:tag%")
    List<Expense> findByUserIdAndTagsContaining(@Param("userId") Long userId, @Param("tag") String tag);

    long countByUserId(Long userId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId")
    Double sumAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId AND e.expenseCategoryId = :categoryId")
    Double sumAmountByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}