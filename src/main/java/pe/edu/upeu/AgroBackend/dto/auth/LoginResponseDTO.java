package pe.edu.upeu.AgroBackend.dto.auth;
import lombok.*;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private long expiraEnSegundos;
    private String usuario;
    private String nombre;
    private String rol;
    private List<String> privilegios;
}
