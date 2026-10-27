package pe.edu.upeu.AgroBackend.security;

import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pe.edu.upeu.AgroBackend.exception.dto.ErrorResponseDTO;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;
    public RestAccessDeniedHandler(ObjectMapper mapper) { this.mapper = mapper; }
    @Override public void handle(HttpServletRequest req, HttpServletResponse res,
                                 AccessDeniedException e) throws IOException {
        res.setStatus(403); res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getWriter(), new ErrorResponseDTO(
            LocalDateTime.now(), 403, "Forbidden",
            "No tiene permisos para realizar esta operación", req.getRequestURI(), null));
    }
}
