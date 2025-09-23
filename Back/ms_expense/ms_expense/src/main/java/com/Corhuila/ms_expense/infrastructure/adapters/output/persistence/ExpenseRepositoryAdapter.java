package com.Corhuila.ms_expense.infrastructure.adapters.output.persistence;

import com.Corhuila.ms_expense.domain.model.Expense;
import com.Corhuila.ms_expense.domain.ports.ExpenseRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ExpenseRepositoryAdapter implements ExpenseRepositoryPort {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseRepositoryAdapter(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return expenseRepository.findAll(pageable);
    }

    @Override
    public List<Expense> findByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public Page<Expense> findByUserId(Long userId, Pageable pageable) {
        return expenseRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<Expense> findByUserIdAndExpenseCategoryId(Long userId, Long categoryId) {
        return expenseRepository.findByUserIdAndExpenseCategoryId(userId, categoryId);
    }

    @Override
    public List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
    }

    @Override
    public List<Expense> findByUserIdAndTagsContaining(Long userId, String tag) {
        return expenseRepository.findByUserIdAndTagsContaining(userId, tag);
    }

    @Override
    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return expenseRepository.existsById(id);
    }

    @Override
    public long countByUserId(Long userId) {
        return expenseRepository.countByUserId(userId);
    }
}