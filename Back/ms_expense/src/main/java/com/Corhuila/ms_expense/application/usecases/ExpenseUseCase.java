package com.Corhuila.ms_expense.application.usecases;

import com.Corhuila.ms_expense.domain.model.Expense;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseRequest;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseUpdateRequest;
import com.Corhuila.ms_expense.domain.ports.ExpenseRepositoryPort;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseUseCase implements ExpenseServicePort {

    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Autowired
    public ExpenseUseCase(ExpenseRepositoryPort expenseRepositoryPort) {
        this.expenseRepositoryPort = expenseRepositoryPort;
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .expenseDate(request.getExpenseDate())
                .description(request.getDescription())
                .tags(request.getTags())
                .userId(request.getUserId())
                .expenseCategoryId(request.getExpenseCategoryId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Expense savedExpense = expenseRepositoryPort.save(expense);
        return mapToResponse(savedExpense);
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        return expenseRepositoryPort.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepositoryPort.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ExpenseResponse> getAllExpenses(Pageable pageable) {
        return expenseRepositoryPort.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserId(Long userId) {
        return expenseRepositoryPort.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ExpenseResponse> getExpensesByUserId(Long userId, Pageable pageable) {
        return expenseRepositoryPort.findByUserId(userId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndCategory(Long userId, Long categoryId) {
        return expenseRepositoryPort.findByUserIdAndExpenseCategoryId(userId, categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepositoryPort.findByUserIdAndExpenseDateBetween(userId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndTag(Long userId, String tag) {
        return expenseRepositoryPort.findByUserIdAndTagsContaining(userId, tag).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseUpdateRequest request) {
        Expense expense = expenseRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            expense.setCurrency(request.getCurrency());
        }
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate());
        }
        if (request.getDescription() != null) {
            expense.setDescription(request.getDescription());
        }
        if (request.getTags() != null) {
            expense.setTags(request.getTags());
        }
        if (request.getExpenseCategoryId() != null) {
            expense.setExpenseCategoryId(request.getExpenseCategoryId());
        }
        expense.setUpdatedAt(LocalDateTime.now());

        Expense updatedExpense = expenseRepositoryPort.save(expense);
        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepositoryPort.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepositoryPort.deleteById(id);
    }

    @Override
    public long countExpensesByUserId(Long userId) {
        return expenseRepositoryPort.countByUserId(userId);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .amount(expense.getAmount())
                .currency(expense.getCurrency())
                .expenseDate(expense.getExpenseDate())
                .description(expense.getDescription())
                .tags(expense.getTags())
                .userId(expense.getUserId())
                .expenseCategoryId(expense.getExpenseCategoryId())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}