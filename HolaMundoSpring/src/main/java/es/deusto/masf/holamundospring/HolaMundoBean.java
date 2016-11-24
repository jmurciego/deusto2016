package es.deusto.masf.holamundospring;

public class HolaMundoBean implements HolaMundoInterface {

    @Override
    public String saluda() {
        return "hola mundo";
    }

}
