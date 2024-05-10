package com.scdlc.kfe.DTO;

import java.util.Date;

public class VentasDTO {
    private Long idProducto;
    private Long idUsuario;
    private int cantidad;
    private Date fecha;

    public VentasDTO(Long idProducto, Long idUsuario, int cantidad, Date fecha) {
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "VentasDTO{" + "idProducto=" + idProducto + ", idUsuario=" + idUsuario + ", cantidad=" + cantidad + ", fecha=" + fecha + '}';
    }
}
