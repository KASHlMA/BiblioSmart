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
// IMPORTANTE: Asegúrate de crear esta clase en org.example.models
// import org.example.models.Loan;

public class LoanManagementView {

    private Stage stage;
    private User currentUser;

    public LoanManagementView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // 1. BARRA SUPERIOR
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
        Label title = new Label("Gestión de Préstamos de Libros");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Subtítulo
        Label subtitle = new Label("Solicitudes pendientes de aprobación");
        subtitle.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 18px;");

        // Tabla de Solicitudes Pendientes
        TableView<Object> loanTable = createLoanTable();

        content.getChildren().addAll(title, subtitle, loanTable);
        return content;
    }

    // El tipo debe ser Loan cuando se cree el modelo: TableView<Loan>
    private TableView<Object> createLoanTable() {
        TableView<Object> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(450);

        // Define las columnas de la tabla (basado en la maqueta)

        TableColumn<Object, String> idClientCol = new TableColumn<>("id Cliente");
        idClientCol.setPrefWidth(80);
        // idClientCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        TableColumn<Object, String> nameClientCol = new TableColumn<>("Nombre del Cliente");
        nameClientCol.setPrefWidth(180);
        // nameClientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        TableColumn<Object, String> idBookCol = new TableColumn<>("Id Libro");
        idBookCol.setPrefWidth(80);
        // idBookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Object, String> titleCol = new TableColumn<>("Titulo");
        titleCol.setPrefWidth(250);
        // titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Object, String> dateLoanCol = new TableColumn<>("Fecha Préstamo");
        dateLoanCol.setPrefWidth(120);
        // dateLoanCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        TableColumn<Object, String> dateLimitCol = new TableColumn<>("Fecha Límite");
        dateLimitCol.setPrefWidth(120);
        // dateLimitCol.setCellValueFactory(new PropertyValueFactory<>("limitDate"));

        // Columna: Acciones (Aceptar/Rechazar)
        TableColumn<Object, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setPrefWidth(180);
        actionsCol.setStyle("-fx-alignment: CENTER;");

        // CellFactory para los botones de Aceptar/Rechazar
        actionsCol.setCellFactory(param -> new TableCell<Object, Void>() {
            private final HBox buttons = new HBox(5);
            private final Button acceptBtn = new Button("Aceptar");
            private final Button rejectBtn = new Button("Rechazar");

            {
                // Estilos básicos de botones
                acceptBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-cursor: hand;");
                rejectBtn.setStyle("-fx-background-color: #E53E3E; -fx-text-fill: white; -fx-cursor: hand;");
                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(acceptBtn, rejectBtn);

                // Lógica de acciones (Aquí irá la llamada al controlador de préstamos)
                acceptBtn.setOnAction(event -> {
                    // Lógica para ACEPTAR el préstamo
                    System.out.println("Solicitud ACEPTADA");
                });

                rejectBtn.setOnAction(event -> {
                    // Lógica para RECHAZAR el préstamo
                    System.out.println("Solicitud RECHAZADA");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(idClientCol, nameClientCol, idBookCol, titleCol, dateLoanCol, dateLimitCol, actionsCol);

        return table;
    }
}