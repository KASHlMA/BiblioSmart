package org.example.views;

import javafx.collections.FXCollections;
import org.example.database.LocalDatabase;
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
import org.example.database.BookData;
import org.example.models.Book;
import org.example.models.User;

public class BookManagementView {

    private Stage stage;
    private User currentUser;
    private TableView<Book> table;

    public BookManagementView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // Barra superior
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Contenido principal
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Label title = new Label("Gestión de Libros");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        table = createTable();
        loadTableData();

        mainContent.getChildren().addAll(title, table);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1150, 720);
        return scene;
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

        Button backBtn = new Button("← Volver al Menú");
        backBtn.setStyle(
                "-fx-background-color: #0E4F6E; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> {
            MainMenuView menuView = new MainMenuView(stage, currentUser);
            stage.setScene(menuView.getScene());
            stage.setTitle("BiblioSmart - Menú Principal");
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, backBtn);
        return topBar;
    }

    private TableView<Book> createTable() {
        TableView<Book> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Título");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Autor");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Book, String> editorialCol = new TableColumn<>("Editorial");
        editorialCol.setCellValueFactory(new PropertyValueFactory<>("editorial"));

        TableColumn<Book, String> categoryCol = new TableColumn<>("Categoría");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Book, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Book, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Editar");
            private final Button deleteBtn = new Button("Eliminar");
            private final HBox container = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #E53E3E; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");

                editBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    openEditDialog(book);
                });

                deleteBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    deleteBook(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        tableView.getColumns().addAll(idCol, titleCol, authorCol, editorialCol, categoryCol, statusCol, actionsCol);
        return tableView;
    }

    private void loadTableData() {
        ObservableList<Book> data = FXCollections.observableArrayList(BookData.getAllBooks());
        table.setItems(data);
    }

    private void openEditDialog(Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Editar Libro");
        dialog.setHeaderText("Modificar información del libro");

        // Botones
        ButtonType saveButton = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField(book.getTitulo());
        TextField authorField = new TextField(book.getAutor());
        TextField editorialField = new TextField(book.getEditorial());
        TextField categoryField = new TextField(book.getCategoria());
        TextField isbnField = new TextField(book.getIsbn());

        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Editorial:"), 0, 2);
        grid.add(editorialField, 1, 2);
        grid.add(new Label("Categoría:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("ISBN:"), 0, 4);
        grid.add(isbnField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                book.setTitulo(titleField.getText());
                book.setAutor(authorField.getText());
                book.setEditorial(editorialField.getText());
                book.setCategoria(categoryField.getText());
                book.setIsbn(isbnField.getText());
                // Actualizar disponibilidad automáticamente
                BookData.updateBookAvailability(book.getId(), book.isDisponible());
                loadTableData();
                return book;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteBook(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Libro");
        alert.setHeaderText("¿Deseas eliminar el libro '" + book.getTitulo() + "'?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LocalDatabase.books.remove(book);
                loadTableData();
            }
        });
    }
}
