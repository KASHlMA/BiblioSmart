package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.models.User;

public class LoginView {

    private Stage stage;
    private LoginController loginController;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.loginController = new LoginController();
    }

    public Scene getScene() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-root");

        // BARRA SUPERIOR
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(18, 35, 18, 35));

        ImageView bookIcon = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        bookIcon.setFitHeight(40);
        bookIcon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart"); // Etiqueta de la app (basado en el PDF)
        appName.getStyleClass().add("app-name");

        topBar.getChildren().addAll(bookIcon, appName);
        root.setTop(topBar);

        // CONTENEDOR PRINCIPAL
        HBox centerBox = new HBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20, 80, 20, 80));
        centerBox.setSpacing(100);

        // TEXTO IZQUIERDO (Basado en el PDF, página 1)
        VBox leftText = new VBox();
        leftText.setAlignment(Pos.CENTER_LEFT);
        leftText.setSpacing(5);

        Label line1 = new Label("TU BIBLIOTECA DIGITAL");
        Label line2 = new Label("¡PIDE, LEE Y DEVUELVE");
        Label line3 = new Label("CON UN CLICK!");

        line1.getStyleClass().add("big-left-text");
        line2.getStyleClass().add("big-left-text2");
        line3.getStyleClass().add("big-left-text2");

        leftText.getChildren().addAll(line1, line2, line3);

        // CARD LOGIN
        VBox card = new VBox();
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(25);
        card.setPadding(new Insets(45, 55, 45, 55));
        card.getStyleClass().add("login-card");

        card.setPrefSize(360, 520);
        card.setMinSize(360, 520);
        card.setMaxSize(360, 520);

        Label title = new Label("Biblioteca - Inicio de Sesión");
        title.getStyleClass().add("card-title");

        // LOGO CIRCULAR
        StackPane logoContainer = new StackPane();
        logoContainer.getStyleClass().add("logo-container");
        logoContainer.setPrefSize(140, 140);
        logoContainer.setMaxSize(140, 140);

        // NOTA: Asumo que tienes una imagen llamada 'logo.png' o 'bibliosmart.png'
        ImageView logo = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        logo.setFitWidth(120);
        logo.setFitHeight(120);
        logo.setPreserveRatio(true);

        Circle clip = new Circle(60, 60, 60);
        logo.setClip(clip);

        logoContainer.getChildren().add(logo);

        // INPUTS
        TextField userField = new TextField();
        userField.setPromptText("Usuario");
        userField.getStyleClass().add("login-field");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        passField.getStyleClass().add("login-field");

        // MENSAJE DE ERROR
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 12px; -fx-padding: 5 0 0 0;");
        errorLabel.setVisible(false);

        // BOTÓN
        Button loginBtn = new Button("Ingresar");
        loginBtn.getStyleClass().add("login-button");

        // ACCIÓN DEL BOTÓN
        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("⚠ Por favor completa todos los campos");
                errorLabel.setVisible(true);
                return;
            }

            // >>> CORRECCIÓN: Llamar al método handleLogin() <<<
            User user = loginController.handleLogin(username, password);

            if (user != null) {
                System.out.println("✅ Login exitoso: " + user.getNombre());

                // Ir al menú principal
                MainMenuView menuView = new MainMenuView(stage, user);
                stage.setScene(menuView.getScene());
                stage.setTitle("BiblioSmart - Menú Principal");

            } else {
                errorLabel.setText("❌ Usuario o contraseña incorrectos");
                errorLabel.setVisible(true);
                passField.clear();
            }
        });

        // Login con Enter
        passField.setOnAction(e -> loginBtn.fire());

        card.getChildren().addAll(
                title,
                logoContainer,
                userField,
                passField,
                errorLabel,
                loginBtn
        );

        centerBox.getChildren().addAll(leftText, card);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1150, 720);
        scene.getStylesheets().add(
                getClass().getResource("/org/example/css/main.css").toExternalForm()
        );

        return scene;
    }
}