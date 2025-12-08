package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.example.views.LoginView;

public class AppLauncher extends Application {

    @Override
    public void start(Stage stage) {
        LoginView view = new LoginView(stage);
        stage.setScene(view.getScene());
        stage.setTitle("BiblioSmart - Login");
        stage.show();
    }
}
