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
@RequestMapping("/comerciante")
@CrossOrigin(origins = "*")
public class ComerciateController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LoteService loteService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @PostMapping("/{comercianteId}/recibir/{loteId}")
    public ResponseEntity<Map<String, Object>> recibirLote(
            @PathVariable Long comercianteId,
            @PathVariable Long loteId,
            @RequestParam String ubicacionOrigen,
            @RequestParam String ubicacionDestino) {
        try {
            Optional<Usuario> comerciante = usuarioService.obtenerUsuarioPorId(comercianteId);
            Optional<Lote> lote = loteService.obtenerLotePorId(loteId);

            if (!comerciante.isPresent() || !lote.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (!comerciante.get().getRol().equals(Usuario.RolUsuario.COMERCIANTE)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Obtener el último actor (distribuidor)
            java.util.List<Bloque> cadena = blockchainService.obtenerCadenaPorLote(loteId);
            Usuario remitente = cadena.isEmpty() ? lote.get().getProductor() : cadena.get(cadena.size() - 1).getActor();

            // Crear transacción
            Transaccion transaccion = new Transaccion();
            transaccion.setLote(lote.get());
            transaccion.setRemitente(remitente);
            transaccion.setDestinatario(comerciante.get());
            transaccion.setTipoTransaccion("RECEPCION_COMERCIANTE");
            transaccion.setUbicacionOrigen(ubicacionOrigen);
            transaccion.setUbicacionDestino(ubicacionDestino);
            transaccion.setEstado(Transaccion.EstadoTransaccion.CONFIRMADA);
            transaccionRepository.save(transaccion);

            // Crear bloque
            Map<String, Object> datosBloque = new HashMap<>();
            datosBloque.put("accion", "RECEPCION_COMERCIANTE");
            datosBloque.put("comerciante", comerciante.get().getNombre());
            datosBloque.put("ubicacionOrigen", ubicacionOrigen);
            datosBloque.put("ubicacionDestino", ubicacionDestino);
            Bloque bloqueNuevo = blockchainService.crearBloque(lote.get(), comerciante.get(), "RECEPCION_COMERCIANTE", datosBloque);

            // Actualizar estado
            loteService.actualizarEstadoLote(loteId, Lote.EstadoLote.EN_COMERCIO);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Lote recibido en comercio exitosamente");
            response.put("bloque", bloqueNuevo);
            response.put("nuevoEstado", "EN_COMERCIO");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}