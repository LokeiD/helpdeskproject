package com.utpintegrador.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller

public class login {
    @GetMapping("/login")
    public String login() {
        return "login"; // Esto devuelve "login.html" de la carpeta templates
    }
}
