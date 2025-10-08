package com.digis01.IHernandezProgramacionNCapas.Controller;

import com.digis01.IHernandezProgramacionNCapas.ML.Colonia;
import com.digis01.IHernandezProgramacionNCapas.ML.Direccion;
import com.digis01.IHernandezProgramacionNCapas.ML.Estado;
import com.digis01.IHernandezProgramacionNCapas.ML.Municipio;
import com.digis01.IHernandezProgramacionNCapas.ML.Pais;
import com.digis01.IHernandezProgramacionNCapas.ML.Result;
import com.digis01.IHernandezProgramacionNCapas.ML.Rol;
import com.digis01.IHernandezProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("usuario")
public class UsuarioController 
{

//    <---------------------------------------------- V I S T A S   P A R A   U S U A R I O ---------------------------------------------->
//    VISTA PARA INDEX 
//    UsuarioDireccionGetAll
    @GetMapping // localhost:8081/usuario
    public String Index(Model model) 
    {
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity <Result<List<Usuario>>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Usuario>>>(){
                                                                                                            });
        
        ResponseEntity <Result<List<Rol>>> responseRol = restTemplate.exchange("http://localhost:8080/api/rol", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Rol>>>(){
                                                                                                            });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) ) 
        {
            model.addAttribute("usuarioBusqueda", new Usuario());
            
            Result result = responseEntity.getBody();
                
            if (result.correct) 
            {
                model.addAttribute("usuarios", result.object);
            } else 
            {
                model.addAttribute("usuarios", null);
            }
        }
        if (responseRol.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultRol = responseRol.getBody();
                if(resultRol.correct)
                {
                    model.addAttribute("roles", resultRol.object);
                }else{
                    model.addAttribute("roles", null);
                }
            }
        
        return "UsuarioIndex";
    }

    @PostMapping()
    public String Index(Model model, @ModelAttribute("usuarioBusqueda") Usuario usuarioBusqueda)
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity <Result<List<Usuario>>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Usuario>>>(){
                                                                                                            });
        
        ResponseEntity <Result<List<Rol>>> responseRol = restTemplate.exchange("http://localhost:8080/api/rol", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Rol>>>(){
                                                                                                            });
        
        List<Usuario> resultU = new ArrayList<>();
        
        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
        {
            Result result = responseEntity.getBody();
                
            if (result.correct) 
            {
                resultU = (List<Usuario>) result.object;
            } else 
            {
                model.addAttribute("usuarios", null);
            }
        }
        if (responseRol.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultRol = responseRol.getBody();
                if(resultRol.correct)
                {
                    model.addAttribute("roles", resultRol.object);
                }else{
                    model.addAttribute("roles", null);
                }
            }
        
        if(usuarioBusqueda.getNombre() != null && !usuarioBusqueda.getNombre().isBlank())
        {
            resultU = resultU.stream().filter(usuario -> usuario.getNombre() != null && usuario.getNombre().toLowerCase().contains(usuarioBusqueda.getNombre().toLowerCase())).collect(Collectors.toList());
        }
        
        if(usuarioBusqueda.getApellidoPaterno() != null && !usuarioBusqueda.getApellidoPaterno().isBlank())
        {
            resultU = resultU.stream().filter(usuario -> usuario.getApellidoPaterno() != null && usuario.getApellidoPaterno().toLowerCase().contains(usuarioBusqueda.getApellidoPaterno().toLowerCase())).collect(Collectors.toList());
        }
        
        if(usuarioBusqueda.getApellidoMaterno() != null && !usuarioBusqueda.getApellidoMaterno().isBlank())
        {
            resultU = resultU.stream().filter(usuario -> usuario.getApellidoMaterno() != null && usuario.getApellidoMaterno().toLowerCase().contains(usuarioBusqueda.getApellidoMaterno().toLowerCase())).collect(Collectors.toList());
        }
        
        if(usuarioBusqueda.getRol() != null && usuarioBusqueda.getRol().getIdRol() > 0)
        {
            int idRolBuscado = usuarioBusqueda.getRol().getIdRol();
            resultU = resultU.stream().filter(usuario -> usuario.getRol() != null && usuario.getRol().getIdRol() == idRolBuscado).collect(Collectors.toList());
        }
        
        model.addAttribute("usuarioBusqueda", usuarioBusqueda);
        model.addAttribute("usuarios", resultU);
         return "UsuarioIndex";
    }
    
//    VISTA QUE MUESTRA UsuarioDetail (si el usuario existe) o UsuarioForm (si el usuario no existe) 
//    UsuarioGetById
    @GetMapping("/action/{IdUsuario}") // localhost:8081/usuario/action/{idUsuario}
    public String Add(Model model, @PathVariable("IdUsuario") int IdUsuario, @RequestParam(required = false) Integer IdPais) {
        if (IdUsuario == 0) //usuario no existe - Muestra el UsuarioForm.html - AGREGAR USUARIO
        {
            Usuario usuario = new Usuario();
            model.addAttribute("usuario", usuario);
            
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity <Result<List<Rol>>> responseRol = restTemplate.exchange("http://localhost:8080/api/rol", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Rol>>>(){
                                                                                                            });
            ResponseEntity <Result<List<Pais>>> responsePais = restTemplate.exchange("http://localhost:8080/api/pais", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Pais>>>(){
                                                                                                            });
            if (responseRol.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultRol = responseRol.getBody();
                if(resultRol.correct)
                {
                    model.addAttribute("roles", resultRol.object);
                }else{
                    model.addAttribute("roles", null);
                }
            }
            
            if (responsePais.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultPais = responsePais.getBody();
                if (resultPais.correct) 
                {
                    model.addAttribute("paises", resultPais.object);
                } else {
                    model.addAttribute("paises", null);
                }
            }
            return "UsuarioForm";
            
        } else // IdUsuario > 0 // usuario si existe - muestra UsuarioDetail.html - VISTA USUARIO DETAIL
        {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity <Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario/action/" + IdUsuario, 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<Usuario>>(){
                                                                                                            });

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                model.addAttribute("usuario", new Usuario());
                Result result = responseEntity.getBody();

                if (result.correct) 
                {
                    model.addAttribute("usuario", result.object);
                } else 
                {
                    model.addAttribute("usuario", null);
                }
            }
            return "UsuarioDetail";
        }
    }
    
//    VISTA Y MÉTODO PARA RETORNAR LUEGO DE ELIMINAR UN USUARIO
    @GetMapping("delete/{IdUsuario}")
    public String Delete(@PathVariable("IdUsuario") int IdUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario/" + IdUsuario,
                                                                                                                                HttpMethod.DELETE, HttpEntity.EMPTY,
                                                                                                                                new ParameterizedTypeReference<Result<Usuario>>() {
                                                                                                                                 });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
        {
            model.addAttribute("usuario", new Usuario());
            Result result = responseEntity.getBody();

            if (result.correct) 
            {
                model.addAttribute("usuario", result.object);
            } else 
            {
                model.addAttribute("usuario", null);
            }
        }
        return "redirect:/usuario";
    }

//   VISTA QUE MUESTRA EL UsuarioForm PARA EDITAR USUARIO Y AGREGAR DIRECCION
//    UsuarioGetById, DireccionAdd
    @GetMapping("formEditable") // localhost:8080/usuario/formEditable
    public String FormEditable(@RequestParam int IdUsuario,
                                                @RequestParam(required = false) Integer IdDireccion,
                                                Model model) {
        if (IdDireccion == null) // Vista para editar usuario // IdUsuario > 0 && IdDireccion == -1
        {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity <Result<Usuario>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario/action/" + IdUsuario, 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<Usuario>>(){
                                                                                                            });
            ResponseEntity <Result<List<Rol>>> responseRol = restTemplate.exchange("http://localhost:8080/api/rol", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Rol>>>(){
                                                                                                            });
             if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result result = responseEntity.getBody();
                Usuario usuario = (Usuario) result.object;
                usuario.Direccion = new ArrayList<>();
                usuario.Direccion.add(new Direccion(-1));

                if (result.correct) 
                {
                    model.addAttribute("usuario", result.object);
                } else 
                {
                    model.addAttribute("usuario", null);
                }
            }
             
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
            return "UsuarioForm";
            
        } else if (IdDireccion == 0) // Vista para agregar dirección // IdUsuario > 0 && IdDireccion == 0
        {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(IdUsuario);
            model.addAttribute("usuario", usuario);
            
            Direccion direccion = new Direccion();
            model.addAttribute("direccion", direccion);
            
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity <Result<List<Pais>>> responsePais = restTemplate.exchange("http://localhost:8080/api/pais", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Pais>>>(){
                                                                                                            });
            if (responsePais.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultPais = responsePais.getBody();
                if (resultPais.correct) 
                {
                    model.addAttribute("paises", resultPais.object);
                } else {
                    model.addAttribute("paises", null);
                }
            }
            return "UsuarioForm";
        }
        return "redirect:/usuario";
    }

//    AGREGA USUARIO Y DIRECCION, EDITAR USUARIO, AGREGAR DIRECCION, EDITAR DIRECCION
//    IdUsuario == 0 && IdDireccion == 0  
//    UsuarioDireccionAdd, UsuarioUpdate, DireccionAdd, DireccionUpdate
    @PostMapping("add") // localhost:8080/usuario/add   
    public String Add(@ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model,
            @RequestParam(required = false, value = "imagenFile") MultipartFile imagen) {
        if (bindingResult.hasErrors()) 
        {
            model.addAttribute("usuario", usuario);
            return "UsuarioForm";
        } else 
        {
            if (usuario.getIdUsuario() == 0 && usuario.Direccion.get(0).getIdDireccion() == 0) // Agregar Usuario y direccion
            {
                usuario.setImagen(validarImagen(imagen));
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<Usuario> entity = new HttpEntity<>(usuario);
                ResponseEntity<Result<Usuario>> responseUsuario = restTemplate.exchange("http://localhost:8080/api/usuario/add",
                                                                                                                                                     HttpMethod.POST, entity,
                                                                                                                              new ParameterizedTypeReference<Result<Usuario>>() 
                                                                                                                                                      {   });
                if (responseUsuario.getStatusCode() == HttpStatusCode.valueOf(200)) 
                {
                    model.addAttribute("usuario", new Usuario());
                    Result result = responseUsuario.getBody();

                    if (result.correct) 
                    {
                        model.addAttribute("usuario", result.object);
                    } else 
                    {
                        model.addAttribute("usuario", null);
                    }
                }
                return "redirect:/usuario";
            } else if (usuario.getIdUsuario() > 0 && usuario.Direccion.get(0).getIdDireccion() == -1) // Editar usuario
            {
                usuario.setImagen(validarImagen(imagen));
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<Usuario> entity = new HttpEntity<>(usuario);
                ResponseEntity<Result<Usuario>> responseUsuario = restTemplate.exchange("http://localhost:8080/api/usuario/" + usuario.getIdUsuario(),
                                                                                                                                                        HttpMethod.PUT, entity,
                                                                                                                            new ParameterizedTypeReference<Result<Usuario>>() {
                                                                                                                                                     });
                if (responseUsuario.getStatusCode() == HttpStatusCode.valueOf(200)) 
                {
                    model.addAttribute("usuario", new Usuario());
                    Result result = responseUsuario.getBody();

                    if (result.correct) 
                    {
                        model.addAttribute("usuario", result.object);
                    } else 
                    {
                        model.addAttribute("usuario", null);
                    }
                }
                return "redirect:/usuario/action/" + usuario.getIdUsuario();
            } else if (usuario.getIdUsuario() > 0 && usuario.Direccion.get(0).getIdDireccion() == 0) // Agregar dirección
            {
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<Direccion> entity = new HttpEntity<>(usuario.Direccion.get(0));
                ResponseEntity<Result<Direccion>> responseUsuario = restTemplate.exchange("http://localhost:8080/api/direccion/add/" + usuario.getIdUsuario(),
                                                                                                                                                    HttpMethod.POST, entity,
                                                                                                                        new ParameterizedTypeReference<Result<Direccion>>() {
                                                                                                                                                        });
                if (responseUsuario.getStatusCode() == HttpStatusCode.valueOf(200)) {
                    model.addAttribute("direccion", new Direccion());
                    Result result = responseUsuario.getBody();

                    if (result.correct) 
                    {
                        model.addAttribute("direccion", result.object);
                    } else 
                    {
                        model.addAttribute("direccion", null);
                    }
                    return "redirect:/usuario/action/" + usuario.getIdUsuario();
                }
            } else if (usuario.getIdUsuario() > 0 && usuario.Direccion.get(0).getIdDireccion() > 0) // Editar dirección
            {
                Direccion direccion = new Direccion();
                model.addAttribute("direccion", direccion);

                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<Direccion> entity = new HttpEntity<>(usuario.Direccion.get(0));
                ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange("http://localhost:8080/api/direccion/" + usuario.getIdUsuario() +"/direccion/" + usuario.Direccion.get(0).getIdDireccion(),
                                                                                                            HttpMethod.PUT, entity,
                                                                                                                            new ParameterizedTypeReference<Result<Direccion>>() {
                                                                                                                    });
                if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
                {
                    Result resultDireccion = responseEntity.getBody();
                    if (resultDireccion.correct) 
                    {
                        model.addAttribute("direccion", resultDireccion.object);
                    } else 
                    {
                        model.addAttribute("direccion", null);
                    }
                }
                return "redirect:/usuario/action/" + usuario.getIdUsuario();
            }
            return "redirect:/usuario/action/" + usuario.getIdUsuario();
        }
    }
    
//    VISTA PARA EDITAR DIRECCION
//    IdUsuario > 0 && IdDireccion > 0
//    DireccionUpdate
    @GetMapping("/update") 
    public String Update(@RequestParam("IdDireccion") int IdDireccion, @RequestParam("IdUsuario") int IdUsuario, Model model){
       if (IdDireccion > 0) // Vista para editar direccion - Muestra el UsuarioForm.html
        {     
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity <Result<Direccion>> responseDireccion = restTemplate.exchange("http://localhost:8080/api/direccion/detail/" + IdDireccion, 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<Direccion>>(){
                                                                                                            });
            ResponseEntity <Result<List<Pais>>> responsePais = restTemplate.exchange("http://localhost:8080/api/pais", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Pais>>>(){
                                                                                                            });
            Result resultDireccion = responseDireccion.getBody();
            if (responseDireccion.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                
                if (resultDireccion.correct) 
                {
                    Direccion direccion = new Direccion();
                    direccion.setIdDireccion(IdDireccion);
                    direccion = (Direccion) resultDireccion.object;
                    
                    Usuario usuario  = new Usuario();
                    direccion.Usuario = usuario;
                    direccion.Usuario.setIdUsuario(IdUsuario);
                    
                    usuario.Direccion = new ArrayList<>();
                    usuario.Direccion.add(direccion);
                    model.addAttribute("usuario", usuario);
                } else 
                {
                    model.addAttribute("paises", null);
                }
            }
            if (responsePais.getStatusCode() == HttpStatusCode.valueOf(200)) 
            {
                Result resultPais = responsePais.getBody();
                if (resultPais.correct) 
                {     
                    model.addAttribute("paises", resultPais.object);
                    
                    ResponseEntity<Result<List<Estado>>> responseEstado = restTemplate.exchange("http://127.0.0.1:8080/api/estado/pais/" + ((Direccion) resultDireccion.object).Colonia.Municipio.Estado.Pais.getIdPais(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Estado>>>(){});
                    model.addAttribute("estados", responseEstado.getBody().object);
                    
                    ResponseEntity<Result<List<Municipio>>> responseMunicipio = restTemplate.exchange("http://127.0.0.1:8080/api/municipio/estado/" + ((Direccion) resultDireccion.object).Colonia.Municipio.Estado.getIdEstado(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Municipio>>>(){});
                    model.addAttribute("municipios", responseMunicipio.getBody().object);
                    
                    ResponseEntity<Result<List<Colonia>>> responseColonia = restTemplate.exchange("http://127.0.0.1:8080/api/colonia/municipio/" + ((Direccion) resultDireccion.object).Colonia.Municipio.getIdMunicipio(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Colonia>>>(){});
                    model.addAttribute("colonias", responseColonia.getBody().object);
                            
                } else 
                {
                    model.addAttribute("paises", null);
                }
            }
            return "UsuarioForm";
        }
        return "UsuarioForm";
    }

//    VISTA Y MÉTODO PARA RETORNAR LUEGO DE ELIMINAR LA DIRECCIÓN DE UN USUARIO
    @GetMapping("/deleteD")
    public String DeleteDireccion(@RequestParam("IdDireccion") int IdDireccion, Model model, @RequestParam("IdUsuario") int IdUsuario){
        Direccion direccion = new Direccion();
        direccion.setIdDireccion(IdDireccion);
        direccion.Usuario = new Usuario();
        direccion.Usuario.setIdUsuario(IdUsuario);
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange("http://localhost:8080/api/direccion/" + IdDireccion,
                                                                                                                                HttpMethod.DELETE, HttpEntity.EMPTY,
                                                                                                                                new ParameterizedTypeReference<Result<Direccion>>() {
                                                                                                                                 });

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200)) 
        {
            model.addAttribute("direccion", new Direccion());
            Result result = responseEntity.getBody();

            if (result.correct) 
            {
                model.addAttribute("direccion", result.object);
            } else 
            {
                model.addAttribute("direccion", null);
            }
        }
         return "redirect:/usuario/action/" + IdUsuario;
    }
    
//    VALIDA QUE EL FORMATO DE LA IMAGEN SEA CORRECTO Y LO CONVIERTE A BASE64
    public String validarImagen(MultipartFile imagen) 
    {
        if (imagen != null && imagen.getOriginalFilename() != "") 
        {
            String nombre = imagen.getOriginalFilename();
            String extension = nombre.split("\\.")[1];
            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) 
            {
                try 
                {
                    byte[] bytes = imagen.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(bytes);

                    return base64Image;
                } 
                catch (Exception ex) 
                {
                    System.out.println("Solo se permiten archivos .jpg, .jpeg, .png");
                }
            }
            return null;
        }
        return null;
    }

////    <-------------------------------------------------------- C A R G A   M A S I V A -------------------------------------------------------->
//    VISTA PARA LA CARGA MASIVA
    @GetMapping("cargamasiva") // localhost:8081/usuario/cargaMasiva
    public String CargaMasiva() {
        return "CargaMasiva";
    }

//    GUARDA Y VALIDA EL ESTADO DEL ARCHIVO GUARDÁNDOLO EN UN LOG
    @PostMapping("cargamasiva") // localhost:8081/usuario/cargamasiva
    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {     
        try 
        {
            ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) 
            {
                @Override
                public String getFilename() 
                {
                    return file.getOriginalFilename(); // Nombre del archivo para el otro servidor
                }
            };
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("archivo", fileAsResource);
            
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
            ResponseEntity<Result> responseFile = restTemplate.exchange("http://localhost:8080/api/usuario/cargamasiva",
                                                                                                                                            HttpMethod.POST, requestEntity,
                                                                                                                               new ParameterizedTypeReference<Result>() {
                                                                                                                       });
            Result result = responseFile.getBody();
            
            if (responseFile.getStatusCode() == HttpStatusCode.valueOf(200) && result != null) 
            {
                if (result.correct)
                {
                    // Guardar nombreArchivo en sesión para procesarlo después
                    Map<String, Object> infoArchivo = (Map<String, Object>) result.object;
                    String rutaCifrada = (String) infoArchivo.get("rutaCifrada");
                    session.setAttribute("rutaCifrada", rutaCifrada);

                    model.addAttribute("archivoCorrecto", true);
                    model.addAttribute("mensaje", result.errorMessage);
                    model.addAttribute("listaErrores", new ArrayList<>());
                } else
                {
                    // Si hay errores en la validación del backend
                    model.addAttribute("archivoCorrecto", false);
                    model.addAttribute("listaErrores", result.object); // Lista de errores
                    model.addAttribute("mensaje", result.errorMessage);
                }
            }
        } catch (IOException ex) 
        {
            model.addAttribute("mensaje", "Error al leer el archivo.");
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
            return "CargaMasiva";
    }

//    MUESTRA LA VISTA PARA PROCESAR EL ARCHIVO UNA VEZ QUE VALIDÓ QUE EL TIPO DE ARCHIVO ERA CORRECTO
    @GetMapping("cargamasiva/procesar") // localhost:8081/usuario/cargaMasiva/procesar
    public String CargaMasivaProcesar(HttpSession session, Model model) {
        
        Object rutaCifradaObj = session.getAttribute("rutaCifrada");
        if (rutaCifradaObj == null) 
        {
            model.addAttribute("mensaje", "No hay archivo cargado para procesar. Por favor, cargue un archivo primero.");
            model.addAttribute("archivoCorrecto", false);
            return "CargaMasiva";
        }
        String rutaCifrada = rutaCifradaObj.toString();

        try 
        {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("rutaCifrada", rutaCifrada);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario/cargamasiva/procesar",
                                                                                                                                                               HttpMethod.POST, requestEntity,
                                                                                                                                                  new ParameterizedTypeReference<Result>() {
                                                                                                                           });
            Result result = responseEntity.getBody();

            if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && result != null) 
            {
                if (result.correct) 
                {
                    model.addAttribute("archivoCorrecto", true);
                    model.addAttribute("listaErrores", new ArrayList<>());
                    model.addAttribute("mensaje", result.errorMessage);
                } else 
                {
                    model.addAttribute("archivoCorrecto", false);
                    model.addAttribute("listaErrores", result.object);
                    model.addAttribute("mensaje", result.errorMessage);
                }
            } 

        }catch (Exception ex) 
        {
            model.addAttribute("mensaje", "Error inesperado al procesar el archivo: " + ex.getMessage());
            model.addAttribute("archivoCorrecto", false);
        }
        session.removeAttribute("rutaCifrada");
        // En lugar de redirigir, regresar la misma vista para mostrar mensajes
        return "CargaMasiva";
    }
}