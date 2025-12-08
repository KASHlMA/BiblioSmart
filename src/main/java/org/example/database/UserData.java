package org.example.database;

import org.example.models.User;
import java.util.Optional;

public class UserData {

    /**
     * Busca y valida al usuario por identificador (ID, Correo) y contraseña.
     * @return El objeto User autenticado, o null si las credenciales fallan.
     */
    public static User authenticateUser(String identifier, String password) {
        // Buscar el usuario por ID o correo
        Optional<User> foundUser = LocalDatabase.users.stream()
                .filter(user ->
                        user.getCorreo().equalsIgnoreCase(identifier) ||
                                user.getId().equalsIgnoreCase(identifier))
                .findFirst();

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            // Comparar la contraseña (asumiendo que está guardada como texto plano en LocalDatabase)
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Método para obtener un usuario por ID (útil para historial y gestión)
    public static User getUserById(String id) {
        return LocalDatabase.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}