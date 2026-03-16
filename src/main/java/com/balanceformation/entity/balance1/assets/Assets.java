package com.balanceformation.entity.balance1.assets;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assets {

    @Embedded
    private NonCurrentAssets nonCurrentAssets;

    @Embedded
    private CurrentAssets currentAssets;

    private BigDecimal nonCurrentAssetsForSale;

    @Transient
    public BigDecimal getTotal() {
        return nonCurrentAssets.getTotal()
                .add(currentAssets.getTotal())
                .add(nullSafe(nonCurrentAssetsForSale));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}