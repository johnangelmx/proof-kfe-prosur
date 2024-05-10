package com.scdlc.kfe.service;

import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProducto(Long id) {
        return productoRepository.findById( id ).orElseThrow( () -> new IllegalArgumentException( "El producto " + id + " no existe" ) );
    }

    public Producto deleteProducto(Long id) {
        if (productoRepository.existsById( id )) {
            Producto tmpProd = productoRepository.findById( id ).get();
            productoRepository.deleteById( id );
            return tmpProd;
        } else {
            throw new IllegalArgumentException( "El producto " + id + " no existe" );
        }

    }

    public Producto addProducto(Producto producto) {
        if (productoRepository.findByNombre( producto.getNombre() ).isEmpty()) {
            return productoRepository.save( producto );
        } else {
            throw new IllegalArgumentException( "ya existe un producto con el nombre " + producto );
        }

    }

    public Producto updateProducto(Long id, String nombre, String descripcion, Double precio, Integer cantidadStock) {
        if (productoRepository.existsById( id )) {
            Producto tmpProd = productoRepository.findById( id ).get();
            if (nombre != null) tmpProd.setNombre( nombre );
            if (descripcion != null) tmpProd.setDescripcion( descripcion );
            if (precio != null && precio >= 0) tmpProd.setPrecio( precio );
            if (cantidadStock != null && cantidadStock >= 0) tmpProd.setCantidadStock( cantidadStock );
            return productoRepository.save( tmpProd );
        } else {
            throw new IllegalArgumentException( "El producto " + id + " no existe" );
        }
    }
}
