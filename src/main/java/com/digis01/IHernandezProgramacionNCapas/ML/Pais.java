package com.digis01.IHernandezProgramacionNCapas.ML;

public class Pais 
{
    private int IdPais;
    private String Nombre;
    
    public Pais(){}

    public Pais(int idPais, String nombre) 
    {
        this.IdPais = idPais;
        this.Nombre = nombre;
    }

    public void setIdPais(int idPais) 
    {
        this.IdPais = idPais;
    }
    public int getIdPais() 
    {
        return IdPais;
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
