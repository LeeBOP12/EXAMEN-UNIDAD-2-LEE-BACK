package pe.edu.upeu.AgroBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LaborRequestDTO {
    @NotNull @Positive
    private Long loteId;
    @NotNull
    private TipoLabor tipo;
    @NotNull
    private LocalDate fecha;
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    private Double cantidad;
    @Positive(message = "La cantidad de jornales debe ser mayor que cero")
    private Integer cantidadJornales = 1;
    private String observacion = "";
}
