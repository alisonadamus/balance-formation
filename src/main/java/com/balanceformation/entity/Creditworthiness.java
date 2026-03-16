package com.balanceformation.entity;

import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.balance2.Balance2;
import com.balanceformation.entity.solvencygroups.Group2;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Entity
@Data
@Getter
public class Creditworthiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "balance_1_id")
    private Balance1 balance1;

    @OneToOne
    @JoinColumn(name = "balance_2_id")
    private Balance2 balance2;

    @OneToOne
    @JoinColumn(name = "group_2_id")
    private Group2 group2;

    private BigDecimal k1, muK1;
    private BigDecimal k2, muK2;
    private BigDecimal k3, muK3;
    private BigDecimal k4, muK4;
    private BigDecimal k5, muK5;
    private BigDecimal k6, muK6;
    private BigDecimal k7, muK7;
    private BigDecimal k8, muK8;
    private BigDecimal k9, muK9;
    private BigDecimal k10, muK10;
    private BigDecimal k11, muK11;

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);
    private BigDecimal years;
    private BigDecimal K;
    private BigDecimal M;

    @PrePersist
    @PreUpdate
    public void calculateAll() {
        k1 = div(balance1.getAssets().getCurrentAssets().getCurrentFinancialInvestments()
                .add(balance1.getAssets().getCurrentAssets().getCash()),
                balance1.getLiabilities().getCurrentLiabilities().getTotal());
        muK1 = muK1(k1);

        k2 = div(balance1.getAssets().getCurrentAssets().getReceivablesBudget()
                .add(balance1.getAssets().getCurrentAssets().getOtherReceivables())
                .add(balance1.getAssets().getCurrentAssets().getCurrentFinancialInvestments())
                .add(balance1.getAssets().getCurrentAssets().getCash()),
                balance1.getLiabilities().getCurrentLiabilities().getTotal());
        muK2 = muK2(k2);

        k3 = div(balance1.getAssets().getCurrentAssets().getTotal(),
                balance1.getLiabilities().getCurrentLiabilities().getTotal());
        muK3 = muK3(k3);

        k4 = div(balance1.getLiabilities().getLongTermLiabilities().getTargetedFinancing()
                .add(balance1.getLiabilities().getLongTermLiabilities().getTotal())
                .add(balance1.getLiabilities().getCurrentLiabilities().getTotal()),
                balance1.getLiabilities().getEquity().getTotal());
        muK4 = muK4(k4);

        k5 = div(balance1.getLiabilities().getEquity().getTotal()
                .subtract(balance1.getAssets().getNonCurrentAssets().getTotal()),
                balance1.getLiabilities().getEquity().getTotal());
        muK5 = muK5(k5);

        k6 = div(balance2.getFinancialResults().getNetFinancialResult(),
                balance2.getOperatingExpenses().getMaterialCosts()
                        .add(balance2.getOperatingExpenses().getPayrollExpenses())
                        .add(balance2.getOperatingExpenses().getSocialSecurityContributions())
                        .add(balance2.getOperatingExpenses().getDepreciation())
                        .add(balance2.getOperatingExpenses().getOtherOperatingExpenses()));
        muK6 = muK6(k6);

        k7 = BigDecimal.valueOf(group2.getPastYearsPerformanceRatio().getValue());
        muK7 = muK7(k7);

        k8 = group2.getLargestPreviouslyRepaidLoanRatio();
        muK8 = muK8(k8);

        k9 = years;
        muK9 = muK9(k9);

        k10 = div(K, group2.getLargestPreviouslyRepaidLoanRatio());
        muK10 = muK10(k10);

        k11 = div(M, group2.getLargestPreviouslyRepaidLoanRatio());
        muK11 = muK11(k11);
    }

    private BigDecimal div(BigDecimal a, BigDecimal b) {
        return (b == null || b.compareTo(BigDecimal.ZERO) == 0)
                ? BigDecimal.ZERO
                : a.divide(b, MC);
    }

    private BigDecimal sq(BigDecimal x) {
        return x.multiply(x);
    }

    private BigDecimal bd(double d) {
        return new BigDecimal(String.valueOf(d), MC);
    }

    private BigDecimal muK1(BigDecimal k) {
        if (k.compareTo(bd(0.2)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.225)) <= 0)
            return bd(32).multiply(sq(k.subtract(BigDecimal.ONE)));
        if (k.compareTo(bd(0.25)) < 0)
            return BigDecimal.ONE.subtract(
                    bd(50).multiply(sq(BigDecimal.ONE.subtract(k.multiply(bd(4)))))
            );
        return BigDecimal.ONE;
    }

    private BigDecimal muK2(BigDecimal k) {
        if (k.compareTo(bd(0.5)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.75)) <= 0)
            return bd(2).multiply(sq(k.multiply(bd(2)).subtract(BigDecimal.ONE)));
        if (k.compareTo(BigDecimal.ONE) < 0)
            return BigDecimal.ONE.subtract(
                    bd(8).multiply(sq(BigDecimal.ONE.subtract(k)))
            );
        return BigDecimal.ONE;
    }

    private BigDecimal muK3(BigDecimal k) {
        if (k.compareTo(BigDecimal.ONE) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(1.75)) <= 0)
            return bd(4).multiply(k.subtract(BigDecimal.ONE)).divide(bd(3), MC);
        if (k.compareTo(bd(2.5)) < 0)
            return bd(10).subtract(bd(4).multiply(k)).divide(bd(3), MC);
        return BigDecimal.ZERO;
    }

    private BigDecimal muK4(BigDecimal k) {
        if (k.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(BigDecimal.ONE) <= 0) return k;
        if (k.compareTo(bd(2)) < 0) return bd(2).subtract(k);
        return BigDecimal.ZERO;
    }

    private BigDecimal muK5(BigDecimal k) {
        if (k.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.5)) <= 0) return k.multiply(bd(2));
        if (k.compareTo(BigDecimal.ONE) < 0) return bd(2).subtract(k.multiply(bd(2)));
        return BigDecimal.ZERO;
    }

    private BigDecimal muK6(BigDecimal k) {
        if (k.compareTo(bd(0.05)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.075)) <= 0)
            return bd(2).multiply(sq(k.multiply(bd(20)).subtract(BigDecimal.ONE)));
        if (k.compareTo(bd(0.1)) < 0)
            return BigDecimal.ONE.subtract(
                    bd(8).multiply(sq(BigDecimal.ONE.subtract(k.multiply(bd(10)))))
            );
        return BigDecimal.ONE;
    }

    private BigDecimal muK7(BigDecimal k) {
        if (k.compareTo(BigDecimal.ONE) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(3)) <= 0)
            return sq(k.subtract(BigDecimal.ONE)).divide(bd(8), MC);
        if (k.compareTo(bd(5)) < 0)
            return BigDecimal.ONE.subtract(
                    sq(bd(5).subtract(k)).divide(bd(8), MC)
            );
        return BigDecimal.ONE;
    }

    private BigDecimal muK8(BigDecimal k) {
        if (k.compareTo(bd(0.8)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.9)) <= 0)
            return bd(2).multiply(sq(k.multiply(bd(5)).subtract(bd(4))));
        if (k.compareTo(BigDecimal.ONE) < 0)
            return BigDecimal.ONE.subtract(
                    bd(2).multiply(sq(bd(5).subtract(k.multiply(bd(5)))))
            );
        return BigDecimal.ONE;
    }

    private BigDecimal muK9(BigDecimal k) {
        return muK7(k);
    }

    private BigDecimal muK10(BigDecimal k) {
        if (k.compareTo(bd(0.2)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.4)) < 0) return k.multiply(bd(5)).subtract(BigDecimal.ONE);
        return BigDecimal.ONE;
    }

    private BigDecimal muK11(BigDecimal k) {
        if (k.compareTo(bd(0.25)) <= 0) return BigDecimal.ZERO;
        if (k.compareTo(bd(0.625)) <= 0)
            return bd(2).multiply(sq(k.multiply(bd(4)).subtract(BigDecimal.ONE)))
                    .divide(bd(9), MC);
        if (k.compareTo(BigDecimal.ONE) < 0)
            return BigDecimal.ONE.subtract(
                    bd(32).multiply(sq(BigDecimal.ONE.subtract(k))).divide(bd(9), MC)
            );
        return BigDecimal.ONE;
    }

}
