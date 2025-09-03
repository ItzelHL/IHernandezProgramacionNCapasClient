package com.digis01.IHernandezProgramacionNCapas.ML;

public class Direccion 
{
    private int IdDireccion;
    private String Calle;
    private String NumeroInterior;
    private String NumeroExterior;
    public Colonia Colonia;
    public Direccion(){}
    
    public Direccion( int idDireccion)
    {
        this.IdDireccion = idDireccion;
    }
    
    public Direccion(int idDireccion, String calle, String numeroInterior, String numeroExterior) 
    {
        this.IdDireccion = idDireccion;
        this.Calle = calle;
        this.NumeroInterior = numeroInterior;
        this.NumeroExterior = numeroExterior;
    }
    
    public void setIdDireccion(int idDireccion) 
    {
        this.IdDireccion = idDireccion;
    }
    public int getIdDireccion() 
    {
        return IdDireccion;
    }

    public void setCalle(String calle) 
    {
        this.Calle = calle;
    }
    public String getCalle() 
    {
        return Calle;
    }
    
    public void setNumeroExterior(String numeroExterior) 
    {
        this.NumeroExterior = numeroExterior;
    }
    public String getNumeroExterior() 
    {
        return NumeroExterior;
    }
    
    public void setNumeroInterior(String numeroInterior) 
    {
        this.NumeroInterior = numeroInterior;
    }
    public String getNumeroInterior() 
    {
        return NumeroInterior;
    }

    public void setColonia(Colonia colonia)
    {
        this.Colonia = colonia;
    }
    public Colonia getColonia() 
    {
        return Colonia;
    }
}
