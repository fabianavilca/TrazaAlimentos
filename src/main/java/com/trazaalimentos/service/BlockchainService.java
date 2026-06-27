package com.trazaalimentos.service;

import com.trazaalimentos.entity.Bloque;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.BloqueRepository;
import com.trazaalimentos.util.SHA256Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlockchainService {
    @Autowired
    private BloqueRepository bloqueRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Bloque crearBloque(Lote lote, Usuario actor, String accion, Map<String, Object> datos) {
        List<Bloque> bloquesPrevios = bloqueRepository.findByLoteIdOrderByNumeroBloqueAsc(lote.getId());
        
        int numeroBloque = bloquesPrevios.size() + 1;
        String hashAnterior = "0";
        
        if (numeroBloque > 1) {
            Bloque bloqueAnterior = bloquesPrevios.get(bloquesPrevios.size() - 1);
            hashAnterior = bloqueAnterior.getHashActual();
        } else {
            hashAnterior = lote.getHashInicial();
        }

        try {
            String datosJson = objectMapper.writeValueAsString(datos);
            String hashActual = SHA256Util.generateBlockHash(hashAnterior, datosJson, System.currentTimeMillis());

            Bloque bloque = new Bloque();
            bloque.setLote(lote);
            bloque.setNumeroBloque(numeroBloque);
            bloque.setActor(actor);
            bloque.setActorRol(actor.getRol().toString());
            bloque.setAccion(accion);
            bloque.setHashAnterior(hashAnterior);
            bloque.setHashActual(hashActual);
            bloque.setDatosJson(datosJson);

            return bloqueRepository.save(bloque);
        } catch (Exception e) {
            throw new RuntimeException("Error creando bloque: " + e.getMessage());
        }
    }

    public List<Bloque> obtenerCadenaPorLote(Long loteId) {
        return bloqueRepository.findByLoteIdOrderByNumeroBloqueAsc(loteId);
    }

    public boolean verificarIntegridad(Long loteId) {
        List<Bloque> bloques = obtenerCadenaPorLote(loteId);
        
        if (bloques.isEmpty()) {
            return false;
        }

        for (int i = 1; i < bloques.size(); i++) {
            Bloque bloqueActual = bloques.get(i);
            Bloque bloqueAnterior = bloques.get(i - 1);

            if (!bloqueActual.getHashAnterior().equals(bloqueAnterior.getHashActual())) {
                return false;
            }
        }

        return true;
    }
}