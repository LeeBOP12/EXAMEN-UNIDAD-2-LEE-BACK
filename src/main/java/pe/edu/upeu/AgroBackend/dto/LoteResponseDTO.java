package pe.edu.upeu.AgroBackend.dto;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoteResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Double hectareas;
    private String cultivo;
    private String valle;
    private Boolean campaniaCerrada;
    private Double dosisMaximaPorHectarea;
}
