package com.trazaalimentos.repository;

import com.trazaalimentos.entity.EventoTrazabilidad;
import com.trazaalimentos.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoTrazabilidadRepository extends JpaRepository<EventoTrazabilidad, Long> {
    List<EventoTrazabilidad> findByLote(Lote lote);
    List<EventoTrazabilidad> findByLoteOrderByCreatedAtAsc(Lote lote);
}
