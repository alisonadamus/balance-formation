package com.balanceformation;

import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.balance1.assets.Assets;
import com.balanceformation.entity.balance1.assets.CurrentAssets;
import com.balanceformation.entity.balance1.assets.NonCurrentAssets;
import com.balanceformation.entity.balance1.liabilities.CurrentLiabilities;
import com.balanceformation.entity.balance1.liabilities.Equity;
import com.balanceformation.entity.balance1.liabilities.Liabilities;
import com.balanceformation.entity.balance1.liabilities.LongTermLiabilities;
import com.balanceformation.repository.Balance1Repository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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
public class BalanceForm1Controller {

    private final Balance1Repository balance1Repository;
    private final ExcelExporterBalance1 excelExporter;
    private final ApplicationContext applicationContext;
    @FXML
    private ScrollPane root;
    @FXML
    private TextField intangibleInitialCostField;
    @FXML
    private TextField intangibleAmortizationField;
    @FXML
    private TextField unfinishedInvestmentsField;
    @FXML
    private TextField fixedAssetsInitialCostField;
    @FXML
    private TextField fixedAssetsDepreciationField;
    @FXML
    private TextField investmentPropertyField;
    @FXML
    private TextField longTermBiologicalAssetsField;
    @FXML
    private TextField longTermFinancialInvestmentsEquityField;
    @FXML
    private TextField otherFinancialInvestmentsField;
    @FXML
    private TextField longTermReceivablesField;
    @FXML
    private TextField deferredTaxAssetsField;
    @FXML
    private TextField otherNonCurrentAssetsField;
    @FXML
    private TextField nonCurrentAssetsForSaleField;

    // Current Assets
    @FXML
    private TextField inventoriesField;
    @FXML
    private TextField biologicalAssetsCurrentField;
    @FXML
    private TextField receivablesProductsField;
    @FXML
    private TextField advancesIssuedField;
    @FXML
    private TextField receivablesBudgetField;
    @FXML
    private TextField incomeTaxReceivableField;
    @FXML
    private TextField otherReceivablesField;
    @FXML
    private TextField currentFinancialInvestmentsField;
    @FXML
    private TextField cashField;
    @FXML
    private TextField prepaidExpensesField;
    @FXML
    private TextField otherCurrentAssetsField;

    // Equity
    @FXML
    private TextField registeredCapitalField;
    @FXML
    private TextField revaluationCapitalField;
    @FXML
    private TextField additionalCapitalField;
    @FXML
    private TextField reserveCapitalField;
    @FXML
    private TextField retainedEarningsField;
    @FXML
    private TextField unpaidCapitalField;
    @FXML
    private TextField withdrawnCapitalField;

    // Long-term Liabilities
    @FXML
    private TextField deferredTaxLiabilitiesField;
    @FXML
    private TextField longTermBankLoansField;
    @FXML
    private TextField otherLongTermLiabilitiesField;
    @FXML
    private TextField longTermProvisionsField;
    @FXML
    private TextField targetedFinancingField;

    // Current Liabilities
    @FXML
    private TextField shortTermLoansField;
    @FXML
    private TextField payableLongTermField;
    @FXML
    private TextField payableSuppliersField;
    @FXML
    private TextField payableBudgetField;
    @FXML
    private TextField incomeTaxPayableField;
    @FXML
    private TextField insurancePayableField;
    @FXML
    private TextField salaryPayableField;
    @FXML
    private TextField currentProvisionsField;
    @FXML
    private TextField deferredIncomeField;
    @FXML
    private TextField otherCurrentLiabilitiesField;
    private Balance1 balance1;

    @FXML
    private void handleSave() {
        Balance1 balance = new Balance1();

        // --- Assets ---
        NonCurrentAssets nca = NonCurrentAssets.builder()
                .intangibleInitialCost(toBigDecimal(intangibleInitialCostField))
                .intangibleAmortization(toBigDecimal(intangibleAmortizationField))
                .unfinishedInvestments(toBigDecimal(unfinishedInvestmentsField))
                .fixedAssetsInitialCost(toBigDecimal(fixedAssetsInitialCostField))
                .fixedAssetsDepreciation(toBigDecimal(fixedAssetsDepreciationField))
                .investmentProperty(toBigDecimal(investmentPropertyField))
                .longTermBiologicalAssets(toBigDecimal(longTermBiologicalAssetsField))
                .longTermFinancialInvestmentsEquity(toBigDecimal(longTermFinancialInvestmentsEquityField))
                .otherFinancialInvestments(toBigDecimal(otherFinancialInvestmentsField))
                .longTermReceivables(toBigDecimal(longTermReceivablesField))
                .deferredTaxAssets(toBigDecimal(deferredTaxAssetsField))
                .otherNonCurrentAssets(toBigDecimal(otherNonCurrentAssetsField))
                .build();

        Assets assets = Assets.builder()
                .nonCurrentAssets(nca)
                .currentAssets(CurrentAssets.builder()
                        .inventories(toBigDecimal(inventoriesField))
                        .biologicalAssetsCurrent(toBigDecimal(biologicalAssetsCurrentField))
                        .receivablesProducts(toBigDecimal(receivablesProductsField))
                        .advancesIssued(toBigDecimal(advancesIssuedField))
                        .receivablesBudget(toBigDecimal(receivablesBudgetField))
                        .incomeTaxReceivable(toBigDecimal(incomeTaxReceivableField))
                        .otherReceivables(toBigDecimal(otherReceivablesField))
                        .currentFinancialInvestments(toBigDecimal(currentFinancialInvestmentsField))
                        .cash(toBigDecimal(cashField))
                        .prepaidExpenses(toBigDecimal(prepaidExpensesField))
                        .otherCurrentAssets(toBigDecimal(otherCurrentAssetsField))
                        .build())
                .nonCurrentAssetsForSale(toBigDecimal(nonCurrentAssetsForSaleField))
                .build();

        // --- Liabilities ---
        Equity equity = Equity.builder()
                .registeredCapital(toBigDecimal(registeredCapitalField))
                .revaluationCapital(toBigDecimal(revaluationCapitalField))
                .additionalCapital(toBigDecimal(additionalCapitalField))
                .reserveCapital(toBigDecimal(reserveCapitalField))
                .retainedEarnings(toBigDecimal(retainedEarningsField))
                .unpaidCapital(toBigDecimal(unpaidCapitalField))
                .withdrawnCapital(toBigDecimal(withdrawnCapitalField))
                .build();

        LongTermLiabilities ltl = LongTermLiabilities.builder()
                .deferredTaxLiabilities(toBigDecimal(deferredTaxLiabilitiesField))
                .longTermBankLoans(toBigDecimal(longTermBankLoansField))
                .otherLongTermLiabilities(toBigDecimal(otherLongTermLiabilitiesField))
                .longTermProvisions(toBigDecimal(longTermProvisionsField))
                .targetedFinancing(toBigDecimal(targetedFinancingField))
                .build();

        CurrentLiabilities cl = CurrentLiabilities.builder()
                .shortTermLoans(toBigDecimal(shortTermLoansField))
                .payableLongTerm(toBigDecimal(payableLongTermField))
                .payableSuppliers(toBigDecimal(payableSuppliersField))
                .payableBudget(toBigDecimal(payableBudgetField))
                .incomeTaxPayable(toBigDecimal(incomeTaxPayableField))
                .insurancePayable(toBigDecimal(insurancePayableField))
                .salaryPayable(toBigDecimal(salaryPayableField))
                .currentProvisions(toBigDecimal(currentProvisionsField))
                .deferredIncome(toBigDecimal(deferredIncomeField))
                .otherCurrentLiabilities(toBigDecimal(otherCurrentLiabilitiesField))
                .build();

        Liabilities liabilities = Liabilities.builder()
                .equity(equity)
                .longTermLiabilities(ltl)
                .currentLiabilities(cl)
                .build();

        balance.setAssets(assets);
        balance.setLiabilities(liabilities);

        // --- Перевірка балансу ---
        if (!balance.isBalanced()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Баланс не збалансований");
            alert.setHeaderText(null);
            alert.setContentText("Сума активів не дорівнює сумі пасивів." +
                    "\n АКТИВИ: " + balance.getAssetsTotal() +
                    "\n ПАСИВИ: " + balance.getLiabilitiesTotal() +
                    "\n Перевірте введені дані.");
            alert.showAndWait();
        } else {
            // Зберігаємо баланс
            balance1Repository.save(balance);


            try {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Зберегти баланс");

                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Excel file", "*.xlsx")
                );

                fileChooser.setInitialFileName(
                        "balance-form1_" + LocalDate.now() + ".xlsx"
                );

                File file = fileChooser.showSaveDialog(null);

                if (file == null) {
                    return;
                }

                InputStream template = BalanceFormationApplication.class.getResourceAsStream("/templates/balance-form1-template.xlsx");

                excelExporter.export(balance, template, file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Баланс збережено");
            alert.setHeaderText(null);
            alert.setContentText("Баланс успішно збережено.");
            alert.showAndWait();

            balance1 = balance;
        }
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
    private void goToGroup1() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource("group-1.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            VBox group1 = fxmlLoader.load();
            Group1Controller group1Controller = fxmlLoader.getController();
            group1Controller.setBalance1(balance1);
            VBox menu = (VBox) root.getParent();
            menu.getChildren().clear();
            menu.getChildren().add(group1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}