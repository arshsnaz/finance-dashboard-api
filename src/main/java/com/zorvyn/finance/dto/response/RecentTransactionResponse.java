package com.zorvyn.finance.dto.response;

import com.zorvyn.finance.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDate date;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;

    public static RecentTransactionResponse fromEntity(Transaction t) {
        return RecentTransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType().name())
                .category(t.getCategory())
                .date(t.getDate())
                .notes(t.getNotes())
                .createdBy(t.getCreatedBy().getName())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
