package pe.edu.upeu.AgroBackend.security;

import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String ATRIBUTO_ERROR = "jwt.error";
    private final JwtService jwt;
    private final UserDetailsService usuarios;
    public JwtAuthenticationFilter(JwtService jwt, UserDetailsService usuarios) {
        this.jwt = jwt; this.usuarios = usuarios;
    }
    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                               FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) { chain.doFilter(req, res); return; }
        try {
            String username = jwt.extraerUsername(header.substring(7).trim());
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails details = usuarios.loadUserByUsername(username);
                if (!details.isEnabled()) {
                    req.setAttribute(ATRIBUTO_ERROR, "La cuenta está deshabilitada");
                } else {
                    var auth = new UsernamePasswordAuthenticationToken(
                        details, null, details.getAuthorities()
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (ExpiredJwtException e) {
            req.setAttribute(ATRIBUTO_ERROR, "El token ha expirado. Inicie sesión nuevamente");
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            req.setAttribute(ATRIBUTO_ERROR, "El token no es válido");
        }
        chain.doFilter(req, res);
    }
}
