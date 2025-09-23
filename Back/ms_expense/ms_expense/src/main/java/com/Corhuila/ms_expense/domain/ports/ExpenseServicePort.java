package com.Corhuila.ms_expense.domain.ports;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseRequest;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseServicePort {
    ExpenseResponse createExpense(ExpenseRequest request);
    ExpenseResponse getExpenseById(Long id);
    List<ExpenseResponse> getAllExpenses();
    Page<ExpenseResponse> getAllExpenses(Pageable pageable);
    List<ExpenseResponse> getExpensesByUserId(Long userId);
    Page<ExpenseResponse> getExpensesByUserId(Long userId, Pageable pageable);
    List<ExpenseResponse> getExpensesByUserIdAndCategory(Long userId, Long categoryId);
    List<ExpenseResponse> getExpensesByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<ExpenseResponse> getExpensesByUserIdAndTag(Long userId, String tag);
    ExpenseResponse updateExpense(Long id, ExpenseUpdateRequest request);
    void deleteExpense(Long id);
    long countExpensesByUserId(Long userId);
}