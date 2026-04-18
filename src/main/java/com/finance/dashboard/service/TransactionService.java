package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.TransactionRequest;
import com.finance.dashboard.dto.response.ApiResponse;
import com.finance.dashboard.dto.response.TransactionResponse;
import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.model.Transaction;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.TransactionRepository;
import com.finance.dashboard.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    public TransactionResponse createTransaction(TransactionRequest request, String currentUserEmail) {
        User user = getCurrentUser(currentUserEmail);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .isDeleted(false)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(savedTransaction);
    }

    public List<TransactionResponse> getAllTransactions(TransactionType type,
                                                        String category,
                                                        LocalDate startDate,
                                                        LocalDate endDate) {
        List<Transaction> transactions;

        if (type != null && category != null && startDate != null && endDate != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndTypeAndCategoryAndDateBetween(
                    type, category, startDate, endDate);
        } else if (type != null && category != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndTypeAndCategory(type, category);
        } else if (type != null && startDate != null && endDate != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndTypeAndDateBetween(type, startDate, endDate);
        } else if (category != null && startDate != null && endDate != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndCategoryAndDateBetween(category, startDate, endDate);
        } else if (type != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndType(type);
        } else if (category != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndCategory(category);
        } else if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByIsDeletedFalseAndDateBetween(startDate, endDate);
        } else {
            transactions = transactionRepository.findByIsDeletedFalse();
        }

        return transactions.stream().map(TransactionResponse::fromEntity).toList();
    }

    public Page<TransactionResponse> getAllTransactionsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Transaction> result = transactionRepository.findByIsDeletedFalse(pageable);
        return result.map(TransactionResponse::fromEntity);
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        return TransactionResponse.fromEntity(transaction);
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(saved);
    }

    public ApiResponse deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> !Boolean.TRUE.equals(t.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);

        return new ApiResponse(200, "Transaction deleted successfully", null);
    }
}
