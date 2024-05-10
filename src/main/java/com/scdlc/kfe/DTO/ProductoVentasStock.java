package com.scdlc.kfe.DTO;

import com.scdlc.kfe.model.Producto;

public class ProductoVentasStock {
    private Producto producto;
    private int cantidadVendida;
    private int stockActual;

    public ProductoVentasStock(Producto producto, int cantidadVendida, int stockActual) {
        this.producto = producto;
        this.cantidadVendida = cantidadVendida;
        this.stockActual = stockActual;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    @Override
    public String toString() {
        return "ProductoVentasStock{" + "producto=" + producto + ", cantidadVendida=" + cantidadVendida + ", stockActual=" + stockActual + '}';
    }
}

