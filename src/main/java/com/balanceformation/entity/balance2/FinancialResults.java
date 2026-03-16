package com.balanceformation.entity.balance2;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialResults {

    private BigDecimal netRevenue; // Чистий дохід від реалізації продукції
    private BigDecimal costOfGoodsSold; // Собівартість реалізованої продукції

    private BigDecimal otherOperatingIncome; // Інші операційні доходи
    private BigDecimal administrativeExpenses; // Адміністративні витрати
    private BigDecimal sellingExpenses; // Витрати на збут

    private BigDecimal incomeFromEquityParticipation; // Дохід від участі в капіталі
    private BigDecimal otherFinancialIncome; // Інші фінансові доходи
    private BigDecimal otherIncome; // Інші доходи
    private BigDecimal financialExpenses; // Фінансові витрати
    private BigDecimal lossesFromEquityParticipation; // Втрати від участі в капіталі
    private BigDecimal otherExpenses; // Інші витрати

    private BigDecimal incomeTaxExpenses; // Витрати (дохід) з податку на прибуток
    private BigDecimal profitFromDiscontinuedOperations; // Прибуток (збиток) від припиненої діяльності

    @Transient
    public BigDecimal getGrossProfit() {
        BigDecimal profit = netRevenue.subtract(costOfGoodsSold);
        return profit.max(BigDecimal.ZERO); // прибуток або нуль
    }

    @Transient
    public BigDecimal getGrossLoss() {
        BigDecimal loss = costOfGoodsSold.subtract(netRevenue);
        return loss.max(BigDecimal.ZERO);
    }

    @Transient
    public BigDecimal getOperatingProfit() {
        return getGrossProfit()
                .add(otherOperatingIncome != null ? otherOperatingIncome : BigDecimal.ZERO)
                .subtract(administrativeExpenses != null ? administrativeExpenses : BigDecimal.ZERO)
                .subtract(sellingExpenses != null ? sellingExpenses : BigDecimal.ZERO);
    }

    @Transient
    public BigDecimal getOperatingLoss() {
        BigDecimal loss = getOperatingProfit().negate();
        return loss.compareTo(BigDecimal.ZERO) > 0 ? loss : BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getNetFinancialResult() {
        BigDecimal profitBeforeTax = getOperatingProfit()
                .add(nullSafe(incomeFromEquityParticipation))
                .add(nullSafe(otherFinancialIncome))
                .add(nullSafe(otherIncome))
                .subtract(nullSafe(financialExpenses))
                .subtract(nullSafe(lossesFromEquityParticipation));

        BigDecimal net = profitBeforeTax.subtract(nullSafe(incomeTaxExpenses)).add(nullSafe(profitFromDiscontinuedOperations));
        return net;
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}