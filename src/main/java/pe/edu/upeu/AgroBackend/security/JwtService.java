package pe.edu.upeu.AgroBackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class JwtService {
    private final SecretKey clave;
    private final long expiracionMinutos;
    public JwtService(@Value("${app.jwt.secret}") String secreto,
                      @Value("${app.jwt.expiracion-minutos:60}") long minutos) {
        this.clave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));
        this.expiracionMinutos = minutos;
    }
    public String generarToken(UserDetails usuario, Long usuarioId) {
        Instant ahora = Instant.now();
        List<String> permisos = usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder().subject(usuario.getUsername())
            .claim("usuarioId", usuarioId).claim("authorities", permisos)
            .issuedAt(Date.from(ahora))
            .expiration(Date.from(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)))
            .signWith(clave).compact();
    }
    public String extraerUsername(String token) { return leerClaims(token).getSubject(); }
    public Claims leerClaims(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }
    public long getExpiracionSegundos() { return expiracionMinutos * 60; }
}
