package com.trazaalimentos.controller;

import com.trazaalimentos.dto.LoginDTO;
import com.trazaalimentos.dto.RegistroDTO;
import com.trazaalimentos.dto.AuthResponseDTO;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.service.UsuarioService;
import com.trazaalimentos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registro(@RequestBody RegistroDTO registroDTO) {
        try {
            Usuario usuario = usuarioService.registrarUsuario(registroDTO);
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().toString(), usuario.getId());

            AuthResponseDTO response = new AuthResponseDTO();
            response.setToken(token);
            response.setUsuarioId(usuario.getId());
            response.setNombre(usuario.getNombre());
            response.setEmail(usuario.getEmail());
            response.setRol(usuario.getRol().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorEmail(loginDTO.getEmail());

            if (usuario.isPresent() && passwordEncoder.matches(loginDTO.getPassword(), usuario.get().getPassword())) {
                String token = jwtUtil.generateToken(usuario.get().getEmail(), usuario.get().getRol().toString(), usuario.get().getId());

                AuthResponseDTO response = new AuthResponseDTO();
                response.setToken(token);
                response.setUsuarioId(usuario.get().getId());
                response.setNombre(usuario.get().getNombre());
                response.setEmail(usuario.get().getEmail());
                response.setRol(usuario.get().getRol().toString());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}