package com.digis01.IHernandezProgramacionNCapas.ML;

public class Rol 
{
    private int IdRol;
    private String Nombre;
    
    public Rol(){}
    
    public Rol(int  idRol)
    {
        this.IdRol = idRol;
    }
    
    public Rol(int idRol, String nombre)
    {
        this.IdRol = idRol;
        this.Nombre = nombre;
    }
    
    public void setIdRol(int idRol)
    {
        this.IdRol = idRol;
    }
    public int getIdRol()
    {
        return IdRol;
    }
    
    public void setNombre(String nombre)
    {
        this.Nombre = nombre;
    }
    public String getNombre()
    {
        return Nombre;
    }
}