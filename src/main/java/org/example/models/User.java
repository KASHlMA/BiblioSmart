package org.example.models;

public class User {
    private String id;
    private String nombre;
    private String correo;
    private String password;
    private String rol; // USER, ADMIN, SUPERADMIN

    public User(String id, String nombre, String correo, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    // Getters (correctos)
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }

    // Setter (opcional, pero útil)
    public void setId(String id) { this.id = id; }
}