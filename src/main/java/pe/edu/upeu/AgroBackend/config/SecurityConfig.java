package pe.edu.upeu.AgroBackend.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.upeu.AgroBackend.enums.Permiso;
import pe.edu.upeu.AgroBackend.security.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtService jwt, UserDetailsService usuarios,
        RestAuthenticationEntryPoint entry, RestAccessDeniedHandler denied) throws Exception {
        var filtro = new JwtAuthenticationFilter(jwt, usuarios);
        http.csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .cors(cors -> {})
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/login", "/error").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/lotes/*/labores")
                    .hasAuthority(Permiso.LABOR_LEER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/lotes/**")
                    .hasAuthority(Permiso.LOTE_LEER.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/lotes/*/cerrar-campania")
                    .hasAuthority(Permiso.CAMPANIA_CERRAR.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/labores")
                    .hasAuthority(Permiso.LABOR_CREAR.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/labores/**")
                    .hasAuthority(Permiso.LABOR_EDITAR.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/labores/**")
                    .hasAuthority(Permiso.LABOR_ELIMINAR.name())
                .requestMatchers("/api/v1/usuarios/me").authenticated()
                .requestMatchers("/api/v1/usuarios/**")
                    .hasAuthority(Permiso.USUARIO_GESTIONAR.name())
                .anyRequest().authenticated())
            .exceptionHandling(e -> e.authenticationEntryPoint(entry).accessDeniedHandler(denied))
            .addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }
}
