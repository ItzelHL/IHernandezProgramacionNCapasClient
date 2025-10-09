package com.digis01.IHernandezProgramacionNCapas.Controller;

import com.digis01.IHernandezProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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
    
    @PostMapping
    public String Login(Model model, @RequestParam String username, @RequestParam String password, Usuario usuario, HttpSession session)
    {
        usuario.setUsername(username);
        usuario.setPassword(password);
        
        RestTemplate restTemplate = new RestTemplate();
        
        try 
        {
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/login",
                                                                                                                                    entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.getBody() != null) 
            {
                String token = responseEntity.getBody();
                session.setAttribute("jwtToken", token);
                return "redirect:/usuario";
            }
            else
            {
                model.addAttribute("error", "Credenciales inválidas");
                return "Login";
            }
            
        } catch (Exception ex) 
        {
            model.addAttribute("error", "Error al iniciar sesión: " + ex.getLocalizedMessage());
            return "Login";
        }
    }
    
    @PostMapping("logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}