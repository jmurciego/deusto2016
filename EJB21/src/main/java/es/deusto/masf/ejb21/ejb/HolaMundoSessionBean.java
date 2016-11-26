package es.deusto.masf.ejb21.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class HolaMundoSessionBean implements SessionBean {

    private SessionContext context;

    public void setSessionContext(SessionContext aContext) {
        context = aContext;
    }

    public void ejbActivate() {

    }

    public void ejbPassivate() {

    }

    public void ejbRemove() {

    }

    public void ejbCreate() {
    }

    public String saluda() {
        return "hola mundo";
    }
}
