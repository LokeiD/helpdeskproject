package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // <-- AÑADIR import

@RestController
@RequestMapping("/api/subcategorias") // URL base para subcategorías
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    // 1. Inyectamos el servicio
    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    /**
     * ENDPOINT MODIFICADO: Obtener SOLO las subcategorías ACTIVAS.
     * GET http://localhost:8080/api/subcategorias
     * (Útil para vistas de mantenimiento, no para el combo de Nuevo Ticket)
     */
    @GetMapping
    public List<SubCategoria> obtenerTodasActivas() {
        List<SubCategoria> todas = subCategoriaService.obtenerTodasLasSubCategorias();
        // Filtramos por estado activo
        return todas.stream()
                .filter(SubCategoria::isEstado)
                .collect(Collectors.toList());
    }

    // ENDPOINT: Obtener una subcategoría por ID (Sin cambios)
    // GET http://localhost:8080/api/subcategorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoria> obtenerPorId(@PathVariable Integer id) {
        return subCategoriaService.obtenerSubCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- ¡NUEVO ENDPOINT PARA EL COMBO DINÁMICO! ---
    /**
     * Reemplaza a: subcategoria.php?op=combo
     * Devuelve una lista JSON de subcategorías ACTIVAS filtradas por cat_id.
     * GET http://localhost:8080/api/subcategorias/por-categoria?cat_id=100
     */
    @GetMapping("/por-categoria")
    public ResponseEntity<List<SubCategoria>> getPorCategoria(@RequestParam("cat_id") Integer catId) {
        // Asegúrate que tu SubCategoriaService tenga este método
        List<SubCategoria> subCategorias = subCategoriaService.obtenerPorCategoria(catId);
        // Filtramos por estado activo
        List<SubCategoria> activas = subCategorias.stream()
                .filter(SubCategoria::isEstado)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activas);
    }


    // ENDPOINT: Crear una nueva subcategoría (Sin cambios)
    // POST http://localhost:8080/api/subcategorias?categoriaId=101
    // Body (JSON): { ... }
    @PostMapping
    public ResponseEntity<SubCategoria> crearSubCategoria(
            @RequestBody SubCategoria subCategoria,
            @RequestParam Integer categoriaId
    ) {
        try {
            SubCategoria nuevaSubCategoria = subCategoriaService.guardarSubCategoria(subCategoria, categoriaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSubCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ENDPOINT: Eliminar una subcategoría (Sin cambios)
    // DELETE http://localhost:8080/api/subcategorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubCategoria(@PathVariable Integer id) {
        subCategoriaService.eliminarSubCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
