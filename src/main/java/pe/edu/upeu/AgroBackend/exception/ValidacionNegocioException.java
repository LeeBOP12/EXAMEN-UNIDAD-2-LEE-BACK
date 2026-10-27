package pe.edu.upeu.AgroBackend.exception;
import java.util.Map;
import lombok.Getter;
@Getter
public class ValidacionNegocioException extends RuntimeException {
    private final Map<String,String> errores;
    public ValidacionNegocioException(String mensaje, Map<String,String> errores) {
        super(mensaje); this.errores = errores;
    }
}
