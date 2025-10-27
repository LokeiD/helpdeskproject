package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.utpintegrador.helpdesk.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // 1. Buscamos al usuario por su correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // 2. Verificamos si el usuario está activo (estado = true)
        if (!usuario.isEstado()) {
            throw new UsernameNotFoundException("El usuario está deshabilitado");
        }

        // 3. Creamos la lista de "roles" o "permisos"
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Asumimos que el Nombre_Rol en tu BD es "ADMIN", "USUARIO", etc.
        // "ROLE_" es un prefijo requerido por Spring Security.
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol()));

        // 4. Creamos el objeto UserDetails que Spring Security entiende
        return new CustomUserDetails(usuario);
    }
}