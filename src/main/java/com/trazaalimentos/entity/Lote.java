package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "lotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigoLote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productor_id", nullable = false)
    private Usuario productor;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 50)
    private String unidad;

    @Column(length = 256)
    private String hashInicial;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] codigoQr;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('CREADO', 'EN_TRANSITO', 'EN_COMERCIO', 'VENDIDO') DEFAULT 'CREADO'")
    private EstadoLote estado = EstadoLote.CREADO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum EstadoLote {
        CREADO, EN_TRANSITO, EN_COMERCIO, VENDIDO
    }
}