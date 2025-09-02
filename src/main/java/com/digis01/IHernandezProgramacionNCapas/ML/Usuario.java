package com.digis01.IHernandezProgramacionNCapas.ML;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario 
{

    private int IdUsuario;
    private String Imagen;
    private String Username;
    private String Nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Email;
    private String Password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento; 
    private String Sexo;
    private String Telefono;
    private String Celular;
    private String Curp;
    public Rol Rol;
    public List<Direccion> Direccion;

    public Usuario() {}

    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, Rol rol) 
    {
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.Rol = rol;
    }

    public Usuario(String imagen, int idUsuario, String username, String nombre, String apellidoPaterno, String apellidoMaterno, String email,
            String password, Date fechaNacimiento, String sexo, String telefono, String celular, String curp) 
    {
        this.IdUsuario = idUsuario;
        this.Imagen = imagen;
        this.Username = username;
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.Email = email;
        this.Password = password;
        this.FechaNacimiento = fechaNacimiento;
        this.Sexo = sexo;
        this.Telefono = telefono;
        this.Celular = celular;
        this.Curp = curp;
    }

    public void setIdUsuario(int idUsuario) 
    {
        this.IdUsuario = idUsuario;
    }
    public int getIdUsuario() 
    {
        return IdUsuario;
    }
    
    public void setImagen(String imagen) 
    {
        this.Imagen = imagen;
    }
    public String getImagen() 
    {
        return Imagen;
    }

    public void setUsername(String username) 
    {
        this.Username = username;
    }
    public String getUsername() 
    {
        return Username;
    }

    public void setNombre(String nombre) 
    {
        this.Nombre = nombre;
    }
    public String getNombre() 
    {
        return Nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) 
    {
        this.ApellidoPaterno = apellidoPaterno;
    }
    public String getApellidoPaterno() 
    {
        return ApellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) 
    {
        this.ApellidoMaterno = apellidoMaterno;
    }
    public String getApellidoMaterno() 
    {
        return ApellidoMaterno;
    }

    public void setEmail(String email) 
    {
        this.Email = email;
    }
    public String getEmail() 
    {
        return Email;
    }

    public void setPassword(String password) 
    {
        this.Password = password;
    }
    public String getPassword() 
    {
        return Password;
    }

    public void setFechaNacimiento(Date fechaNacimiento) 
    {
        this.FechaNacimiento = fechaNacimiento;
    }
    public Date getFechaNacimiento() 
    {
        return FechaNacimiento;
    }

    public void setSexo(String sexo) 
    {
        this.Sexo = sexo;
    }
    public String getSexo() 
    {
        return Sexo;
    }

    public void setTelefono(String telefono) 
    {
        this.Telefono = telefono;
    }
    public String getTelefono() 
    {
        return Telefono;
    }

    public void setCelular(String celular) 
    {
        this.Celular = celular;
    }
    public String getCelular() 
    {
        return Celular;
    }

    public void setCurp(String curp) 
    {
        this.Curp = curp;
    }
    public String getCurp() 
    {
        return Curp;
    }
}
