package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DetalleEvidencia;
import com.utpintegrador.helpdesk.service.DetalleEvidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-evidencias") // URL base
public class DetalleEvidenciaController {

    private final DetalleEvidenciaService detalleEvidenciaService;

    // 1. Inyectamos el servicio
    @Autowired
    public DetalleEvidenciaController(DetalleEvidenciaService detalleEvidenciaService) {
        this.detalleEvidenciaService = detalleEvidenciaService;
    }

    // ENDPOINT: Obtener todos los detalles
    // GET http://localhost:8080/api/detalle-evidencias
    @GetMapping
    public List<DetalleEvidencia> obtenerTodos() {
        return detalleEvidenciaService.obtenerTodosLosDetalles();
    }

    // ENDPOINT: Obtener un detalle por ID
    // GET http://localhost:8080/api/detalle-evidencias/101
    @GetMapping("/{id}")
    public ResponseEntity<DetalleEvidencia> obtenerPorId(@PathVariable Integer id) {
        return detalleEvidenciaService.obtenerDetallePorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear un nuevo detalle de evidencia
    // Petición: POST http://localhost:8080/api/detalle-evidencias?nombreDetalle=captura_pantalla_01.png&evidenciaId=101
    @PostMapping
    public ResponseEntity<DetalleEvidencia> crearDetalleEvidencia(
            @RequestParam String nombreDetalle, // 1. El nombre o detalle
            @RequestParam Integer evidenciaId   // 2. El ID de la evidencia padre
    ) {
        try {
            // 3. Pasamos todo al servicio
            DetalleEvidencia nuevoDetalle = detalleEvidenciaService.guardarDetalleArchivo(nombreDetalle, evidenciaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (RuntimeException e) {
            // Error si la evidencia no existe
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // ENDPOINT: Eliminar un detalle
    // DELETE http://localhost:8080/api/detalle-evidencias/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Integer id) {
        detalleEvidenciaService.eliminarDetalleEvidencia(id);
        return ResponseEntity.noContent().build();
    }
}