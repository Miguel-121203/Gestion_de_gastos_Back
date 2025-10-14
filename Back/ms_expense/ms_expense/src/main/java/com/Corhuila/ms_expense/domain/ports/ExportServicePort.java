package com.Corhuila.ms_expense.domain.ports;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ExportServicePort {
    ByteArrayOutputStream exportToPdf(List<ExpenseResponse> expenses);
    ByteArrayOutputStream exportToExcel(List<ExpenseResponse> expenses);
}
