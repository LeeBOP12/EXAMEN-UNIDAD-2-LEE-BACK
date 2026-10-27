package pe.edu.upeu.AgroBackend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pe.edu.upeu.AgroBackend.entity.Rol;
import pe.edu.upeu.AgroBackend.enums.Permiso;
import java.util.*;

public final class PermisosPorRol {
    private static final Map<String, Set<Permiso>> PERMISOS = Map.of(
        "TECNICO", Collections.unmodifiableSet(EnumSet.of(
            Permiso.LOTE_LEER, Permiso.LABOR_LEER, Permiso.LABOR_CREAR, Permiso.LABOR_EDITAR)),
        "JEFE_CAMPO", Collections.unmodifiableSet(EnumSet.of(
            Permiso.LOTE_LEER, Permiso.LABOR_LEER, Permiso.LABOR_CREAR,
            Permiso.LABOR_EDITAR, Permiso.LABOR_ELIMINAR, Permiso.CAMPANIA_CERRAR)),
        "ADMINISTRADOR", Collections.unmodifiableSet(EnumSet.of(
            Permiso.LOTE_LEER, Permiso.LABOR_LEER, Permiso.USUARIO_GESTIONAR)),
        "SUPERVISOR_CALIDAD", Collections.unmodifiableSet(EnumSet.of(
            Permiso.LOTE_LEER, Permiso.LABOR_LEER, Permiso.LABOR_EDITAR))
    );
    private PermisosPorRol() {}

    public static List<String> permisos(Collection<Rol> roles) {
        Set<String> salida = new TreeSet<>();
        roles.forEach(r -> PERMISOS.getOrDefault(r.getNombre(), Set.of())
            .forEach(p -> salida.add(p.name())));
        return new ArrayList<>(salida);
    }

    public static List<GrantedAuthority> authorities(Collection<Rol> roles) {
        List<GrantedAuthority> salida = new ArrayList<>();
        roles.forEach(r -> salida.add(new SimpleGrantedAuthority("ROLE_" + r.getNombre())));
        permisos(roles).forEach(p -> salida.add(new SimpleGrantedAuthority(p)));
        return salida;
    }

    public static boolean puedeEditarTodas(Collection<? extends GrantedAuthority> authorities) {
        Set<String> permisos = new HashSet<>();
        authorities.forEach(a -> permisos.add(a.getAuthority()));
        return permisos.contains(Permiso.LABOR_EDITAR.name()) &&
            (!permisos.contains(Permiso.LABOR_CREAR.name()) ||
             permisos.contains(Permiso.LABOR_ELIMINAR.name()) ||
             permisos.contains(Permiso.CAMPANIA_CERRAR.name()));
    }
}
