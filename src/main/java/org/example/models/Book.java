package org.example.models;

public class Book {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String categoria;
    private boolean disponible;
    private String isbn;

    // Atributos adicionales que otras vistas pueden usar
    private String estado;

    // Constructor (adaptado al formato de tu BookData)
    public Book(int id, String titulo, String autor, String editorial, String categoria, boolean disponible, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.categoria = categoria;
        this.disponible = disponible;
        this.isbn = isbn;
        this.estado = disponible ? "Disponible" : "Prestado";
    }

    // --- GETTERS (Necesarios para la tabla y lectura) ---
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }
    public String getIsbn() { return isbn; }
    public String getEstado() { return estado; }

    // --- SETTERS (Necesarios para la Gestión de Datos) ---
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    // 1. Método faltante para resolver el error de BookData.addBook()
    public void setId(int id) {
        this.id = id;
    }

    // 2. Métodos para LoanData para cambiar la disponibilidad
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        this.estado = disponible ? "Disponible" : "Prestado";
    }

    public void setEstado(String estado) {
        this.estado = estado;
        this.disponible = estado.equals("Disponible");
    }
}