package com.trazaalimentos.controller;

import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Transaccion;
import com.trazaalimentos.entity.Bloque;
import com.trazaalimentos.service.UsuarioService;
import com.trazaalimentos.service.LoteService;
import com.trazaalimentos.service.BlockchainService;
import com.trazaalimentos.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/distribuidor")
@CrossOrigin(origins = "*")
public class DistribuidorController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LoteService loteService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @PostMapping("/{distribuidorId}/recibir/{loteId}")
    public ResponseEntity<Map<String, Object>> recibirLote(
            @PathVariable Long distribuidorId,
            @PathVariable Long loteId,
            @RequestParam String ubicacionOrigen,
            @RequestParam String ubicacionDestino) {
        try {
            Optional<Usuario> distribuidor = usuarioService.obtenerUsuarioPorId(distribuidorId);
            Optional<Lote> lote = loteService.obtenerLotePorId(loteId);

            if (!distribuidor.isPresent() || !lote.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (!distribuidor.get().getRol().equals(Usuario.RolUsuario.DISTRIBUIDOR)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Crear transacción
            Transaccion transaccion = new Transaccion();
            transaccion.setLote(lote.get());
            transaccion.setRemitente(lote.get().getProductor());
            transaccion.setDestinatario(distribuidor.get());
            transaccion.setTipoTransaccion("RECEPCION_DISTRIBUIDOR");
            transaccion.setUbicacionOrigen(ubicacionOrigen);
            transaccion.setUbicacionDestino(ubicacionDestino);
            transaccion.setEstado(Transaccion.EstadoTransaccion.CONFIRMADA);
            transaccionRepository.save(transaccion);

            // Crear bloque en la cadena
            Map<String, Object> datosBloque = new HashMap<>();
            datosBloque.put("accion", "RECEPCION_DISTRIBUIDOR");
            datosBloque.put("distribuidor", distribuidor.get().getNombre());
            datosBloque.put("ubicacionOrigen", ubicacionOrigen);
            datosBloque.put("ubicacionDestino", ubicacionDestino);
            Bloque bloqueNuevo = blockchainService.crearBloque(lote.get(), distribuidor.get(), "RECEPCION_DISTRIBUIDOR", datosBloque);

            // Actualizar estado del lote
            loteService.actualizarEstadoLote(loteId, Lote.EstadoLote.EN_TRANSITO);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Lote recibido exitosamente");
            response.put("bloque", bloqueNuevo);
            response.put("nuevoEstado", "EN_TRANSITO");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}