package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service // Marca esta clase como un Servicio de Spring
public class UsuarioService {

    // 1. Necesitamos los 3 repositorios para trabajar con usuarios
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AreaRepository areaRepository;

    // 2. Inyectamos los repositorios por el constructor
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          AreaRepository areaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.areaRepository = areaRepository;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;


    // --- Métodos de Lógica de Negocio ---

    /**
     * Método para crear un nuevo usuario.
     * Recibirá un objeto Usuario (parcial) desde el controlador,
     * pero aquí aplicaremos la lógica de negocio.
     */
    public Usuario crearUsuario(Usuario usuario, Integer rolId, Integer areaId) {

        // 1. Lógica de Validación: Verificar que el Rol y Area existan
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con id: " + areaId));

        String passCifrado = passwordEncoder.encode(usuario.getPasswoord());
        usuario.setPasswoord(passCifrado); // Guardamos la versión encriptada

        // 2. Lógica de Negocio: Asignar las entidades encontradas
        usuario.setRol(rol);
        usuario.setArea(area);

        // 3. Lógica de Negocio: Establecer valores por defecto
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado(true); // Asumimos que un usuario nuevo siempre está activo

        // (OJO: Aquí faltaría la lógica para encriptar el 'passwoord' antes de guardarlo.
        // Eso lo veremos más adelante con Spring Security).

        // 4. Guardar en la BD
        return usuarioRepository.save(usuario);
    }

    // --- Métodos CRUD (Crear, Leer, Actualizar, Borrar) básicos ---

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    // (Aquí podríamos añadir un método para "eliminar" lógicamente a un usuario)
    public Usuario eliminarUsuarioLogicamente(Integer id) {
        // 1. Buscamos el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // 2. Aplicamos la lógica de negocio (baja lógica)
        usuario.setEstado(false);
        usuario.setFechaEliminacion(LocalDateTime.now());

        // 3. Actualizamos en la BD
        return usuarioRepository.save(usuario);
    }
}