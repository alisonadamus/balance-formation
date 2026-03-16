package com.balanceformation.entity.balance2;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareProfitabilityIndicators {

    private BigDecimal averageNumberOfShares; // Середньорічна кількість простих акцій
    private BigDecimal adjustedAverageNumberOfShares; // Скоригована середньорічна кількість простих акцій
    private BigDecimal netProfitPerShare; // Чистий прибуток (збиток) на одну просту акцію
    private BigDecimal adjustedNetProfitPerShare; // Скоригований чистий прибуток (збиток) на одну просту акцію
    private BigDecimal dividendsPerShare; // Дивіденди на одну просту акцію
}