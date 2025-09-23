package com.Corhuila.ms_expense.domain.model.dto;

import jakarta.validation.constraints.*;
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
public class ExpenseRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Amount must have at most 17 integer digits and 2 decimal places")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency must be 3 characters max")
    private String currency;

    @NotNull(message = "Expense date is required")
    private LocalDateTime expenseDate;

    @Size(max = 500, message = "Description must be 500 characters max")
    private String description;

    @Size(max = 255, message = "Tags must be 255 characters max")
    private String tags;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Expense category ID is required")
    @Positive(message = "Expense category ID must be positive")
    private Long expenseCategoryId;
}