package pe.edu.upeu.AgroBackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AgroBackend.entity.Usuario;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameIgnoreCase(String username);
}
