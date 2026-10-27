package pe.edu.upeu.AgroBackend.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.*;
import java.util.function.Function;

@Getter @AllArgsConstructor
public class PaginaResponseDTO<T> {
    private List<T> contenido;
    private int pagina;
    private int tamanio;
    private long totalElementos;
    private int totalPaginas;
    private boolean ultima;

    public static <T,R> PaginaResponseDTO<R> de(Page<T> page, Function<T,R> mapper) {
        return new PaginaResponseDTO<>(
            page.getContent().stream().map(mapper).toList(),
            page.getNumber(), page.getSize(), page.getTotalElements(),
            page.getTotalPages(), page.isLast());
    }
}
