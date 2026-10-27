package pe.edu.upeu.AgroBackend.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import pe.edu.upeu.AgroBackend.entity.Usuario;
import pe.edu.upeu.AgroBackend.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarios;
    public UsuarioDetailsService(UsuarioRepository usuarios) { this.usuarios = usuarios; }
    @Override public UserDetails loadUserByUsername(String username) {
        Usuario u = usuarios.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(u.getUsername(), u.getPassword(), u.getEstado(), true, true, true,
            PermisosPorRol.authorities(u.getRoles()));
    }
}
