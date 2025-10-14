package com.Corhuila.ms_expense.infrastructure.adapters.input.rest;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.ports.ExportServicePort;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/export")
@CrossOrigin(origins = "*")
public class ExportController {

    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);
    private final ExpenseServicePort expenseServicePort;
    private final ExportServicePort exportServicePort;

    @Autowired
    public ExportController(ExpenseServicePort expenseServicePort, ExportServicePort exportServicePort) {
        this.expenseServicePort = expenseServicePort;
        this.exportServicePort = exportServicePort;
    }

    /**
     * Exportar gastos de un usuario por mes en formato PDF o Excel
     *
     * @param userId ID del usuario
     * @param year Año (ejemplo: 2024)
     * @param month Mes (1-12, donde 1=enero, 2=febrero, etc.)
     * @param format Formato de exportación: "pdf" o "excel"
     * @return Archivo PDF o Excel con los gastos del mes
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<byte[]> exportExpensesByMonth(
            @PathVariable Long userId,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "pdf") String format) {

        logger.info("Solicitud recibida para exportar gastos - Usuario: {}, Año: {}, Mes: {}, Formato: {}",
                userId, year, month, format);

        // Validar mes
        if (month < 1 || month > 12) {
            logger.error("Mes inválido: {}. Debe estar entre 1 y 12", month);
            return ResponseEntity.badRequest().build();
        }

        // Calcular primer y último día del mes
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        LocalDateTime startDateTime = firstDay.atStartOfDay();
        LocalDateTime endDateTime = lastDay.atTime(23, 59, 59);

        logger.info("Rango de fechas calculado: {} a {}", startDateTime, endDateTime);

        // Obtener gastos del mes
        List<ExpenseResponse> expenses = expenseServicePort.getExpensesByUserIdAndDateRange(
                userId, startDateTime, endDateTime);

        if (expenses.isEmpty()) {
            logger.warn("No se encontraron gastos para usuario: {} en {}/{}", userId, month, year);
            return ResponseEntity.noContent().build();
        }

        try {
            ByteArrayOutputStream outputStream;
            String contentType;
            String extension;

            if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
                logger.info("Generando archivo Excel con {} gastos", expenses.size());
                outputStream = exportServicePort.exportToExcel(expenses);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                extension = ".xlsx";
            } else {
                logger.info("Generando archivo PDF con {} gastos", expenses.size());
                outputStream = exportServicePort.exportToPdf(expenses);
                contentType = "application/pdf";
                extension = ".pdf";
            }

            // Nombre del mes en español
            String[] monthNames = {
                "enero", "febrero", "marzo", "abril", "mayo", "junio",
                "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
            };
            String monthName = monthNames[month - 1];

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("gastos_usuario_%d_%s_%d_%s%s",
                    userId, monthName, year, timestamp, extension);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("Archivo exportado exitosamente: {} con {} gastos", filename, expenses.size());
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al exportar gastos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
