package com.finance.dashboard.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTrendResponse {

    private int year;
    private int month;
    private String monthName;
    private String type;
    private BigDecimal total;
}
