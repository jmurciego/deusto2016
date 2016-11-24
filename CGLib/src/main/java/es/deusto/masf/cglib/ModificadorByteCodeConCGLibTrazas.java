package es.deusto.masf.cglib;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ModificadorByteCodeConCGLibTrazas {

    public static <S, W> W createWrapper(final S target, final Class<W> wrapperClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(wrapperClass);
        enhancer.setInterfaces(wrapperClass.getInterfaces());
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object proxy, Method method, Object[] args,
                    MethodProxy methodProxy) throws Throwable {
                System.out.println("Llamada al metodo " + method.getName()
                        + " de la clase " + target.getClass());
                Object resultado = methodProxy.invoke(target, args);
                System.out.println("Fin de la llamada");
                return resultado;
            }
        });

        return (W) enhancer.create();
    }

}
