package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Rol;
import com.utpintegrador.helpdesk.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // 1. Marca la clase como un Controlador que devuelve JSON
@RequestMapping("/api/roles") // 2. URL base para todos los métodos en esta clase
public class RolController {

    private final RolService rolService;

    // 3. Inyectamos el Servicio (el controlador solo habla con el servicio)
    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // --- MÉTODOS DEL API ---

    // ENDPOINT 1: Obtener todos los roles
    // Petición: GET http://localhost:8080/api/roles
    @GetMapping
    public List<Rol> obtenerTodos() {
        return rolService.obtenerTodosLosRoles();
    }

    // ENDPOINT 2: Obtener un rol por su ID
    // Petición: GET http://localhost:8080/api/roles/101
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Integer id) {
        Optional<Rol> rol = rolService.obtenerRolPorId(id);

        if (rol.isPresent()) {
            return ResponseEntity.ok(rol.get()); // Devuelve 200 OK y el rol
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    // ENDPOINT 3: Crear un nuevo rol
    // Petición: POST http://localhost:8080/api/roles
    // Body (JSON): { "nombreRol": "NUEVO ROL", "estado": true }
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.guardarRol(rol);
        // Devuelve 201 Created y el rol recién creado
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    // ENDPOINT 4: Actualizar un rol existente
    // Petición: PUT http://localhost:8080/api/roles/101
    // Body (JSON): { "nombreRol": "NOMBRE ACTUALIZADO", "estado": true }
    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Integer id, @RequestBody Rol rol) {
        // Primero, verificamos si el rol existe
        Optional<Rol> rolExistente = rolService.obtenerRolPorId(id);
        if (!rolExistente.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        // Asignamos el ID al objeto rol que viene en el body
        rol.setCodigoRol(id);
        Rol rolActualizado = rolService.guardarRol(rol);

        return ResponseEntity.ok(rolActualizado); // 200 OK y el rol actualizado
    }

    // ENDPOINT 5: Eliminar un rol
    // Petición: DELETE http://localhost:8080/api/roles/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}