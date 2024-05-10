package com.scdlc.kfe.controller;

import com.scdlc.kfe.DTO.ProductoVentasStock;
import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.model.Venta;
import com.scdlc.kfe.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ventas/")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<Venta> getAllVenta() {
        return ventaService.getAllVenta();
    }

    @GetMapping(path = "{ventaId}")
    public Venta getVenta(@PathVariable("ventaId") Long id) {
        return ventaService.getVenta( id );
    }

    @GetMapping("/periodo")
    public List<Venta> obtenerVentasPeriodo(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {

        return ventaService.obtenerVentasPorPeriodo( fechaInicio, fechaFin );
    }

    @GetMapping("/mas_vendidos")
    public List<Producto> obtenerTresProductosMasVendidos() {
        return ventaService.obtenerTresProductosMasVendidos();
    }

    @GetMapping("/producto/{idProducto}")
    public ProductoVentasStock obtenerVentasYStockPorProducto(@PathVariable Long idProducto) {
        return ventaService.obtenerVentasYStockPorProducto( idProducto );
    }
}