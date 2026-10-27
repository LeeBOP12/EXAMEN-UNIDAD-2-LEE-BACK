package pe.edu.upeu.AgroBackend.mapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroBackend.dto.LaborResponseDTO;
import pe.edu.upeu.AgroBackend.entity.Labor;
import pe.edu.upeu.AgroBackend.security.PermisosPorRol;
import java.time.LocalDateTime;

@Component
public class LaborMapper {
    public LaborResponseDTO toResponse(Labor labor) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        boolean tieneEdicion = autenticacion != null && autenticacion.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("LABOR_EDITAR"));
        boolean edicionGlobal = autenticacion != null &&
            PermisosPorRol.puedeEditarTodas(autenticacion.getAuthorities());
        boolean propiaYVigente = tieneEdicion &&
            labor.getResponsable().getUsername().equalsIgnoreCase(autenticacion.getName()) &&
            !labor.getFechaRegistro().isBefore(LocalDateTime.now().minusHours(24));
        return new LaborResponseDTO(
            labor.getId(), labor.getLote().getId(), labor.getTipo(), labor.getFecha(),
            labor.getResponsable().getNombre(), labor.getResponsable().getUsername(),
            labor.getCantidad().doubleValue(), labor.getCantidadJornales(),
            labor.getObservacion(), labor.getFechaRegistro(), edicionGlobal || propiaYVigente
        );
    }
}
