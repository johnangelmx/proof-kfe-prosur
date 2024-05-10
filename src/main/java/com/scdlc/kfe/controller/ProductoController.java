package com.scdlc.kfe.controller;

import org.springframework.web.bind.annotation.*;

// CRUD   Create   -   Read    -   Update   -     Delete
// HTTP    POST    -   GET     -    PUT     -     DELETE
@RestController
@RequestMapping(path = "/api/productos/")
public class ProductoController {
//    private final ProductoService productoService;
//
//    @Autowired
//    private ProductoController(ProductoService productoService) {
//        this.productoService = productoService;
//    }
//
//    @GetMapping
//    public List<Producto> getAllProducts() {
//        return productoService.getAllProductos();
//    }//getAllProducts
//
//    @GetMapping(path = "{prodId}")
//    public Producto getProducto(@PathVariable("prodId") Long id) {
//        return productoService.getProducto(id);
//    }
//
//    @DeleteMapping(path = "{prodId}")
//    public Producto deleteProducto(@PathVariable("prodId") Long id) {
//        return productoService.deleteProducto(id);
//    }
//
//    @PostMapping
//    public Producto addProducto(@RequestBody Producto producto) {
//        return productoService.addProducto(producto);
//    }
//
//    @PutMapping(path = "{prodId}")
//    public Producto updateProducto(@PathVariable("prodId") Long id, @RequestParam(required = false) String nombre, @RequestParam(required = false) String descripcion, @RequestParam(required = false) String imagen, @RequestParam(required = false) Double precio) {
//        return productoService.updateProducto(id, nombre, descripcion, imagen, precio);
//    }
}//class ProductoController
