package com.finance.dashboard.dto.request;

import com.finance.dashboard.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;
}
