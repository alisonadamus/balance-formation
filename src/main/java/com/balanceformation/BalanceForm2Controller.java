package com.balanceformation;

import com.balanceformation.entity.balance2.*;
import com.balanceformation.repository.Balance2Repository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BalanceForm2Controller {

    private final ApplicationContext applicationContext;
    private final Balance2Repository balance2Repository;
    private final ExcelExporterBalance2 excelExporter;
    @FXML private ScrollPane root;

    // --- I. Financial Results ---
    @FXML private TextField netRevenueField;
    @FXML private TextField costOfGoodsSoldField;
    @FXML private TextField otherOperatingIncomeField;
    @FXML private TextField administrativeExpensesField;
    @FXML private TextField sellingExpensesField;
    @FXML private TextField incomeFromEquityParticipationField;
    @FXML private TextField otherFinancialIncomeField;
    @FXML private TextField otherIncomeField;
    @FXML private TextField financialExpensesField;
    @FXML private TextField lossesFromEquityParticipationField;
    @FXML private TextField otherExpensesField;
    @FXML private TextField incomeTaxExpensesField;
    @FXML private TextField profitFromDiscontinuedOperationsField;
    @FXML private Label netFinancialResultLabel;

    // --- II. Comprehensive Income ---
    @FXML private TextField revaluationNonCurrentAssetsField;
    @FXML private TextField revaluationFinancialInstrumentsField;
    @FXML private TextField accumulatedCurrencyDifferencesField;
    @FXML private TextField shareOfOtherComprehensiveIncomeAssocField;
    @FXML private TextField otherComprehensiveIncomeField;
    @FXML private TextField otherComprehensiveIncomeBeforeTaxField;
    @FXML private TextField taxOnOtherComprehensiveIncomeField;
    @FXML private Label otherComprehensiveIncomeAfterTaxLabel;
    @FXML private Label totalComprehensiveIncomeLabel;

    // --- III. Operating Expenses ---
    @FXML private TextField materialCostsField;
    @FXML private TextField payrollExpensesField;
    @FXML private TextField socialSecurityContributionsField;
    @FXML private TextField depreciationField;
    @FXML private TextField otherOperatingExpensesOpField;
    @FXML private Label totalOperatingExpensesLabel;

    // --- IV. Share Profitability Indicators ---
    @FXML private TextField averageNumberOfSharesField;
    @FXML private TextField adjustedAverageNumberOfSharesField;
    @FXML private TextField netProfitPerShareField;
    @FXML private TextField adjustedNetProfitPerShareField;
    @FXML private TextField dividendsPerShareField;

    private Balance2 balance2;

    @FXML
    private void handleSave() {

        // --- I. Financial Results ---
        FinancialResults financialResults = FinancialResults.builder()
                .netRevenue(toBigDecimal(netRevenueField))
                .costOfGoodsSold(toBigDecimal(costOfGoodsSoldField))
                .otherOperatingIncome(toBigDecimal(otherOperatingIncomeField))
                .administrativeExpenses(toBigDecimal(administrativeExpensesField))
                .sellingExpenses(toBigDecimal(sellingExpensesField))
                .incomeFromEquityParticipation(toBigDecimal(incomeFromEquityParticipationField))
                .otherFinancialIncome(toBigDecimal(otherFinancialIncomeField))
                .otherIncome(toBigDecimal(otherIncomeField))
                .financialExpenses(toBigDecimal(financialExpensesField))
                .lossesFromEquityParticipation(toBigDecimal(lossesFromEquityParticipationField))
                .otherExpenses(toBigDecimal(otherExpensesField))
                .incomeTaxExpenses(toBigDecimal(incomeTaxExpensesField))
                .profitFromDiscontinuedOperations(toBigDecimal(profitFromDiscontinuedOperationsField))
                .build();

        netFinancialResultLabel.setText(financialResults.getNetFinancialResult().toString());

        // --- II. Comprehensive Income ---
        ComprehensiveIncome comprehensiveIncome = ComprehensiveIncome.builder()
                .revaluationNonCurrentAssets(toBigDecimal(revaluationNonCurrentAssetsField))
                .revaluationFinancialInstruments(toBigDecimal(revaluationFinancialInstrumentsField))
                .accumulatedCurrencyDifferences(toBigDecimal(accumulatedCurrencyDifferencesField))
                .shareOfOtherComprehensiveIncomeAssoc(toBigDecimal(shareOfOtherComprehensiveIncomeAssocField))
                .otherComprehensiveIncome(toBigDecimal(otherComprehensiveIncomeField))
                .otherComprehensiveIncomeBeforeTax(toBigDecimal(otherComprehensiveIncomeBeforeTaxField))
                .taxOnOtherComprehensiveIncome(toBigDecimal(taxOnOtherComprehensiveIncomeField))
                .build();

        otherComprehensiveIncomeAfterTaxLabel.setText(comprehensiveIncome.getOtherComprehensiveIncomeAfterTax().toString());
        totalComprehensiveIncomeLabel.setText(comprehensiveIncome.getTotalComprehensiveIncome(financialResults.getNetFinancialResult()).toString());

        // --- III. Operating Expenses ---
        OperatingExpenses operatingExpenses = OperatingExpenses.builder()
                .materialCosts(toBigDecimal(materialCostsField))
                .payrollExpenses(toBigDecimal(payrollExpensesField))
                .socialSecurityContributions(toBigDecimal(socialSecurityContributionsField))
                .depreciation(toBigDecimal(depreciationField))
                .otherOperatingExpenses(toBigDecimal(otherOperatingExpensesOpField))
                .build();

        totalOperatingExpensesLabel.setText(operatingExpenses.getTotal().toString());

        // --- IV. Share Profitability Indicators ---
        ShareProfitabilityIndicators indicators = ShareProfitabilityIndicators.builder()
                .averageNumberOfShares(toBigDecimal(averageNumberOfSharesField))
                .adjustedAverageNumberOfShares(toBigDecimal(adjustedAverageNumberOfSharesField))
                .netProfitPerShare(toBigDecimal(netProfitPerShareField))
                .adjustedNetProfitPerShare(toBigDecimal(adjustedNetProfitPerShareField))
                .dividendsPerShare(toBigDecimal(dividendsPerShareField))
                .build();

        // --- Збереження у БД ---
        Balance2 balance = Balance2.builder()
                .financialResults(financialResults)
                .comprehensiveIncome(comprehensiveIncome)
                .operatingExpenses(operatingExpenses)
                .shareProfitabilityIndicators(indicators)
                .build();

        balance2Repository.save(balance);


        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Зберегти форму 2");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel file","*.xls")
            );

            fileChooser.setInitialFileName(
                    "balance2_" + LocalDate.now() + ".xls"
            );

            File file = fileChooser.showSaveDialog(null);

            if(file == null){
                return;
            }

            InputStream template =
                    getClass().getResourceAsStream("/templates/balance-form2-template.xls");

            excelExporter.export(balance, template, file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Баланс Форма-2");
        alert.setHeaderText(null);
        alert.setContentText("Баланс Форма-2 успішно збережено!");
        alert.showAndWait();

        this.balance2 = balance;
    }

    private BigDecimal toBigDecimal(TextField field) {
        try {
            String text = field.getText();
            return (text == null || text.isBlank()) ? BigDecimal.ZERO : new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    @FXML
    public void goToGroup2() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource("group-2.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            VBox group2 = fxmlLoader.load();
            Group2Controller group2Controller = fxmlLoader.getController();
            group2Controller.setBalance2(balance2);
            VBox menu = (VBox) root.getParent();
            menu.getChildren().clear();
            menu.getChildren().add(group2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}