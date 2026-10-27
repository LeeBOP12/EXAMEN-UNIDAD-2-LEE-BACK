package pe.edu.upeu.AgroBackend.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AgroBackend.dto.*;
import pe.edu.upeu.AgroBackend.entity.*;
import pe.edu.upeu.AgroBackend.enums.*;
import pe.edu.upeu.AgroBackend.exception.*;
import pe.edu.upeu.AgroBackend.mapper.LaborMapper;
import pe.edu.upeu.AgroBackend.repository.LaborRepository;
import pe.edu.upeu.AgroBackend.security.PermisosPorRol;
import pe.edu.upeu.AgroBackend.service.service.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class LaborServiceImpl implements LaborService {
    private final LaborRepository labores;
    private final LoteService lotes;
    private final AuthService auth;
    private final LaborMapper mapper;
    public LaborServiceImpl(LaborRepository labores, LoteService lotes,
                            AuthService auth, LaborMapper mapper) {
        this.labores = labores; this.lotes = lotes; this.auth = auth; this.mapper = mapper;
    }
    @Override @Transactional(readOnly = true)
    public PaginaResponseDTO<LaborResponseDTO> listar(Long loteId, int pagina, int tamanio) {
        lotes.buscar(loteId);
        return PaginaResponseDTO.de(labores.findByLoteIdOrderByFechaDescIdDesc(loteId,
            PageRequest.of(Math.max(0, pagina), Math.max(1, Math.min(100, tamanio)))), mapper::toResponse);
    }
    @Override @Transactional
    public LaborResponseDTO crear(LaborRequestDTO request) {
        Lote lote = lotes.buscar(request.getLoteId());
        verificarAbierta(lote);
        validarDosis(request, lote);
        Labor labor = new Labor(null, lote, request.getTipo(), request.getFecha(),
            auth.entidadActual(), BigDecimal.valueOf(request.getCantidad()),
            request.getCantidadJornales() == null ? 1 : request.getCantidadJornales(),
            request.getObservacion() == null ? "" : request.getObservacion().trim(), LocalDateTime.now());
        return mapper.toResponse(labores.save(labor));
    }
    @Override @Transactional
    public LaborResponseDTO actualizar(Long id, LaborRequestDTO request) {
        Labor labor = buscar(id);
        verificarAbierta(labor.getLote());
        verificarCorreccion(labor);
        Lote destino = lotes.buscar(request.getLoteId());
        verificarAbierta(destino);
        validarDosis(request, destino);
        labor.setLote(destino); labor.setTipo(request.getTipo()); labor.setFecha(request.getFecha());
        labor.setCantidad(BigDecimal.valueOf(request.getCantidad()));
        labor.setCantidadJornales(request.getCantidadJornales() == null ? 1 : request.getCantidadJornales());
        labor.setObservacion(request.getObservacion() == null ? "" : request.getObservacion().trim());
        return mapper.toResponse(labores.save(labor));
    }
    @Override @Transactional public void eliminar(Long id) { labores.delete(buscar(id)); }

    private Labor buscar(Long id) {
        return labores.findById(id).orElseThrow(() ->
            new RecursoNoEncontradoException("Labor no encontrada"));
    }
    private void verificarAbierta(Lote lote) {
        if (Boolean.TRUE.equals(lote.getCampaniaCerrada()))
            throw new ReglaNegocioException("No se puede registrar ni corregir una labor en una campaña cerrada");
    }
    private void verificarCorreccion(Labor labor) {
        if (PermisosPorRol.puedeEditarTodas(
                SecurityContextHolder.getContext().getAuthentication().getAuthorities())) return;
        Usuario actual = auth.entidadActual();
        if (!labor.getResponsable().getId().equals(actual.getId()))
            throw new AccessDeniedException("Un técnico solo puede corregir sus propias labores");
        if (labor.getFechaRegistro().isBefore(LocalDateTime.now().minusHours(24)))
            throw new AccessDeniedException("La labor superó las 24 horas permitidas");
    }
    private void validarDosis(LaborRequestDTO request, Lote lote) {
        if (request.getTipo() != TipoLabor.FUMIGACION) return;
        BigDecimal maximo = lote.getDosisMaximaPorHectarea().multiply(lote.getHectareas());
        if (BigDecimal.valueOf(request.getCantidad()).compareTo(maximo) > 0)
            throw new ValidacionNegocioException("Datos inválidos",
                Map.of("cantidad", "La cantidad supera el máximo de " + maximo.toPlainString()));
    }
}
