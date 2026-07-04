package com.trazaalimentos.controller;

import com.trazaalimentos.entity.EntidadCertificadora;
import com.trazaalimentos.service.EntidadCertificadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entidad-certificadora")
@CrossOrigin(origins = "*")
public class EntidadCertificadoraController {
    
    @Autowired
    private EntidadCertificadoraService service;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody EntidadCertificadora entidad) {
        try {
            EntidadCertificadora resultado = service.registrar(entidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            EntidadCertificadora entidad = service.obtenerPorId(id);
            return ResponseEntity.ok(entidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<?> obtenerPorRuc(@PathVariable String ruc) {
        try {
            EntidadCertificadora entidad = service.obtenerPorRuc(ruc);
            return ResponseEntity.ok(entidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        try {
            EntidadCertificadora entidad = service.obtenerPorNombre(nombre);
            return ResponseEntity.ok(entidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodas() {
        try {
            List<EntidadCertificadora> entidades = service.obtenerTodas();
            return ResponseEntity.ok(entidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/activas/listar")
    public ResponseEntity<?> obtenerActivas() {
        try {
            List<EntidadCertificadora> entidades = service.obtenerActivas();
            return ResponseEntity.ok(entidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody EntidadCertificadora entidad) {
        try {
            EntidadCertificadora resultado = service.actualizar(id, entidad);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            service.desactivar(id);
            return ResponseEntity.ok("Entidad desactivada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            service.activar(id);
            return ResponseEntity.ok("Entidad activada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
