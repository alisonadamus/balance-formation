package com.balanceformation.entity.balance1.liabilities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liabilities {

    @Embedded
    private Equity equity;

    @Embedded
    private LongTermLiabilities longTermLiabilities;

    @Embedded
    private CurrentLiabilities currentLiabilities;

    @Transient
    public BigDecimal getTotal() {
        return equity.getTotal()
                .add(longTermLiabilities.getTotal())
                .add(currentLiabilities.getTotal());
    }

}