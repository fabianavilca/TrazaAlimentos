package com.trazaalimentos.service;

import com.trazaalimentos.entity.Comerciante;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.ComercianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ComercianteService {
    
    @Autowired
    private ComercianteRepository repository;
    
    public Comerciante registrar(Comerciante comerciante) {
        if (repository.findByDocumentoIdentidad(comerciante.getDocumentoIdentidad()).isPresent()) {
            throw new RuntimeException("El documento de identidad ya está registrado");
        }
        return repository.save(comerciante);
    }
    
    public Comerciante obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comerciante no encontrado"));
    }
    
    public Comerciante obtenerPorUsuario(Usuario usuario) {
        return repository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Comerciante no encontrado"));
    }
    
    public Comerciante obtenerPorDocumento(String documento) {
        return repository.findByDocumentoIdentidad(documento)
                .orElseThrow(() -> new RuntimeException("Comerciante no encontrado"));
    }
    
    public List<Comerciante> obtenerTodos() {
        return repository.findAll();
    }
    
    public List<Comerciante> obtenerActivos() {
        return repository.findByActivo(true);
    }
    
    public List<Comerciante> obtenerPorEmpresa(String empresa) {
        return repository.findByEmpresa(empresa);
    }
    
    public Comerciante actualizar(Long id, Comerciante comerciante) {
        Comerciante existente = obtenerPorId(id);
        existente.setNombre(comerciante.getNombre());
        existente.setEmpresa(comerciante.getEmpresa());
        existente.setTelefono(comerciante.getTelefono());
        existente.setDireccion(comerciante.getDireccion());
        existente.setCiudad(comerciante.getCiudad());
        existente.setPais(comerciante.getPais());
        existente.setLatitud(comerciante.getLatitud());
        existente.setLongitud(comerciante.getLongitud());
        existente.setPuntosVenta(comerciante.getPuntosVenta());
        existente.setDescripcionNegocio(comerciante.getDescripcionNegocio());
        return repository.save(existente);
    }
    
    public void desactivar(Long id) {
        Comerciante comerciante = obtenerPorId(id);
        comerciante.setActivo(false);
        repository.save(comerciante);
    }
    
    public void activar(Long id) {
        Comerciante comerciante = obtenerPorId(id);
        comerciante.setActivo(true);
        repository.save(comerciante);
    }
}
