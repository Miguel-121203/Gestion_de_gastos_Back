package com.Corhuila.ms_expense.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime expenseDate;
    private String description;
    private String tags;
    private Long userId;
    private Long expenseCategoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}