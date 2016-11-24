package es.deusto.masf.cglib;

public class Main {

    public static void main(String args[]) {
        ClaseSaludo saludador = new ClaseSaludo();
        System.out.println(saludador);
        ClaseSaludo saludadorConCGLIB = (ClaseSaludo) ModificadorByteCodeConCGLibTrazas.createWrapper(saludador, ClaseSaludo.class);
        System.out.println(saludadorConCGLIB.saluda("Javi"));
        System.out.println(saludadorConCGLIB.getClass().getName());
        System.out.println(saludador);
    }

}
