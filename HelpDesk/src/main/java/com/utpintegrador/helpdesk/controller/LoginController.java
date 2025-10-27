package com.utpintegrador.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // ¡OJO! @Controller, NO @RestController
public class LoginController {

    /**
     * Este método maneja la petición GET para /login.
     * Simplemente devuelve el nombre del archivo HTML (sin .html).
     * Spring + Thymeleaf buscarán en /templates/login.html
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * (Opcional por ahora) Una página de inicio simple después del login.
     * Necesitarás crear un archivo /templates/index.html
     */
    @GetMapping("/")
    public String showHomePage() {
        return "index"; // Esto buscará /templates/index.html
    }
}