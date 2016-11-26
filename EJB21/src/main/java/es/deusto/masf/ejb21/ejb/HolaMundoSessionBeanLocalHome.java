package es.deusto.masf.ejb21.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface HolaMundoSessionBeanLocalHome extends EJBLocalHome{
    
    HolaMundoSessionBeanLocal create() throws CreateException;
    
}
