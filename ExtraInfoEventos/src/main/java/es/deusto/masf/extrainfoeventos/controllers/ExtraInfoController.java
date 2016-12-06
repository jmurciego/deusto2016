package es.deusto.masf.extrainfoeventos.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ExtraInfoController {
    
    final static Map<String,Map> resultadosMock = new HashMap<String,Map>();
    static{
        Map springIO= new HashMap();
        springIO.put("localizacion","Barcelona");
        springIO.put("precio","250€");       
        resultadosMock.put("SpringIO", springIO);
        Map abdEurope= new HashMap();
        abdEurope.put("localizacion","Sevilla");
        abdEurope.put("precio","900€");       
        resultadosMock.put("Apache BigData Europe", abdEurope);
        Map codemotion= new HashMap();
        codemotion.put("localizacion","Madrid");
        codemotion.put("precio","Gratis");       
        resultadosMock.put("Codemotion", codemotion);  
    }
    
    @RequestMapping("/eventos/{id}")
    public Map getEvento(@PathVariable("id") String id) {
        return resultadosMock.get(id);
    }
}
