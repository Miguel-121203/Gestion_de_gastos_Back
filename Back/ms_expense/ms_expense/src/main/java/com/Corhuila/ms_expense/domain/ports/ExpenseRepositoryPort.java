package com.Corhuila.ms_expense.domain.ports;

import com.Corhuila.ms_expense.domain.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryPort {
    Expense save(Expense expense);
    Optional<Expense> findById(Long id);
    List<Expense> findAll();
    Page<Expense> findAll(Pageable pageable);
    List<Expense> findByUserId(Long userId);
    Page<Expense> findByUserId(Long userId, Pageable pageable);
    List<Expense> findByUserIdAndExpenseCategoryId(Long userId, Long categoryId);
    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Expense> findByUserIdAndTagsContaining(Long userId, String tag);
    void deleteById(Long id);
    boolean existsById(Long id);
    long countByUserId(Long userId);
}