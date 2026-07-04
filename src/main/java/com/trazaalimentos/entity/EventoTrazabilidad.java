package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_trazabilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoTrazabilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private LocalDateTime fechaEvento;
    
    @Column(columnDefinition = "TEXT")
    private String ubicacion;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(nullable = false)
    private String hashAnterior;
    
    @Column(nullable = false)
    private String hashActual;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fechaEvento == null) {
            fechaEvento = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum TipoEvento {
        SIEMBRA,
        FERTILIZACION,
        RIEGO,
        CONTROL_SANITARIO,
        COSECHA,
        TRANSPORTE,
        DISTRIBUCION,
        COMERCIALIZACION,
        OTRA
    }
}
