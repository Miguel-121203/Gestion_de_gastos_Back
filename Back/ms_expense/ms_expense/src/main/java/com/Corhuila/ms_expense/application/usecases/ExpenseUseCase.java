package com.Corhuila.ms_expense.application.usecases;

import com.Corhuila.ms_expense.domain.model.Expense;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseRequest;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseUpdateRequest;
import com.Corhuila.ms_expense.domain.ports.ExpenseRepositoryPort;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseUseCase implements ExpenseServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseUseCase.class);
    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Autowired
    public ExpenseUseCase(ExpenseRepositoryPort expenseRepositoryPort) {
        this.expenseRepositoryPort = expenseRepositoryPort;
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        logger.info("Iniciando creación de gasto para usuario: {} con monto: {}", request.getUserId(), request.getAmount());

        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .expenseCategoryId(request.getExpenseCategoryId())
                .expenseDate(request.getExpenseDate().atStartOfDay())
                .description(request.getDescription())
                .userId(request.getUserId())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Expense savedExpense = expenseRepositoryPort.save(expense);
        logger.info("Gasto creado exitosamente con ID: {} para usuario: {}", savedExpense.getExpenseId(), savedExpense.getUserId());

        return mapToResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        logger.info("Obteniendo todos los gastos activos");

        List<ExpenseResponse> expenses = expenseRepositoryPort.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} gastos activos", expenses.size());
        return expenses;
    }



    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndCategory(Long userId, Long categoryId) {
        logger.info("Filtrando gastos por usuario: {} y categoría: {}", userId, categoryId);

        List<ExpenseResponse> expenses = expenseRepositoryPort.findByUserIdAndExpenseCategoryIdAndActiveTrue(userId, categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} gastos para usuario: {} y categoría: {}", expenses.size(), userId, categoryId);
        return expenses;
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Filtrando gastos por usuario: {} en el rango de fechas: {} a {}", userId, startDate.toLocalDate(), endDate.toLocalDate());

        List<ExpenseResponse> expenses = expenseRepositoryPort.findByUserIdAndExpenseDateBetweenAndActiveTrue(userId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} gastos para usuario: {} en el rango de fechas especificado", expenses.size(), userId);
        return expenses;
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserIdAndAmountRange(Long userId, BigDecimal minAmount, BigDecimal maxAmount) {
        logger.info("Filtrando gastos por usuario: {} en el rango de montos: {} a {}", userId, minAmount, maxAmount);

        List<ExpenseResponse> expenses = expenseRepositoryPort.findByUserIdAndAmountBetweenAndActiveTrue(userId, minAmount, maxAmount).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} gastos para usuario: {} en el rango de montos especificado", expenses.size(), userId);
        return expenses;
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseUpdateRequest request) {
        logger.info("Iniciando actualización de gasto con ID: {}", id);

        Expense expense = expenseRepositoryPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.error("No se encontró gasto activo con ID: {}", id);
                    return new RuntimeException("Gasto no encontrado con ID: " + id);
                });

        if (request.getAmount() != null) {
            logger.info("Actualizando monto de {} a {}", expense.getAmount(), request.getAmount());
            expense.setAmount(request.getAmount());
        }
        if (request.getExpenseCategoryId() != null) {
            logger.info("Actualizando categoría de {} a {}", expense.getExpenseCategoryId(), request.getExpenseCategoryId());
            expense.setExpenseCategoryId(request.getExpenseCategoryId());
        }
        if (request.getExpenseDate() != null) {
            logger.info("Actualizando fecha de {} a {}", expense.getExpenseDate().toLocalDate(), request.getExpenseDate());
            expense.setExpenseDate(request.getExpenseDate().atStartOfDay());
        }
        if (request.getDescription() != null) {
            logger.info("Actualizando descripción del gasto");
            expense.setDescription(request.getDescription());
        }
        expense.setUpdatedAt(LocalDateTime.now());

        Expense updatedExpense = expenseRepositoryPort.save(expense);
        logger.info("Gasto actualizado exitosamente con ID: {}", updatedExpense.getExpenseId());

        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        logger.info("Iniciando eliminación lógica de gasto con ID: {}", id);

        Expense expense = expenseRepositoryPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.error("No se encontró gasto activo con ID: {} para eliminar", id);
                    return new RuntimeException("Gasto no encontrado con ID: " + id);
                });

        expense.setActive(false);
        expense.setUpdatedAt(LocalDateTime.now());
        expenseRepositoryPort.save(expense);

        logger.info("Gasto eliminado lógicamente exitosamente con ID: {}", id);
    }


    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .amount(expense.getAmount())
                .expenseCategoryId(expense.getExpenseCategoryId())
                .expenseDate(expense.getExpenseDate())
                .description(expense.getDescription())
                .userId(expense.getUserId())
                .active(expense.getActive())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}