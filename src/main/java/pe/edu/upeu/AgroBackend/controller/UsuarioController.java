package pe.edu.upeu.AgroBackend.controller;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroBackend.dto.auth.UsuarioActualDTO;
import pe.edu.upeu.AgroBackend.service.service.AuthService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final AuthService auth;
    public UsuarioController(AuthService auth) { this.auth = auth; }
    @GetMapping("/me") public UsuarioActualDTO actual() { return auth.usuarioActual(); }
}
