package org.example.controllers;

import org.example.database.UserData;
import org.example.models.User;
// ... otras importaciones si las tienes

public class LoginController {

    /**
     * Maneja el intento de login verificando las credenciales contra la base de datos local.
     * @param identifier ID o Correo ingresado por el usuario.
     * @param password Contraseña ingresada.
     * @return El objeto User si el login es exitoso, o null en caso contrario.
     */
    public User handleLogin(String identifier, String password) {

        User authenticatedUser = UserData.authenticateUser(identifier, password);

        if (authenticatedUser != null) {
            System.out.println("Login exitoso para: " + authenticatedUser.getNombre() + " (" + authenticatedUser.getRol() + ")");
            return authenticatedUser;
        } else {
            System.out.println("Login fallido: Credenciales inválidas.");
            return null;
        }
    }
}