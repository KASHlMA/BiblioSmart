package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.User;
// Se importan las nuevas clases de vista
// import org.example.views.LoanManagementView;
// import org.example.views.ReturnManagementView;
// import org.example.views.UserHistoryView;
// import org.example.views.ReportsView;
// import org.example.views.LoginView;
// import org.example.views.CatalogView;


public class MainMenuView {

    private Stage stage;
    private User currentUser;

    public MainMenuView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // BARRA SUPERIOR
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // CONTENIDO CENTRAL
        VBox centerContent = new VBox(30);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(50));

        // Título de bienvenida
        Label welcome = new Label("¡Bienvenido, " + currentUser.getNombre() + "!");
        welcome.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label subtitle = new Label("Rol: " + getRoleName(currentUser.getRol()));
        subtitle.setStyle("-fx-text-fill: #8fa3ad; -fx-font-size: 18px;");

        // Grid de opciones del menú según rol
        GridPane menuGrid = createMenuGrid();

        centerContent.getChildren().addAll(welcome, subtitle, menuGrid);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1150, 720);
        return scene;
    }

    private String getRoleName(String rol) {
        switch (rol) {
            case "SUPERADMIN": return "Super Administrador";
            case "ADMIN": return "Administrador";
            case "USER": return "Usuario";
            default: return rol;
        }
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

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.setStyle(
                "-fx-background-color: #8B1E3F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand;"
        );

        // Clic en Cerrar Sesión
        logoutBtn.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.getScene());
            stage.setTitle("BiblioSmart - Login");
        });

        topBar.getChildren().addAll(bookIcon, appName, spacer, logoutBtn);
        return topBar;
    }

    private GridPane createMenuGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(30));

        String rol = currentUser.getRol();

        if (rol.equals("USER")) {
            // USUARIO NORMAL
            Button catalogBtn = createMenuButton("📚", "Catálogo de Libros", "#0E4F6E");
            Button myLoansBtn = createMenuButton("📋", "Mis Préstamos", "#1A6080");

            grid.add(catalogBtn, 0, 0);
            grid.add(myLoansBtn, 1, 0);

            // TODO: Conectar catalogBtn a CatalogView.java
            // TODO: Conectar myLoansBtn a MyLoansView.java

        } else if (rol.equals("ADMIN")) {
            // ADMINISTRADOR - Funcionalidades conectadas aquí:
            Button catalogBtn = createMenuButton("📚", "Catálogo de Libros", "#0E4F6E");
            Button loansBtn = createMenuButton("📋", "Gestión de Préstamos", "#1A6080");
            Button returnsBtn = createMenuButton("✅", "Devoluciones", "#2C7A7B");
            Button historyBtn = createMenuButton("👥", "Historial de Usuarios", "#6B46C1");
            Button statsBtn = createMenuButton("📊", "Estadísticas", "#5A67D8");

            grid.add(catalogBtn, 0, 0);
            grid.add(loansBtn, 1, 0);
            grid.add(returnsBtn, 0, 1);
            grid.add(historyBtn, 1, 1);
            grid.add(statsBtn, 0, 2);

            // --- CONEXIÓN DE BOTONES ADMIN ---

            // 1. Catálogo de Libros
            catalogBtn.setOnAction(e -> {
                CatalogView catalogView = new CatalogView(stage, currentUser);
                stage.setScene(catalogView.getScene());
                stage.setTitle("BiblioSmart - Catálogo");
            });

            // 2. Gestión de Préstamos
            loansBtn.setOnAction(e -> {
                // Asegúrate de crear la clase LoanManagementView
                LoanManagementView loansView = new LoanManagementView(stage, currentUser);
                stage.setScene(loansView.getScene());
                stage.setTitle("BiblioSmart - Gestión de Préstamos");
            });

            // 3. Devoluciones
            returnsBtn.setOnAction(e -> {
                // Asegúrate de crear la clase ReturnManagementView
                ReturnManagementView returnsView = new ReturnManagementView(stage, currentUser);
                stage.setScene(returnsView.getScene());
                stage.setTitle("BiblioSmart - Gestión de Devoluciones");
            });

            // 4. Historial de Usuarios
            historyBtn.setOnAction(e -> {
                // Asegúrate de crear la clase UserHistoryView
                UserHistoryView historyView = new UserHistoryView(stage, currentUser);
                stage.setScene(historyView.getScene());
                stage.setTitle("BiblioSmart - Historial de Usuarios");
            });

            // 5. Estadísticas
            statsBtn.setOnAction(e -> {
                // Asegúrate de crear la clase ReportsView
                ReportsView reportsView = new ReportsView(stage, currentUser);
                stage.setScene(reportsView.getScene());
                stage.setTitle("BiblioSmart - Estadísticas y Reportes");
            });


        } else if (rol.equals("SUPERADMIN")) {
            // SUPER ADMINISTRADOR - Acceso completo
            Button catalogBtn = createMenuButton("📚", "Catálogo de Libros", "#0E4F6E");
            Button addBookBtn = createMenuButton("➕", "Registrar Libro", "#38A169");
            Button loansBtn = createMenuButton("📋", "Gestión de Préstamos", "#1A6080");
            Button returnsBtn = createMenuButton("✅", "Devoluciones", "#2C7A7B");
            Button historyBtn = createMenuButton("👥", "Historial de Usuarios", "#6B46C1");
            Button reportsBtn = createMenuButton("📊", "Reportes Completos", "#5A67D8");

            grid.add(catalogBtn, 0, 0);
            grid.add(addBookBtn, 1, 0);
            grid.add(loansBtn, 0, 1);
            grid.add(returnsBtn, 1, 1);
            grid.add(historyBtn, 0, 2);
            grid.add(reportsBtn, 1, 2);

            // TODO: Conectar los botones del SuperAdmin a sus respectivas vistas
        }

        return grid;
    }

    private Button createMenuButton(String icon, String text, String color) {
        VBox buttonContent = new VBox(10);
        buttonContent.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 48px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 600;");

        buttonContent.getChildren().addAll(iconLabel, textLabel);

        Button btn = new Button();
        btn.setGraphic(buttonContent);
        btn.setPrefSize(220, 180);
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

        // La acción temporal es eliminada para los botones del ADMIN y SuperADMIN
        // pero se mantiene para USER para evitar errores hasta que implementes sus vistas.
        if (currentUser.getRol().equals("USER")) {
            btn.setOnAction(e -> {
                System.out.println("Clic en: " + text + " (Vista en desarrollo)");
            });
        }

        return btn;
    }
}