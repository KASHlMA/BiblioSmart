package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.User;

public class ReportsView {

    private Stage stage;
    private User currentUser;

    public ReportsView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // 1. BARRA SUPERIOR (Común)
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // 2. CONTENIDO CENTRAL
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        return new Scene(root, 1150, 720);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #13242C;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(18, 35, 18, 35));
        topBar.setSpacing(20);

        ImageView bookIcon = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        bookIcon.setFitHeight(40);
        bookIcon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botón Regresar
        Button backBtn = new Button("← Regresar");
        backBtn.setStyle(
                "-fx-background-color: #0E4F6E; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> {
            MainMenuView menuView = new MainMenuView(stage, currentUser);
            stage.setScene(menuView.getScene());
            stage.setTitle("BiblioSmart - Menú Principal");
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, backBtn);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(40);
        content.setPadding(new Insets(50));
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Reportes y Estadísticas de la Biblioteca");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("Genere reportes de transacciones para análisis.");
        subtitle.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 18px;");

        HBox reportOptions = new HBox(40);
        reportOptions.setAlignment(Pos.CENTER);

        Button prestadosBtn = createReportButton("Generar Reporte de Préstamos Activos", "#1A6080");
        Button devueltosBtn = createReportButton("Generar Reporte de Devoluciones", "#2C7A7B");

        reportOptions.getChildren().addAll(prestadosBtn, devueltosBtn);
        content.getChildren().addAll(title, subtitle, reportOptions);

        return content;
    }

    private Button createReportButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefSize(300, 80);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        btn.setOnAction(e -> {
            if (text.contains("Préstamos")) {
                new LoansReportTableView(stage, currentUser).show();
            } else {
                new ReturnsReportTableView(stage, currentUser).show();
            }
        });

        return btn;
    }
}
