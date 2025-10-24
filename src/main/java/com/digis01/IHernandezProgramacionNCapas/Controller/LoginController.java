package com.digis01.IHernandezProgramacionNCapas.Controller;

import com.digis01.IHernandezProgramacionNCapas.ML.Pais;
import com.digis01.IHernandezProgramacionNCapas.ML.Result;
import com.digis01.IHernandezProgramacionNCapas.ML.Rol;
import com.digis01.IHernandezProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("login")
public class LoginController 
{
    private final RestTemplate restTemplate;
    
    public LoginController(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }
    
    @GetMapping
    public String Login(Model model, @RequestParam(required = false) String logout, @RequestParam(required = false) String error) 
    {   
        ResponseEntity<Result<List<Rol>>> responseRol = restTemplate.exchange("http://localhost:8080/api/rol",
                                                                                                                HttpMethod.GET, HttpEntity.EMPTY,
                                                                                                                new ParameterizedTypeReference<Result<List<Rol>>>() {});
        if (responseRol.getStatusCode() == HttpStatusCode.valueOf(200)) 
        {
            Result resultRol = responseRol.getBody();
            if (resultRol.correct) 
            {
                model.addAttribute("roles", resultRol.object);
            } else 
            {
                model.addAttribute("roles", null);
            }
        }
        
        ResponseEntity<Result<List<Pais>>> responsePais = restTemplate.exchange("http://localhost:8080/api/pais",
                                                                                                                HttpMethod.GET, HttpEntity.EMPTY,
                                                                                                                new ParameterizedTypeReference<Result<List<Pais>>>() {});
        if (responsePais.getStatusCode() == HttpStatusCode.valueOf(200)) 
        {
            Result resultPais = responsePais.getBody();
            if (resultPais.correct) 
            {
                model.addAttribute("paises", resultPais.object);
            } else 
            {
                model.addAttribute("paises", null);
            }
        }

        if (error != null) 
        {
            model.addAttribute("error", "Usuario deshabilitado, contacte al administrador.");
        }
        if (logout != null) 
        {
            model.addAttribute("logout", "Sesión cerrada correctamente.");
        }
        model.addAttribute("usuario", new Usuario());
        return "Login";
    }
    
    @PostMapping
    public String Login(Model model, @RequestParam String username, @RequestParam String password, Usuario usuario, HttpSession session) 
    {
        usuario.setUsername(username);
        usuario.setPassword(password);
        try 
        {        
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/login", 
                                                                                                                                entity, Map.class);

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.getBody() != null) 
            {
                Map<String, Object> response = responseEntity.getBody();
                String token = (String) response.get("token");
                
                String extractedUsername = (String) response.get("username");
                session.setAttribute("token", token);
                
                ResponseEntity<Result<Usuario>> userResponse = restTemplate.exchange("http://localhost:8080/api/usuario/username/" + extractedUsername,
                                                                                                                                        HttpMethod.GET, HttpEntity.EMPTY,
                                                                                                                                new ParameterizedTypeReference<Result<Usuario>>() {});
                if (userResponse.getStatusCode() == HttpStatusCode.valueOf(200) && userResponse.getBody() != null) 
                {
                    Result<Usuario> resultUser = userResponse.getBody();

                    if (resultUser.correct) 
                    {
                        Usuario dbUser = resultUser.object;

                        session.setAttribute("username", dbUser.getUsername());
                        session.setAttribute("nombre", dbUser.getNombre());
                        session.setAttribute("userRole", dbUser.getRol().getNombre());
                        session.setAttribute("idUsuario", dbUser.getIdUsuario());

                        return "redirect:/usuario";
                    }
                }
            }
            model.addAttribute("error", "Credenciales inválidas");
            return "Login";

        }
        catch(HttpClientErrorException.Forbidden ex)
        {
            model.addAttribute("error", "Tu cuenta no ha sido verificada, revisa tu correo y haz clic en el enlace de activación.");
            return "Login";
        }
        catch(HttpClientErrorException.Unauthorized ex)
        {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "Login";
        }
        catch (Exception ex) 
        {
            model.addAttribute("error", "Error al iniciar sesión: " + ex.getLocalizedMessage());
            return "Login";
        }
    }
    
    @PostMapping("signup")
    public String Signup(Model model, @ModelAttribute Usuario usuario) 
    {
        try 
        {
            usuario.setStatus(1);
            
            HttpEntity<Usuario> entity = new HttpEntity<>(usuario);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/signup", 
                                                                                                                        entity, String.class);

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                model.addAttribute("logout", "Usuario registrado correctamente. Inicia sesión.");
                return "Login";
            } 
            else 
            {
                model.addAttribute("error", "No se pudo registrar el usuario. Intenta nuevamente.");
                return "Login";
            }
        } catch (Exception ex) 
        {
            model.addAttribute("error", "Error al registrar: " + ex.getLocalizedMessage());
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