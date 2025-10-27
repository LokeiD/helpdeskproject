package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios") // URL base para usuarios
public class UsuarioController {

    private final UsuarioService usuarioService;

    // 1. Inyectamos el servicio de Usuario
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ENDPOINT: Obtener todos los usuarios
    // GET http://localhost:8080/api/usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    // ENDPOINT: Obtener un usuario por ID
    // GET http://localhost:8080/api/usuarios/101
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);

        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear un nuevo usuario (¡Este es el importante!)
    // Petición: POST http://localhost:8080/api/usuarios?rolId=101&areaId=102
    // Body (JSON): {
    //   "nombres": "Diego",
    //   "apellidoPaterno": "Fernández",
    //   "apellidoMaterno": "Alburuqueque",
    //   "correo": "diego@correo.com",
    //   "passwoord": "miPasswordSeguro"
    // }
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @RequestBody Usuario usuario, // 1. Spring toma el JSON del Body
            @RequestParam Integer rolId,  // 2. Spring toma 'rolId' de la URL
            @RequestParam Integer areaId  // 3. Spring toma 'areaId' de la URL
    ) {
        // 4. Pasamos todo al servicio, que ya sabe qué hacer
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario, rolId, areaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // ENDPOINT: Eliminar un usuario (Baja Lógica)
    // Petición: DELETE http://localhost:8080/api/usuarios/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable Integer id) {
        // Usamos el método de BAJA LÓGICA que creamos en el servicio
        try {
            Usuario usuarioEliminado = usuarioService.eliminarUsuarioLogicamente(id);
            return ResponseEntity.ok(usuarioEliminado); // Devuelve 200 OK y el usuario actualizado
        } catch (RuntimeException e) {
            // Esto pasa si el usuario no se encuentra (lanzado por orElseThrow)
            return ResponseEntity.notFound().build();
        }
    }

    // NOTA SOBRE ACTUALIZAR (PUT):
    // El endpoint para actualizar (PUT) es más complejo.
    // Necesitaríamos crear un nuevo método en 'UsuarioService' llamado 'actualizarUsuario'
    // que sepa manejar qué campos se pueden cambiar (ej: no se debería cambiar la fecha de creación)
    // y si se puede cambiar el Rol o Área.
    // Por ahora, nos enfocamos en Crear, Leer y Borrar.
}