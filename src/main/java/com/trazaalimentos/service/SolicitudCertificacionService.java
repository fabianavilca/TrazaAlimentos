package com.trazaalimentos.service;

import com.trazaalimentos.entity.SolicitudCertificacion;
import com.trazaalimentos.entity.EntidadCertificadora;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.SolicitudCertificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SolicitudCertificacionService {
    
    @Autowired
    private SolicitudCertificacionRepository repository;
    
    @Autowired
    private EntidadCertificadoraService entidadService;
    
    public SolicitudCertificacion crearSolicitud(Long loteId, Long entidadId, Usuario productor) {
        EntidadCertificadora entidad = entidadService.obtenerPorId(entidadId);
        // Aquí se necesitaría obtener el lote, asumimos que existe
        
        // Verificar que no exista solicitud previa
        Lote lote = new Lote(); // Esto debería venir del loteService
        lote.setId(loteId);
        
        if (repository.findByLoteAndEntidadCertificadora(lote, entidad).isPresent()) {
            throw new RuntimeException("Ya existe una solicitud para este lote en esta entidad");
        }
        
        SolicitudCertificacion solicitud = SolicitudCertificacion.builder()
                .lote(lote)
                .entidadCertificadora(entidad)
                .productor(productor)
                .estado(SolicitudCertificacion.EstadoSolicitud.PENDIENTE)
                .build();
        
        return repository.save(solicitud);
    }
    
    public SolicitudCertificacion obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }
    
    public List<SolicitudCertificacion> obtenerPorEntidad(Long entidadId) {
        EntidadCertificadora entidad = entidadService.obtenerPorId(entidadId);
        return repository.findByEntidadCertificadora(entidad);
    }
    
    public List<SolicitudCertificacion> obtenerPorProductor(Usuario productor) {
        return repository.findByProductor(productor);
    }
    
    public List<SolicitudCertificacion> obtenerPendientes() {
        return repository.findByEstado(SolicitudCertificacion.EstadoSolicitud.PENDIENTE);
    }
    
    public SolicitudCertificacion cambiarEstado(Long id, SolicitudCertificacion.EstadoSolicitud nuevoEstado, 
                                               Usuario evaluador) {
        SolicitudCertificacion solicitud = obtenerPorId(id);
        solicitud.setEstado(nuevoEstado);
        solicitud.setEvaluador(evaluador);
        solicitud.setFechaEvaluacion(LocalDateTime.now());
        return repository.save(solicitud);
    }
    
    public SolicitudCertificacion rechazar(Long id, String razon, Usuario evaluador) {
        SolicitudCertificacion solicitud = obtenerPorId(id);
        solicitud.setEstado(SolicitudCertificacion.EstadoSolicitud.RECHAZADA);
        solicitud.setRazonRechazo(razon);
        solicitud.setEvaluador(evaluador);
        solicitud.setFechaEvaluacion(LocalDateTime.now());
        return repository.save(solicitud);
    }
    
    public SolicitudCertificacion aprobar(Long id, Usuario evaluador) {
        return cambiarEstado(id, SolicitudCertificacion.EstadoSolicitud.APROBADA, evaluador);
    }
}
