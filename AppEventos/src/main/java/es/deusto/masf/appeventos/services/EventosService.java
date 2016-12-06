package es.deusto.masf.appeventos.services;

import es.deusto.masf.appeventos.domain.Evento;
import es.deusto.masf.appeventos.mappers.EventosMapper;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventosService {
    
    @Autowired
    EventosMapper eventosMapper;
    
    @Autowired
    RestTemplate restTemplate;
    
    public List<Evento> getEventosByTipo(String tipo){
        List<Evento> listadoEventos = eventosMapper.getListadoEventosByTipo(tipo);
        for(Evento evento: listadoEventos){
            evento.setExtraInfo(restTemplate.getForObject("http://localhost:9999/eventos/"+evento.getNombreEvento(), Map.class));
        }    
        return listadoEventos;
    }
    
}
