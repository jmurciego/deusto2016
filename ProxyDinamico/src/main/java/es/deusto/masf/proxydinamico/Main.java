package es.deusto.masf.proxydinamico;

public class Main {

    public static void main(String args[]) {
        //Sin proxy
        InterfaceSaludo saludador = new ClaseSaludo();
        System.out.println(saludador.saluda("Javi"));
        System.out.println(saludador.getClass().getName());

        //Proxy dinamico
        InterfaceSaludo saludadorConProxy
                = (InterfaceSaludo) ProxyDinamicoTrazas
                .creaUnProxy(saludador, InterfaceSaludo.class);
        System.out.println(saludadorConProxy.saluda("Javi"));
        System.out.println(saludadorConProxy.getClass().getName());

    }

}
