package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.views.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Cargar la vista del login pasando el stage
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.getScene());

        stage.setTitle("BiblioSmart - Login");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}