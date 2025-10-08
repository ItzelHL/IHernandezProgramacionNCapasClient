package com.digis01.IHernandezProgramacionNCapas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("login")
public class LoginController 
{
    @GetMapping
    public String Login(Model model, @RequestParam(required = false) String logout, @RequestParam(required = false) String error)
    {
        if (error != null) 
        {
            model.addAttribute("error", "Usuario deshabilitado, contacte al administrador.");
        }
        if(logout != null)
        {
            model.addAttribute("logout", "Sesión cerrada correctamente.");
        }
        return "Login";
    }
}