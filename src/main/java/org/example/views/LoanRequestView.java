package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.database.LoanData;
import org.example.models.Book;
import org.example.models.User;

public class LoanRequestView {

    private Stage stage;
    private User currentUser;
    private Book selectedBook;

    public LoanRequestView(Stage stage, User user, Book book) {
        this.stage = stage;
        this.currentUser = user;
        this.selectedBook = book;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // BARRA SUPERIOR (Común)
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // CONTENIDO CENTRAL
        VBox centerContent = createCenterContent();
        centerContent.setAlignment(Pos.CENTER);

        root.setCenter(centerContent);

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

        Button closeBtn = new Button("X"); // Botón para cerrar la solicitud
        closeBtn.setStyle(
                "-fx-background-color: #8B1E3F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 5 10; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        closeBtn.setOnAction(e -> {
            // Regresar al catálogo o BookListView
            // Lógica para volver a la pantalla anterior
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, closeBtn);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(500);
        content.setStyle("-fx-background-color: #13242C; -fx-background-radius: 10;");

        Label title = new Label("Préstamo de Libros");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        // Información del Libro Solicitado
        VBox bookInfo = new VBox(5);
        bookInfo.setAlignment(Pos.CENTER_LEFT);
        bookInfo.setStyle("-fx-padding: 15; -fx-background-color: #0B1C26; -fx-background-radius: 5;");

        Label bookTitle = new Label(selectedBook.getTitulo());
        bookTitle.setStyle("-fx-text-fill: #9AE6B4; -fx-font-size: 18px; -fx-font-weight: 600;");

        Label bookDetails = new Label("Autor: " + selectedBook.getAutor() +
                " | ISBN: " + selectedBook.getIsbn() +
                " | ID: " + selectedBook.getId());
        bookDetails.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 12px;");

        Label statusLabel = new Label("Estado: " + selectedBook.getEstado());
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        bookInfo.getChildren().addAll(bookTitle, bookDetails, statusLabel);

        // Formulario

        TextField userIdField = new TextField(currentUser.getId()); // ID de Usuario prellenado
        userIdField.setPromptText("ID de Usuario");
        userIdField.setEditable(false); // No se puede cambiar
        userIdField.setStyle("-fx-background-color: #0B1C26; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");

        TextField addressField = new TextField();
        addressField.setPromptText("Dirección de Contacto (Obligatorio)");
        addressField.setStyle("-fx-background-color: #0B1C26; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");

        // Botón de Solicitud
        Button requestBtn = new Button("Solicitar Libro");
        requestBtn.setPrefWidth(Double.MAX_VALUE);
        requestBtn.setStyle(
                "-fx-background-color: #38A169; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand;"
        );

        // ACCIÓN DE SOLICITUD
        requestBtn.setOnAction(e -> {
            String address = addressField.getText().trim();
            if (address.isEmpty()) {
                showAlert("Error", "Por favor, ingrese una dirección de contacto.", Alert.AlertType.ERROR);
                return;
            }
            if (!selectedBook.isDisponible()) {
                showAlert("Error", "El libro no está disponible actualmente.", Alert.AlertType.WARNING);
                return;
            }

            LoanData.requestLoan(currentUser.getId(), selectedBook.getId());
            showAlert("Solicitud Enviada con Éxito",
                    "Tu solicitud para '" + selectedBook.getTitulo() +
                            "' ha sido enviada. Espera la aprobación del administrador.",
                    Alert.AlertType.INFORMATION);

            // Regresar al Catálogo
            CatalogView catalogView = new CatalogView(stage, currentUser);
            stage.setScene(catalogView.getScene());
        });

        content.getChildren().addAll(title, bookInfo, userIdField, addressField, requestBtn);
        return content;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}