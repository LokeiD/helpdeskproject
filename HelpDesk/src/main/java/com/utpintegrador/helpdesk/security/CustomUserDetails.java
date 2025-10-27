package com.utpintegrador.helpdesk.security;

import com.utpintegrador.helpdesk.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    // --- Métodos estándar de UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getPasswoord();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isEstado();
    }

    // --- ¡MÉTODOS PERSONALIZADOS! (El reemplazo de $_SESSION) ---

    public Integer getId() {
        return usuario.getCodigoUsuario();
    }

    public Integer getRolId() {
        return usuario.getRol().getCodigoRol();
    }

    public String getNombre() {
        return usuario.getNombres();
    }

    public String getApellido() {
        return usuario.getApellidoPaterno();
    }

    // (Puedes añadir cualquier otro getter que necesites de tu entidad Usuario)
}