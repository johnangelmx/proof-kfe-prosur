package com.scdlc.kfe.DTO;

public class LoginResponse {
    private String token;
    private Long idUsuario;

    public LoginResponse(String token, Long idUsuario) {
        this.token = token;
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "LoginResponse{" + "token='" + token + '\'' + ", idUsuario=" + idUsuario + '}';
    }
}
