package pe.edu.upeu.AgroBackend.dto;
import lombok.*;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LaborResponseDTO {
    private Long id;
    private Long loteId;
    private TipoLabor tipo;
    private LocalDate fecha;
    private String responsable;
    private String responsableUsuario;
    private Double cantidad;
    private Integer cantidadJornales;
    private String observacion;
    private LocalDateTime fechaRegistro;
    private Boolean puedeEditar;
}
