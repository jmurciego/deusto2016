package es.deusto.masf.holamundospring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Cliente {

    public static void main(String args[]) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        HolaMundoInterface holaMundo = (HolaMundoInterface) context.getBean("holaMundoBean");
        System.out.println(holaMundo.saluda());
    }

}
