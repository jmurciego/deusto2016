package es.deusto.masf.proxydinamico;

import java.lang.reflect.Method;

public class ProxyDinamicoTrazas implements java.lang.reflect.InvocationHandler {

    Object target = null;

    public ProxyDinamicoTrazas(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        System.out.println("Llamada al metodo " + method.getName()
                + " de la clase " + target.getClass());
        Object salida = method.invoke(target, args);
        System.out.println("Fin de la llamada");
        return salida;

    }

    public static Object creaUnProxy(Object target, Class interfaz) {
        return java.lang.reflect.Proxy
                .newProxyInstance(target.getClass().getClassLoader(),
                        new Class[]{interfaz},
                        new ProxyDinamicoTrazas(target));

    }

}
