package com.trazaalimentos.repository;

import com.trazaalimentos.entity.SolicitudCertificacion;
import com.trazaalimentos.entity.EntidadCertificadora;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudCertificacionRepository extends JpaRepository<SolicitudCertificacion, Long> {
    List<SolicitudCertificacion> findByEntidadCertificadora(EntidadCertificadora entidad);
    List<SolicitudCertificacion> findByProductor(Usuario productor);
    List<SolicitudCertificacion> findByLote(Lote lote);
    Optional<SolicitudCertificacion> findByLoteAndEntidadCertificadora(Lote lote, EntidadCertificadora entidad);
    List<SolicitudCertificacion> findByEstado(SolicitudCertificacion.EstadoSolicitud estado);
}
