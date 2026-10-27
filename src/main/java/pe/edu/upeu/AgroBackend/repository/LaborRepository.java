package pe.edu.upeu.AgroBackend.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AgroBackend.entity.Labor;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;

public interface LaborRepository extends JpaRepository<Labor, Long> {
    Page<Labor> findByLoteIdOrderByFechaDescIdDesc(Long loteId, Pageable pageable);
    boolean existsByLoteIdAndTipo(Long loteId, TipoLabor tipo);
}
