package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategorias") // URL base para subcategorías
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    // 1. Inyectamos el servicio
    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    // ENDPOINT: Obtener todas las subcategorías
    // GET http://localhost:8080/api/subcategorias
    @GetMapping
    public List<SubCategoria> obtenerTodas() {
        return subCategoriaService.obtenerTodasLasSubCategorias();
    }

    // ENDPOINT: Obtener una subcategoría por ID
    // GET http://localhost:8080/api/subcategorias/101
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoria> obtenerPorId(@PathVariable Integer id) {
        return subCategoriaService.obtenerSubCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva subcategoría
    // Petición: POST http://localhost:8080/api/subcategorias?categoriaId=101
    // Body (JSON): {
    //   "nombre": "Problema de Impresora",
    //   "estado": true
    // }
    @PostMapping
    public ResponseEntity<SubCategoria> crearSubCategoria(
            @RequestBody SubCategoria subCategoria, // 1. El JSON del body
            @RequestParam Integer categoriaId      // 2. El ID de la URL
    ) {
        try {
            // 3. Pasamos todo al servicio
            SubCategoria nuevaSubCategoria = subCategoriaService.guardarSubCategoria(subCategoria, categoriaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSubCategoria);
        } catch (RuntimeException e) {
            // Esto captura el error si la Categoría no existe
            return ResponseEntity.badRequest().build(); // Devuelve 400 Bad Request
        }
    }

    // ENDPOINT: Eliminar una subcategoría
    // DELETE http://localhost:8080/api/subcategorias/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubCategoria(@PathVariable Integer id) {
        subCategoriaService.eliminarSubCategoria(id);
        return ResponseEntity.noContent().build();
    }
}