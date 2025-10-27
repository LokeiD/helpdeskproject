package com.utpintegrador.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // ¡OJO! @Controller, NO @RestController
public class HomeController {

    /**
     * Este método manejará la página principal después del login.
     * Corresponde a la URL "/" que pusimos en SecurityConfig.
     *
     * @return El nombre del archivo HTML en /templates
     */
    @GetMapping("/home")
    public String showHomePage() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/home.html
        return "home";
    }
}