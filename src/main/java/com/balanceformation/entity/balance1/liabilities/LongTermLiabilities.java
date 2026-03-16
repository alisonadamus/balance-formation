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
public class LongTermLiabilities {

    private BigDecimal deferredTaxLiabilities;

    private BigDecimal longTermBankLoans;

    private BigDecimal otherLongTermLiabilities;

    private BigDecimal longTermProvisions;

    private BigDecimal targetedFinancing;

    @Transient
    public BigDecimal getTotal() {
        return nullSafe(deferredTaxLiabilities)
                .add(nullSafe(longTermBankLoans))
                .add(nullSafe(otherLongTermLiabilities))
                .add(nullSafe(longTermProvisions))
                .add(nullSafe(targetedFinancing));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}