package com.balanceformation.entity.balance2;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprehensiveIncome {

    private BigDecimal revaluationNonCurrentAssets; // Дооцінка (уцінка) необоротних активів
    private BigDecimal revaluationFinancialInstruments; // Дооцінка (уцінка) фінансових інструментів
    private BigDecimal accumulatedCurrencyDifferences; // Накопичені курсові різниці
    private BigDecimal shareOfOtherComprehensiveIncomeAssoc; // Частка іншого сукупного доходу асоційованих
    private BigDecimal otherComprehensiveIncome; // Інший сукупний дохід
    private BigDecimal otherComprehensiveIncomeBeforeTax; // Інший сукупний дохід до оподаткування
    private BigDecimal taxOnOtherComprehensiveIncome; // Податок на прибуток, пов’язаний з іншим сукупним доходом

    @Transient
    public BigDecimal getOtherComprehensiveIncomeAfterTax() {
        return nullSafe(otherComprehensiveIncomeBeforeTax).subtract(nullSafe(taxOnOtherComprehensiveIncome));
    }

    @Transient
    public BigDecimal getTotalComprehensiveIncome(BigDecimal netFinancialResult) {
        return netFinancialResult.add(getOtherComprehensiveIncomeAfterTax());
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}