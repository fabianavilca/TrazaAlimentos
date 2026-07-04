package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_certificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudCertificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_certificadora_id", nullable = false)
    private EntidadCertificadora entidadCertificadora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productor_id", nullable = false)
    private Usuario productor;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(columnDefinition = "TEXT")
    private String razonRechazo;
    
    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;
    
    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluador_id")
    private Usuario evaluador;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fechaSolicitud == null) {
            fechaSolicitud = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum EstadoSolicitud {
        PENDIENTE,
        EN_EVALUACION,
        APROBADA,
        RECHAZADA,
        OBSERVACIONES
    }
}
