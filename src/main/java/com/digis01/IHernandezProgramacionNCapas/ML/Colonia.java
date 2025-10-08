package com.digis01.IHernandezProgramacionNCapas.ML;

public class Colonia 
{
    private int IdColonia;
    private String Nombre;
    private String CodigoPostal;
    public Municipio Municipio;
    
    
    public Colonia(){}

    public Colonia(int idColonia, String nombre, String codigoPostal) 
    {
        this.IdColonia = idColonia;
        this.Nombre = nombre;
        this.CodigoPostal = codigoPostal;
    }
    
    public void setIdColonia(int idColonia)
    {
        this.IdColonia = idColonia;
    }
    public int getIdColonia()
    {
        return IdColonia;
    }

    public void setMunicipio(Municipio municipio) 
    {
        this.Municipio = municipio;
    }
    public Municipio getMunicipio() 
    {
        return Municipio;
    }

    public void setNombre(String nombre)
    {
        this.Nombre = nombre;
    }
    public String getNombre()
    {
        return Nombre;
    }
    
    public void setCodigoPostal(String codigoPostal)
    {
        this.CodigoPostal = codigoPostal;
    }
    public String getCodigoPostal()
    {
        return CodigoPostal;
    }
}