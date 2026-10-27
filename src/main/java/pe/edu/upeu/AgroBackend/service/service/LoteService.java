package pe.edu.upeu.AgroBackend.service.service;
import pe.edu.upeu.AgroBackend.dto.*;
import pe.edu.upeu.AgroBackend.entity.Lote;
public interface LoteService {
    PaginaResponseDTO<LoteResponseDTO> listar(int pagina, int tamanio, String valle, Boolean cerrada);
    LoteResponseDTO obtener(Long id);
    LoteResponseDTO cerrarCampania(Long id);
    Lote buscar(Long id);
}
