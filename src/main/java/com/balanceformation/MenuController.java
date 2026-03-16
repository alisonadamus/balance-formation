package com.balanceformation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuController {

    private final ApplicationContext applicationContext;

    @FXML
    public VBox vbox;

    @FXML
    private void openForm1() {
        openForm("balance-form-1.fxml");
    }

    @FXML
    private void openForm2() {
        openForm("balance-form-2.fxml");
    }

    @FXML
    private void openCreditworthiness() {
        openForm("creditworthiness.fxml");
    }

    private void openForm(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource(fxmlFile));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            vbox.getChildren().clear();
            vbox.getChildren().add(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
