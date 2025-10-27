package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Rol;
import com.utpintegrador.helpdesk.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // ¡Clave! Le dice a Spring que esta clase es un Servicio.
public class RolService {

    private final RolRepository rolRepository;

    // --- Inyección de Dependencias por Constructor ---
    // Esta es la forma moderna y recomendada de "pedirle" a Spring
    // que nos pase el repositorio que necesitamos.
    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Metodo para obtener todos los roles
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll(); // Llama al repositorio
    }

    // Metodo para obtener un rol por su ID
    public Optional<Rol> obtenerRolPorId(Integer id) {
        return rolRepository.findById(id);
    }

    // Metodo para guardar un nuevo rol (o actualizar uno existente)
    public Rol guardarRol(Rol rol) {
        // Aquí podría ir lógica de negocio, ej:
        // if (rol.getNombreRol().equals("ADMIN")) { ... }
        return rolRepository.save(rol);
    }

    // Metodo para eliminar un rol
    public void eliminarRol(Integer id) {
        // Aquí podríamos validar: "No eliminar el rol si hay usuarios con él"
        rolRepository.deleteById(id);
    }
}