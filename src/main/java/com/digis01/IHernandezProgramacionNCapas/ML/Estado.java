package com.digis01.IHernandezProgramacionNCapas.ML;

public class Estado 
{
    private int IdEstado;
    private String Nombre;
    public Pais Pais;
    
    public Estado(){}

    public Estado(int idEstado, String nombre) 
    {
        this.IdEstado = idEstado;
        this.Nombre = nombre;
    }
    
    public void setIdEstado(int idEstado) 
    {
        this.IdEstado = idEstado;
    }
    public int getIdEstado() 
    {
        return IdEstado;
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
