package com.Corhuila.ms_expense.infrastructure.adapters.input.rest;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseRequest;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseUpdateRequest;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseServicePort expenseServicePort;

    @Autowired
    public ExpenseController(ExpenseServicePort expenseServicePort) {
        this.expenseServicePort = expenseServicePort;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        logger.info("Solicitud recibida para crear gasto - Usuario: {}, Monto: {}, Categoría: {}",
                request.getUserId(), request.getAmount(), request.getExpenseCategoryId());

        ExpenseResponse response = expenseServicePort.createExpense(request);

        logger.info("Gasto creado exitosamente con ID: {}", response.getExpenseId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseUpdateRequest request) {
        logger.info("Solicitud recibida para actualizar gasto con ID: {}", id);

        ExpenseResponse response = expenseServicePort.updateExpense(id, request);

        logger.info("Respuesta enviada para gasto actualizado con ID: {}", response.getExpenseId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar gasto con ID: {}", id);

        expenseServicePort.deleteExpense(id);

        logger.info("Respuesta enviada - Gasto eliminado lógicamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        logger.info("Solicitud recibida para obtener todos los gastos");

        List<ExpenseResponse> responses = expenseServicePort.getAllExpenses();

        logger.info("Respuesta enviada con {} gastos", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        logger.info("Solicitud recibida para filtrar gastos por fecha - Usuario: {}, Rango: {} a {}",
                userId, startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndDateRange(userId, startDateTime, endDateTime);

        logger.info("Respuesta enviada con {} gastos filtrados por fecha para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        logger.info("Solicitud recibida para filtrar gastos por categoría - Usuario: {}, Categoría: {}",
                userId, categoryId);

        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndCategory(userId, categoryId);

        logger.info("Respuesta enviada con {} gastos filtrados por categoría para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/amount-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndAmountRange(
            @PathVariable Long userId,
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        logger.info("Solicitud recibida para filtrar gastos por monto - Usuario: {}, Rango: {} a {}",
                userId, minAmount, maxAmount);

        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndAmountRange(userId, minAmount, maxAmount);

        logger.info("Respuesta enviada con {} gastos filtrados por monto para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }
}