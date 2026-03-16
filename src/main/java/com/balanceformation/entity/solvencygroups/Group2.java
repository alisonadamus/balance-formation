package com.balanceformation.entity.solvencygroups;

import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.balance2.Balance2;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
public class Group2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "balance_2_id")
    private Balance2 balance2;
    private BigDecimal productionProfitabilityRatio;
    private PastYearsPerformance pastYearsPerformanceRatio;
    private BigDecimal largestPreviouslyRepaidLoanRatio;


    public Group2(Balance2 balance2, PastYearsPerformance pastYearsPerformanceRatio, BigDecimal largestLoanRepaid, BigDecimal requestedCredit) {
        this.balance2 = balance2;
        setProductionProfitabilityRatio();
        this.pastYearsPerformanceRatio = pastYearsPerformanceRatio;
        largestPreviouslyRepaidLoanRatio = largestLoanRepaid.divide(requestedCredit, 4, RoundingMode.HALF_UP);
    }

    public Group2() {

    }

    public void setProductionProfitabilityRatio() {
        this.productionProfitabilityRatio = balance2.getFinancialResults().getNetFinancialResult().divide(
                balance2.getOperatingExpenses().getMaterialCosts()
                        .add(balance2.getOperatingExpenses().getPayrollExpenses())
                        .add(balance2.getOperatingExpenses().getSocialSecurityContributions())
                        .add(balance2.getOperatingExpenses().getDepreciation())
                        .add(balance2.getOperatingExpenses().getOtherOperatingExpenses()),4, RoundingMode.HALF_UP
        );
    }
}
