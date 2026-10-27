package pe.edu.upeu.AgroBackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AgroBackend.entity.Rol;
import java.util.Optional;
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
