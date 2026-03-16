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
public class NonCurrentAssets {

    private BigDecimal intangibleInitialCost;
    private BigDecimal intangibleAmortization;

    private BigDecimal unfinishedInvestments;

    private BigDecimal fixedAssetsInitialCost;
    private BigDecimal fixedAssetsDepreciation;

    private BigDecimal investmentProperty;

    private BigDecimal longTermBiologicalAssets;

    private BigDecimal longTermFinancialInvestmentsEquity;

    private BigDecimal otherFinancialInvestments;

    private BigDecimal longTermReceivables;

    private BigDecimal deferredTaxAssets;

    private BigDecimal otherNonCurrentAssets;

    @Transient
    public BigDecimal getIntangibleAssets() {
        return nullSafe(intangibleInitialCost)
                .subtract(nullSafe(intangibleAmortization));
    }

    @Transient
    public BigDecimal getFixedAssets() {
        return nullSafe(fixedAssetsInitialCost)
                .subtract(nullSafe(fixedAssetsDepreciation));
    }

    @Transient
    public BigDecimal getTotal() {
        return getIntangibleAssets()
                .add(nullSafe(unfinishedInvestments))
                .add(getFixedAssets())
                .add(nullSafe(investmentProperty))
                .add(nullSafe(longTermBiologicalAssets))
                .add(nullSafe(longTermFinancialInvestmentsEquity))
                .add(nullSafe(otherFinancialInvestments))
                .add(nullSafe(longTermReceivables))
                .add(nullSafe(deferredTaxAssets))
                .add(nullSafe(otherNonCurrentAssets));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}