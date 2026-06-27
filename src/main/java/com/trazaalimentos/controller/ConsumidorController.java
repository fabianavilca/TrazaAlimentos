package com.trazaalimentos.controller;

import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Bloque;
import com.trazaalimentos.service.LoteService;
import com.trazaalimentos.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/consumidor")
@CrossOrigin(origins = "*")
public class ConsumidorController {
    @Autowired
    private LoteService loteService;

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping("/lote/{codigoLote}")
    public ResponseEntity<Map<String, Object>> obtenerTrazabilidad(@PathVariable String codigoLote) {
        try {
            Optional<Lote> lote = loteService.obtenerLotePorCodigo(codigoLote);

            if (!lote.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<Bloque> cadena = blockchainService.obtenerCadenaPorLote(lote.get().getId());
            boolean integridadValida = blockchainService.verificarIntegridad(lote.get().getId());

            Map<String, Object> response = new HashMap<>();
            response.put("lote", lote.get());
            response.put("producto", lote.get().getProducto().getNombre());
            response.put("productor", lote.get().getProductor().getNombre());
            response.put("empresa", lote.get().getProductor().getEmpresa());
            response.put("certificacionOrganica", lote.get().getProductor().getCertificacionOrganica());
            response.put("historialCompleto", cadena);
            response.put("integridadValida", integridadValida);
            response.put("cantidad", lote.get().getCantidad());
            response.put("unidad", lote.get().getUnidad());
            response.put("estado", lote.get().getEstado());
            response.put("fechaCreacion", lote.get().getFechaCreacion());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}