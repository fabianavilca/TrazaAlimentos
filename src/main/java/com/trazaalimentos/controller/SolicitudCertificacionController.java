package com.trazaalimentos.controller;

import com.trazaalimentos.entity.SolicitudCertificacion;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.service.SolicitudCertificacionService;
import com.trazaalimentos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/solicitud-certificacion")
@CrossOrigin(origins = "*")
public class SolicitudCertificacionController {
    
    @Autowired
    private SolicitudCertificacionService service;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestParam Long loteId, @RequestParam Long entidadId, @RequestParam Long productorId) {
        try {
            Usuario productor = usuarioService.obtenerPorId(productorId);
            SolicitudCertificacion solicitud = service.crearSolicitud(loteId, entidadId, productor);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            SolicitudCertificacion solicitud = service.obtenerPorId(id);
            return ResponseEntity.ok(solicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/entidad/{entidadId}")
    public ResponseEntity<?> obtenerPorEntidad(@PathVariable Long entidadId) {
        try {
            List<SolicitudCertificacion> solicitudes = service.obtenerPorEntidad(entidadId);
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/productor/{productorId}")
    public ResponseEntity<?> obtenerPorProductor(@PathVariable Long productorId) {
        try {
            Usuario productor = usuarioService.obtenerPorId(productorId);
            List<SolicitudCertificacion> solicitudes = service.obtenerPorProductor(productor);
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/pendientes")
    public ResponseEntity<?> obtenerPendientes() {
        try {
            List<SolicitudCertificacion> solicitudes = service.obtenerPendientes();
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobar(@PathVariable Long id, @RequestParam Long evaluadorId) {
        try {
            Usuario evaluador = usuarioService.obtenerPorId(evaluadorId);
            SolicitudCertificacion solicitud = service.aprobar(id, evaluador);
            return ResponseEntity.ok(solicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id, @RequestParam String razon, @RequestParam Long evaluadorId) {
        try {
            Usuario evaluador = usuarioService.obtenerPorId(evaluadorId);
            SolicitudCertificacion solicitud = service.rechazar(id, razon, evaluador);
            return ResponseEntity.ok(solicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, 
            @RequestParam SolicitudCertificacion.EstadoSolicitud nuevoEstado,
            @RequestParam Long evaluadorId) {
        try {
            Usuario evaluador = usuarioService.obtenerPorId(evaluadorId);
            SolicitudCertificacion solicitud = service.cambiarEstado(id, nuevoEstado, evaluador);
            return ResponseEntity.ok(solicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
