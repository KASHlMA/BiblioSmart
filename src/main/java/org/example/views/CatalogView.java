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
import org.example.models.User;

import java.util.List;

public class CatalogView {

    private Stage stage;
    private User currentUser;

    public CatalogView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
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
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Título
        Label title = new Label("Catálogo de Libros por Género");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Barra de búsqueda
        HBox searchBar = createSearchBar();

        // Grid de categorías
        GridPane categoriesGrid = createCategoriesGrid();

        mainContent.getChildren().addAll(title, searchBar, categoriesGrid);

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

        Button backBtn = new Button("← Volver al Menú");
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

    private HBox createSearchBar() {
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10));

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por título o autor...");
        searchField.setPrefWidth(400);
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

        Button searchBtn = new Button("🔍 Buscar");
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
                // Ir a vista de resultados de búsqueda
                BookListView bookListView = new BookListView(stage, currentUser, "Resultados de búsqueda: " + query,
                        BookData.searchByTitle(query));
                stage.setScene(bookListView.getScene());
            }
        });

        searchField.setOnAction(e -> searchBtn.fire());

        searchBox.getChildren().addAll(searchField, searchBtn);
        return searchBox;
    }

    private GridPane createCategoriesGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(20));

        List<String> categories = BookData.getAllCategories();

        // Emojis por categoría
        String[] emojis = {"📖", "🚀", "🔍", "📜", "🎨", "🧠"};
        String[] colors = {"#0E4F6E", "#1A6080", "#2C7A7B", "#5A67D8", "#6B46C1", "#38A169"};

        int col = 0;
        int row = 0;

        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            String emoji = i < emojis.length ? emojis[i] : "📚";
            String color = i < colors.length ? colors[i] : "#0E4F6E";

            Button categoryBtn = createCategoryButton(emoji, category, color);
            grid.add(categoryBtn, col, row);

            col++;
            if (col > 2) { // 3 columnas
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private Button createCategoryButton(String emoji, String category, String color) {
        VBox buttonContent = new VBox(15);
        buttonContent.setAlignment(Pos.CENTER);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 48px;");

        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 600;");
        categoryLabel.setWrapText(true);
        categoryLabel.setMaxWidth(200);
        categoryLabel.setAlignment(Pos.CENTER);

        buttonContent.getChildren().addAll(emojiLabel, categoryLabel);

        Button btn = new Button();
        btn.setGraphic(buttonContent);
        btn.setPrefSize(240, 180);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: derive(" + color + ", 20%); " +
                            "-fx-background-radius: 20; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 8); " +
                            "-fx-scale-x: 1.05; " +
                            "-fx-scale-y: 1.05;"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: " + color + "; " +
                            "-fx-background-radius: 20; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
            );
        });

        btn.setOnAction(e -> {
            // Ir a la lista de libros de esa categoría
            BookListView bookListView = new BookListView(stage, currentUser, category,
                    BookData.getBooksByCategory(category));
            stage.setScene(bookListView.getScene());
            stage.setTitle("BiblioSmart - " + category);
        });

        return btn;
    }
}