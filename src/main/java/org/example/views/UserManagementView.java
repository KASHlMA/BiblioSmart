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
import org.example.database.UserData;
import org.example.models.User;

public class UserManagementView {

    private Stage stage;
    private User currentUser;
    private TableView<User> usersTable;

    public UserManagementView(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1C26;");

        // Top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Center content
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        return new Scene(root, 1150, 720);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #13242C;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(18, 35, 18, 35));
        topBar.setSpacing(20);

        ImageView icon = new ImageView(new Image(getClass().getResource("/org/example/images/bibliosmart.png").toExternalForm()));
        icon.setFitHeight(40);
        icon.setPreserveRatio(true);

        Label appName = new Label("BiblioSmart - Gestión de Usuarios");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 600;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("← Regresar");
        backBtn.setStyle("-fx-background-color: #0E4F6E; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            MainMenuView menuView = new MainMenuView(stage, currentUser);
            stage.setScene(menuView.getScene());
            stage.setTitle("BiblioSmart - Menú Principal");
        });

        topBar.getChildren().addAll(icon, appName, spacer, backBtn);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 50, 30, 50));
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Gestión de Usuarios");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");

        Button addUserBtn = new Button("Agregar Usuario");
        addUserBtn.setStyle("-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 20;");
        addUserBtn.setOnAction(e -> showUserForm(null)); // null = crear nuevo

        usersTable = createUsersTable();
        loadUsers();

        content.getChildren().addAll(title, addUserBtn, usersTable);
        return content;
    }

    private TableView<User> createUsersTable() {
        TableView<User> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(500);

        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<User, String> emailCol = new TableColumn<>("Correo");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("correo"));

        TableColumn<User, String> roleCol = new TableColumn<>("Rol");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        TableColumn<User, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<User, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setStyle("-fx-alignment: CENTER;");
        actionsCol.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button editBtn = new Button("Editar");
            private final Button deleteBtn = new Button("Eliminar");
            private final HBox box = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #D69E2E; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #E53E3E; -fx-text-fill: white; -fx-cursor: hand;");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showUserForm(user);
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Eliminar Usuario");
                        confirm.setHeaderText(null);
                        confirm.setContentText("¿Deseas eliminar al usuario " + user.getNombre() + "?");
                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                UserData.deleteUser(user.getId());
                                loadUsers();
                            }
                        });
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(idCol, nameCol, emailCol, roleCol, statusCol, actionsCol);
        return table;
    }

    private void loadUsers() {
        ObservableList<User> data = FXCollections.observableArrayList(UserData.getAllUsers());
        usersTable.setItems(data);
    }

    private void showUserForm(User user) {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.setTitle(user == null ? "Agregar Usuario" : "Editar Usuario");

        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        TextField idField = new TextField();
        idField.setPromptText("ID");
        if (user != null) idField.setText(user.getId());
        idField.setDisable(user != null); // no se puede cambiar ID al editar

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre");
        if (user != null) nameField.setText(user.getNombre());

        TextField emailField = new TextField();
        emailField.setPromptText("Correo");
        if (user != null) emailField.setText(user.getCorreo());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        if (user != null) passwordField.setText(user.getPassword());

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("USER", "ADMIN", "SUPERADMIN");
        if (user != null) roleBox.setValue(user.getRol());
        else roleBox.setValue("USER");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("ACTIVO", "INACTIVO");
        if (user != null) statusBox.setValue(user.getEstado());
        else statusBox.setValue("ACTIVO");

        Button saveBtn = new Button("Guardar");
        saveBtn.setStyle("-fx-background-color: #38A169; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = passwordField.getText().trim();
            String role = roleBox.getValue();
            String status = statusBox.getValue();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campos incompletos");
                alert.setHeaderText(null);
                alert.setContentText("Todos los campos son obligatorios.");
                alert.showAndWait();
                return;
            }

            if (user == null) { // Crear
                UserData.addUser(new User(id, name, email, pass, role, status));
            } else { // Editar
                user.setNombre(name);
                user.setCorreo(email);
                user.setPassword(pass);
                user.setRol(role);
                user.setEstado(status);
                UserData.updateUser(user);
            }

            loadUsers();
            dialog.close();
        });

        box.getChildren().addAll(idField, nameField, emailField, passwordField, roleBox, statusBox, saveBtn);

        Scene scene = new Scene(box, 400, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
