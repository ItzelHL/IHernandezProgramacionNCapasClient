package com.digis01.IHernandezProgramacionNCapas.ML;

public class Municipio {
    private int IdMunicipio;
    private String Nombre;
    public Estado Estado;
    
    public Municipio(){}

    public Municipio(int idMunicipio, String nombre)
    {
        this.IdMunicipio = idMunicipio;
        this.Nombre = nombre;
    }
    
    public void setIdMunicipio(int idMunicipio)
    {
        this.IdMunicipio = idMunicipio;
    }
    public int getIdMunicipio()
    {
        return IdMunicipio;
    }
    
    public void setNombre(String nombre)
    {
        this.Nombre = nombre;
    }
    public String getNombre()
    {
        return Nombre;
    }

    public void setEstado(Estado estado) 
    {
        this.Estado = estado;
    }
    public Estado getEstado() 
    {
        return Estado;
    }
}
