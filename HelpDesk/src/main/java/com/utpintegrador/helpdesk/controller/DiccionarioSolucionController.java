package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DiccionarioSolucion;
import com.utpintegrador.helpdesk.service.DiccionarioSolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diccionario-soluciones") // URL base
public class DiccionarioSolucionController {

    private final DiccionarioSolucionService diccionarioSolucionService;

    // 1. Inyectamos el servicio
    @Autowired
    public DiccionarioSolucionController(DiccionarioSolucionService diccionarioSolucionService) {
        this.diccionarioSolucionService = diccionarioSolucionService;
    }

    // ENDPOINT: Obtener todas las soluciones
    // GET http://localhost:8080/api/diccionario-soluciones
    @GetMapping
    public List<DiccionarioSolucion> obtenerTodas() {
        return diccionarioSolucionService.obtenerTodasLasSoluciones();
    }

    // ENDPOINT: Obtener una solución por ID
    // GET http://localhost:8080/api/diccionario-soluciones/101
    @GetMapping("/{id}")
    public ResponseEntity<DiccionarioSolucion> obtenerPorId(@PathVariable Integer id) {
        return diccionarioSolucionService.obtenerSolucionPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva solución
    // Petición: POST http://localhost:8080/api/diccionario-soluciones?usuarioId=101&subCategoriaId=105
    // Body (JSON): {
    //   "titulo": "Solución para error de impresora X",
    //   "descripcion": "Ir a panel de control y reiniciar la cola de impresión..."
    // }
    @PostMapping
    public ResponseEntity<DiccionarioSolucion> crearSolucion(
            @RequestBody DiccionarioSolucion solucion, // 1. El JSON del body
            @RequestParam Integer usuarioId,           // 2. ID del usuario
            @RequestParam Integer subCategoriaId       // 3. ID de la subcategoría
    ) {
        try {
            // 4. Pasamos todo al servicio
            DiccionarioSolucion nuevaSolucion = diccionarioSolucionService.crearSolucion(solucion, usuarioId, subCategoriaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolucion);
        } catch (RuntimeException e) {
            // Error si el usuario o la subcategoría no existen
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // ENDPOINT: Eliminar una solución
    // DELETE http://localhost:8080/api/diccionario-soluciones/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolucion(@PathVariable Integer id) {
        diccionarioSolucionService.eliminarSolucion(id);
        return ResponseEntity.noContent().build();
    }
}