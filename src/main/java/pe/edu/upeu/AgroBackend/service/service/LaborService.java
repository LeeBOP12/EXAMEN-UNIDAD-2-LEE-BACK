package pe.edu.upeu.AgroBackend.service.service;
import pe.edu.upeu.AgroBackend.dto.*;
public interface LaborService {
    PaginaResponseDTO<LaborResponseDTO> listar(Long loteId, int pagina, int tamanio);
    LaborResponseDTO crear(LaborRequestDTO request);
    LaborResponseDTO actualizar(Long id, LaborRequestDTO request);
    void eliminar(Long id);
}
