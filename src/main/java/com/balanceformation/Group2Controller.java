package com.balanceformation;

import com.balanceformation.entity.Creditworthiness;
import com.balanceformation.entity.balance2.Balance2;
import com.balanceformation.entity.solvencygroups.Group2;
import com.balanceformation.entity.solvencygroups.PastYearsPerformance;
import com.balanceformation.repository.Group2Repository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Group2Controller {

    private final ApplicationContext applicationContext;
    private final Group2Repository group2Repository;
    @FXML
    public VBox root;

    @FXML
    private ComboBox<PastYearsPerformance> pastYearsCombo;
    @FXML private TextField largestLoanField;
    @FXML private TextField requestedCreditField;

    @FXML private Label productionProfitabilityLabel;
    @FXML private Label k7Label;
    @FXML private Label k8Label;

    @Setter
    private Balance2 balance2;
    private Group2 group2;

    @FXML
    public void initialize() {
        pastYearsCombo.getItems().setAll(PastYearsPerformance.values());

        pastYearsCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(PastYearsPerformance item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescription());
            }
        });

        pastYearsCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(PastYearsPerformance item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescription());
            }
        });

    }

    @FXML
    private void handleCalculate() {

        try {
            BigDecimal largestLoan = new BigDecimal(largestLoanField.getText());
            BigDecimal requestedCredit = new BigDecimal(requestedCreditField.getText());
            PastYearsPerformance k7 = pastYearsCombo.getValue();

            if (k7 == null) {
                showAlert("Виберіть діяльності минулих років");
                return;
            }

            group2 = new Group2(balance2, k7, largestLoan, requestedCredit);

            showResults();

        } catch (Exception e) {
            showAlert("Неправильно введені дані");
        }
    }

    private void showResults() {
        productionProfitabilityLabel.setText(format(group2.getProductionProfitabilityRatio()));
        k7Label.setText(String.valueOf(group2.getPastYearsPerformanceRatio().getValue()));
        k8Label.setText(format(group2.getLargestPreviouslyRepaidLoanRatio()));
    }

    private String format(BigDecimal val) {
        return val != null ? val.setScale(4, RoundingMode.HALF_UP).toString() : "0";
    }


    @FXML
    private void handleSaveExcel() {

        if (group2 == null) {
            showAlert("Calculate first!");
            return;
        }

        group2Repository.save(group2);

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Group2");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel", "*.xlsx")
            );

            fileChooser.setInitialFileName("group2_" + LocalDate.now() + ".xlsx");

            File file = fileChooser.showSaveDialog(null);
            if (file == null) return;

            export(group2, file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(Group2 g, String path) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Group2");

        int i = 0;

        i = row(sheet, i, "Коефіцієнт рентабельності виробництва", g.getProductionProfitabilityRatio());
        i = row(sheet, i, "Коефіцієнт діяльності минулих років", new java.math.BigDecimal(g.getPastYearsPerformanceRatio().getValue()));
        i = row(sheet, i, "коефіцієнт найбільшої суми раніше повернутого кредиту", g.getLargestPreviouslyRepaidLoanRatio());

        try (FileOutputStream fos = new FileOutputStream(path)) {
            wb.write(fos);
        }

        wb.close();
    }

    private int row(Sheet s, int i, String name, java.math.BigDecimal val) {
        Row r = s.createRow(i);
        r.createCell(0).setCellValue(name);
        r.createCell(1).setCellValue(val != null ? val.doubleValue() : 0);
        return i + 1;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void goBack(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource("balance-form-2.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            VBox menu = (VBox) root.getParent();
            menu.getChildren().clear();
            menu.getChildren().add(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
