package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.database.LocalDatabase;
import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.User;

import java.time.LocalDate;

public class RequestLoanView {

    private Stage stage;
    private User currentUser;

    public RequestLoanView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // 1. Top Bar
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // 2. Center Content
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(30));

        Label title = new Label("Solicitar Préstamo");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        TableView<Book> table = createBookTable();
        centerContent.getChildren().addAll(title, table);

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

    private TableView<Book> createBookTable() {
        TableView<Book> table = new TableView<>();
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columnas
        TableColumn<Book, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Autor");
        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAutor()));

        TableColumn<Book, String> categoryCol = new TableColumn<>("Categoría");
        categoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria()));

        TableColumn<Book, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));

        table.getColumns().addAll(titleCol, authorCol, categoryCol, statusCol);

        // Datos filtrados (solo disponibles)
        ObservableList<Book> availableBooks = FXCollections.observableArrayList();
        for (Book b : LocalDatabase.books) {
            if (b.isDisponible()) {
                availableBooks.add(b);
            }
        }
        table.setItems(availableBooks);

        // Botón de solicitud
        table.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Book selectedBook = row.getItem();
                    handleLoanRequest(selectedBook);
                }
            });
            return row;
        });

        return table;
    }

    private void handleLoanRequest(Book book) {
        // Confirmación
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Préstamo");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Deseas solicitar el libro '" + book.getTitulo() + "'?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Crear préstamo PENDING
                Loan newLoan = new Loan(
                        LocalDatabase.loans.size() + 1001,
                        currentUser.getId(),
                        currentUser.getNombre(),
                        book.getId(),
                        book.getTitulo(),
                        LocalDate.now(),
                        LocalDate.now().plusDays(15),
                        "PENDING"
                );

                LocalDatabase.loans.add(newLoan);
                book.setDisponible(false); // Cambiar estado del libro

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Solicitud Enviada");
                done.setHeaderText(null);
                done.setContentText("Tu solicitud para '" + book.getTitulo() + "' ha sido enviada.");
                done.showAndWait();

                // Refrescar vista
                stage.setScene(getScene());
            }
        });
    }
}
