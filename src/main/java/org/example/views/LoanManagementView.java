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
import org.example.models.Loan;
import org.example.models.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoanManagementView {

    private Stage stage;
    private User currentUser;
    private TableView<Loan> loanTable;

    // Formato de fecha para la UI
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        loanTable = createLoanTable(); // Inicializar la tabla
        loadPendingRequests();         // Cargar los datos

        content.getChildren().addAll(title, subtitle, loanTable);
        return content;
    }

    private TableView<Loan> createLoanTable() {
        TableView<Loan> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(450);

        // Columnas
        TableColumn<Loan, String> idClientCol = new TableColumn<>("id Cliente");
        idClientCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idClientCol.setPrefWidth(80);

        // NOTA: Para obtener el nombre del cliente, se necesitaría un método en LoanData
        // o UserData para buscar por ID de usuario. Usaremos el ID por simplicidad.
        TableColumn<Loan, String> nameClientCol = new TableColumn<>("Nombre del Cliente");
        nameClientCol.setCellValueFactory(new PropertyValueFactory<>("userId")); // Usamos ID como marcador de posición
        nameClientCol.setPrefWidth(180);

        TableColumn<Loan, Integer> idBookCol = new TableColumn<>("Id Libro");
        idBookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        idBookCol.setPrefWidth(80);

        // NOTA: Para obtener el título, se necesitaría buscar en BookData
        TableColumn<Loan, String> titleCol = new TableColumn<>("Titulo");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookId")); // Usamos ID como marcador de posición
        titleCol.setPrefWidth(250);

        // Columna de Fecha de Préstamo (se personaliza para formato)
        TableColumn<Loan, String> dateLoanCol = new TableColumn<>("Fecha Préstamo");
        dateLoanCol.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getLoanDate().format(DATE_FORMATTER);
            return new TableCell<Loan, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formattedDate);
                }
            }.textProperty();
        });
        dateLoanCol.setPrefWidth(120);

        // Columna de Fecha Límite (se personaliza para formato)
        TableColumn<Loan, String> dateLimitCol = new TableColumn<>("Fecha Límite");
        dateLimitCol.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getDueDate().format(DATE_FORMATTER);
            return new TableCell<Loan, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formattedDate);
                }
            }.textProperty();
        });
        dateLimitCol.setPrefWidth(120);

        // Columna: Acciones (Aceptar/Rechazar)
        TableColumn<Loan, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setPrefWidth(180);
        actionsCol.setStyle("-fx-alignment: CENTER;");

        // CellFactory para los botones de Aceptar/Rechazar
        actionsCol.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final HBox buttons = new HBox(5);
            private final Button acceptBtn = new Button("Aceptar");
            private final Button rejectBtn = new Button("Rechazar");

            {
                // Estilos básicos de botones
                acceptBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-cursor: hand;");
                rejectBtn.setStyle("-fx-background-color: #E53E3E; -fx-text-fill: white; -fx-cursor: hand;");
                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(acceptBtn, rejectBtn);

                // --- LÓGICA DE GESTIÓN DE DATOS LOCALES ---
                acceptBtn.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (LoanData.approveLoan(loan.getId())) {
                        showAlert("Éxito", "Préstamo aprobado para el libro ID: " + loan.getBookId(), Alert.AlertType.INFORMATION);
                        loadPendingRequests(); // Recargar la tabla
                    } else {
                        showAlert("Error", "No se pudo aprobar el préstamo.", Alert.AlertType.ERROR);
                    }
                });

                rejectBtn.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (LoanData.rejectLoan(loan.getId())) {
                        showAlert("Éxito", "Préstamo rechazado para el libro ID: " + loan.getBookId(), Alert.AlertType.INFORMATION);
                        loadPendingRequests(); // Recargar la tabla
                    } else {
                        showAlert("Error", "No se pudo rechazar el préstamo.", Alert.AlertType.ERROR);
                    }
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

    // Método para cargar los datos de solicitudes pendientes
    private void loadPendingRequests() {
        List<Loan> pendingLoans = LoanData.getPendingRequests();
        ObservableList<Loan> data = FXCollections.observableArrayList(pendingLoans);
        loanTable.setItems(data);
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}