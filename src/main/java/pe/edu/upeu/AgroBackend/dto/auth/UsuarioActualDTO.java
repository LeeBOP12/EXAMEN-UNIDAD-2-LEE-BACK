package pe.edu.upeu.AgroBackend.dto.auth;
import lombok.*;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioActualDTO {
    private String usuario;
    private String nombre;
    private String rol;
    private List<String> privilegios;
}
