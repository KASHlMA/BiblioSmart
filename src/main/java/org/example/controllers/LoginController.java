package org.example.controllers;

import org.example.models.User;

public class LoginController {

    private static final String SUPERADMIN_USER = "superadmin";
    private static final String SUPERADMIN_PASS = "super";

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin";

    private static final String USER_USER = "user";
    private static final String USER_PASS = "user";

    public User authenticate(String username, String password) {

        if (username.equals(SUPERADMIN_USER) && password.equals(SUPERADMIN_PASS)) {
            return new User(1, "superadmin", "Super Administrador", "SUPERADMIN");
        }

        if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
            return new User(2, "admin", "Administrador", "ADMIN");
        }

        if (username.equals(USER_USER) && password.equals(USER_PASS)) {
            return new User(3, "user", "Usuario Biblioteca", "USER");
        }

        return null;
    }
}