package com.trazaalimentos.dto;

import com.trazaalimentos.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private String nombre;
    private String email;
    private String password;
    private Usuario.RolUsuario rol;
    private String empresa;
    private String telefono;
    private String direccion;
    private String certificacionOrganica;
}