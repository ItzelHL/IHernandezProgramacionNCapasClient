package com.digis01.IHernandezProgramacionNCapas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("demo")
public class DemoController 
{
    @GetMapping("HolaMundo")
    public String HolaMundo()
    {
        return "HolaMundo";
    }
    
    @GetMapping("/persona/{persona}")
    public String Persona(@PathVariable String persona, Model model)
    {
        model.addAttribute("persona", persona);
        return "HolaMundo";
    }
}
