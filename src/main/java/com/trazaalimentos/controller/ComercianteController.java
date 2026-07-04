package com.trazaalimentos.controller;

import com.trazaalimentos.entity.Comerciante;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.service.ComercianteService;
import com.trazaalimentos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comerciante")
@CrossOrigin(origins = "*")
public class ComercianteController {
    
    @Autowired
    private ComercianteService service;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Comerciante comerciante) {
        try {
            Comerciante resultado = service.registrar(comerciante);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Comerciante comerciante = service.obtenerPorId(id);
            return ResponseEntity.ok(comerciante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(usuarioId);
            Comerciante comerciante = service.obtenerPorUsuario(usuario);
            return ResponseEntity.ok(comerciante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/documento/{documento}")
    public ResponseEntity<?> obtenerPorDocumento(@PathVariable String documento) {
        try {
            Comerciante comerciante = service.obtenerPorDocumento(documento);
            return ResponseEntity.ok(comerciante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Comerciante> comerciantes = service.obtenerTodos();
            return ResponseEntity.ok(comerciantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/activos/listar")
    public ResponseEntity<?> obtenerActivos() {
        try {
            List<Comerciante> comerciantes = service.obtenerActivos();
            return ResponseEntity.ok(comerciantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/empresa/{empresa}")
    public ResponseEntity<?> obtenerPorEmpresa(@PathVariable String empresa) {
        try {
            List<Comerciante> comerciantes = service.obtenerPorEmpresa(empresa);
            return ResponseEntity.ok(comerciantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Comerciante comerciante) {
        try {
            Comerciante resultado = service.actualizar(id, comerciante);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            service.desactivar(id);
            return ResponseEntity.ok("Comerciante desactivado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            service.activar(id);
            return ResponseEntity.ok("Comerciante activado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
