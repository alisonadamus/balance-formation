package com.balanceformation;

import com.balanceformation.entity.Creditworthiness;
import com.balanceformation.entity.balance1.Balance1;
import com.balanceformation.entity.balance2.Balance2;
import com.balanceformation.entity.solvencygroups.Group2;
import com.balanceformation.repository.Balance1Repository;
import com.balanceformation.repository.Balance2Repository;
import com.balanceformation.repository.Group2Repository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreditworthinessController {

    private final Balance1Repository balance1Repository;
    private final Balance2Repository balance2Repository;
    private final Group2Repository group2Repository;

    @FXML
    private ComboBox<Balance1> balance1Combo;
    @FXML private ComboBox<Balance2> balance2Combo;
    @FXML private ComboBox<Group2> group2Combo;

    @FXML private TextField yearsExistField;
    @FXML private TextField ownFundsField;
    @FXML private TextField liquidAssetsField;

    @FXML private Label resultLabel;
    @FXML private Label ratingLabel;

    @FXML private TableView<Map<String, Object>> table;
    @FXML private TableColumn<Map<String, Object>, String> colName;
    @FXML private TableColumn<Map<String, Object>, BigDecimal> colK;
    @FXML private TableColumn<Map<String, Object>, BigDecimal> colMu;

    @FXML private TextField w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11;

    private Creditworthiness current;

    @FXML
    public void initialize() {
        loadData();
        setupDisplay();

        colName.setCellValueFactory(data ->
                new SimpleStringProperty((String) data.getValue().get("name"))
        );

        colK.setCellValueFactory(data ->
                new SimpleObjectProperty<>((BigDecimal) data.getValue().get("k"))
        );

        colMu.setCellValueFactory(data ->
                new SimpleObjectProperty<>((BigDecimal) data.getValue().get("mu"))
        );
    }

    private void loadData() {
        balance1Combo.getItems().setAll(balance1Repository.findAll());
        balance2Combo.getItems().setAll(balance2Repository.findAll());
        group2Combo.getItems().setAll(group2Repository.findAll());
    }

    // як відображати в ComboBox
    private void setupDisplay() {

        balance1Combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Balance1 item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        "ID=" + item.getId() +
                                " | Активи=" + item.getAssetsTotal() +
                                " | Пасиви=" + item.getLiabilitiesTotal());
            }
        });

        balance1Combo.setButtonCell(balance1Combo.getCellFactory().call(null));


        balance2Combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Balance2 item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        "ID=" + item.getId() +
                                " | Фінансовий результат=" + item.getFinancialResults().getNetFinancialResult() +
                                " | Сукупний дохід=" + item.getOperatingExpenses().getMaterialCosts()
                                .add(item.getOperatingExpenses().getPayrollExpenses())
                                .add(item.getOperatingExpenses().getSocialSecurityContributions())
                                .add(item.getOperatingExpenses().getDepreciation())
                                .add(item.getOperatingExpenses().getOtherOperatingExpenses()));
            }
        });

        balance2Combo.setButtonCell(balance2Combo.getCellFactory().call(null));


        group2Combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Group2 item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        "ID=" + item.getId() +
                                " | Минулорічна діяльність=" + item.getProductionProfitabilityRatio() +
                                " | Коеф. суми кредиту=" + item.getLargestPreviouslyRepaidLoanRatio());
            }
        });

        group2Combo.setButtonCell(group2Combo.getCellFactory().call(null));
    }

    @FXML
    private void onCalculate() {
        try {
            Balance1 b1 = balance1Combo.getValue();
            Balance2 b2 = balance2Combo.getValue();
            Group2 g2 = group2Combo.getValue();

            if (b1 == null || b2 == null || g2 == null) {
                resultLabel.setText("Обери всі дані!");
                return;
            }

            int years = Integer.parseInt(yearsExistField.getText());
            BigDecimal K = new BigDecimal(ownFundsField.getText());
            BigDecimal M = new BigDecimal(liquidAssetsField.getText());

            current = new Creditworthiness();
            current.setBalance1(b1);
            current.setBalance2(b2);
            current.setGroup2(g2);
            current.setYears(BigDecimal.valueOf(years));
            current.setK(K);
            current.setM(M);


            current.calculateAll();

            ObservableList<Map<String, Object>> rows = FXCollections.observableArrayList();

            rows.add(row("Коефіцієнт миттєвої ліквідності", current.getK1(), current.getMuK1()));
            rows.add(row("Коефіцієнт поточної ліквідності", current.getK2(), current.getMuK2()));
            rows.add(row("Коефіцієнт загальної ліквідності", current.getK3(), current.getMuK3()));
            rows.add(row("Коефіцієнт фінансової незалежності", current.getK4(), current.getMuK4()));
            rows.add(row("Коефіцієнт маневреності власних коштів", current.getK5(), current.getMuK5()));
            rows.add(row("Коефіцієнт рентабельності виробництва", current.getK6(), current.getMuK6()));
            rows.add(row("Коефіцієнт діяльності минулих років", current.getK7(), current.getMuK7()));
            rows.add(row("Коефіцієнт найбільшої суми раніше повернутого кредиту", current.getK8(), current.getMuK8()));
            rows.add(row("Термін існування підприємства", current.getK9(), current.getMuK9()));
            rows.add(row("Коефіцієнт питомої ваги коштів підприємства у вартості кредитного проекту", current.getK10(), current.getMuK10()));
            rows.add(row("Коефіцієнт наявності власного ліквідного майна", current.getK11(), current.getMuK11()));

            table.setItems(rows);


        } catch (Exception e) {
            resultLabel.setText("Помилка: " + e.getMessage());
        }
    }

    private Map<String, Object> row(String name, BigDecimal k, BigDecimal mu) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("k", k);
        map.put("mu", mu);
        return map;
    }

    @FXML
    private void onCalculateRating() {
        try {
            if (current == null) {
                ratingLabel.setText("Спочатку виконай розрахунок!");
                return;
            }

            List<BigDecimal> v = List.of(
                    getWeight(w1), getWeight(w2), getWeight(w3),
                    getWeight(w4), getWeight(w5), getWeight(w6),
                    getWeight(w7), getWeight(w8), getWeight(w9),
                    getWeight(w10), getWeight(w11)
            );

            BigDecimal sumV = v.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sumV.compareTo(BigDecimal.ZERO) == 0) {
                ratingLabel.setText("Ваги не можуть бути всі 0");
                return;
            }

            List<BigDecimal> w = v.stream()
                    .map(v_i -> v_i.divide(sumV, 6, RoundingMode.HALF_UP))
                    .toList();

            List<BigDecimal> mu = List.of(
                    current.getMuK1(),
                    current.getMuK2(),
                    current.getMuK3(),
                    current.getMuK4(),
                    current.getMuK5(),
                    current.getMuK6(),
                    current.getMuK7(),
                    current.getMuK8(),
                    current.getMuK9(),
                    current.getMuK10(),
                    current.getMuK11()
            );

            BigDecimal mP = BigDecimal.ZERO;

            for (int i = 0; i < 11; i++) {
                mP = mP.add(w.get(i).multiply(mu.get(i)));
            }

            mP = mP.setScale(4, RoundingMode.HALF_UP);

            String rating = getRating(mP);

            ratingLabel.setText("m(P) = " + mP + " → " + rating);

            exportToExcel(mP, rating, w, mu);

        } catch (Exception e) {
            ratingLabel.setText("Помилка: " + e.getMessage());
        }
    }

    private BigDecimal getWeight(TextField tf) {
        if (tf.getText() == null || tf.getText().isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(tf.getText());
    }

    private String getRating(BigDecimal mP) {

        if (mP.compareTo(new BigDecimal("0.57")) > 0) {
            return "I категорія (AAA / AA) — найвища кредитоспроможність";
        }

        if (mP.compareTo(new BigDecimal("0.37")) > 0) {
            return "II категорія (A / BBB) — висока кредитоспроможність";
        }

        if (mP.compareTo(new BigDecimal("0.19")) > 0) {
            return "III категорія (BB / B) — спекулятивний рейтинг";
        }

        if (mP.compareTo(new BigDecimal("0.11")) > 0) {
            return "IV категорія (CCC) — можливий дефолт";
        }

        return "V категорія (C / RD / D) — дефолт неминучий";

    }

    private void exportToExcel(BigDecimal mP, String rating, List<BigDecimal> weights, List<BigDecimal> muValues) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Credit Rating");

            int rowIdx = 0;

            Row title = sheet.createRow(rowIdx++);
            title.createCell(0).setCellValue("Рейтинг кредитоспроможності");

            Row rowMP = sheet.createRow(rowIdx++);
            rowMP.createCell(0).setCellValue("m(P)");
            rowMP.createCell(1).setCellValue(mP.doubleValue());

            Row rowRating = sheet.createRow(rowIdx++);
            rowRating.createCell(0).setCellValue("Категорія");
            rowRating.createCell(1).setCellValue(rating);

            rowIdx++;

            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("№");
            header.createCell(1).setCellValue("w_i");
            header.createCell(2).setCellValue("μ(K_i)");
            header.createCell(3).setCellValue("w_i * μ(K_i)");

            for (int i = 0; i < 11; i++) {
                Row row = sheet.createRow(rowIdx++);

                BigDecimal wi = weights.get(i);
                BigDecimal mu = muValues.get(i);
                BigDecimal mul = wi.multiply(mu);

                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(wi.doubleValue());
                row.createCell(2).setCellValue(mu.doubleValue());
                row.createCell(3).setCellValue(mul.doubleValue());
            }

            rowIdx++;
            Row timeRow = sheet.createRow(rowIdx);
            timeRow.createCell(0).setCellValue("Створено:");
            timeRow.createCell(1).setCellValue(LocalDateTime.now().toString());

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream("credit_rating.xlsx")) {
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ratingLabel.setText("Помилка Excel: " + e.getMessage());
        }
    }
}