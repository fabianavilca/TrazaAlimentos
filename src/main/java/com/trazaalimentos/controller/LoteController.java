package com.trazaalimentos.controller;

import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Bloque;
import com.trazaalimentos.service.LoteService;
import com.trazaalimentos.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lotes")
@CrossOrigin(origins = "*")
public class LoteController {
    @Autowired
    private LoteService loteService;

    @Autowired
    private BlockchainService blockchainService;

    @PostMapping
    public ResponseEntity<Lote> crearLote(@RequestBody Lote lote, @RequestParam Long productorId, @RequestParam Long productoId) {
        try {
            Lote nuevoLote = loteService.crearLote(lote, productorId, productoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> obtenerLote(@PathVariable Long id) {
        Optional<Lote> lote = loteService.obtenerLotePorId(id);
        return lote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Lote> obtenerLotePorCodigo(@PathVariable String codigo) {
        Optional<Lote> lote = loteService.obtenerLotePorCodigo(codigo);
        return lote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/productor/{productorId}")
    public ResponseEntity<List<Lote>> obtenerLotesDelProductor(@PathVariable Long productorId) {
        List<Lote> lotes = loteService.obtenerLotesDelProductor(productorId);
        return ResponseEntity.ok(lotes);
    }

    @GetMapping
    public ResponseEntity<List<Lote>> obtenerTodosLosLotes() {
        List<Lote> lotes = loteService.obtenerTodosLosLotes();
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{loteId}/cadena")
    public ResponseEntity<List<Bloque>> obtenerCadena(@PathVariable Long loteId) {
        List<Bloque> cadena = blockchainService.obtenerCadenaPorLote(loteId);
        return ResponseEntity.ok(cadena);
    }

    @GetMapping("/{loteId}/verificar")
    public ResponseEntity<Map<String, Object>> verificarIntegridad(@PathVariable Long loteId) {
        boolean esValida = blockchainService.verificarIntegridad(loteId);
        Map<String, Object> response = new HashMap<>();
        response.put("loteId", loteId);
        response.put("integridadValida", esValida);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Lote> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Lote.EstadoLote estado = Lote.EstadoLote.valueOf(nuevoEstado);
            Lote loteActualizado = loteService.actualizarEstadoLote(id, estado);
            return ResponseEntity.ok(loteActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}