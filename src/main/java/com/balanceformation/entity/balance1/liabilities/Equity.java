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
public class Equity {

    private BigDecimal registeredCapital;

    private BigDecimal revaluationCapital;

    private BigDecimal additionalCapital;

    private BigDecimal reserveCapital;

    private BigDecimal retainedEarnings;

    private BigDecimal unpaidCapital;

    private BigDecimal withdrawnCapital;

    @Transient
    public BigDecimal getTotal() {
        return nullSafe(registeredCapital)
                .add(nullSafe(revaluationCapital))
                .add(nullSafe(additionalCapital))
                .add(nullSafe(reserveCapital))
                .add(nullSafe(retainedEarnings))
                .subtract(nullSafe(unpaidCapital))
                .subtract(nullSafe(withdrawnCapital));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}