package es.deusto.masf.appeventos.controllers;

import es.deusto.masf.appeventos.services.EventosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventosController {
    
    @Autowired
    EventosService eventosService;
    
    @RequestMapping("/listadoEventos.action")
    public String getEventos(Model model,@RequestParam(name = "tipo",defaultValue = "IT")String tipo){
        model.addAttribute("listadoEventos", eventosService.getEventosByTipo(tipo));
        return "listadoEventos";
    }
}
