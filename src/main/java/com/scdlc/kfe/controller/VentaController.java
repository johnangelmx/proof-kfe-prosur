package com.scdlc.kfe.controller;

import com.scdlc.kfe.DTO.ProductoVentasStock;
import com.scdlc.kfe.DTO.VentasDTO;
import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.model.Venta;
import com.scdlc.kfe.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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

    @GetMapping("/hoy")
    public List<Venta> obtenerVentasHoy(@RequestParam("hoy") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hoy) {
        Date fecha = java.sql.Date.valueOf( hoy );
        System.out.println( fecha );
        return ventaService.obtenerVentasHoy( fecha );
    }

    @GetMapping("/mes")
    public List<Venta> obtenerVentasMes(@RequestParam("mes") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mes) {
        LocalDate primerDiaMes = mes.withDayOfMonth( 1 );
        LocalDate ultimoDiaMes = mes.withDayOfMonth( mes.lengthOfMonth() );

        Date fechaInicio = java.sql.Date.valueOf( primerDiaMes );
        Date fechaFin = java.sql.Date.valueOf( ultimoDiaMes );

        return ventaService.obtenerVentasMes( fechaInicio, fechaFin );
    }


    @PostMapping("/crear")
    public ResponseEntity<Venta> crearVenta(@RequestBody VentasDTO ventasDTO) throws ParseException {
        Venta venta = ventaService.crearVenta( ventasDTO );
        return new ResponseEntity<>( venta, HttpStatus.CREATED );
    }

    @DeleteMapping(path = "{ventaId}")
    public ResponseEntity<String> eliminarVenta(@PathVariable("ventaId") Long id) {
        ventaService.eliminarVenta( id );
        return new ResponseEntity<>( "Venta eliminada exitosamente", HttpStatus.OK );
    }

    @PutMapping(path = "{ventaId}")
    public ResponseEntity<Venta> editarVenta(@PathVariable("ventaId") Long id, @RequestParam Long idProducto, @RequestParam Long idUsuario, @RequestParam int cantidad, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha, @RequestParam BigDecimal total) {
        Venta ventaEditada = ventaService.editarVenta( id, idProducto, idUsuario, cantidad, fecha, total );
        return new ResponseEntity<>( ventaEditada, HttpStatus.OK );
    }
}