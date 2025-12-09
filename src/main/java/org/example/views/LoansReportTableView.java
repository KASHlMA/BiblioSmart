package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.models.Loan;
import org.example.models.User;
import org.example.database.LoanData;

public class LoansReportTableView {

    private Stage stage;
    private User currentUser;

    public LoansReportTableView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public void show() {
        TableView<Loan> table = new TableView<>();

        TableColumn<Loan, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Loan, String> userCol = new TableColumn<>("Usuario");
        userCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        TableColumn<Loan, String> bookCol = new TableColumn<>("Libro");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Loan, String> loanCol = new TableColumn<>("Fecha Préstamo");
        loanCol.setCellValueFactory(new PropertyValueFactory<>("loanDate"));

        table.getColumns().addAll(idCol, userCol, bookCol, loanCol);

        ObservableList<Loan> data = FXCollections.observableArrayList(
                LoanData.getAll().stream()
                        .filter(l -> l.getStatus().equals("APPROVED"))
                        .toList()
        );

        table.setItems(data);

        Button backBtn = new Button("← Volver");
        backBtn.setOnAction(e -> {
            ReportsView reportsView = new ReportsView(stage, currentUser);
            stage.setScene(reportsView.getScene());
        });

        HBox bottom = new HBox(backBtn);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(bottom);

        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Reporte de Préstamos");
        stage.show();
    }
}
