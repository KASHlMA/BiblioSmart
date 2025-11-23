package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.Book;
import org.example.models.User;

import java.util.List;

public class BookListView {

    private Stage stage;
    private User currentUser;
    private String categoryTitle;
    private List<Book> books;

    public BookListView(Stage stage, User user, String categoryTitle, List<Book> books) {
        this.stage = stage;
        this.currentUser = user;
        this.categoryTitle = categoryTitle;
        this.books = books;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // BARRA SUPERIOR
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // CONTENIDO PRINCIPAL
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 50, 30, 50));

        // Título
        Label title = new Label(categoryTitle);
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label(books.size() + " libros encontrados");
        subtitle.setStyle("-fx-text-fill: #8fa3ad; -fx-font-size: 16px;");

        // Lista de libros
        VBox booksList = new VBox(15);
        booksList.setPadding(new Insets(20, 0, 20, 0));

        if (books.isEmpty()) {
            Label noBooks = new Label("No hay libros disponibles en esta categoría");
            noBooks.setStyle("-fx-text-fill: #8fa3ad; -fx-font-size: 18px;");
            booksList.getChildren().add(noBooks);
        } else {
            for (Book book : books) {
                HBox bookCard = createBookCard(book);
                booksList.getChildren().add(bookCard);
            }
        }

        mainContent.getChildren().addAll(title, subtitle, booksList);

        ScrollPane scrollPane = new ScrollPane(mainContent);
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

        ImageView bookIcon = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        bookIcon.setFitHeight(40);
        bookIcon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("← Volver al Catálogo");
        backBtn.setStyle(
                "-fx-background-color: #0E4F6E; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand;"
        );

        backBtn.setOnAction(e -> {
            CatalogView catalogView = new CatalogView(stage, currentUser);
            stage.setScene(catalogView.getScene());
            stage.setTitle("BiblioSmart - Catálogo");
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, backBtn);
        return topBar;
    }

    private HBox createBookCard(Book book) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: #13242C; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);"
        );

        // Imagen del libro (placeholder)
        VBox bookCover = new VBox();
        bookCover.setPrefSize(80, 120);
        bookCover.setStyle(
                "-fx-background-color: #1A6080; " +
                        "-fx-background-radius: 8;"
        );
        Label bookEmoji = new Label("📖");
        bookEmoji.setStyle("-fx-font-size: 48px;");
        bookCover.setAlignment(Pos.CENTER);
        bookCover.getChildren().add(bookEmoji);

        // Información del libro
        VBox info = new VBox(8);
        info.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label titleLabel = new Label(book.getTitulo());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        Label authorLabel = new Label("Autor: " + book.getAutor());
        authorLabel.setStyle("-fx-text-fill: #B8CDD8; -fx-font-size: 14px;");

        Label editorialLabel = new Label("Editorial: " + book.getEditorial());
        editorialLabel.setStyle("-fx-text-fill: #8fa3ad; -fx-font-size: 13px;");

        Label statusLabel = new Label(book.isDisponible() ? "✅ Disponible" : "❌ No disponible");
        statusLabel.setStyle(
                "-fx-text-fill: " + (book.isDisponible() ? "#4CAF50" : "#ff6b6b") + "; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold;"
        );

        info.getChildren().addAll(titleLabel, authorLabel, editorialLabel, statusLabel);

        // Botón solicitar
        Button requestBtn = new Button("Solicitar Libro");
        requestBtn.setStyle(
                "-fx-background-color: " + (book.isDisponible() ? "#38A169" : "#666666") + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: " + (book.isDisponible() ? "hand" : "not-allowed") + ";"
        );

        requestBtn.setDisable(!book.isDisponible());

        requestBtn.setOnAction(e -> {
            showRequestDialog(book);
        });

        card.getChildren().addAll(bookCover, info, requestBtn);

        return card;
    }

    private void showRequestDialog(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Solicitar Préstamo");
        alert.setHeaderText("¿Deseas solicitar este libro?");
        alert.setContentText(
                "Libro: " + book.getTitulo() + "\n" +
                        "Autor: " + book.getAutor() + "\n\n" +
                        "El préstamo será registrado a tu nombre."
        );

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Aquí iría la lógica para registrar el préstamo
                book.setDisponible(false);

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Préstamo Registrado");
                success.setHeaderText("¡Solicitud exitosa!");
                success.setContentText("El libro ha sido reservado para ti.\nTienes 14 días para devolverlo.");
                success.showAndWait();

                // Recargar la vista
                stage.setScene(getScene());
            }
        });
    }
}