package pe.edu.upeu.AgroBackend.dto.auth;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank private String username;
    @NotBlank private String password;
}
