package es.deusto.masf.appeventos.mappers;

import es.deusto.masf.appeventos.domain.Evento;
import java.util.List;

public interface EventosMapper {
    
    List<Evento> getListadoEventosByTipo(String tipo);
    
}
