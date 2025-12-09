package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.User;

public class SuperAdminMenuView {

    private Stage stage;
    private User currentUser;

    public SuperAdminMenuView(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public Scene getScene() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(50));

        Button addBookBtn = new Button("➕ Agregar Libro");
        Button manageBooksBtn = new Button("📚 Gestionar Libros");
        Button manageUsersBtn = new Button("👥 Gestionar Usuarios");

        addBookBtn.setPrefWidth(250);
        manageBooksBtn.setPrefWidth(250);
        manageUsersBtn.setPrefWidth(250);

        addBookBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
        manageBooksBtn.setStyle("-fx-background-color: #0E4F6E; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
        manageUsersBtn.setStyle("-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");

        addBookBtn.setOnAction(e -> {
            AddBookView addBookView = new AddBookView(stage, currentUser);
            stage.setScene(addBookView.getScene());
        });

        manageBooksBtn.setOnAction(e -> {
            BookManagementView bookManagement = new BookManagementView(stage, currentUser);
            stage.setScene(bookManagement.getScene());
        });

        manageUsersBtn.setOnAction(e -> {
            UserManagementView userManagement = new UserManagementView(stage, currentUser);
            stage.setScene(userManagement.getScene());
        });

        menu.getChildren().addAll(addBookBtn, manageBooksBtn, manageUsersBtn);

        return new Scene(menu, 1150, 720);
    }
}
