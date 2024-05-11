package com.scdlc.kfe.DTO;

public class Login {
    private String email;
    private String contrasena;

    public Login(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
