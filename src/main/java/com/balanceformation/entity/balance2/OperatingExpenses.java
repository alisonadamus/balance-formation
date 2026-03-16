package com.balanceformation.entity.balance2;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingExpenses {

    private BigDecimal materialCosts; // Матеріальні затрати
    private BigDecimal payrollExpenses; // Витрати на оплату праці
    private BigDecimal socialSecurityContributions; // Відрахування на соціальні заходи
    private BigDecimal depreciation; // Амортизація
    private BigDecimal otherOperatingExpenses; // Інші операційні витрати

    @Transient
    public BigDecimal getTotal() {
        return nullSafe(materialCosts)
                .add(nullSafe(payrollExpenses))
                .add(nullSafe(socialSecurityContributions))
                .add(nullSafe(depreciation))
                .add(nullSafe(otherOperatingExpenses));
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}