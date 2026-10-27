package pe.edu.upeu.AgroBackend.service.impl;

import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AgroBackend.dto.auth.*;
import pe.edu.upeu.AgroBackend.entity.*;
import pe.edu.upeu.AgroBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AgroBackend.repository.UsuarioRepository;
import pe.edu.upeu.AgroBackend.security.*;
import pe.edu.upeu.AgroBackend.service.service.AuthService;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager manager;
    private final JwtService jwt;
    private final UsuarioRepository usuarios;
    private final UsuarioDetailsService details;
    public AuthServiceImpl(AuthenticationManager manager, JwtService jwt,
                           UsuarioRepository usuarios, UsuarioDetailsService details) {
        this.manager = manager; this.jwt = jwt; this.usuarios = usuarios; this.details = details;
    }
    @Override @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername().trim(), request.getPassword()));
        Usuario u = buscar(request.getUsername().trim());
        UserDetails principal = details.loadUserByUsername(u.getUsername());
        List<String> permisos = PermisosPorRol.permisos(u.getRoles());
        String rol = u.getRoles().stream().map(Rol::getNombre).sorted().findFirst().orElse("SIN_ROL");
        return new LoginResponseDTO(jwt.generarToken(principal, u.getId()),
            jwt.getExpiracionSegundos(), u.getUsername(), u.getNombre(), rol, permisos);
    }
    @Override @Transactional(readOnly = true)
    public UsuarioActualDTO usuarioActual() {
        Usuario u = entidadActual();
        String rol = u.getRoles().stream().map(Rol::getNombre).sorted().findFirst().orElse("SIN_ROL");
        return new UsuarioActualDTO(u.getUsername(), u.getNombre(), rol,
            PermisosPorRol.permisos(u.getRoles()));
    }
    @Override @Transactional(readOnly = true)
    public Usuario entidadActual() {
        String nombre = SecurityContextHolder.getContext().getAuthentication().getName();
        return buscar(nombre);
    }
    private Usuario buscar(String username) {
        return usuarios.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }
}
