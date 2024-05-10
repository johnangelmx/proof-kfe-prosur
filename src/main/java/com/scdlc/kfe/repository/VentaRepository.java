package com.scdlc.kfe.repository;

import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario(Usuario usuario);
}
