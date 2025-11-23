package org.example.controllers;

import javafx.fxml.FXML;

public class MainController {

    @FXML
    public void initialize() {
        System.out.println("MainView cargada correctamente.");
    }

    @FXML
    private void goHome() {
        System.out.println("Ir a inicio...");
    }

    @FXML
    private void goBooks() {
        System.out.println("Ir a libros...");
    }

    @FXML
    private void goUsers() {
        System.out.println("Ir a usuarios...");
    }

    @FXML
    private void logout() {
        System.out.println("Salir...");
    }
}
