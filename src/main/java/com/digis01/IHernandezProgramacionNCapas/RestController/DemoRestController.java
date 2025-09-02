package com.digis01.IHernandezProgramacionNCapas.RestController;

import com.digis01.IHernandezProgramacionNCapas.ML.Usuario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoRestController {
//    INFORMACIÓN NULA Y/O VACÍA DE UN USUARIO EN FORMATO JSON
//    localhost:8080/demo --- GET en Postman
    @GetMapping 
    public Usuario Index() 
    {
        return new Usuario();
    }

//    SUMA DE DOS NÚMEROS CON VERBO HTTP --- GET
//    localhost:8080/demo/suma?numeroA=10&numeroB=25
    @GetMapping("/suma")
    public String Suma(@RequestParam int numeroA, @RequestParam int numeroB) 
    {
        return "La suma es " + (numeroA + numeroB);
    }
    
//    SUMA DE ELEMENTOS EN UN ARREGLO INGRESADO EN JAVA
//    localhost:8080/demo/sumaArreglo
    @PostMapping("/sumaArreglo")
    public String SumaArreglo()
    {
        int[] arreglo = {10, 20, 30, 40};
        int suma = 0;
        
        for (int i = 0; i < arreglo.length ; i++) 
        {
            suma += arreglo[i];
        }
        
        return "La suma de los elementos del arreglo es: " + suma;
    }
    
//    SUMA DE ELEMENTOS EN UN ARREGLO INGRESANDO DESDE POSTMAN EN JSON
    @PostMapping("/sumaArray")
    public String SumaArray(@RequestBody List<Integer> arreglo)
    {
        int suma = 0;

        for (Integer elemento : arreglo) 
        {
            suma += elemento;
        }

        return "La suma del arreglo es: " + suma;
    }

//    SALUDO A UN USUARIO MOSTRANDO SU INFORMACIÓN
    @PostMapping("saludo")
    public String Saludo(@RequestBody Usuario usuario)
    {
        return "Hola " + usuario.getNombre() + " " + usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno() + " tus datos son: "
                + "\nUsername: " + usuario.getUsername() + "\nFecha de nacimiento: " + usuario.getFechaNacimiento() 
                + "\nSexo: " + usuario.getSexo() + "\nCURP: " + usuario.getCurp() + "\nEmail: " + usuario.getEmail() 
                + "\nTeléfono: " + usuario.getTelefono() + "\nCelular: " + usuario.getCelular();
    }
    
//    ACTUALIZAR UN ELEMENTO DEL ARREGLO SEGÚN LA POSICIÓN
//    localhost:8080/demo/actualizarArray?posicion=2&nombre=Itzel
    private List<String> arreglo = Arrays.asList("Karla", "José", "Sofía", "Miguel", "Dana"); // -----> SE MANTIENE GLOBAL, POR LO QUE LA LISTA NO SE REINICIA Y SOLO SE MODIFICA DENTRO DEL MÉTODO
    @PatchMapping("/actualizarArray")
    public List<String> ActualizarArray(@RequestParam Integer posicion, @RequestParam String nombre)
    {
//        for(String nombres : arreglo)
//        {
//            System.out.println("Arreglo original: " + arreglo);
//            System.out.println("Arreglo actualizado: " + arreglo.set(posicion, nombre));
//        }       
        arreglo.set(posicion, nombre);
        return arreglo;
    }
}
