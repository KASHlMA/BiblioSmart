package org.example.database;

import org.example.models.User;

import java.util.List;
import java.util.Optional;

public class UserData {

    // Autenticación
    public static User authenticateUser(String identifier, String password) {
        Optional<User> foundUser = LocalDatabase.users.stream()
                .filter(user -> user.getCorreo().equalsIgnoreCase(identifier) ||
                        user.getId().equalsIgnoreCase(identifier))
                .findFirst();

        if (foundUser.isPresent() && foundUser.get().getPassword().equals(password)) {
            return foundUser.get();
        }
        return null;
    }

    public static User getUserById(String id) {
        return LocalDatabase.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Nuevo: Obtener todos los usuarios
    public static List<User> getAllUsers() {
        return LocalDatabase.users;
    }

    // Nuevo: Agregar usuario
    public static void addUser(User user) {
        LocalDatabase.users.add(user);
    }

    // Nuevo: Actualizar usuario
    public static void updateUser(User updatedUser) {
        User existingUser = getUserById(updatedUser.getId());
        if (existingUser != null) {
            existingUser.setNombre(updatedUser.getNombre());
            existingUser.setCorreo(updatedUser.getCorreo());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRol(updatedUser.getRol());
            existingUser.setEstado(updatedUser.getEstado());
        }
    }

    // Nuevo: Eliminar usuario
    public static boolean deleteUser(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            return LocalDatabase.users.remove(user);
        }
        return false;
    }
}
