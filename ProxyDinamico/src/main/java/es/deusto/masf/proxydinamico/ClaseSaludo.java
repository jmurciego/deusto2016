package es.deusto.masf.proxydinamico;

public class ClaseSaludo implements InterfaceSaludo {
	
	
	public ClaseSaludo(){}

        @Override
	public String saluda(String nombre){
		return "hola " + nombre;	
	}
	
}
