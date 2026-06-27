package com.trazaalimentos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteDTO {
    private String codigoLote;
    private Long productoId;
    private Integer cantidad;
    private String unidad;
}