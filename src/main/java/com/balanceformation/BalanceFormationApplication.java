package com.balanceformation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class BalanceFormationApplication extends Application {
    /**
     * Spring-контекст для управління залежностями додатка.
     */
    private AnnotationConfigApplicationContext context;

    /**
     * Ініціалізує Spring-контекст додатка перед запуском JavaFX-інтерфейсу.
     * Створює новий {@link AnnotationConfigApplicationContext} на основі конфігураційного класу {@link AppConfig}.
     */
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BalanceFormationApplication.class.getResource("menu.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Введення балансових звітів");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Закриває Spring-контекст при завершенні роботи додатка.
     * Викликається автоматично, коли користувач закриває вікно додатка.
     */
    @Override
    public void stop() {
        context.close();
    }

    /**
     * Точка входу для запуску JavaFX-додатка.
     * Викликає метод {@link #launch(String...)} для ініціалізації та запуску додатка.
     *
     * @param args аргументи командного рядка, які передаються до додатка
     */
    public static void main(String[] args) {
        launch();
    }
}