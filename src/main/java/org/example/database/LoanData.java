package org.example.database;

import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.User; // Necesaria para obtener el nombre del cliente
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LoanData {

    // Métodos de consulta y gestión usando LocalDatabase.loans

    public static List<Loan> getPendingRequests() {
        return LocalDatabase.loans.stream()
                .filter(loan -> loan.getStatus().equals("PENDING"))
                .collect(Collectors.toList());
    }

    public static List<Loan> getReturnRequests() {
        return LocalDatabase.loans.stream()
                .filter(loan -> loan.getStatus().equals("RETURN_REQUESTED"))
                .collect(Collectors.toList());
    }

    public static boolean approveLoan(int loanId) {
        Loan loan = getLoanById(loanId);
        if (loan != null && loan.getStatus().equals("PENDING")) {
            loan.setStatus("APPROVED");
            Book book = BookData.getBookById(loan.getBookId());
            if (book != null) {
                book.setEstado("Prestado");
                return true;
            }
        }
        return false;
    }

    public static boolean rejectLoan(int loanId) {
        Loan loan = getLoanById(loanId);
        if (loan != null && loan.getStatus().equals("PENDING")) {
            loan.setStatus("REJECTED");
            return true;
        }
        return false;
    }

    public static boolean confirmReturn(int loanId) {
        Loan loan = getLoanById(loanId);
        if (loan != null && loan.getStatus().equals("RETURN_REQUESTED")) {
            loan.setStatus("RETURNED");
            loan.setReturnDate(LocalDate.now());
            Book book = BookData.getBookById(loan.getBookId());
            if (book != null) {
                book.setEstado("Disponible");
                return true;
            }
        }
        return false;
    }

    public static boolean requestReturn(int loanId) {
        Loan loan = getLoanById(loanId);
        if (loan != null && loan.getStatus().equals("APPROVED")) {
            loan.setStatus("RETURN_REQUESTED");
            return true;
        }
        return false;
    }

    /**
     * Crea una nueva solicitud de préstamo con estado PENDING en la base de datos local.
     * Este método resuelve el error de compilación en LoanRequestView.
     * @param userId El ID del usuario solicitante.
     * @param bookId El ID del libro solicitado.
     */
    public static void requestLoan(String userId, int bookId) {

        // 1. Generar un nuevo ID único para el préstamo
        int newId = LocalDatabase.loans.stream()
                .mapToInt(Loan::getId)
                .max()
                .orElse(1000) + 1;

        // 2. Obtener los nombres y títulos necesarios
        User user = UserData.getUserById(userId);
        String clientName = (user != null) ? user.getNombre() : "Desconocido";

        Book book = BookData.getBookById(bookId);
        String bookTitle = (book != null) ? book.getTitulo() : "Libro Desconocido";

        // 3. Establecer las fechas
        LocalDate requestDate = LocalDate.now();
        LocalDate limitDate = LocalDate.now().plusDays(15); // Plazo de 15 días

        // 4. Crear el objeto Loan con estado PENDING
        Loan newLoan = new Loan(
                newId,
                userId,
                clientName,
                bookId,
                bookTitle,
                requestDate,
                limitDate,
                "PENDING"
        );

        // 5. Añadir a la base de datos local
        LocalDatabase.loans.add(newLoan);
    }
    // --------------------------------------

    // Método para obtener el historial completo de préstamos de un usuario
    public static List<Loan> getHistoryByUserId(String userId) {
        return LocalDatabase.loans.stream()
                .filter(loan -> loan.getUserId().equalsIgnoreCase(userId))
                .collect(Collectors.toList());
    }
    public static List<Loan> getPendingReturnRequests() {
        return LocalDatabase.loans.stream()
                .filter(l -> l.getStatus().equals("RETURN_REQUESTED"))
                .collect(Collectors.toList());
    }



    public static Loan getLoanById(int id) {
        return LocalDatabase.loans.stream()
                .filter(loan -> loan.getId() == id)
                .findFirst()
                .orElse(null);
    }
}