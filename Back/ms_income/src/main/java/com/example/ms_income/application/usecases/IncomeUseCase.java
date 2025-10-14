package com.example.ms_income.application.usecases;

import com.example.ms_income.domain.model.Income;
import com.example.ms_income.domain.model.dto.IncomeResponse;
import com.example.ms_income.domain.model.dto.IncomeRequest;
import com.example.ms_income.domain.model.dto.IncomeUpdateRequest;
import com.example.ms_income.domain.ports.IncomeRepositoryPort;
import com.example.ms_income.domain.ports.IncomeServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeUseCase implements IncomeServicePort {

    private static final Logger logger = LoggerFactory.getLogger(IncomeUseCase.class);
    private final IncomeRepositoryPort incomeRepositoryPort;

    @Autowired
    public IncomeUseCase(IncomeRepositoryPort incomeRepositoryPort) {
        this.incomeRepositoryPort = incomeRepositoryPort;
    }

    @Override
    public IncomeResponse createIncome(IncomeRequest request) {
        logger.info("Iniciando creación de ingreso para usuario: {} con monto: {}", request.getUserId(), request.getAmount());

        Income income = Income.builder()
                .incomeDate(request.getIncomeDate().atStartOfDay())
                .amount(request.getAmount())
                .incomeCategoryId(request.getIncomeCategoryId())
                .description(request.getDescription())
                .additionalNotes(request.getAdditionalNotes())
                .userId(request.getUserId())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Income savedIncome = incomeRepositoryPort.save(income);
        logger.info("Ingreso creado exitosamente con ID: {} para usuario: {}", savedIncome.getIncomeId(), savedIncome.getUserId());

        return mapToResponse(savedIncome);
    }

    @Override
    public List<IncomeResponse> getAllIncomes() {
        logger.info("Obteniendo todos los ingresos activos");

        List<IncomeResponse> incomes = incomeRepositoryPort.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} ingresos activos", incomes.size());
        return incomes;
    }

    @Override
    public List<IncomeResponse> getIncomesByUserIdAndCategory(Long userId, Long categoryId) {
        logger.info("Filtrando ingresos por usuario: {} y categoría: {}", userId, categoryId);

        List<IncomeResponse> incomes = incomeRepositoryPort.findByUserIdAndIncomeCategoryIdAndActiveTrue(userId, categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} ingresos para usuario: {} y categoría: {}", incomes.size(), userId, categoryId);
        return incomes;
    }

    @Override
    public List<IncomeResponse> getIncomesByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Filtrando ingresos por usuario: {} en el rango de fechas: {} a {}", userId, startDate.toLocalDate(), endDate.toLocalDate());

        List<IncomeResponse> incomes = incomeRepositoryPort.findByUserIdAndIncomeDateBetweenAndActiveTrue(userId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} ingresos para usuario: {} en el rango de fechas especificado", incomes.size(), userId);
        return incomes;
    }

    @Override
    public List<IncomeResponse> getIncomesByUserIdAndAmountRange(Long userId, BigDecimal minAmount, BigDecimal maxAmount) {
        logger.info("Filtrando ingresos por usuario: {} en el rango de montos: {} a {}", userId, minAmount, maxAmount);

        List<IncomeResponse> incomes = incomeRepositoryPort.findByUserIdAndAmountBetweenAndActiveTrue(userId, minAmount, maxAmount).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} ingresos para usuario: {} en el rango de montos especificado", incomes.size(), userId);
        return incomes;
    }

    @Override
    public IncomeResponse updateIncome(Long id, IncomeUpdateRequest request) {
        logger.info("Iniciando actualización de ingreso con ID: {}", id);

        Income income = incomeRepositoryPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.error("No se encontró ingreso activo con ID: {}", id);
                    return new RuntimeException("Ingreso no encontrado con ID: " + id);
                });

        if (request.getIncomeDate() != null) {
            logger.info("Actualizando fecha de {} a {}", income.getIncomeDate().toLocalDate(), request.getIncomeDate());
            income.setIncomeDate(request.getIncomeDate().atStartOfDay());
        }
        if (request.getAmount() != null) {
            logger.info("Actualizando monto de {} a {}", income.getAmount(), request.getAmount());
            income.setAmount(request.getAmount());
        }
        if (request.getIncomeCategoryId() != null) {
            logger.info("Actualizando categoría de {} a {}", income.getIncomeCategoryId(), request.getIncomeCategoryId());
            income.setIncomeCategoryId(request.getIncomeCategoryId());
        }
        if (request.getDescription() != null) {
            logger.info("Actualizando descripción del ingreso");
            income.setDescription(request.getDescription());
        }
        if (request.getAdditionalNotes() != null) {
            logger.info("Actualizando notas adicionales del ingreso");
            income.setAdditionalNotes(request.getAdditionalNotes());
        }
        income.setUpdatedAt(LocalDateTime.now());

        Income updatedIncome = incomeRepositoryPort.save(income);
        logger.info("Ingreso actualizado exitosamente con ID: {}", updatedIncome.getIncomeId());

        return mapToResponse(updatedIncome);
    }

    @Override
    public void deleteIncome(Long id) {
        logger.info("Iniciando eliminación lógica de ingreso con ID: {}", id);

        Income income = incomeRepositoryPort.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.error("No se encontró ingreso activo con ID: {} para eliminar", id);
                    return new RuntimeException("Ingreso no encontrado con ID: " + id);
                });

        income.setActive(false);
        income.setUpdatedAt(LocalDateTime.now());
        incomeRepositoryPort.save(income);

        logger.info("Ingreso eliminado lógicamente exitosamente con ID: {}", id);
    }

    private IncomeResponse mapToResponse(Income income) {
        return IncomeResponse.builder()
                .incomeId(income.getIncomeId())
                .incomeDate(income.getIncomeDate())
                .amount(income.getAmount())
                .incomeCategoryId(income.getIncomeCategoryId())
                .description(income.getDescription())
                .additionalNotes(income.getAdditionalNotes())
                .userId(income.getUserId())
                .active(income.getActive())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .build();
    }
}
