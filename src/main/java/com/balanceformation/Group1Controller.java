package com.balanceformation;


import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.solvencygroups.Group1;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Group1Controller {

    private final ApplicationContext applicationContext;
    @FXML
    public VBox root;
    @FXML
    private Label immediateLiquidityLabel;
    @FXML
    private Label currentLiquidityLabel;
    @FXML
    private Label overallLiquidityLabel;
    @FXML
    private Label financialIndependenceLabel;
    @FXML
    private Label equityManeuverabilityLabel;

    private Group1 group1;

    public void setBalance1(Balance1 balance1) {
        this.group1 = new Group1(balance1);
        showData();
    }

    private void showData() {

        immediateLiquidityLabel.setText(format(group1.getImmediateLiquidityRatio()));
        currentLiquidityLabel.setText(format(group1.getCurrentLiquidityRatio()));
        overallLiquidityLabel.setText(format(group1.getOverallLiquidityRatio()));
        financialIndependenceLabel.setText(format(group1.getFinancialIndependenceRatio()));
        equityManeuverabilityLabel.setText(format(group1.getEquityManeuverabilityRatio()));
    }

    private String format(BigDecimal value) {
        return value != null ? value.setScale(4, RoundingMode.HALF_UP).toString() : "0";
    }

    @FXML
    private void handleSaveExcel() {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Зберегти");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel file", "*.xlsx")
            );

            fileChooser.setInitialFileName(
                    "group1_" + LocalDate.now() + ".xlsx"
            );

            File file = fileChooser.showSaveDialog(null);

            if (file == null) return;

            export(group1, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(Group1 group1, String outputPath) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Group1 Ratios");

        int rowIndex = 0;

        rowIndex = createRow(sheet, rowIndex, "Коефіцієнт миттєвої ліквідності", group1.getImmediateLiquidityRatio());
        rowIndex = createRow(sheet, rowIndex, "Коефіцієнт поточної ліквідності", group1.getCurrentLiquidityRatio());
        rowIndex = createRow(sheet, rowIndex, "Коефіцієнт загальної ліквідності", group1.getOverallLiquidityRatio());
        rowIndex = createRow(sheet, rowIndex, "Коефіцієнт фінансової незалежності", group1.getFinancialIndependenceRatio());
        rowIndex = createRow(sheet, rowIndex, "Коефіцієнт маневреності власних коштів", group1.getEquityManeuverabilityRatio());

        autoSize(sheet);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }

        workbook.close();
    }

    private int createRow(Sheet sheet, int rowIndex, String name, BigDecimal value) {

        Row row = sheet.createRow(rowIndex);

        Cell nameCell = row.createCell(0);
        nameCell.setCellValue(name);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value != null ? value.doubleValue() : 0);

        return rowIndex + 1;
    }

    private void autoSize(Sheet sheet) {
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    @FXML
    private void goBack(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource("balance-form-1.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            VBox menu = (VBox) root.getParent();
            menu.getChildren().clear();
            menu.getChildren().add(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}