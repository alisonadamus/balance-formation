package com.balanceformation.entity.balance2;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FinancialResults financialResults;

    @Embedded
    private ComprehensiveIncome comprehensiveIncome;

    @Embedded
    private OperatingExpenses operatingExpenses;

    @Embedded
    private ShareProfitabilityIndicators shareProfitabilityIndicators;

    @Transient
    public BigDecimal getNetProfit() {
        return financialResults.getNetFinancialResult();
    }

}