package com.trazaalimentos.service;

import com.trazaalimentos.entity.EventoTrazabilidad;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.EventoTrazabilidadRepository;
import com.trazaalimentos.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EventoTrazabilidadService {
    
    @Autowired
    private EventoTrazabilidadRepository repository;
    
    @Autowired
    private LoteService loteService;
    
    public EventoTrazabilidad registrarEvento(Long loteId, EventoTrazabilidad.TipoEvento tipoEvento, 
                                              String descripcion, Usuario usuario) {
        Lote lote = loteService.obtenerPorId(loteId);
        
        // Obtener el último evento para la cadena
        List<EventoTrazabilidad> eventos = repository.findByLoteOrderByCreatedAtAsc(lote);
        String hashAnterior = eventos.isEmpty() ? "" : eventos.get(eventos.size() - 1).getHashActual();
        
        // Generar nuevo hash
        String datos = tipoEvento + descripcion + LocalDateTime.now() + hashAnterior;
        String hashActual = SHA256Util.calcularHash(datos);
        
        EventoTrazabilidad evento = EventoTrazabilidad.builder()
                .lote(lote)
                .tipoEvento(tipoEvento)
                .descripcion(descripcion)
                .hashAnterior(hashAnterior)
                .hashActual(hashActual)
                .usuario(usuario)
                .build();
        
        return repository.save(evento);
    }
    
    public List<EventoTrazabilidad> obtenerEventosPorLote(Long loteId) {
        Lote lote = loteService.obtenerPorId(loteId);
        return repository.findByLoteOrderByCreatedAtAsc(lote);
    }
    
    public EventoTrazabilidad obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }
    
    public List<EventoTrazabilidad> obtenerTodos() {
        return repository.findAll();
    }
}
