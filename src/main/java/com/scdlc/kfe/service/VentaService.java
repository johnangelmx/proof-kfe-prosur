package com.scdlc.kfe.service;

import com.scdlc.kfe.DTO.ProductoVentasStock;
import com.scdlc.kfe.DTO.VentaPorMesDTO;
import com.scdlc.kfe.DTO.VentasDTO;
import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.model.Venta;
import com.scdlc.kfe.repository.ProductoRepository;
import com.scdlc.kfe.repository.UsuarioRepository;
import com.scdlc.kfe.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
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
        return productoRepository.findAllById( idsProductosMasVendidos );
    }

    public ProductoVentasStock obtenerVentasYStockPorProducto(Long idProducto) {
        Producto producto = productoRepository.findById( idProducto ).orElseThrow( () -> new IllegalArgumentException( "Producto no encontrado" ) );
        int cantidadVendida = ventaRepository.sumCantidadByProductoId( idProducto );
        return new ProductoVentasStock( producto, cantidadVendida, producto.getCantidadStock() );
    }

    public Venta crearVenta(VentasDTO ventasDTO) {
        Producto producto = productoRepository.findById( ventasDTO.getIdProducto() ).orElseThrow( () -> new IllegalArgumentException( "Producto no encontrado" ) );
        Usuario usuario = usuarioRepository.findById( ventasDTO.getIdUsuario() ).orElseThrow( () -> new IllegalArgumentException( "Usuario no encontrado" ) );

        if (producto.getCantidadStock() < ventasDTO.getCantidad()) {
            throw new IllegalArgumentException( "La cantidad solicitada excede el stock disponible" );
        }
        Date fecha = java.sql.Date.valueOf( ventasDTO.getFecha() );
        Venta venta = new Venta();
        venta.setProducto( producto );
        venta.setUsuario( usuario );
        venta.setCantidad( ventasDTO.getCantidad() );
        venta.setFecha( fecha );
        venta.setTotal( ventasDTO.getTotal() );
        producto.setCantidadStock( producto.getCantidadStock() - ventasDTO.getCantidad() );
        productoRepository.save( producto );

        return ventaRepository.save( venta );
    }

    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById( id ).orElseThrow( () -> new IllegalArgumentException( "Venta no encontrada" ) );

        Producto producto = venta.getProducto();
        producto.setCantidadStock( producto.getCantidadStock() + venta.getCantidad() );
        productoRepository.save( producto );

        ventaRepository.delete( venta );
    }

    public Venta editarVenta(Long id, Long idProducto, Long idUsuario, int cantidad, Date fecha, BigDecimal total) {
        Venta venta = ventaRepository.findById( id ).orElseThrow( () -> new IllegalArgumentException( "Venta no encontrada" ) );
        Producto producto = productoRepository.findById( idProducto ).orElseThrow( () -> new IllegalArgumentException( "Producto no encontrado" ) );
        Usuario usuario = usuarioRepository.findById( idUsuario ).orElseThrow( () -> new IllegalArgumentException( "Usuario no encontrado" ) );

        int cantidadAnterior = venta.getCantidad();
        int diferenciaCantidad = cantidad - cantidadAnterior;

        if (producto.getCantidadStock() < diferenciaCantidad) {
            throw new IllegalArgumentException( "La cantidad solicitada excede el stock disponible" );
        }

        producto.setCantidadStock( producto.getCantidadStock() - diferenciaCantidad );
        productoRepository.save( producto );

        venta.setProducto( producto );
        venta.setUsuario( usuario );
        venta.setCantidad( cantidad );
        venta.setFecha( fecha );
        venta.setTotal( total );

        return ventaRepository.save( venta );
    }

    public List<Venta> obtenerVentasHoy(Date hoy) {
        return ventaRepository.findByFecha( hoy );
    }

    public List<Venta> obtenerVentasMes(Date fechaInicio, Date fechaFin) {
        return ventaRepository.findByFechaBetween( fechaInicio, fechaFin );
    }

    public List<VentaPorMesDTO> obtenerVentasPorMesYProducto(Long productoId) {
        List<Object[]> ventasPorMes = ventaRepository.obtenerVentasPorMesYProducto( productoId );
        List<VentaPorMesDTO> ventasPorMesDTO = new ArrayList<>();
        for (Object[] venta : ventasPorMes) {
            int mes = (int) venta[0];
            long totalVenta = (long) venta[1];
            ventasPorMesDTO.add( new VentaPorMesDTO( mes, totalVenta ) );
        }
        return ventasPorMesDTO;
    }

}
