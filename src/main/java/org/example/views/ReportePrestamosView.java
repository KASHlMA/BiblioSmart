package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.example.database.LoanData;
import org.example.models.Loan;

import javafx.beans.property.SimpleStringProperty;

public class ReportePrestamosView {

    public void show(Stage stage) {

        TableView<Loan> table = new TableView<>();

        // -------------------
        // Definición de columnas
        // -------------------
        TableColumn<Loan, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<Loan, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClientName()));

        TableColumn<Loan, String> colLibro = new TableColumn<>("Libro");
        colLibro.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBookTitle()));

        TableColumn<Loan, String> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getLoanDate() != null ? c.getValue().getLoanDate().toString() : "—"
        ));

        TableColumn<Loan, String> colFechaLimite = new TableColumn<>("Fecha Límite");
        colFechaLimite.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDueDate() != null ? c.getValue().getDueDate().toString() : "—"
        ));

        TableColumn<Loan, String> colFechaDev = new TableColumn<>("Fecha Devolución");
        colFechaDev.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getReturnDate() != null ? c.getValue().getReturnDate().toString() : "—"
        ));

        TableColumn<Loan, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        // -------------------
        // Añadir columnas a la tabla
        // -------------------
        table.getColumns().addAll(
                colId, colUsuario, colLibro, colFechaPrestamo,
                colFechaLimite, colFechaDev, colEstado
        );

        // Ajustar tamaño automático
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // -------------------
        // Cargar datos (no necesitamos service)
        // -------------------
        table.getItems().addAll(LoanData.getAll());

        // -------------------
        // Botón regresar
        // -------------------
        Button btnBack = new Button("Volver");
        btnBack.setStyle("-fx-font-size: 14px; -fx-padding: 5 15;");
        btnBack.setOnAction(e -> new StatisticsView().show(stage));

        HBox bottom = new HBox(btnBack);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(15));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setCenter(table);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Reporte de Préstamos");
        stage.show();
    }
}
