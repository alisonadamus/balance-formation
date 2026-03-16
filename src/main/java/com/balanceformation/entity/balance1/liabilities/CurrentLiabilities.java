package com.balanceformation.entity.balance1.liabilities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentLiabilities {

    private BigDecimal shortTermLoans;

    private BigDecimal payableLongTerm;

    private BigDecimal payableSuppliers;

    private BigDecimal payableBudget;

    private BigDecimal incomeTaxPayable;

    private BigDecimal insurancePayable;

    private BigDecimal salaryPayable;

    private BigDecimal currentProvisions;

    private BigDecimal deferredIncome;

    private BigDecimal otherCurrentLiabilities;

    @Transient
    public BigDecimal getTotal() {
        return nullSafe(shortTermLoans)
                .add(nullSafe(payableLongTerm))
                .add(nullSafe(payableSuppliers))
                .add(nullSafe(payableBudget))
                .add(nullSafe(incomeTaxPayable))
                .add(nullSafe(insurancePayable))
                .add(nullSafe(salaryPayable))
                .add(nullSafe(currentProvisions))
                .add(nullSafe(deferredIncome))
                .add(nullSafe(otherCurrentLiabilities));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}