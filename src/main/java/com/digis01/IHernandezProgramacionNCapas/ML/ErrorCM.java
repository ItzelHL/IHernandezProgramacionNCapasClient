package com.digis01.IHernandezProgramacionNCapas.ML;

public class ErrorCM 
{
    public int linea;
    public String dato;
    public String mensaje;
    
    public ErrorCM(){}

    public ErrorCM(int linea, String dato, String mensaje) 
    {
        this.linea = linea;
        this.dato = dato;
        this.mensaje = mensaje;
    }
}
