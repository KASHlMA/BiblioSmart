package org.example.models;

import java.time.LocalDate;

public class Loan {
    private int id;
    private String userId;
    private String clientName; // Nombre del usuario
    private int bookId;
    private String bookTitle; // Título del libro
    private LocalDate loanDate;
    private LocalDate dueDate;
    private String status;
    private LocalDate returnDate; // Fecha real de devolución

    // Constructor principal
    public Loan(int id, String userId, String clientName, int bookId, String bookTitle, LocalDate loanDate, LocalDate dueDate, String status) {
        this(id, userId, clientName, bookId, bookTitle, loanDate, dueDate, status, null);
    }

    // Constructor completo (incluyendo fecha de devolución)
    public Loan(int id, String userId, String clientName, int bookId, String bookTitle, LocalDate loanDate, LocalDate dueDate, String status, LocalDate returnDate) {
        this.id = id;
        this.userId = userId;
        this.clientName = clientName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = status;
        this.returnDate = returnDate;
    }

    // Getters y Setters (obligatorios)
    public int getId() { return id; }
    public String getUserId() { return userId; }
    public String getClientName() { return clientName; }
    public int getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setStatus(String status) { this.status = status; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}