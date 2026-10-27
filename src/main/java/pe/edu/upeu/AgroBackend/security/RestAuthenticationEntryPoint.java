package pe.edu.upeu.AgroBackend.security;

import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroBackend.exception.dto.ErrorResponseDTO;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;
    public RestAuthenticationEntryPoint(ObjectMapper mapper) { this.mapper = mapper; }
    @Override public void commence(HttpServletRequest req, HttpServletResponse res,
                                   AuthenticationException e) throws IOException {
        String mensaje = (String) req.getAttribute(JwtAuthenticationFilter.ATRIBUTO_ERROR);
        res.setStatus(401); res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getWriter(), new ErrorResponseDTO(
            LocalDateTime.now(), 401, "Unauthorized",
            mensaje == null ? "Debe iniciar sesión" : mensaje, req.getRequestURI(), null));
    }
}
