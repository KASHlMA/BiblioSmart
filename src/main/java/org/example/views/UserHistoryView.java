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
// Se necesitará importar el modelo de Préstamo (Loan)
// import org.example.models.Loan;

public class UserHistoryView {

    private Stage stage;
    private User currentUser;

    public UserHistoryView(Stage stage, User user) {
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

        // Título de la página (Maqueta de Historial de usuario)
        Label title = new Label("Historial de Préstamos por Usuario");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Barra de búsqueda de usuario
        HBox searchBox = createSearchUserBar();

        // 3. Tabla de Historial (para el usuario seleccionado)
        TableView<Object> historyTable = createHistoryTable();

        content.getChildren().addAll(title, searchBox, historyTable);
        return content;
    }

    private HBox createSearchUserBar() {
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10, 0, 10, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar ID o Nombre de Usuario...");
        searchField.setPrefWidth(350);
        searchField.setStyle(
                "-fx-background-color: #13242C; " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: #7A8E9C; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #547082; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 1;"
        );

        Button searchBtn = new Button("🔍 Buscar Usuario");
        searchBtn.setStyle(
                "-fx-background-color: #1A6080; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand;"
        );

        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                // TODO: Llamar al controlador para cargar el historial del usuario
                System.out.println("Buscando historial para: " + query);
            }
        });

        searchBox.getChildren().addAll(searchField, searchBtn);
        return searchBox;
    }

    // Método para crear la estructura de la tabla de historial
    private TableView<Object> createHistoryTable() {
        TableView<Object> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        // Columnas basadas en la maqueta de Historial de usuario (página 33)

        TableColumn<Object, String> idLoanCol = new TableColumn<>("id préstamo");
        // idLoanCol.setCellValueFactory(new PropertyValueFactory<>("loanId"));

        TableColumn<Object, String> bookCol = new TableColumn<>("Libro");
        // bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Object, String> dateLoanCol = new TableColumn<>("Fecha Préstamo");
        // dateLoanCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        TableColumn<Object, String> dateLimitCol = new TableColumn<>("Fecha Límite");
        // dateLimitCol.setCellValueFactory(new PropertyValueFactory<>("limitDate"));

        TableColumn<Object, String> dateReturnCol = new TableColumn<>("Fecha Devolución");
        // dateReturnCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        table.getColumns().addAll(idLoanCol, bookCol, dateLoanCol, dateLimitCol, dateReturnCol);

        // TODO: Lógica para llenar la tabla con el historial del usuario buscado.

        return table;
    }
}