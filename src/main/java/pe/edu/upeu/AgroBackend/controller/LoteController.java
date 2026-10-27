package pe.edu.upeu.AgroBackend.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroBackend.dto.*;
import pe.edu.upeu.AgroBackend.service.service.*;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController {
    private final LoteService lotes;
    private final LaborService labores;
    public LoteController(LoteService lotes, LaborService labores) {
        this.lotes = lotes; this.labores = labores;
    }
    @GetMapping
    public PaginaResponseDTO<LoteResponseDTO> listar(
        @RequestParam(defaultValue="0") int pagina,
        @RequestParam(defaultValue="20") int tamanio,
        @RequestParam(required=false) String valle,
        @RequestParam(required=false) Boolean campaniaCerrada) {
        return lotes.listar(pagina, tamanio, valle, campaniaCerrada);
    }
    @GetMapping("/{id}") public LoteResponseDTO obtener(@PathVariable Long id) {
        return lotes.obtener(id);
    }
    @GetMapping("/{id}/labores")
    public PaginaResponseDTO<LaborResponseDTO> labores(@PathVariable Long id,
        @RequestParam(defaultValue="0") int pagina,
        @RequestParam(defaultValue="20") int tamanio) {
        return labores.listar(id, pagina, tamanio);
    }
    @PostMapping("/{id}/cerrar-campania")
    public LoteResponseDTO cerrar(@PathVariable Long id) { return lotes.cerrarCampania(id); }
}
