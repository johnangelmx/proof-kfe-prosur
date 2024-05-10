package com.scdlc.kfe.service;

import com.scdlc.kfe.DTO.ProductoVentasStock;
import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.model.Venta;
import com.scdlc.kfe.repository.ProductoRepository;
import com.scdlc.kfe.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    public List<Venta> getAllVenta() {
        return ventaRepository.findAll();
    }

    public Venta getVenta(Long id) {
        return ventaRepository.findById( id ).orElseThrow( () -> new IllegalArgumentException( "La venta con el: " + id + " no existe" ) );
    }


    public List<Venta> obtenerVentasPorPeriodo(Date fechaInicio, Date fechaFin) {
        return ventaRepository.findByFechaBetween( fechaInicio, fechaFin );
    }

    public List<Producto> obtenerTresProductosMasVendidos() {
        List<Long> idsProductosMasVendidos = ventaRepository.findTop3ProductosMasVendidos();
        List<Producto> productosMasVendidos = productoRepository.findAllById( idsProductosMasVendidos );
        return productosMasVendidos;
    }

    public ProductoVentasStock obtenerVentasYStockPorProducto(Long idProducto) {
        Producto producto = productoRepository.findById( idProducto ).orElseThrow( () -> new IllegalArgumentException( "Producto no encontrado" ) );
        int cantidadVendida = ventaRepository.sumCantidadByProductoId( idProducto );
        return new ProductoVentasStock( producto, cantidadVendida, producto.getCantidadStock() );
    }
}
