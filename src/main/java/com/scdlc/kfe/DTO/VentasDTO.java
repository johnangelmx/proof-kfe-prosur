package com.scdlc.kfe.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentasDTO {
    private Long idProducto;
    private Long idUsuario;
    private int cantidad;
    private LocalDate fecha;
    private BigDecimal total;

    public VentasDTO(Long idProducto, Long idUsuario, int cantidad, LocalDate fecha, BigDecimal total) {
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.total = total;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "VentasDTO{" +
                "idProducto=" + idProducto +
                ", idUsuario=" + idUsuario +
                ", cantidad=" + cantidad +
                ", fecha=" + fecha +
                ", total=" + total +
                '}';
    }
}
