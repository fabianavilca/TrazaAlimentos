package com.trazaalimentos.repository;

import com.trazaalimentos.entity.Certificado;
import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.EntidadCertificadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    Optional<Certificado> findByNumeroCertificado(String numero);
    List<Certificado> findByLote(Lote lote);
    List<Certificado> findByEntidadCertificadora(EntidadCertificadora entidad);
    List<Certificado> findByEstado(Certificado.EstadoCertificado estado);
}
