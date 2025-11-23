package org.example.database;

import org.example.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookData {

    private static List<Book> books = new ArrayList<>();

    static {
        // Libros de FANTASÍA
        books.add(new Book(1, "El Señor de los Anillos", "J.R.R. Tolkien", "Minotauro", "Fantasía", true, "978-8445071796"));
        books.add(new Book(2, "Harry Potter y la Piedra Filosofal", "J.K. Rowling", "Salamandra", "Fantasía", true, "978-8478884452"));
        books.add(new Book(3, "El Hobbit", "J.R.R. Tolkien", "Minotauro", "Fantasía", false, "978-8445073711"));
        books.add(new Book(4, "Juego de Tronos", "George R.R. Martin", "Gigamesh", "Fantasía", true, "978-8496208537"));

        // Libros de CIENCIA FICCIÓN
        books.add(new Book(5, "1984", "George Orwell", "Debolsillo", "Ciencia Ficción", true, "978-8499890944"));
        books.add(new Book(6, "Dune", "Frank Herbert", "Debolsillo", "Ciencia Ficción", true, "978-8497593762"));
        books.add(new Book(7, "Fundación", "Isaac Asimov", "Debolsillo", "Ciencia Ficción", false, "978-8497593773"));
        books.add(new Book(8, "Fahrenheit 451", "Ray Bradbury", "Debolsillo", "Ciencia Ficción", true, "978-8497593786"));

        // Libros de NOVELA NEGRA
        books.add(new Book(9, "El Código Da Vinci", "Dan Brown", "Planeta", "Novela Negra", true, "978-8408163251"));
        books.add(new Book(10, "La Chica del Tren", "Paula Hawkins", "Planeta", "Novela Negra", true, "978-8408163268"));
        books.add(new Book(11, "El Silencio de los Corderos", "Thomas Harris", "Debolsillo", "Novela Negra", true, "978-8497593799"));

        // Libros de HISTORIA
        books.add(new Book(12, "Sapiens", "Yuval Noah Harari", "Debate", "Historia", true, "978-8499926711"));
        books.add(new Book(13, "Homo Deus", "Yuval Noah Harari", "Debate", "Historia", true, "978-8499926728"));
        books.add(new Book(14, "Una Historia de España", "Arturo Pérez-Reverte", "Alfaguara", "Historia", false, "978-8420414935"));

        // Libros INFANTIL Y JUVENIL
        books.add(new Book(15, "El Principito", "Antoine de Saint-Exupéry", "Salamandra", "Infantil y Juvenil", true, "978-8498381498"));
        books.add(new Book(16, "Matilda", "Roald Dahl", "Alfaguara", "Infantil y Juvenil", true, "978-8420482699"));
        books.add(new Book(17, "Percy Jackson y el Ladrón del Rayo", "Rick Riordan", "Salamandra", "Infantil y Juvenil", true, "978-8498386752"));

        // Libros de AUTOAYUDA Y PSICOLOGÍA
        books.add(new Book(18, "El Poder del Ahora", "Eckhart Tolle", "Gaia", "Autoayuda y Psicología", true, "978-8484453949"));
        books.add(new Book(19, "Inteligencia Emocional", "Daniel Goleman", "Kairós", "Autoayuda y Psicología", true, "978-8472453715"));
        books.add(new Book(20, "El Hombre en Busca de Sentido", "Viktor Frankl", "Herder", "Autoayuda y Psicología", true, "978-8425432781"));
    }

    // Obtener todos los libros
    public static List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Obtener libros por categoría
    public static List<Book> getBooksByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategoria().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // Obtener libros disponibles
    public static List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isDisponible)
                .collect(Collectors.toList());
    }

    // Buscar libros por título
    public static List<Book> searchByTitle(String query) {
        return books.stream()
                .filter(book -> book.getTitulo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar libros por autor
    public static List<Book> searchByAuthor(String query) {
        return books.stream()
                .filter(book -> book.getAutor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Obtener todas las categorías únicas
    public static List<String> getAllCategories() {
        return books.stream()
                .map(Book::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Obtener un libro por ID
    public static Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }
}