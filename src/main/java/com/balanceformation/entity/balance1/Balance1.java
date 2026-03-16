package com.balanceformation.entity.balance1;

import com.balanceformation.entity.balance1.assets.Assets;
import com.balanceformation.entity.balance1.liabilities.Liabilities;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Assets assets;

    @Embedded
    private Liabilities liabilities;

    @Transient
    public BigDecimal getAssetsTotal() {
        return assets.getTotal();
    }

    @Transient
    public BigDecimal getLiabilitiesTotal() {
        return liabilities.getTotal();
    }

    public boolean isBalanced() {
        BigDecimal assets = getAssetsTotal();
        BigDecimal liabilities = getLiabilitiesTotal();

        if (assets == null || liabilities == null) return false;

        // Обчислюємо відносну різницю
        BigDecimal difference = assets.subtract(liabilities).abs(); // абсолютна різниця
        BigDecimal tolerance = assets.multiply(BigDecimal.valueOf(0.10)); // 10% від активів

        return difference.compareTo(tolerance) <= 0; // true, якщо різниця <= 10%
    }

}