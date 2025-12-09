package org.example.models;

public class User {
    private String id;
    private String nombre;
    private String correo;
    private String password;
    private String rol;      // USER, ADMIN, SUPERADMIN
    private String estado;   // ACTIVO, INACTIVO

    public User(String id, String nombre, String correo, String password, String rol, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    // GETTERS
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEstado(String estado) { this.estado = estado; }
}
