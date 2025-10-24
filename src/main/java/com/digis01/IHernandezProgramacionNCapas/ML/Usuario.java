package com.digis01.IHernandezProgramacionNCapas.ML;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario 
{
    private int status;
    private int idUsuario;
    private String imagen;
    private String username;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento; 
    private String sexo;
    private String telefono;
    private String celular;
    private String curp;
    public Rol Rol;
    @JsonProperty("Direcciones")
    public List<Direccion> Direcciones;
   private String token;

    public Usuario() {}

    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, Rol rol) 
    {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.Rol = rol;
    }

    public Usuario(int status, int idUsuario, String imagen, String username, String nombre, String apellidoPaterno, String apellidoMaterno, String email,
            String password, Date fechaNacimiento, String sexo, String telefono, String celular, String curp, String token) 
    {
        this.status = status;
        this.idUsuario = idUsuario;
        this.imagen = imagen;
        this.username = username;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.telefono = telefono;
        this.celular = celular;
        this.curp = curp;
        this.token = token;
    }
    
    public void setStatus(int status) 
    {
        this.status = status;
    }
    public int getStatus() 
    {
        return status;
    }

    public void setIdUsuario(int idUsuario) 
    {
        this.idUsuario = idUsuario;
    }
    public int getIdUsuario() 
    {
        return idUsuario;
    }
    
    public void setImagen(String imagen) 
    {
        this.imagen = imagen;
    }
    public String getImagen() 
    {
        return imagen;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }
    public String getUsername() 
    {
        return username;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }
    public String getNombre() 
    {
        return nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) 
    {
        this.apellidoPaterno = apellidoPaterno;
    }
    public String getApellidoPaterno() 
    {
        return apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) 
    {
        this.apellidoMaterno = apellidoMaterno;
    }
    public String getApellidoMaterno() 
    {
        return apellidoMaterno;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }
    public String getEmail() 
    {
        return email;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }
    public String getPassword() 
    {
        return password;
    }

    public void setFechaNacimiento(Date fechaNacimiento) 
    {
        this.fechaNacimiento = fechaNacimiento;
    }
    public Date getFechaNacimiento() 
    {
        return fechaNacimiento;
    }

    public void setSexo(String sexo) 
    {
        this.sexo = sexo;
    }
    public String getSexo() 
    {
        return sexo;
    }

    public void setTelefono(String telefono) 
    {
        this.telefono = telefono;
    }
    public String getTelefono() 
    {
        return telefono;
    }

    public void setCelular(String celular) 
    {
        this.celular = celular;
    }
    public String getCelular() 
    {
        return celular;
    }

    public void setCurp(String curp) 
    {
        this.curp = curp;
    }
    public String getCurp() 
    {
        return curp;
    }

    public void setRol(Rol rol) 
    {
        this.Rol = rol;
    }
    public Rol getRol() 
    {
        return Rol;
    }

    public void setDirecciones(List<Direccion> direccion) 
    {
        this.Direcciones = direccion;
    }
    public List<Direccion> getDirecciones()
    {
        return Direcciones;
    }
    
   public void setToken(String token)
   {
       this.token = token;
   }
   public String getToken()
   {
       return token;
   }
}