package pe.edu.upeu.AgroBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroBackend.dto.*;
import pe.edu.upeu.AgroBackend.service.service.LaborService;

@RestController
@RequestMapping("/api/v1/labores")
public class LaborController {
    private final LaborService labores;
    public LaborController(LaborService labores) { this.labores = labores; }
    @PostMapping
    public ResponseEntity<LaborResponseDTO> crear(@Valid @RequestBody LaborRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labores.crear(request));
    }
    @PutMapping("/{id}")
    public LaborResponseDTO actualizar(@PathVariable Long id,
        @Valid @RequestBody LaborRequestDTO request) {
        return labores.actualizar(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        labores.eliminar(id); return ResponseEntity.noContent().build();
    }
}
