package com.trazaalimentos.controller;

import com.trazaalimentos.entity.EventoTrazabilidad;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.service.EventoTrazabilidadService;
import com.trazaalimentos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/evento-trazabilidad")
@CrossOrigin(origins = "*")
public class EventoTrazabilidadController {
    
    @Autowired
    private EventoTrazabilidadService service;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestParam Long loteId,
            @RequestParam EventoTrazabilidad.TipoEvento tipoEvento,
            @RequestParam String descripcion,
            @RequestParam Long usuarioId) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(usuarioId);
            EventoTrazabilidad evento = service.registrarEvento(loteId, tipoEvento, descripcion, usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(evento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/lote/{loteId}")
    public ResponseEntity<?> obtenerEventosPorLote(@PathVariable Long loteId) {
        try {
            List<EventoTrazabilidad> eventos = service.obtenerEventosPorLote(loteId);
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            EventoTrazabilidad evento = service.obtenerPorId(id);
            return ResponseEntity.ok(evento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<EventoTrazabilidad> eventos = service.obtenerTodos();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
