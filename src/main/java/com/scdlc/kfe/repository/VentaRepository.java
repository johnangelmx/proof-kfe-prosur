package com.scdlc.kfe.repository;

import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario(Usuario usuario);

    List<Venta> findByFecha(Date hoy);

    List<Venta> findByFechaBetween(Date fechaInicio, Date fechaFin);

    @Query(value = "SELECT p.id FROM productos p INNER JOIN ventas v ON p.id = v.id_producto GROUP BY p.id ORDER BY SUM(v.cantidad) DESC LIMIT 3", nativeQuery = true)
    List<Long> findTop3ProductosMasVendidos();

    @Query("SELECT SUM(v.cantidad) FROM Venta v WHERE v.producto.id = :idProducto")
    int sumCantidadByProductoId(@Param("idProducto") Long idProducto);

    @Modifying
    @Transactional
    @Query("DELETE FROM Venta v WHERE v.producto.id = :productoId")
    void deleteByProductoId(@Param("productoId") Long productoId);

    @Query("SELECT MONTH(v.fecha) AS mes, SUM(v.cantidad) AS totalVenta FROM Venta v WHERE v.producto.id = :productoId GROUP BY MONTH(v.fecha)")
    List<Object[]> obtenerVentasPorMesYProducto(Long productoId);
}
