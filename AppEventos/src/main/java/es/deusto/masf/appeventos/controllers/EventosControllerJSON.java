package es.deusto.masf.appeventos.controllers;

import es.deusto.masf.appeventos.domain.Evento;
import es.deusto.masf.appeventos.services.EventosService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventosControllerJSON {

    @Autowired
    EventosService eventosService;

    @RequestMapping("/listadoEventosJSON.action")
    public List<Evento> getEventos(Model model, @RequestParam(name = "tipo", defaultValue = "IT") String tipo) {
        return eventosService.getEventosByTipo(tipo);
    }
}
