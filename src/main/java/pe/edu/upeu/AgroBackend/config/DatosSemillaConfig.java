package pe.edu.upeu.AgroBackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.AgroBackend.entity.*;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;
import pe.edu.upeu.AgroBackend.repository.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Configuration
public class DatosSemillaConfig {
    @Bean
    CommandLineRunner datosSemilla(
        RolRepository roles,
        UsuarioRepository usuarios,
        LoteRepository lotes,
        LaborRepository labores,
        PasswordEncoder encoder
    ) {
        return args -> {
            Rol tecnico = asegurarRol(roles, "TECNICO", "Técnico de campo");
            Rol jefe = asegurarRol(roles, "JEFE_CAMPO", "Jefe de campo");
            Rol admin = asegurarRol(roles, "ADMINISTRADOR", "Administrador");
            Rol supervisor = asegurarRol(
                roles, "SUPERVISOR_CALIDAD", "Supervisor de calidad"
            );

            asegurarUsuario(usuarios, encoder, "tecnico1", "Lee Orbezo", tecnico);
            asegurarUsuario(usuarios, encoder, "jefe1", "Pool Orbezo", jefe);
            asegurarUsuario(usuarios, encoder, "admin", "Sonia Orbezo", admin);
            asegurarUsuario(usuarios, encoder, "supervisor1", "Henry Orbezo", supervisor);

            if (lotes.count() == 0) {
                List<Lote> lista = lotes.saveAll(List.of(
                    lote("VAL-001","Lote Amanecer",4.5,"Papa","Valle Azul",false,2.0),
                    lote("VAL-002","Lote Esperanza",6.0,"Maíz","Valle Azul",false,1.5),
                    lote("SUR-001","Lote Pucará",3.2,"Quinua","Valle Sur",false,1.2),
                    lote("SUR-002","Lote Andenes",5.8,"Papa","Valle Sur",false,2.0),
                    lote("ALT-001","Lote Killa",2.5,"Haba","Valle Alto",false,1.0),
                    lote("ALT-002","Lote Inti",7.0,"Maíz","Valle Alto",false,1.4),
                    lote("VAL-003","Lote Histórico",4.0,"Cebada","Valle Azul",true,1.1),
                    lote("SUR-003","Lote Cerrado",3.7,"Papa","Valle Sur",true,1.8)
                ));
                Usuario usuarioTecnico = usuarios.findByUsernameIgnoreCase("tecnico1").orElseThrow();
                Usuario usuarioJefe = usuarios.findByUsernameIgnoreCase("jefe1").orElseThrow();
                List<Labor> semillas = new ArrayList<>();
                for (int i = 0; i < lista.size(); i++) {
                    int cantidad = i < 4 ? 3 : 2;
                    for (int p = 0; p < cantidad; p++) {
                        TipoLabor tipo = i == 0 && p == 2 ? TipoLabor.COSECHA :
                            (p == 1 ? TipoLabor.FUMIGACION : TipoLabor.RIEGO);
                        semillas.add(new Labor(
                            null,
                            lista.get(i),
                            tipo,
                            LocalDate.now().minusDays(i + p),
                            (i + p) % 2 == 0 ? usuarioTecnico : usuarioJefe,
                            BigDecimal.valueOf(tipo == TipoLabor.FUMIGACION ? 2.0 : 15.0 + p),
                            2 + p,
                            "Labor semilla " + (p + 1) + " en " + lista.get(i).getCodigo(),
                            LocalDateTime.now().minusHours(i * 3L + p)
                        ));
                    }
                }
                labores.saveAll(semillas);
            }
        };
    }

    private Rol asegurarRol(RolRepository roles, String nombre, String descripcion) {
        return roles.findByNombre(nombre)
            .orElseGet(() -> roles.save(new Rol(null, nombre, descripcion)));
    }

    private void asegurarUsuario(
        UsuarioRepository usuarios,
        PasswordEncoder encoder,
        String username,
        String nombre,
        Rol rol
    ) {
        Usuario usuario = usuarios.findByUsernameIgnoreCase(username)
            .orElseGet(() -> new Usuario(
                null,
                username,
                encoder.encode("Agro2026*"),
                nombre,
                true,
                new HashSet<>(Set.of(rol))
            ));
        usuario.setNombre(nombre);
        usuario.getRoles().add(rol);
        usuarios.save(usuario);
    }

    private Lote lote(
        String codigo,
        String nombre,
        double hectareas,
        String cultivo,
        String valle,
        boolean cerrada,
        double dosis
    ) {
        return new Lote(
            null, codigo, nombre, BigDecimal.valueOf(hectareas), cultivo, valle,
            cerrada, BigDecimal.valueOf(dosis)
        );
    }
}
