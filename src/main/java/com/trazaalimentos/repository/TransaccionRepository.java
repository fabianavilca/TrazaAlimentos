package com.trazaalimentos.repository;

import com.trazaalimentos.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByLoteId(Long loteId);
    List<Transaccion> findByRemitenteId(Long remitenteId);
    List<Transaccion> findByDestinatarioId(Long destinatarioId);
}