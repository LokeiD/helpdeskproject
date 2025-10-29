package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subcategorias")
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    @GetMapping
    public List<SubCategoria> obtenerTodasActivas() {
        List<SubCategoria> todas = subCategoriaService.obtenerTodasLasSubCategorias();
        return todas.stream()
                .filter(SubCategoria::isEstado)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategoria> obtenerPorId(@PathVariable Integer id) {
        return subCategoriaService.obtenerSubCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/por-categoria")
    public ResponseEntity<List<SubCategoria>> getPorCategoria(@RequestParam("cat_id") Integer catId) {
        List<SubCategoria> subCategorias = subCategoriaService.obtenerPorCategoria(catId);
        List<SubCategoria> activas = subCategorias.stream()
                .filter(SubCategoria::isEstado)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activas);
    }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubCategoria(@PathVariable Integer id) {
        subCategoriaService.eliminarSubCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
