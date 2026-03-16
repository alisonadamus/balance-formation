package com.balanceformation.entity.balance1.assets;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentAssets {

    private BigDecimal inventories;

    private BigDecimal biologicalAssetsCurrent;

    private BigDecimal receivablesProducts;

    private BigDecimal advancesIssued;

    private BigDecimal receivablesBudget;

    private BigDecimal incomeTaxReceivable;

    private BigDecimal otherReceivables;

    private BigDecimal currentFinancialInvestments;

    private BigDecimal cash;

    private BigDecimal prepaidExpenses;

    private BigDecimal otherCurrentAssets;

    @Transient
    public BigDecimal getTotal() {
        return nullSafe(inventories)
                .add(nullSafe(biologicalAssetsCurrent))
                .add(nullSafe(receivablesProducts))
                .add(nullSafe(advancesIssued))
                .add(nullSafe(receivablesBudget))
                .add(nullSafe(incomeTaxReceivable))
                .add(nullSafe(otherReceivables))
                .add(nullSafe(currentFinancialInvestments))
                .add(nullSafe(cash))
                .add(nullSafe(prepaidExpenses))
                .add(nullSafe(otherCurrentAssets));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}
