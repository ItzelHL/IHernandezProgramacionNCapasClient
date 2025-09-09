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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String Index(Model model) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity <Result<List<Usuario>>> responseEntity = restTemplate.exchange("http://localhost:8080/api/usuario", 
                                                                                                            HttpMethod.GET, HttpEntity.EMPTY, 
                                                                                                            new ParameterizedTypeReference<Result<List<Usuario>>>(){
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
////    VISTA PARA LA CARGA MASIVA
//    @GetMapping("cargaMasiva") // localhost:8080/usuario/cargaMasiva
//    public String CargaMasiva() {
//        return "CargaMasiva";
//    }
//
////    NOMBRA EL ARCHIVO COMO ÚNICO, LO GUARDA Y VALIDA QUE SEA EXCEL O TXT
//    @PostMapping("cargaMasiva") // localhost:8080/usuario/cargaMasiva
//    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {
//        String root = System.getProperty("user.dir");
//        String rutaArchivo = "/src/main/resources/archivos/";
//        String fechaSubida = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//        String rutaFinal = root + rutaArchivo + fechaSubida + file.getOriginalFilename();
//
//        try {
//            file.transferTo(new File(rutaFinal)); //Guarda el archivo en la ruta "/src/main/resources/archivos/" asignada con el nuevo nombre
//        } catch (Exception ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        if (file.getOriginalFilename().split("\\.")[1].equals("txt")) //txt
//        {
//            List<Usuario> usuarios = ProcesarTXT(new File(rutaFinal)); // Manda a leer los campos del archivo TXT 
//            List<ErrorCM> errores = ValidarDatos(usuarios);
//
//            if (errores.isEmpty()) {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", true);
//                session.setAttribute("path", rutaFinal);
//            } else {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", false);
//            }
//        } else // excel
//        {
//            List<Usuario> usuarios = ProcesarExcel(new File(rutaFinal)); // Manda a leer los campos del archivo Excel
//            List<ErrorCM> errores = ValidarDatos(usuarios);
//
//            if (errores.isEmpty()) {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", true);
//                session.setAttribute("path", rutaFinal);
//            } else {
//                model.addAttribute("listaErrores", errores);
//                model.addAttribute("archivoCorrecto", false);
//            }
//        }
//        return "CargaMasiva";
//    }
//
////    MUESTRA LA VISTA PARA PROCESAR EL ARCHIVO UNA VEZ QUE VALIDÓ QUE EL TIPO DE ARCHIVO ERA CORRECTO
//    @GetMapping("cargaMasiva/procesar") // localhost:8080/usuario/cargaMasiva/procesar
//    public String CargaMasiva(HttpSession session) {
//        try {
//            String ruta = session.getAttribute("path").toString();
//            List<Usuario> usuarios;
//
//            if (ruta.split("\\.")[1].equals("txt")) {
//                usuarios = ProcesarTXT(new File(ruta));
//            } else {
//                usuarios = ProcesarExcel(new File(ruta));
//            }
//
//            for (Usuario usuario : usuarios) {
//                usuarioJPADAOImplementation.Add(usuario);
//            }
//
//            session.removeAttribute("path");
//        } catch (Exception ex) {
//            System.out.println(ex.getLocalizedMessage());
//        }
//        return "redirect:/usuario";
//    }
//
////    LEE LOS CAMPOS DEL ARCHIVO TXT Y LOS DESGLOSA SEGÚN LO NECESARIO PARA AGREGAR AL USUARIO
//    private List<Usuario> ProcesarTXT(File file) {
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//
//            String linea = "";
//            List<Usuario> usuarios = new ArrayList<>();
//            while ((linea = bufferedReader.readLine()) != null) {
//                String[] campo = linea.split("\\|");
//                Usuario usuario = new Usuario();
//                usuario.setUsername(campo[0]);
//                usuario.setNombre(campo[1]);
//                usuario.setApellidoPaterno(campo[2]);
//                usuario.setApellidoMaterno(campo[3]);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                Date fecha = campo[4] == "" ? null : format.parse(campo[4]);
//                usuario.setFechaNacimiento(fecha);
//                usuario.setSexo(campo[5]);
//                usuario.setCurp(campo[6]);
//                usuario.setEmail(campo[7]);
//                usuario.setPassword(campo[8]);
//                usuario.setTelefono(campo[9]);
//                usuario.setCelular(campo[10]);
//
//                usuario.Rol = new Rol();
//                Integer idRol = campo[11] == "" ? null : Integer.parseInt(campo[11]);
//                usuario.Rol.setIdRol(idRol);
//
//                usuario.Direccion = new ArrayList<>();
//                Direccion direccion = new Direccion();
//                direccion.setCalle(campo[12]);
//                direccion.setNumeroExterior(campo[13]);
//                direccion.setNumeroInterior(campo[14]);
//
//                direccion.Colonia = new Colonia();
//                Integer idColonia = campo[15] == "" ? null : Integer.parseInt(campo[15]);
//                direccion.Colonia.setIdColonia(idColonia);
//                usuario.Direccion.add(direccion);
//
//                usuarios.add(usuario);
//            }
//            return usuarios;
//        } catch (Exception ex) {
//            System.out.println("Error");
//            return null;
//        }
//    }
//
////    LEE LOS CAMPOS DEL ARCHIVO EXCEL Y LOS DESGLOSA SEGÚN LO NECESARIO PARA AGREGAR AL USUARIO
//    private List<Usuario> ProcesarExcel(File file) {
//        List<Usuario> usuarios = new ArrayList<>();
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                Usuario usuario = new Usuario();
//                usuario.setUsername(row.getCell(0) != null ? row.getCell(0).toString() : "");
//                usuario.setNombre(row.getCell(1) != null ? row.getCell(1).toString() : "");
//                usuario.setApellidoPaterno(row.getCell(2) != null ? row.getCell(2).toString() : "");
//                usuario.setApellidoMaterno(row.getCell(3) != null ? row.getCell(3).toString() : "");
//                SimpleDateFormat format = new SimpleDateFormat();
//                if (row.getCell(4) != null) {
//                    if (row.getCell(4).getCellType() == CellType.NUMERIC || DateUtil.isCellDateFormatted(row.getCell(4))) {
//                        usuario.setFechaNacimiento(row.getCell(4).getDateCellValue());
//                    } else {
//                        usuario.setFechaNacimiento(format.parse(row.getCell(4).toString()));
//                    }
//                }
//                usuario.setSexo(row.getCell(5) != null ? row.getCell(5).toString() : "");
//                usuario.setCurp(row.getCell(6) != null ? row.getCell(6).toString() : "");
//                usuario.setEmail(row.getCell(7) != null ? row.getCell(7).toString() : "");
//                usuario.setPassword(row.getCell(8) != null ? row.getCell(8).toString() : "");
//                DataFormatter dataFormatter = new DataFormatter();
//                usuario.setTelefono(row.getCell(9) != null ? dataFormatter.formatCellValue(row.getCell(9)) : "");
//                usuario.setCelular(row.getCell(10) != null ? dataFormatter.formatCellValue(row.getCell(10)) : "");
//
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(row.getCell(11) != null ? (int) row.getCell(11).getNumericCellValue() : 0);
//
//                usuario.Direccion = new ArrayList<>();
//                Direccion direccion = new Direccion();
//                direccion.setCalle(row.getCell(12) != null ? row.getCell(12).toString() : "");
//                direccion.setNumeroExterior(row.getCell(13) != null ? dataFormatter.formatCellValue(row.getCell(13)) : "");
//                direccion.setNumeroInterior(row.getCell(14) != null ? dataFormatter.formatCellValue(row.getCell(14)) : "");
//
//                direccion.Colonia = new Colonia();
//                direccion.Colonia.setIdColonia(row.getCell(15) != null ? (int) row.getCell(15).getNumericCellValue() : 0);
//                usuario.Direccion.add(direccion);
//
//                usuarios.add(usuario);
//            }
//            return usuarios;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
////    VALIDA QUE LOS CAMPOS DEL ARCHIVO CARGADO SEAN CORRECTOS ANTES DE CARGARLOS A LA BD -----FALTA AGREGAR MÁS VALIDACIONES-----
//    private List<ErrorCM> ValidarDatos(List<Usuario> usuarios) {
//        List<ErrorCM> errores = new ArrayList<>();
//        int linea = 1;
//        for (Usuario usuario : usuarios) {
//            if (usuario.getUsername() == null || usuario.getUsername() == "") {
//                errores.add(new ErrorCM(linea, usuario.getUsername(), "El campo USERNAME es obligatorio"));
//            }
//            if (usuario.getNombre() == null || usuario.getNombre() == "") {
//                errores.add(new ErrorCM(linea, usuario.getNombre(), "El campo NOMBRE es obligatorio"));
//            }
//            if (usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno() == "") {
//                errores.add(new ErrorCM(linea, usuario.getApellidoPaterno(), "El campo APELLIDO PATERNO es obligatorio"));
//            }
//            if (usuario.getApellidoMaterno() == null || usuario.getApellidoMaterno() == "") {
//                errores.add(new ErrorCM(linea, usuario.getApellidoMaterno(), "El campo APELLIDO MATERNO es obligatorio"));
//            }
//            if (usuario.getFechaNacimiento() == null || usuario.getFechaNacimiento().equals("")) {
//                errores.add(new ErrorCM(linea, "fecha vacia", "El campo FECHA DE NACIMIENTO es obligatorio"));
//            }
//            if (usuario.getSexo() == null || usuario.getSexo() == "") {
//                errores.add(new ErrorCM(linea, usuario.getSexo(), "El campo SEXO es obligatorio"));
//            }
//            if (usuario.getCurp() == null || usuario.getCurp() == "") {
//                errores.add(new ErrorCM(linea, usuario.getCurp(), "El campo CURP es obligatorio"));
//            }
//            if (usuario.getEmail() == null || usuario.getEmail() == "") {
//                errores.add(new ErrorCM(linea, usuario.getEmail(), "El campo EMAIL es obligatorio"));
//            }
//            if (usuario.getPassword() == null || usuario.getPassword() == "") {
//                errores.add(new ErrorCM(linea, usuario.getPassword(), "El campo PASSWORD es obligatorio"));
//            }
//            if (usuario.getTelefono() == null || usuario.getTelefono() == "") {
//                errores.add(new ErrorCM(linea, usuario.getTelefono(), "El campo TELEFONO es obligatorio"));
//            }
//            if (usuario.getCelular() == null || usuario.getCelular() == "") {
//                errores.add(new ErrorCM(linea, usuario.getCelular(), "El campo CELULAR es obligatorio"));
//            }
//            if (usuario.Rol.getIdRol() <= 0) {
//                errores.add(new ErrorCM(linea, String.valueOf(usuario.Rol.getIdRol()), "El campo ID ROL debe ser mayor a cero"));
//            }
//            if (usuario.Direccion.get(0).getCalle() == null || usuario.Direccion.get(0).getCalle() == "") {
//                errores.add(new ErrorCM(linea, usuario.getNombre(), "El campo CALLE es obligatorio"));
//            }
//            if (usuario.Direccion.get(0).getNumeroExterior() == null || usuario.Direccion.get(0).getNumeroExterior() == "") {
//                errores.add(new ErrorCM(linea, usuario.Direccion.get(0).getNumeroExterior(), "El campo NÚMERO EXTERIOR es obligatorio"));
//            }
//            if (usuario.Direccion.get(0).getNumeroInterior() == null || usuario.Direccion.get(0).getNumeroInterior() == "") {
//                errores.add(new ErrorCM(linea, usuario.Direccion.get(0).getNumeroInterior(), "El campo NÚMERO INTERIOR es obligatorio"));
//            }
//            if (usuario.Direccion.get(0).Colonia.getIdColonia() <= 0) {
//                errores.add(new ErrorCM(linea, String.valueOf(usuario.Direccion.get(0).Colonia.getIdColonia()), "El campo ID COLONIA debe ser mayor a cero"));
//            }
//            linea++;
//        }
//        return errores;
//    }
}
