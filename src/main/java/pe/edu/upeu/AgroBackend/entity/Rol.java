package pe.edu.upeu.AgroBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ag_roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String nombre;
    @Column(length = 180)
    private String descripcion;
}
