package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.database.BookData;
import org.example.models.Book;
import org.example.models.User;

public class AddBookView {

    private Stage stage;
    private User currentUser;

    public AddBookView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // 1. BARRA SUPERIOR
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // 2. CONTENIDO CENTRAL (Formulario)
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

        ImageView bookIcon = new ImageView(
                new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm())
        );
        bookIcon.setFitHeight(40);
        bookIcon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botón Regresar (al menú principal)
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
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(600);

        // Título (Agregar nuevo libro)
        Label title = new Label("Agregar Nuevo Libro");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        // Formulario (Maqueta, página 18)
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setPadding(new Insets(20));
        formGrid.setStyle("-fx-background-color: #13242C; -fx-background-radius: 10;");

        // Campos de entrada
        // Creamos los contenedores para poder añadirlos al Grid
        VBox titleContainer = createTextField("Título del Libro:", "titleField");
        VBox authorContainer = createTextField("Autor:", "authorField");
        VBox editorialContainer = createTextField("Editorial:", "editorialField");
        VBox yearContainer = createTextField("Año de Publicacion:", "yearField");
        VBox genreContainer = createTextField("Genero:", "genreField");
        VBox themeContainer = createTextField("Tema:", "themeField");
        VBox pagesContainer = createTextField("N° de Páginas:", "pagesField");
        VBox isbnContainer = createTextField("ISBN:", "isbnField");

        // Obtenemos las referencias a los TextFields desde sus contenedores
        TextField titleField = (TextField) titleContainer.lookup("#titleField");
        TextField authorField = (TextField) authorContainer.lookup("#authorField");
        TextField editorialField = (TextField) editorialContainer.lookup("#editorialField");
        TextField yearField = (TextField) yearContainer.lookup("#yearField");
        TextField genreField = (TextField) genreContainer.lookup("#genreField");
        TextField themeField = (TextField) themeContainer.lookup("#themeField");
        TextField pagesField = (TextField) pagesContainer.lookup("#pagesField");
        TextField isbnField = (TextField) isbnContainer.lookup("#isbnField");

        // Colocación en el Grid
        formGrid.add(titleContainer, 0, 0, 2, 1);
        formGrid.add(authorContainer, 0, 1, 2, 1);
        formGrid.add(editorialContainer, 0, 2, 2, 1);
        formGrid.add(yearContainer, 0, 3, 2, 1);
        formGrid.add(genreContainer, 0, 4, 2, 1);
        formGrid.add(themeContainer, 0, 5, 2, 1);

        // N° de Páginas / ISBN en la misma fila
        HBox pagesIsbnBox = new HBox(20);
        pagesIsbnBox.getChildren().addAll(pagesContainer, isbnContainer);
        formGrid.add(pagesIsbnBox, 0, 6, 2, 1);

        // Botón Final
        Button registerBtn = new Button("➕ Agregar Libro");
        registerBtn.setPrefWidth(Double.MAX_VALUE);
        registerBtn.setStyle(
                "-fx-background-color: #38A169; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );

        registerBtn.setOnAction(e -> handleRegistration(
                titleField,
                authorField,
                editorialField,
                yearField,
                genreField,
                themeField,
                pagesField,
                isbnField
        ));

        VBox formContainer = new VBox(20);
        formContainer.getChildren().addAll(formGrid, registerBtn);

        content.getChildren().addAll(title, formContainer);
        return content;
    }

    // Helper para crear etiquetas y campos de texto
    private VBox createTextField(String labelText, String fieldId) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white;");
        TextField field = new TextField();
        field.setId(fieldId); // Asignamos un ID para poder referenciarlo
        field.setStyle("-fx-background-color: #0B1C26; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");

        VBox container = new VBox(5, label, field);
        container.setPadding(new Insets(5, 0, 5, 0));

        return container; // Retornamos el contenedor VBox completo
    }

    // Lógica para manejar el registro
    private void handleRegistration(TextField titleF, TextField authorF, TextField editorialF, TextField yearF, TextField genreF, TextField themeF, TextField pagesF, TextField isbnF) {

        // 1. Obtener y validar datos
        String title = titleF.getText().trim();
        String author = authorF.getText().trim();
        String editorial = editorialF.getText().trim();
        String year = yearF.getText().trim();
        String genre = genreF.getText().trim();
        String isbn = isbnF.getText().trim();
        String pages = pagesF.getText().trim(); // Se permite vacío

        if (title.isEmpty() || author.isEmpty() || editorial.isEmpty() || year.isEmpty() || genre.isEmpty() || isbn.isEmpty()) {
            showAlert("Error de Validación", "Por favor, complete todos los campos obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int yearInt = Integer.parseInt(year);
            // int pagesInt = pages.isEmpty() ? 0 : Integer.parseInt(pages); // Si quieres validar páginas

            // 2. Crear el objeto Book (usando el constructor simple de tu BookData)
            Book newBook = new Book(
                    0, // ID 0, ya que BookData.addBook() asignará uno nuevo
                    title,
                    author,
                    editorial,
                    genre, // Usando 'genre' como categoría
                    true,  // Disponible = true por defecto
                    isbn
            );

            // 3. Registrar en la base de datos local
            BookData.addBook(newBook);

            showAlert("Registro Exitoso", "El libro '" + title + "' ha sido registrado exitosamente.", Alert.AlertType.INFORMATION);

            // 4. Navegar de vuelta al menú principal
            MainMenuView menuView = new MainMenuView(stage, currentUser);
            stage.setScene(menuView.getScene());
            stage.setTitle("BiblioSmart - Menú Principal");

        } catch (NumberFormatException e) {
            showAlert("Error de Formato", "El Año de Publicación debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error del Sistema", "Ocurrió un error al registrar el libro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}