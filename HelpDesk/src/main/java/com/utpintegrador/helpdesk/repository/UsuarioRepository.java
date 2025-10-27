package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Más adelante, aquí podremos añadir métodos como:
    // Optional<Usuario> findByCorreo(String correo);

    // ¡AÑADE ESTE METODO!
    // Spring Security lo usará para buscar al usuario por su correo
    Optional<Usuario> findByCorreo(String correo);
}