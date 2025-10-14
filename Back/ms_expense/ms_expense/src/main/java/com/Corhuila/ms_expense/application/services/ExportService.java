package com.Corhuila.ms_expense.application.services;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.ports.ExportServicePort;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService implements ExportServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ExportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ByteArrayOutputStream exportToPdf(List<ExpenseResponse> expenses) {
        logger.info("Iniciando generación de PDF con {} gastos", expenses.size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("Reporte de Gastos")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Información adicional
            Paragraph info = new Paragraph(String.format("Total de gastos: %d | Generado: %s",
                    expenses.size(), java.time.LocalDateTime.now().format(DATE_FORMATTER)))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(info);

            // Tabla
            float[] columnWidths = {1, 2, 2, 2, 3, 2, 1};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            String[] headers = {"ID", "Monto", "Categoría", "Fecha", "Descripción", "Usuario", "Estado"};
            for (String header : headers) {
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(header).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }

            // Datos
            BigDecimal total = BigDecimal.ZERO;
            for (ExpenseResponse expense : expenses) {
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(expense.getExpenseId()))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("$" + expense.getAmount().toString()))
                        .setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(expense.getExpenseCategoryId()))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(expense.getExpenseDate().format(DATE_FORMATTER))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(
                        expense.getDescription() != null ? expense.getDescription() : "N/A")));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(expense.getUserId()))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(expense.getActive() ? "Activo" : "Inactivo"))
                        .setTextAlignment(TextAlignment.CENTER));

                total = total.add(expense.getAmount());
            }

            // Fila de total
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("TOTAL").setBold()));
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("$" + total.toString()).setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW));
            table.addCell(new com.itextpdf.layout.element.Cell(1, 5).add(new Paragraph("")));

            document.add(table);
            document.close();

            logger.info("PDF generado exitosamente con {} gastos. Total: ${}", expenses.size(), total);
            return outputStream;

        } catch (Exception e) {
            logger.error("Error al generar PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    @Override
    public ByteArrayOutputStream exportToExcel(List<ExpenseResponse> expenses) {
        logger.info("Iniciando generación de Excel con {} gastos", expenses.size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Gastos");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Monto", "Categoría ID", "Fecha de Gasto", "Descripción", "Usuario ID", "Estado", "Fecha Creación", "Última Actualización"};

            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            BigDecimal total = BigDecimal.ZERO;

            for (ExpenseResponse expense : expenses) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(expense.getExpenseId());

                org.apache.poi.ss.usermodel.Cell amountCell = row.createCell(1);
                amountCell.setCellValue(expense.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);

                row.createCell(2).setCellValue(expense.getExpenseCategoryId());

                org.apache.poi.ss.usermodel.Cell expenseDateCell = row.createCell(3);
                expenseDateCell.setCellValue(expense.getExpenseDate().format(DATE_FORMATTER));
                expenseDateCell.setCellStyle(dateStyle);

                row.createCell(4).setCellValue(
                        expense.getDescription() != null ? expense.getDescription() : "N/A");

                row.createCell(5).setCellValue(expense.getUserId());
                row.createCell(6).setCellValue(expense.getActive() ? "Activo" : "Inactivo");

                org.apache.poi.ss.usermodel.Cell createdAtCell = row.createCell(7);
                createdAtCell.setCellValue(expense.getCreatedAt().format(DATE_FORMATTER));
                createdAtCell.setCellStyle(dateStyle);

                org.apache.poi.ss.usermodel.Cell updatedAtCell = row.createCell(8);
                updatedAtCell.setCellValue(expense.getUpdatedAt().format(DATE_FORMATTER));
                updatedAtCell.setCellStyle(dateStyle);

                total = total.add(expense.getAmount());
            }

            // Fila de total
            Row totalRow = sheet.createRow(rowNum);
            org.apache.poi.ss.usermodel.Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("TOTAL");
            totalLabelCell.setCellStyle(totalStyle);

            org.apache.poi.ss.usermodel.Cell totalAmountCell = totalRow.createCell(1);
            totalAmountCell.setCellValue(total.doubleValue());
            totalAmountCell.setCellStyle(totalStyle);

            // Auto-size columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            logger.info("Excel generado exitosamente con {} gastos. Total: ${}", expenses.size(), total);
            return outputStream;

        } catch (IOException e) {
            logger.error("Error al generar Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar Excel", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }
}
