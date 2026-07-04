package com.trazaalimentos.repository;

import com.trazaalimentos.entity.EntidadCertificadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EntidadCertificadoraRepository extends JpaRepository<EntidadCertificadora, Long> {
    Optional<EntidadCertificadora> findByRuc(String ruc);
    Optional<EntidadCertificadora> findByCorreo(String correo);
    Optional<EntidadCertificadora> findByNombre(String nombre);
    List<EntidadCertificadora> findByActivo(Boolean activo);
}
