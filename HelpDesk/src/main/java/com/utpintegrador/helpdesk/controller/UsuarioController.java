package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Para mensajes de respuesta simples
import java.util.Optional;
// import java.util.stream.Collectors; // No es necesario ahora

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/usuarios
     * Obtiene todos los usuarios y los devuelve en el formato
     * esperado por DataTables: { "data": [...] }
     */
    @GetMapping
    public ResponseEntity<Map<String, List<Usuario>>> obtenerTodosParaDataTable() { // <-- Tipo de retorno corregido
        // TODO: Reemplazar 'Usuario' con un 'UsuarioDTO' sin 'passwoord'
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        // Temporalmente quitar contraseñas
        usuarios.forEach(u -> u.setPasswoord(null));

        // Envolvemos la lista en un Map con la clave "data"
        Map<String, List<Usuario>> response = Map.of("data", usuarios);

        // --- ¡LÍNEA CORREGIDA! ---
        // Devolvemos el Map 'response', no la lista 'usuarios'
        return ResponseEntity.ok(response);
        // -------------------------
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene un usuario por ID (para cargar datos al editar en el modal).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswoord(null); // Temporalmente quitar contraseña
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuarioRequest) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuarioConObjetos(usuarioRequest);
            nuevoUsuario.setPasswoord(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) { // Captura validaciones específicas
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Captura otros errores (ej: Rol no encontrado)
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al crear usuario."));
        }
    }


    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioRequest) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioRequest);
            usuarioActualizado.setPasswoord(null);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) { // Captura validaciones específicas
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Captura otros errores (ej: Usuario/Rol no encontrado)
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().startsWith("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al actualizar usuario."));
        }
    }


    /**
     * DELETE /api/usuarios/{id}
     * Elimina (lógicamente) un usuario.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuarioLogicamente(id);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado lógicamente con ID: " + id));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al eliminar usuario."));
        }
    }

    // --- ¡NUEVO ENDPOINT PARA ACTIVAR! ---
    /**
     * PUT /api/usuarios/{id}/activar
     * Reactiva un usuario que fue eliminado lógicamente.
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.activarUsuario(id);
            return ResponseEntity.ok(Map.of("message", "Usuario reactivado con ID: " + id));
        } catch (RuntimeException e) {
            // Si el usuario no se encuentra
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al activar usuario."));
        }
    }
    // -------------------------------------

}