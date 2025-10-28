package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.model.Rol;
import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.AreaRepository;
import com.utpintegrador.helpdesk.repository.RolRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Asegúrate que esté importado
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Importante para atomicidad
import org.springframework.util.StringUtils; // <-- Para verificar si la contraseña está vacía

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AreaRepository areaRepository;
    private final PasswordEncoder passwordEncoder; // <-- Necesario para encriptar

    // --- CONSTRUCTOR MODIFICADO ---
    // Inyectamos todas las dependencias necesarias aquí
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          AreaRepository areaRepository,
                          PasswordEncoder passwordEncoder) { // <-- Añadido PasswordEncoder
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.areaRepository = areaRepository;
        this.passwordEncoder = passwordEncoder; // <-- Asignado
    }

    // --- OBTENER (Sin cambios) ---
    public List<Usuario> obtenerTodosLosUsuarios() {
        // TODO: Considerar devolver DTOs sin contraseña para la tabla
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        // TODO: Considerar devolver DTO sin contraseña
        return usuarioRepository.findById(id);
    }

    // --- CREAR (MODIFICADO para aceptar Objeto JSON) ---
    /**
     * Crea un nuevo usuario. Espera que el objeto usuarioRequest
     * contenga objetos anidados para Rol y Area con sus IDs.
     * Ejemplo JSON esperado por el JS:
     * { nombres: "...", ..., passwoord: "...", rol: { codigoRol: 100 }, area: { codigoArea: 100 } }
     */
    @Transactional // Asegura que todas las operaciones (buscar, guardar) sean atómicas
    public Usuario crearUsuarioConObjetos(Usuario usuarioRequest) {
        // 1. Validar y obtener IDs de Rol y Area desde el objeto anidado
        Integer rolId = Optional.ofNullable(usuarioRequest.getRol())
                .map(Rol::getCodigoRol) // Obtiene el codigoRol si rol no es null
                .orElseThrow(() -> new IllegalArgumentException("El Rol es obligatorio (rol.codigoRol)"));
        Integer areaId = Optional.ofNullable(usuarioRequest.getArea())
                .map(Area::getCodigoArea) // Obtiene el codigoArea si area no es null
                .orElseThrow(() -> new IllegalArgumentException("El Área es obligatoria (area.codigoArea)"));

        // 2. Buscar las entidades Rol y Area
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con id: " + areaId));

        // 3. Crear una nueva instancia de Usuario para guardar (buenas prácticas)
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(usuarioRequest.getNombres());
        nuevoUsuario.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        nuevoUsuario.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        nuevoUsuario.setCorreo(usuarioRequest.getCorreo()); // TODO: Validar si el correo ya existe

        // 4. Validar y Encriptar contraseña (¡Obligatorio!)
        if (!StringUtils.hasText(usuarioRequest.getPasswoord())) { // Verifica que no sea null ni vacío
            throw new IllegalArgumentException("La contraseña es obligatoria al crear usuario");
        }
        nuevoUsuario.setPasswoord(passwordEncoder.encode(usuarioRequest.getPasswoord().trim()));

        // 5. Asignar Rol y Area encontrados
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setArea(area);

        // 6. Valores por defecto
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true); // Activo por defecto
        nuevoUsuario.setFechaModificacion(null); // Nulo al crear
        nuevoUsuario.setFechaEliminacion(null);  // Nulo al crear

        return usuarioRepository.save(nuevoUsuario);
    }


    // --- ACTUALIZAR (¡NUEVO!) ---
    /**
     * Actualiza un usuario existente.
     * @param id El ID del usuario a actualizar.
     * @param usuarioRequest Objeto con los datos actualizados desde el formulario.
     * @return El usuario actualizado.
     */
    @Transactional
    public Usuario actualizarUsuario(Integer id, Usuario usuarioRequest) {
        // 1. Buscar el usuario existente o lanzar error si no se encuentra
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // 2. Actualizar campos básicos
        usuarioExistente.setNombres(usuarioRequest.getNombres());
        usuarioExistente.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        usuarioExistente.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        // TODO: Validar si el correo cambia y si ya existe en otro usuario
        usuarioExistente.setCorreo(usuarioRequest.getCorreo());

        // 3. Actualizar contraseña SÓLO si se proporcionó una nueva (no está vacía)
        if (StringUtils.hasText(usuarioRequest.getPasswoord())) { // Verifica que no sea null ni vacío
            usuarioExistente.setPasswoord(passwordEncoder.encode(usuarioRequest.getPasswoord().trim()));
        }

        // 4. Actualizar Rol (validando que exista)
        Integer rolId = Optional.ofNullable(usuarioRequest.getRol()).map(Rol::getCodigoRol).orElse(null);
        if (rolId != null) {
            // Solo busca y actualiza si el ID del rol ha cambiado
            if (!rolId.equals(usuarioExistente.getRol().getCodigoRol())) {
                Rol rol = rolRepository.findById(rolId)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
                usuarioExistente.setRol(rol);
            }
        } else {
            throw new IllegalArgumentException("El Rol es obligatorio al actualizar (rol.codigoRol)");
        }


        // 5. Actualizar Area (validando que exista)
        Integer areaId = Optional.ofNullable(usuarioRequest.getArea()).map(Area::getCodigoArea).orElse(null);
        if (areaId != null) {
            // Solo busca y actualiza si el ID del area ha cambiado
            if (!areaId.equals(usuarioExistente.getArea().getCodigoArea())) {
                Area area = areaRepository.findById(areaId)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada con id: " + areaId));
                usuarioExistente.setArea(area);
            }
        } else {
            throw new IllegalArgumentException("El Área es obligatoria al actualizar (area.codigoArea)");
        }


        // 6. Actualizar fecha de modificación
        usuarioExistente.setFechaModificacion(LocalDateTime.now());
        // El estado no se cambia aquí, se usa activarUsuario o eliminarUsuarioLogicamente

        return usuarioRepository.save(usuarioExistente);
    }


    // --- ELIMINAR LÓGICAMENTE (Añadir @Transactional) ---
    @Transactional // Buena práctica
    public Usuario eliminarUsuarioLogicamente(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // TODO: Añadir validación aquí (ej: no eliminar si tiene tickets asignados)

        usuario.setEstado(false);
        usuario.setFechaEliminacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    // --- ¡NUEVO MÉTODO ACTIVAR! ---
    @Transactional
    public Usuario activarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Solo activar si está inactivo
        if (!usuario.isEstado()) {
            usuario.setEstado(true);
            usuario.setFechaEliminacion(null); // Limpiar fecha de eliminación
            usuario.setFechaModificacion(LocalDateTime.now()); // Opcional: registrar como modificación
            return usuarioRepository.save(usuario);
        }
        // Si ya estaba activo, no hacemos nada o devolvemos el mismo objeto
        return usuario;
    }
}