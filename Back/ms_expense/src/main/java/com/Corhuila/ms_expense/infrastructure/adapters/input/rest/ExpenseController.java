package com.Corhuila.ms_expense.infrastructure.adapters.input.rest;

import com.Corhuila.ms_expense.domain.model.dto.ExpenseRequest;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseResponse;
import com.Corhuila.ms_expense.domain.model.dto.ExpenseUpdateRequest;
import com.Corhuila.ms_expense.domain.ports.ExpenseServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseServicePort expenseServicePort;

    @Autowired
    public ExpenseController(ExpenseServicePort expenseServicePort) {
        this.expenseServicePort = expenseServicePort;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseServicePort.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse response = expenseServicePort.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> responses = expenseServicePort.getAllExpenses();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ExpenseResponse>> getAllExpensesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "expenseDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ExpenseResponse> responses = expenseServicePort.getAllExpenses(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserId(@PathVariable Long userId) {
        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByUserIdPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "expenseDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ExpenseResponse> responses = expenseServicePort.getExpensesByUserId(userId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndCategory(userId, categoryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/tag/{tag}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserIdAndTag(
            @PathVariable Long userId,
            @PathVariable String tag) {
        List<ExpenseResponse> responses = expenseServicePort.getExpensesByUserIdAndTag(userId, tag);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseUpdateRequest request) {
        ExpenseResponse response = expenseServicePort.updateExpense(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseServicePort.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countExpensesByUserId(@PathVariable Long userId) {
        long count = expenseServicePort.countExpensesByUserId(userId);
        return ResponseEntity.ok(count);
    }
}