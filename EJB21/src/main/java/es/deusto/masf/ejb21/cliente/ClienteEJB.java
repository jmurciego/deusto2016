package es.deusto.masf.ejb21.cliente;

import es.deusto.masf.ejb21.ejb.HolaMundoSessionBeanLocal;
import es.deusto.masf.ejb21.ejb.HolaMundoSessionBeanLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;

public class ClienteEJB {

    public static void main(String args[]) throws Exception {
        Context ic = new InitialContext();

        HolaMundoSessionBeanLocalHome holaMundoHome = (HolaMundoSessionBeanLocalHome) ic.lookup("java:global/HolaMundoEJB21/HolaMundoSessionBean");
        HolaMundoSessionBeanLocal holaMundoBean = holaMundoHome.create();
        System.out.println(holaMundoBean.saluda());

    }

}
