package com.scdlc.kfe.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private int cantidad;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Venta(Long id, Producto producto, Usuario usuario, int cantidad, Date fecha) {
        this.id = id;
        this.producto = producto;
        this.usuario = usuario;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Venta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        return "Venta{" + "id=" + id + ", producto=" + producto + ", usuario=" + usuario + ", cantidad=" + cantidad + ", fecha=" + fecha + '}';
    }
}

