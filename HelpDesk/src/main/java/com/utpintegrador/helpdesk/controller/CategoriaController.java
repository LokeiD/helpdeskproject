package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias") // URL base para categorías
public class CategoriaController {

    private final CategoriaService categoriaService;

    // 1. Inyectamos el servicio de Categoria
    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ENDPOINT: Obtener todas las categorías
    // GET http://localhost:8080/api/categorias
    @GetMapping
    public List<Categoria> obtenerTodas() {
        return categoriaService.obtenerTodasLasCategorias();
    }

    // ENDPOINT: Obtener una categoría por ID
    // GET http://localhost:8080/api/categorias/101
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaService.obtenerCategoriaPorId(id);

        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva categoría
    // POST http://localhost:8080/api/categorias
    // Body (JSON): { "nombreCategoria": "Hardware", "estado": true }
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    // ENDPOINT: Actualizar una categoría
    // PUT http://localhost:8080/api/categorias/101
    // Body (JSON): { "nombreCategoria": "Hardware Básico", "estado": true }
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        // Verificamos si existe
        if (!categoriaService.obtenerCategoriaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        categoria.setCodigoCategoria(id); // Asignamos el ID
        Categoria categoriaActualizada = categoriaService.guardarCategoria(categoria);

        return ResponseEntity.ok(categoriaActualizada);
    }

    // ENDPOINT: Eliminar una categoría
    // DELETE http://localhost:8080/api/categorias/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        // (Aquí podríamos necesitar manejo de errores si la categoría tiene subcategorías)
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}