package pe.edu.upeu.AgroBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;
import java.math.BigDecimal;
import java.time.*;

@Entity
@Table(name = "ag_labores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Labor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLabor tipo;
    @Column(nullable = false)
    private LocalDate fecha;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsable_id", nullable = false)
    private Usuario responsable;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;
    @Column(name = "cantidad_jornales", nullable = false)
    private Integer cantidadJornales;
    @Column(nullable = false, length = 500)
    private String observacion;
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    @PrePersist public void prePersist() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
    }
}
