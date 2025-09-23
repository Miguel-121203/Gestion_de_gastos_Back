package com.Corhuila.ms_expense.domain.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class ExpenseUpdateRequest {
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Amount must have at most 17 integer digits and 2 decimal places")
    private BigDecimal amount;

    @Size(max = 3, message = "Currency must be 3 characters max")
    private String currency;

    private LocalDateTime expenseDate;

    @Size(max = 500, message = "Description must be 500 characters max")
    private String description;

    @Size(max = 255, message = "Tags must be 255 characters max")
    private String tags;

    @Positive(message = "Expense category ID must be positive")
    private Long expenseCategoryId;
}