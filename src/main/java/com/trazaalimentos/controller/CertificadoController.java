package com.trazaalimentos.controller;

import com.trazaalimentos.entity.Certificado;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.service.CertificadoService;
import com.trazaalimentos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/certificado")
@CrossOrigin(origins = "*")
public class CertificadoController {
    
    @Autowired
    private CertificadoService service;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/emitir")
    public ResponseEntity<?> emitir(@RequestParam Long loteId,
            @RequestParam Long entidadId,
            @RequestParam Long solicitudId,
            @RequestParam String tipoCertificacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaVencimiento,
            @RequestParam(required = false) MultipartFile documentoPDF,
            @RequestParam Long usuarioEmisorId) {
        try {
            Usuario usuarioEmisor = usuarioService.obtenerPorId(usuarioEmisorId);
            byte[] pdfBytes = documentoPDF != null ? documentoPDF.getBytes() : null;
            
            Certificado certificado = service.emitir(loteId, entidadId, solicitudId, 
                    tipoCertificacion, fechaVencimiento, pdfBytes, usuarioEmisor);
            return ResponseEntity.status(HttpStatus.CREATED).body(certificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Certificado certificado = service.obtenerPorId(id);
            return ResponseEntity.ok(certificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/numero/{numero}")
    public ResponseEntity<?> obtenerPorNumero(@PathVariable String numero) {
        try {
            Certificado certificado = service.obtenerPorNumero(numero);
            return ResponseEntity.ok(certificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/entidad/{entidadId}")
    public ResponseEntity<?> obtenerPorEntidad(@PathVariable Long entidadId) {
        try {
            List<Certificado> certificados = service.obtenerPorEntidad(entidadId);
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerActivos() {
        try {
            List<Certificado> certificados = service.obtenerActivos();
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Certificado> certificados = service.obtenerTodos();
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/revocar")
    public ResponseEntity<?> revocar(@PathVariable Long id) {
        try {
            Certificado certificado = service.revocar(id);
            return ResponseEntity.ok(certificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
