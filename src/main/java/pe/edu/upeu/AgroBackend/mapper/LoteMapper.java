package pe.edu.upeu.AgroBackend.mapper;
import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroBackend.dto.LoteResponseDTO;
import pe.edu.upeu.AgroBackend.entity.Lote;
@Component
public class LoteMapper {
    public LoteResponseDTO toResponse(Lote l) {
        return new LoteResponseDTO(l.getId(), l.getCodigo(), l.getNombre(),
            l.getHectareas().doubleValue(), l.getCultivo(), l.getValle(),
            l.getCampaniaCerrada(), l.getDosisMaximaPorHectarea().doubleValue());
    }
}
