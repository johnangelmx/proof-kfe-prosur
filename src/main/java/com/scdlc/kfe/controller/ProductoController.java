package com.scdlc.kfe.controller;

import com.scdlc.kfe.model.Producto;
import com.scdlc.kfe.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CRUD   Create   -   Read    -   Update   -     Delete
// HTTP    POST    -   GET     -    PUT     -     DELETE
@RestController
@RequestMapping(path = "/api/productos/")
public class ProductoController {
    private final ProductoService productoService;

    @Autowired
    private ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> getAllProducts() {
        return productoService.getAllProductos();
    }


    @GetMapping(path = "{prodId}")
    public Producto getProducto(@PathVariable("prodId") Long id) {
        return productoService.getProducto( id );
    }

    @DeleteMapping(path = "{prodId}")
    public Producto deleteProducto(@PathVariable("prodId") Long id) {
        return productoService.deleteProducto( id );
    }

    @PostMapping
    public Producto addProducto(@RequestBody Producto producto) {
        return productoService.addProducto( producto );
    }

    @PutMapping(path = "{prodId}")
    public Producto updateProducto(@PathVariable("prodId") Long id,
                                   @RequestParam(required = false) String nombre,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam(required = false) Double precio,
                                   @RequestParam(required = false) Integer cantidadStock) {
        return productoService.updateProducto( id, nombre, descripcion, precio, cantidadStock );
    }
}
