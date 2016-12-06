package es.deusto.masf.appeventos.domain;

import java.util.Date;
import java.util.Map;

public class Evento {

    private Long idEvento;
    private String nombreEvento;
    private String descripcionEvento;
    private String tipo;
    private Date fecha;
    private Map extraInfo;

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Map getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map extraInfo) {
        this.extraInfo = extraInfo;
    }

}
