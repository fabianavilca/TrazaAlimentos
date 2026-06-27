package com.trazaalimentos.repository;

import com.trazaalimentos.entity.Bloque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloqueRepository extends JpaRepository<Bloque, Long> {
    List<Bloque> findByLoteIdOrderByNumeroBloqueAsc(Long loteId);
    Bloque findTopByLoteIdOrderByNumeroBloqueDesc(Long loteId);
}