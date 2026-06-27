package com.trazaalimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Column(nullable = false, length = 50)
    private String tipoTransaccion;

    @Column(length = 255)
    private String ubicacionOrigen;

    @Column(length = 255)
    private String ubicacionDestino;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaTransaccion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDIENTE', 'CONFIRMADA', 'COMPLETADA') DEFAULT 'PENDIENTE'")
    private EstadoTransaccion estado = EstadoTransaccion.PENDIENTE;

    public enum EstadoTransaccion {
        PENDIENTE, CONFIRMADA, COMPLETADA
    }
}