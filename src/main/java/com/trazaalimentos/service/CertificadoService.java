package com.trazaalimentos.service;

import com.trazaalimentos.entity.Certificado;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.EntidadCertificadora;
import com.trazaalimentos.entity.SolicitudCertificacion;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.CertificadoRepository;
import com.trazaalimentos.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CertificadoService {
    
    @Autowired
    private CertificadoRepository repository;
    
    @Autowired
    private EntidadCertificadoraService entidadService;
    
    public Certificado emitir(Long loteId, Long entidadId, Long solicitudId, 
                             String tipoCertificacion, LocalDate fechaVencimiento,
                             byte[] documentoPDF, Usuario usuarioEmisor) {
        
        EntidadCertificadora entidad = entidadService.obtenerPorId(entidadId);
        
        // Generar número único de certificado
        String numeroCertificado = "CERT-" + entidadId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Generar hash del certificado
        String datos = numeroCertificado + tipoCertificacion + LocalDate.now() + loteId;
        String hashBloque = SHA256Util.calcularHash(datos);
        
        Lote lote = new Lote();
        lote.setId(loteId);
        
        SolicitudCertificacion solicitud = new SolicitudCertificacion();
        solicitud.setId(solicitudId);
        
        Certificado certificado = Certificado.builder()
                .numeroCertificado(numeroCertificado)
                .lote(lote)
                .entidadCertificadora(entidad)
                .solicitudCertificacion(solicitud)
                .tipoCertificacion(tipoCertificacion)
                .fechaEmision(LocalDate.now())
                .fechaVencimiento(fechaVencimiento)
                .documentoPDF(documentoPDF)
                .hashBloque(hashBloque)
                .estado(Certificado.EstadoCertificado.ACTIVO)
                .usuarioEmisor(usuarioEmisor)
                .build();
        
        return repository.save(certificado);
    }
    
    public Certificado obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));
    }
    
    public Certificado obtenerPorNumero(String numero) {
        return repository.findByNumeroCertificado(numero)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));
    }
    
    public List<Certificado> obtenerPorEntidad(Long entidadId) {
        EntidadCertificadora entidad = entidadService.obtenerPorId(entidadId);
        return repository.findByEntidadCertificadora(entidad);
    }
    
    public List<Certificado> obtenerActivos() {
        return repository.findByEstado(Certificado.EstadoCertificado.ACTIVO);
    }
    
    public Certificado revocar(Long id) {
        Certificado certificado = obtenerPorId(id);
        certificado.setEstado(Certificado.EstadoCertificado.REVOCADO);
        return repository.save(certificado);
    }
    
    public List<Certificado> obtenerTodos() {
        return repository.findAll();
    }
}
