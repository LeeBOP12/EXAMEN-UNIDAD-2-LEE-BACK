package pe.edu.upeu.AgroBackend.service.service;
import pe.edu.upeu.AgroBackend.dto.auth.*;
import pe.edu.upeu.AgroBackend.entity.Usuario;
public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    UsuarioActualDTO usuarioActual();
    Usuario entidadActual();
}
