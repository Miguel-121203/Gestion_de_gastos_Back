package com.example.ms_income.domain.model.dto;

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
public class IncomeResponse {
    private Long incomeId;
    private LocalDateTime incomeDate;
    private BigDecimal amount;
    private Long incomeCategoryId;
    private String description;
    private String additionalNotes;
    private Long userId;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
