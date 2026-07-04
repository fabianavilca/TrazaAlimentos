package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "entidad_certificadora")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntidadCertificadora {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String ruc;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false, unique = true)
    private String correo;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(columnDefinition = "LONGBLOB")
    private byte[] logo;
    
    @Column(length = 1000)
    private String tiposCertificacion;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
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
}
