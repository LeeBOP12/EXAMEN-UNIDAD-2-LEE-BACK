package pe.edu.upeu.AgroBackend.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AgroBackend.dto.*;
import pe.edu.upeu.AgroBackend.entity.Lote;
import pe.edu.upeu.AgroBackend.enums.TipoLabor;
import pe.edu.upeu.AgroBackend.exception.*;
import pe.edu.upeu.AgroBackend.mapper.LoteMapper;
import pe.edu.upeu.AgroBackend.repository.*;
import pe.edu.upeu.AgroBackend.service.service.LoteService;

@Service
public class LoteServiceImpl implements LoteService {
    private final LoteRepository lotes;
    private final LaborRepository labores;
    private final LoteMapper mapper;
    public LoteServiceImpl(LoteRepository lotes, LaborRepository labores, LoteMapper mapper) {
        this.lotes = lotes; this.labores = labores; this.mapper = mapper;
    }
    @Override @Transactional(readOnly = true)
    public PaginaResponseDTO<LoteResponseDTO> listar(int pagina, int tamanio, String valle, Boolean cerrada) {
        String filtro = valle == null || valle.isBlank() ? null : valle.trim();
        return PaginaResponseDTO.de(lotes.buscar(filtro, cerrada,
            PageRequest.of(Math.max(0, pagina), Math.max(1, Math.min(100, tamanio)))), mapper::toResponse);
    }
    @Override @Transactional(readOnly = true)
    public LoteResponseDTO obtener(Long id) { return mapper.toResponse(buscar(id)); }
    @Override @Transactional
    public LoteResponseDTO cerrarCampania(Long id) {
        Lote lote = buscar(id);
        if (Boolean.TRUE.equals(lote.getCampaniaCerrada()))
            throw new ReglaNegocioException("La campaña ya está cerrada");
        if (!labores.existsByLoteIdAndTipo(id, TipoLabor.COSECHA))
            throw new ReglaNegocioException("El lote no tiene una labor de tipo COSECHA");
        lote.setCampaniaCerrada(true);
        return mapper.toResponse(lotes.save(lote));
    }
    @Override public Lote buscar(Long id) {
        return lotes.findById(id).orElseThrow(() ->
            new RecursoNoEncontradoException("Lote no encontrado"));
    }
}
