package com.trazaalimentos.repository;

import com.trazaalimentos.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    Optional<Lote> findByCodigoLote(String codigoLote);
    List<Lote> findByProductorId(Long productorId);
    List<Lote> findByProductoId(Long productoId);
}