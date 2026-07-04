package com.trazaalimentos.service;

import com.trazaalimentos.entity.EntidadCertificadora;
import com.trazaalimentos.repository.EntidadCertificadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntidadCertificadoraService {
    
    @Autowired
    private EntidadCertificadoraRepository repository;
    
    public EntidadCertificadora registrar(EntidadCertificadora entidad) {
        if (repository.findByRuc(entidad.getRuc()).isPresent()) {
            throw new RuntimeException("El RUC ya está registrado");
        }
        if (repository.findByCorreo(entidad.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        return repository.save(entidad);
    }
    
    public EntidadCertificadora obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entidad certificadora no encontrada"));
    }
    
    public EntidadCertificadora obtenerPorRuc(String ruc) {
        return repository.findByRuc(ruc)
                .orElseThrow(() -> new RuntimeException("Entidad certificadora no encontrada"));
    }
    
    public EntidadCertificadora obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Entidad certificadora no encontrada"));
    }
    
    public List<EntidadCertificadora> obtenerTodas() {
        return repository.findAll();
    }
    
    public List<EntidadCertificadora> obtenerActivas() {
        return repository.findByActivo(true);
    }
    
    public EntidadCertificadora actualizar(Long id, EntidadCertificadora entidad) {
        EntidadCertificadora existente = obtenerPorId(id);
        existente.setNombre(entidad.getNombre());
        existente.setDireccion(entidad.getDireccion());
        existente.setTelefono(entidad.getTelefono());
        existente.setTiposCertificacion(entidad.getTiposCertificacion());
        existente.setDescripcion(entidad.getDescripcion());
        if (entidad.getLogo() != null) {
            existente.setLogo(entidad.getLogo());
        }
        return repository.save(existente);
    }
    
    public void desactivar(Long id) {
        EntidadCertificadora entidad = obtenerPorId(id);
        entidad.setActivo(false);
        repository.save(entidad);
    }
    
    public void activar(Long id) {
        EntidadCertificadora entidad = obtenerPorId(id);
        entidad.setActivo(true);
        repository.save(entidad);
    }
}
