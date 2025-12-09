package org.example.database;

import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocalDatabase {

    public static List<Book> books = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    public static List<Loan> loans = new ArrayList<>();
    private static int nextLoanId = 1001; // ID inicial para nuevos préstamos

    // Bloque estático para inicializar datos de prueba
    static {
        // --- DATOS DE USUARIO ---
        users.add(new User("U-04", "Erick Montiel Campos", "user", "root", "USER", "ACTIVO"));
        users.add(new User("A-01", "Admin General", "admin", "admin", "ADMIN", "ACTIVO"));
        users.add(new User("S-01", "Super Admin", "super", "super", "SUPERADMIN", "ACTIVO"));
        // Contraseña "super"

        // --- DATOS DE LIBROS (Usando el constructor simple de tu BookData) ---
        books.add(new Book(1, "El Señor de los Anillos", "J.R.R. Tolkien", "Minotauro", "Fantasía", true, "978-8445071796"));
        books.add(new Book(3, "El Hobbit", "J.R.R. Tolkien", "Minotauro", "Fantasía", false, "978-8445073711")); // Prestado
        books.add(new Book(5, "1984", "George Orwell", "Debolsillo", "Ciencia Ficción", true, "978-8499890944"));
        books.add(new Book(9, "El Código Da Vinci", "Dan Brown", "Planeta", "Novela Negra", true, "978-8408163251"));
        books.add(new Book(12, "Sapiens", "Yuval Noah Harari", "Debate", "Historia", true, "978-8499926711"));
        books.add(new Book(15, "El Principito", "Antoine de Saint-Exupéry", "Salamandra", "Infantil y Juvenil", true, "978-8498381498"));

        // --- DATOS DE PRÉSTAMOS (SIMULANDO PENDIENTES Y ACTIVOS) ---

        // 1. Solicitud Pendiente (Para LoanManagementView)
        loans.add(new Loan(nextLoanId++,
                "U-04", "Erick Montiel Campos",
                5, "1984",
                LocalDate.now(), LocalDate.now().plusDays(15),
                "PENDING"));

        // 2. Préstamo Aprobado Activo (Para Historial)
        loans.add(new Loan(nextLoanId++,
                "U-04", "Erick Montiel Campos",
                1, "El Señor de los Anillos",
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(10),
                "APPROVED"));

        // 3. Solicitud de Devolución (Para ReturnManagementView)
        loans.add(new Loan(nextLoanId++,
                "U-04", "Erick Montiel Campos",
                9, "El Código Da Vinci",
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(4),
                "RETURN_REQUESTED"));

        // 4. Préstamo Devuelto (Para Historial)
        loans.add(new Loan(nextLoanId++,
                "U-04", "Erick Montiel Campos",
                15, "El Principito",
                LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 15),
                "RETURNED", LocalDate.of(2025, 10, 14)));
    }
}