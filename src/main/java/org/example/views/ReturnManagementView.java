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
import org.example.models.User;
// IMPORTANTE: Se necesitará el modelo de Loan para llenar esta tabla
// import org.example.models.Loan;

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

        // 1. BARRA SUPERIOR (Común)
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // 2. CONTENIDO CENTRAL
        VBox centerContent = createCenterContent();

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #0B1C26; -fx-background-color: #0B1C26;");

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1150, 720);
        return scene;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #13242C;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(18, 35, 18, 35));
        topBar.setSpacing(20);

        // Icono y nombre de la App
        ImageView bookIcon = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        bookIcon.setFitHeight(40);
        bookIcon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botón Regresar (al menú principal del Admin)
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

        // Título de la página
        Label title = new Label("Gestión de Devoluciones de Libros");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Subtítulo
        Label subtitle = new Label("Confirmación de devoluciones recibidas");
        subtitle.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 18px;");

        // 3. Tabla de Solicitudes de Devolución
        TableView<Object> returnTable = createReturnTable();

        content.getChildren().addAll(title, subtitle, returnTable);
        return content;
    }

    private TableView<Object> createReturnTable() {
        TableView<Object> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(450);

        // Columnas basadas en la maqueta de Gestión de devoluciones de libros (página 21)

        TableColumn<Object, String> idLoanCol = new TableColumn<>("id préstamo");
        // idLoanCol.setCellValueFactory(new PropertyValueFactory<>("loanId"));

        TableColumn<Object, String> idUserCol = new TableColumn<>("Id usuario");
        // idUserCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        TableColumn<Object, String> idBookCol = new TableColumn<>("Id Libro");
        // idBookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Object, String> titleCol = new TableColumn<>("Titulo");
        // titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Object, String> dateLoanCol = new TableColumn<>("Fecha Préstamo");
        // dateLoanCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        TableColumn<Object, String> dateLimitCol = new TableColumn<>("Fecha Límite");
        // dateLimitCol.setCellValueFactory(new PropertyValueFactory<>("limitDate"));

        TableColumn<Object, String> dateReturnCol = new TableColumn<>("Fecha Devolución");
        // dateReturnCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Columna para la acción de Confirmar Devolución
        TableColumn<Object, Void> actionsCol = new TableColumn<>("Confirmar");
        actionsCol.setStyle("-fx-alignment: CENTER;");

        // CellFactory para el botón de Confirmar
        actionsCol.setCellFactory(param -> new TableCell<Object, Void>() {
            private final Button confirmBtn = new Button("Confirmar Devolución");

            {
                confirmBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-cursor: hand;");

                // Lógica de acciones
                confirmBtn.setOnAction(event -> {
                    // Object loan = getTableView().getItems().get(getIndex());
                    // Lógica del controlador para CONFIRMAR y actualizar el estado del libro a "Disponible"
                    System.out.println("Devolución CONFIRMADA");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(confirmBtn);
                }
            }
        });

        table.getColumns().addAll(idLoanCol, idUserCol, idBookCol, titleCol, dateLoanCol, dateLimitCol, dateReturnCol, actionsCol);

        // TODO: Cargar datos de solicitudes de devolución pendientes (LoanStatus = 'SOLICITUD_DEVOLUCION')

        return table;
    }
}