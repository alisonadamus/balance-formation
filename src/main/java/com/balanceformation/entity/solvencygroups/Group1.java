package com.balanceformation.entity.solvencygroups;

import com.balanceformation.entity.balance1.Balance1;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
public class Group1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "balance_1_id")
    private Balance1 balance1;
    private BigDecimal immediateLiquidityRatio;
    private BigDecimal currentLiquidityRatio;
    private BigDecimal overallLiquidityRatio;
    private BigDecimal financialIndependenceRatio;
    private BigDecimal equityManeuverabilityRatio;

    public Group1(Balance1 balance1) {
        this.balance1 = balance1;
        setImmediateLiquidityRatio();
        setCurrentLiquidityRatio();
        setOverallLiquidityRatio();
        setFinancialIndependenceRatio();
        setEquityManeuverabilityRatio();
    }

    public Group1() {

    }

    public void setImmediateLiquidityRatio() {
        this.immediateLiquidityRatio = balance1.getAssets().getCurrentAssets().getCurrentFinancialInvestments()
                .add(balance1.getAssets().getCurrentAssets().getCash())
                .divide(balance1.getLiabilities().getCurrentLiabilities().getTotal(), 4, RoundingMode.HALF_UP);
    }

    public void setCurrentLiquidityRatio() {
        this.currentLiquidityRatio = balance1.getAssets().getCurrentAssets().getReceivablesProducts()
                .add(balance1.getAssets().getCurrentAssets().getOtherReceivables())
                .add(balance1.getAssets().getCurrentAssets().getCurrentFinancialInvestments())
                .add(balance1.getAssets().getCurrentAssets().getCash())
                .divide(balance1.getLiabilities().getCurrentLiabilities().getTotal(), 4, RoundingMode.HALF_UP);
    }

    public void setOverallLiquidityRatio() {
        this.overallLiquidityRatio = balance1.getAssets().getCurrentAssets().getTotal()
                .divide(balance1.getLiabilities().getCurrentLiabilities().getTotal(), 4, RoundingMode.HALF_UP);
    }

    public void setFinancialIndependenceRatio() {
        this.financialIndependenceRatio = balance1.getLiabilities().getLongTermLiabilities().getTargetedFinancing()
                .add(balance1.getLiabilities().getLongTermLiabilities().getTotal())
                .add(balance1.getLiabilities().getCurrentLiabilities().getTotal())
                .divide(balance1.getLiabilities().getEquity().getTotal(), 4, RoundingMode.HALF_UP);
    }

    public void setEquityManeuverabilityRatio() {
        this.equityManeuverabilityRatio = balance1.getLiabilities().getEquity().getTotal()
                .subtract(balance1.getAssets().getNonCurrentAssets().getTotal())
                .divide(balance1.getLiabilities().getEquity().getTotal(), 4, RoundingMode.HALF_UP);
    }
}
