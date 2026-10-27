package pe.edu.upeu.AgroBackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.edu.upeu.AgroBackend.exception.dto.ErrorResponseDTO;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponseDTO> respuesta(HttpStatus status, String mensaje,
        HttpServletRequest request, Map<String,String> validaciones) {
        return ResponseEntity.status(status).body(new ErrorResponseDTO(
            LocalDateTime.now(), status.value(), status.getReasonPhrase(),
            mensaje, request.getRequestURI(), validaciones));
    }
    @ExceptionHandler(RecursoNoEncontradoException.class)
    ResponseEntity<ErrorResponseDTO> noEncontrado(RecursoNoEncontradoException e, HttpServletRequest r) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage(), r, null);
    }
    @ExceptionHandler(ReglaNegocioException.class)
    ResponseEntity<ErrorResponseDTO> conflicto(ReglaNegocioException e, HttpServletRequest r) {
        return respuesta(HttpStatus.CONFLICT, e.getMessage(), r, null);
    }
    @ExceptionHandler(ValidacionNegocioException.class)
    ResponseEntity<ErrorResponseDTO> validacionNegocio(ValidacionNegocioException e, HttpServletRequest r) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage(), r, e.getErrores());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponseDTO> validacion(MethodArgumentNotValidException e, HttpServletRequest r) {
        Map<String,String> errores = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> errores.put(x.getField(), x.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Existen errores de validación", r, errores);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ErrorResponseDTO> cuerpo(Exception e, HttpServletRequest r) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo o parámetro no es válido", r,
            Map.of("request", "Revisa el formato y los tipos enviados"));
    }
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponseDTO> autenticacion(AuthenticationException e, HttpServletRequest r) {
        return respuesta(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", r, null);
    }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponseDTO> prohibido(AccessDeniedException e, HttpServletRequest r) {
        return respuesta(HttpStatus.FORBIDDEN, e.getMessage(), r, null);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDTO> general(Exception e, HttpServletRequest r) {
        if (e instanceof ErrorResponse springError) {
            HttpStatus status = HttpStatus.resolve(springError.getStatusCode().value());
            return respuesta(status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status,
                springError.getBody().getDetail(), r, null);
        }
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", r, null);
    }
}
