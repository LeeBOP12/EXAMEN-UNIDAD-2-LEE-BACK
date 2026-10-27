package pe.edu.upeu.AgroBackend.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.AgroBackend.entity.Lote;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    @Query("""
        select l from Lote l
        where (:valle is null or upper(l.valle) = upper(:valle))
          and (:cerrada is null or l.campaniaCerrada = :cerrada)
        order by l.codigo
        """)
    Page<Lote> buscar(@Param("valle") String valle,
                      @Param("cerrada") Boolean cerrada,
                      Pageable pageable);
}
