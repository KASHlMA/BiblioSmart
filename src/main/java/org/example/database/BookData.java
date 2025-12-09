package org.example.database;

import org.example.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookData {

    // --- IMPORTANTE: ELIMINAR LA LISTA STATIC INTERNA ---
    // La lista de libros y el bloque static inicial deben moverse a LocalDatabase.java

    // Obtener todos los libros (usa LocalDatabase)
    public static List<Book> getAllBooks() {
        return new ArrayList<>(LocalDatabase.books);
    }

    // Obtener libros por categoría (usa LocalDatabase)
    public static List<Book> getBooksByCategory(String category) {
        return LocalDatabase.books.stream()
                .filter(book -> {
                    // Usamos getCategoria si es el método de tu modelo, o getGenero si usas ese.
                    // Asumiré getCategoria basado en tu código.
                    return book.getCategoria().equalsIgnoreCase(category);
                })
                .collect(Collectors.toList());
    }

    // Obtener libros disponibles (usa LocalDatabase)
    public static List<Book> getAvailableBooks() {
        return LocalDatabase.books.stream()
                .filter(Book::isDisponible)
                .collect(Collectors.toList());
    }

    // Buscar libros por título (usa LocalDatabase)
    public static List<Book> searchByTitle(String query) {
        return LocalDatabase.books.stream()
                .filter(book -> book.getTitulo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar libros por autor (usa LocalDatabase)
    public static List<Book> searchByAuthor(String query) {
        return LocalDatabase.books.stream()
                .filter(book -> book.getAutor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Obtener todas las categorías únicas (usa LocalDatabase)
    public static List<String> getAllCategories() {
        return LocalDatabase.books.stream()
                .map(Book::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Obtener un libro por ID (usa LocalDatabase)
    public static Book getBookById(int id) {
        return LocalDatabase.books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // --- NUEVOS MÉTODOS REQUERIDOS ---

    /**
     * Registra un nuevo libro en la base de datos local (usado por SuperAdmin).
     * Asigna un ID al libro antes de añadirlo.
     */
    public static void removeBook(int bookId) {
     LocalDatabase.books.removeIf(book -> book.getId() == bookId);

     }

    public static void addBook(Book newBook) {
        // Genera un nuevo ID basado en el máximo actual + 1
        int newId = LocalDatabase.books.stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(20) + 1; // 20 es el último ID en tus datos de prueba

        newBook.setId(newId);
        LocalDatabase.books.add(newBook);
    }

    /**
     * Actualiza la disponibilidad de un libro por ID (usado por LoanData).
     */
    public static void updateBookAvailability(int bookId, boolean isAvailable) {
        LocalDatabase.books.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst()
                .ifPresent(book -> {
                    book.setDisponible(isAvailable);
                    // Opcional: Si el modelo Book tiene el método setEstado, úsalo aquí
                    // book.setEstado(isAvailable ? "Disponible" : "Prestado");
                });
    }
}