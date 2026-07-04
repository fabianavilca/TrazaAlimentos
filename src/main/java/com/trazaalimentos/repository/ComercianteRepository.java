package com.trazaalimentos.repository;

import com.trazaalimentos.entity.Comerciante;
import com.trazaalimentos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ComercianteRepository extends JpaRepository<Comerciante, Long> {
    Optional<Comerciante> findByUsuario(Usuario usuario);
    Optional<Comerciante> findByDocumentoIdentidad(String documento);
    List<Comerciante> findByActivo(Boolean activo);
    List<Comerciante> findByEmpresa(String empresa);
}
