package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/areas") // URL base para áreas
public class AreaController {

    private final AreaService areaService;

    // 1. Inyectamos el servicio de Area
    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    // ENDPOINT: Obtener todas las áreas
    // GET http://localhost:8080/api/areas
    @GetMapping
    public List<Area> obtenerTodas() {
        return areaService.obtenerTodasLasAreas();
    }

    // ENDPOINT: Obtener un área por ID
    // GET http://localhost:8080/api/areas/101
    @GetMapping("/{id}")
    public ResponseEntity<Area> obtenerPorId(@PathVariable Integer id) {
        Optional<Area> area = areaService.obtenerAreaPorId(id);

        return area.map(ResponseEntity::ok) // Forma corta de if/else
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva área
    // POST http://localhost:8080/api/areas
    // Body (JSON): { "nombreArea": "Contabilidad" }
    @PostMapping
    public ResponseEntity<Area> crearArea(@RequestBody Area area) {
        Area nuevaArea = areaService.guardarArea(area);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaArea);
    }

    // ENDPOINT: Actualizar un área
    // PUT http://localhost:8080/api/areas/101
    // Body (JSON): { "nombreArea": "Contabilidad y Finanzas" }
    @PutMapping("/{id}")
    public ResponseEntity<Area> actualizarArea(@PathVariable Integer id, @RequestBody Area area) {
        // Verificamos si existe
        if (!areaService.obtenerAreaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        area.setCodigoArea(id); // Asignamos el ID para asegurar que sea una actualización
        Area areaActualizada = areaService.guardarArea(area);

        return ResponseEntity.ok(areaActualizada);
    }

    // ENDPOINT: Eliminar un área
    // DELETE http://localhost:8080/api/areas/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArea(@PathVariable Integer id) {
        areaService.eliminarArea(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}