package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.database.LoanData;
import org.example.database.LocalDatabase;
import org.example.models.Loan;
import org.example.models.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MyLoansView {

    private Stage stage;
    private User currentUser;
    private TableView<Loan> loansTable;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MyLoansView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        HBox topBar = createTopBar();
        root.setTop(topBar);

        VBox centerContent = createCenterContent();
        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #0B1C26; -fx-background-color: #0B1C26;");

        root.setCenter(scrollPane);

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
        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 50, 30, 50));
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Mi Historial de Préstamos");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("Aquí puedes ver tus libros activos, solicitar su devolución o cancelar solicitudes pendientes.");
        subtitle.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 18px;");

        loansTable = createLoansTable();
        loadMyLoans();

        content.getChildren().addAll(title, subtitle, loansTable);
        return content;
    }

    private TableView<Loan> createLoansTable() {
        TableView<Loan> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(450);

        // Columnas básicas
        TableColumn<Loan, Integer> idLoanCol = new TableColumn<>("ID Préstamo");
        idLoanCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Loan, String> titleCol = new TableColumn<>("Libro");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Loan, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Loan, String> dateLoanCol = createDateColumn("Fecha Solicitud", "loanDate");
        TableColumn<Loan, String> dateLimitCol = createDateColumn("Fecha Límite", "dueDate");

        // Botón para Solicitar Devolución (solo APROBADOS)
        TableColumn<Loan, Void> returnCol = new TableColumn<>("Devolución");
        returnCol.setStyle("-fx-alignment: CENTER;");
        returnCol.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button returnBtn = new Button("Solicitar Devolución");

            {
                returnBtn.setStyle("-fx-background-color: #F6AD55; -fx-text-fill: white; -fx-cursor: hand;");

                returnBtn.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan.getStatus().equals("APPROVED")) {
                        if (LoanData.requestReturn(loan.getId())) {
                            showAlert("Solicitud Enviada", "Solicitud de devolución enviada al administrador.", Alert.AlertType.INFORMATION);
                            loadMyLoans();
                        }
                    } else if (loan.getStatus().equals("RETURN_REQUESTED")) {
                        showAlert("Información", "Ya enviaste una solicitud de devolución.", Alert.AlertType.WARNING);
                    } else {
                        showAlert("Error", "Este préstamo no está activo.", Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Loan loan = empty ? null : getTableView().getItems().get(getIndex());
                setGraphic(empty || loan == null || !loan.getStatus().equals("APPROVED") ? null : returnBtn);
            }
        });

        // Botón para Cancelar Solicitud (solo PENDING)
        TableColumn<Loan, Void> cancelCol = new TableColumn<>("Cancelar");
        cancelCol.setStyle("-fx-alignment: CENTER;");
        cancelCol.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button cancelBtn = new Button("Cancelar Solicitud");

            {
                cancelBtn.setStyle("-fx-background-color: #E53E3E; -fx-text-fill: white; -fx-cursor: hand;");
                cancelBtn.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan.getStatus().equals("PENDING")) {
                        loan.setStatus("REJECTED");
                        LocalDatabase.books.stream()
                                .filter(b -> b.getId() == loan.getBookId())
                                .findFirst()
                                .ifPresent(b -> b.setDisponible(true));
                        showAlert("Solicitud Cancelada", "Tu solicitud para '" + loan.getBookTitle() + "' ha sido cancelada.", Alert.AlertType.INFORMATION);
                        loadMyLoans();
                    } else {
                        showAlert("No Cancelable", "Solo puedes cancelar solicitudes pendientes.", Alert.AlertType.WARNING);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Loan loan = empty ? null : getTableView().getItems().get(getIndex());
                setGraphic(empty || loan == null || !loan.getStatus().equals("PENDING") ? null : cancelBtn);
            }
        });

        table.getColumns().addAll(idLoanCol, titleCol, statusCol, dateLoanCol, dateLimitCol, returnCol, cancelCol);
        return table;
    }

    private TableColumn<Loan, String> createDateColumn(String title, String propertyName) {
        TableColumn<Loan, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            java.time.LocalDate date = null;
            if (propertyName.equals("loanDate")) date = loan.getLoanDate();
            else if (propertyName.equals("dueDate")) date = loan.getDueDate();
            String formattedDate = (date != null) ? date.format(DATE_FORMATTER) : "N/A";
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
        return column;
    }

    private void loadMyLoans() {
        List<Loan> myLoans = LoanData.getHistoryByUserId(currentUser.getId()).stream()
                .filter(loan -> !loan.getStatus().equals("RETURNED") && !loan.getStatus().equals("REJECTED"))
                .collect(Collectors.toList());
        ObservableList<Loan> data = FXCollections.observableArrayList(myLoans);
        loansTable.setItems(data);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
