package com.scdlc.kfe.repository;

import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario(Usuario usuario);

    @Query(value = "SELECT * FROM ventas WHERE fecha BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    List<Venta> findByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query(value = "SELECT p.id FROM productos p INNER JOIN ventas v ON p.id = v.id_producto GROUP BY p.id ORDER BY SUM(v.cantidad) DESC LIMIT 3", nativeQuery = true)
    List<Long> findTop3ProductosMasVendidos();

    @Query("SELECT SUM(v.cantidad) FROM Venta v WHERE v.producto.id = :idProducto")
    int sumCantidadByProductoId(@Param("idProducto") Long idProducto);
}
