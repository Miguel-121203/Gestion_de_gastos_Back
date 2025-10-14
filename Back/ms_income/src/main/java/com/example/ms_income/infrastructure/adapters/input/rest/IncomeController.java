package com.example.ms_income.infrastructure.adapters.input.rest;

import com.example.ms_income.domain.model.dto.IncomeRequest;
import com.example.ms_income.domain.model.dto.IncomeResponse;
import com.example.ms_income.domain.model.dto.IncomeUpdateRequest;
import com.example.ms_income.domain.ports.IncomeServicePort;
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
@RequestMapping("/api/v1/incomes")
@CrossOrigin(origins = "*")
public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);
    private final IncomeServicePort incomeServicePort;

    @Autowired
    public IncomeController(IncomeServicePort incomeServicePort) {
        this.incomeServicePort = incomeServicePort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> createIncome(@Valid @RequestBody IncomeRequest request) {
        logger.info("Solicitud recibida para crear ingreso - Usuario: {}, Monto: {}, Categoría: {}",
                request.getUserId(), request.getAmount(), request.getIncomeCategoryId());

        IncomeResponse response = incomeServicePort.createIncome(request);

        logger.info("Ingreso creado exitosamente con ID: {}", response.getIncomeId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody IncomeUpdateRequest request) {
        logger.info("Solicitud recibida para actualizar ingreso con ID: {}", id);

        IncomeResponse response = incomeServicePort.updateIncome(id, request);

        logger.info("Respuesta enviada para ingreso actualizado con ID: {}", response.getIncomeId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar ingreso con ID: {}", id);

        incomeServicePort.deleteIncome(id);

        logger.info("Respuesta enviada - Ingreso eliminado lógicamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncomes() {
        logger.info("Solicitud recibida para obtener todos los ingresos");

        List<IncomeResponse> responses = incomeServicePort.getAllIncomes();

        logger.info("Respuesta enviada con {} ingresos", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<IncomeResponse>> getIncomesByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        logger.info("Solicitud recibida para filtrar ingresos por fecha - Usuario: {}, Rango: {} a {}",
                userId, startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<IncomeResponse> responses = incomeServicePort.getIncomesByUserIdAndDateRange(userId, startDateTime, endDateTime);

        logger.info("Respuesta enviada con {} ingresos filtrados por fecha para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<IncomeResponse>> getIncomesByUserIdAndCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        logger.info("Solicitud recibida para filtrar ingresos por categoría - Usuario: {}, Categoría: {}",
                userId, categoryId);

        List<IncomeResponse> responses = incomeServicePort.getIncomesByUserIdAndCategory(userId, categoryId);

        logger.info("Respuesta enviada con {} ingresos filtrados por categoría para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/amount-range")
    public ResponseEntity<List<IncomeResponse>> getIncomesByUserIdAndAmountRange(
            @PathVariable Long userId,
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        logger.info("Solicitud recibida para filtrar ingresos por monto - Usuario: {}, Rango: {} a {}",
                userId, minAmount, maxAmount);

        List<IncomeResponse> responses = incomeServicePort.getIncomesByUserIdAndAmountRange(userId, minAmount, maxAmount);

        logger.info("Respuesta enviada con {} ingresos filtrados por monto para usuario: {}", responses.size(), userId);
        return ResponseEntity.ok(responses);
    }
}
