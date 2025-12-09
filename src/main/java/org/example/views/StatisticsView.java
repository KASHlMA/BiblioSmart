package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StatisticsView {

    public void show(Stage stage) {

        Label title = new Label("Estadísticas y Reportes");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button btnReporte = new Button("Ver Reporte Completo");
        btnReporte.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        btnReporte.setOnAction(e -> new ReportePrestamosView().show(stage));

        VBox root = new VBox(30, title, btnReporte);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Estadísticas");
        stage.show();
    }
}
