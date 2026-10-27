package pe.edu.upeu.AgroBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ag_lotes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Lote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;
    @Column(nullable = false, length = 120)
    private String nombre;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hectareas;
    @Column(nullable = false, length = 80)
    private String cultivo;
    @Column(nullable = false, length = 80)
    private String valle;
    @Column(name = "campania_cerrada", nullable = false)
    private Boolean campaniaCerrada = false;
    @Column(name = "dosis_maxima_por_hectarea", nullable = false, precision = 10, scale = 2)
    private BigDecimal dosisMaximaPorHectarea;
}
