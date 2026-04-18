package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.TransactionRequest;
import com.finance.dashboard.dto.response.ApiResponse;
import com.finance.dashboard.dto.response.TransactionResponse;
import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.service.TransactionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createTransaction(@Valid @RequestBody TransactionRequest request,
                                                         Authentication authentication) {
        String email = authentication.getName();
        TransactionResponse result = transactionService.createTransaction(request, email);
        return ResponseEntity.status(201).body(new ApiResponse(201, "Transaction created", result));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse> getAllTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponse> result = transactionService.getAllTransactions(type, category, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse(200, "Transactions fetched", result));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyAuthority('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse> getAllTransactionsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponse> result = transactionService.getAllTransactionsPaged(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "Transactions fetched", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VIEWER','ANALYST','ADMIN')")
    public ResponseEntity<ApiResponse> getTransactionById(@PathVariable Long id) {
        TransactionResponse result = transactionService.getTransactionById(id);
        return ResponseEntity.ok(new ApiResponse(200, "Transaction fetched", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateTransaction(@PathVariable Long id,
                                                         @Valid @RequestBody TransactionRequest request) {
        TransactionResponse result = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(new ApiResponse(200, "Transaction updated", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteTransaction(@PathVariable Long id) {
        ApiResponse result = transactionService.deleteTransaction(id);
        return ResponseEntity.ok(result);
    }
}
