package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // <-- AÑADIR import

@RestController
@RequestMapping("/api/categorias") // URL base para categorías
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * ENDPOINT MODIFICADO: Obtener SOLO las categorías ACTIVAS.
     * GET http://localhost:8080/api/categorias
     * Devuelve una lista JSON de categorías activas para los combos.
     */
    @GetMapping
    public List<Categoria> obtenerTodasActivas() {
        List<Categoria> todas = categoriaService.obtenerTodasLasCategorias();
        // Usamos Streams de Java para filtrar la lista
        return todas.stream()
                .filter(Categoria::isEstado) // Filtra solo las que tienen estado = true
                .collect(Collectors.toList()); // Devuelve la nueva lista filtrada
    }

    // ENDPOINT: Obtener una categoría por ID (Sin cambios)
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaService.obtenerCategoriaPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva categoría (Sin cambios)
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    // ENDPOINT: Actualizar una categoría (Sin cambios)
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        if (!categoriaService.obtenerCategoriaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        categoria.setCodigoCategoria(id);
        Categoria categoriaActualizada = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    // ENDPOINT: Eliminar una categoría (Sin cambios)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
