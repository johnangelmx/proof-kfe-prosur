package com.scdlc.kfe.service;

import org.springframework.stereotype.Service;

@Service
public class ProductoService {
//    private final ProductoRepository productoRepository;
//
//    @Autowired
//    public ProductoService(ProductoRepository productoRepository) {
//        this.productoRepository = productoRepository;
//    }//constructor
//
//    public List<Producto> getAllProductos() {
//        return productoRepository.findAll();
//    }//getAllProductos
//
//
//    public Producto getProducto(Long id) {
//        return productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("El producto " + id + " no existe"));
//    }
//
//    public Producto deleteProducto(Long id) {
//        Producto tmpProd = null;
//        if (productoRepository.existsById(id)) {
//            tmpProd = productoRepository.findById(id).get();
//            productoRepository.deleteById(id);
//        }
//        return tmpProd;
//    }
//
//    public Producto addProducto(Producto producto) {
//        Producto tmpProd = null;
//        if (productoRepository.findByNombre(producto.getNombre()).isEmpty()) {
//            tmpProd = productoRepository.save(producto);
//        } else {
//            System.out.println("ya existe un producto con el nombre " + producto);
//        }
//        return tmpProd;
//    }
//
//    public Producto updateProducto(Long id, String nombre, String descripcion, String imagen, Double precio) {
//        Producto tmpProd = null;
//
//        if (productoRepository.existsById(id)) {
//            tmpProd = productoRepository.findById(id).get();
//            if (nombre != null) tmpProd.setNombre(nombre);
//            if (descripcion != null) tmpProd.setNombre(descripcion);
//            if (imagen != null) tmpProd.setImagen(imagen);
//            if (precio != null) tmpProd.setPrecio(precio);
//            productoRepository.save(tmpProd);
//        } else {
//            System.out.println("update - El producto con id " + id + " no existe");
//        }
//        return tmpProd;
//
//    }
}// class ProductoService
