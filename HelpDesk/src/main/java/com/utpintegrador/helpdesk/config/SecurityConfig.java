package com.utpintegrador.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ¡¡MUY IMPORTANTE!!
    // Define cómo se encriptarán las contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Permite acceso público a estas URLs (login y archivos estáticos)
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fonts/**",
                                "/datos.js",
                                "/1.jpg" // Tu imagen de avatar
                        ).permitAll()
                        // 2. Todas las demás URLs requieren estar logueado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 3. Le decimos dónde está nuestra página de login
                        .loginPage("/login")
                        // 4. Le decimos a dónde ir si el login es exitoso
                        .defaultSuccessUrl("/home", true) // Redirige a la página principal
                        // 5. Le decimos a dónde ir si el login falla
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        // 6. Configura el logout (que por defecto es en /logout)
                        .logoutSuccessUrl("/login?logout=true") // A dónde ir después de logout
                        .permitAll()
                );

        return http.build();
    }
}