package org.example.views;

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
import org.example.models.Loan;
import org.example.models.User;

public class ReturnManagementView {

    private Stage stage;
    private User currentUser;

    public ReturnManagementView(Stage stage, User user) {
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
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, backBtn);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 50, 30, 50));
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Gestión de Devoluciones de Libros");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("Confirmación de devoluciones recibidas");
        subtitle.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 18px;");

        TableView<Loan> returnTable = createReturnTable();

        content.getChildren().addAll(title, subtitle, returnTable);
        return content;
    }

    private TableView<Loan> createReturnTable() {

        TableView<Loan> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(450);

        // COLUMNAS
        TableColumn<Loan, Integer> idLoanCol = new TableColumn<>("ID Préstamo");
        idLoanCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Loan, String> idUserCol = new TableColumn<>("ID Usuario");
        idUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Loan, Integer> idBookCol = new TableColumn<>("ID Libro");
        idBookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Loan, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Loan, String> dateLoanCol = new TableColumn<>("Fecha Préstamo");
        dateLoanCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        TableColumn<Loan, String> dateLimitCol = new TableColumn<>("Fecha Límite");
        dateLimitCol.setCellValueFactory(new PropertyValueFactory<>("limitDate"));

        TableColumn<Loan, String> dateReturnCol = new TableColumn<>("Fecha Devolución");
        dateReturnCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // COLUMNA DE BOTÓN
        TableColumn<Loan, Void> actionsCol = new TableColumn<>("Confirmar");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button confirmBtn = new Button("Confirmar Devolución");

            {
                confirmBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-cursor: hand;");

                confirmBtn.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());

                    boolean success = LoanData.confirmReturn(loan.getId());

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Devolución registrada");
                        alert.setHeaderText(null);
                        alert.setContentText("La devolución del libro fue confirmada.");
                        alert.showAndWait();

                        // Recargar la tabla
                        getTableView().getItems().remove(loan);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : confirmBtn);
            }
        });

        table.getColumns().addAll(
                idLoanCol, idUserCol, idBookCol, titleCol,
                dateLoanCol, dateLimitCol, dateReturnCol, actionsCol
        );

        // CARGAR DATOS reales desde LoanData
        table.getItems().setAll(LoanData.getReturnRequests());

        return table;
    }
}
