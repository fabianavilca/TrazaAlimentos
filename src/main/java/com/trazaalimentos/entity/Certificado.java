package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numeroCertificado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_certificadora_id", nullable = false)
    private EntidadCertificadora entidadCertificadora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_certificacion_id")
    private SolicitudCertificacion solicitudCertificacion;
    
    @Column(nullable = false)
    private String tipoCertificacion;
    
    @Column(nullable = false)
    private LocalDate fechaEmision;
    
    @Column(nullable = false)
    private LocalDate fechaVencimiento;
    
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentoPDF;
    
    @Column(columnDefinition = "TEXT")
    private String urlDocumento;
    
    @Column(columnDefinition = "TEXT")
    private String detalles;
    
    @Column(nullable = false)
    private String hashBloque;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCertificado estado = EstadoCertificado.ACTIVO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_emisor_id")
    private Usuario usuarioEmisor;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum EstadoCertificado {
        ACTIVO,
        VENCIDO,
        REVOCADO,
        SUSPENDIDO
    }
}
