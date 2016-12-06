#CONSTRUCCIÓN DE UNA APLICACIÓN CON SPRING DESDE 0

A continuación, se detalla cómo crear una aplicación utilizando Spring, SpringMVC y MyBatis paso a paso.

##Prerrequisitos
* Tener instalado un entorno de desarrollo Java como NetBeans, Eclipse o IntelliJ IDEA compatible con Maven.  
* Opcionalmente, tener Maven instalado para poder ejecutar el proyecto desde la línea de comando.

##Paso 1: Crear el proyecto
Usando Netbeans, ir a:

	File --> New Project --> Maven --> Web Application

y crear un nuevo proyecto con las opciones:

* **Project Name:** `AppEventos`
* **Project Location:** `Carpeta donde se quiere dejar el proyecto`
* **GroupId:** `es.deusto.masf`

Alternativamente, se puede crear el proyecto utilizando la siguiente sentencia desde la línea de comando:

```Bash
mvn -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeArtifactId=webapp-jee5 -DarchetypeVersion=1.3 -DgroupId=es.deusto.masf -DartifactId=AppEventos -Dversion=1.0-SNAPSHOT -Dpackage=es.deusto.masf.appeventos archetype:generate
```


##Paso 2: Actualizar el POM
Actualizar el fichero `pom.xml` situado en la raíz del proyecto añadiendo las siguientes dependencias:

```xml
<dependency>
    <groupId>ognl</groupId>
    <artifactId>ognl</artifactId>
    <version>3.1.12</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.1</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>4.3.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>4.3.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>4.3.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.10</version>
</dependency>
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.5</version>
</dependency>
```

Compilar el proyecto para se descarguen las nuevas librerías y verificar que todo funciona correctamente.

##Paso 3: Creación del fichero web.xml
Si no existe (depende de la versión de JEE con la que hayamos creado el proyecto), crear el fichero `web.xml`

En el Netbeans:

	File --> New (others) --> Web --> Standard Deployment Descriptor (web.xml)
	
Alternativamente, para construir el fichero a mano, crear un fichero con nombre `web.xml` en la ruta `/src/main/webapp/WEB-INF/web.xml` con el siguiente contenido:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" version="3.1">
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>
```

##Paso 4: Configuración del arranque de Spring (en el módulo web)
El arranque de Spring se realiza usando un listener. Para ello, en el fichero `web.xml`, es necesario incluir las siguientes líneas:

```xml
 <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
        WEB-INF/spring/application-context.xml
    </param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

El parámetro de contexto `contextConfigLocation` sirve para indicar una ruta alternativa del fichero de configuración de Spring (por defecto está en `WEB-INF/applicationContext.xml`).

##Paso 5: Creación del fichero application-context.xml de configuración de Spring 

Crear el fichero `application-context.xml` en la ruta `src/main/webapp/WEB-INF/spring/application-context.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
     http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
     http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd">

    
</beans>
```

Compilar y ejecutar el proyecto para verificar que no hay errores.

##Paso 6: Creación de la capa de acceso a datos

###Paso 6.1: Creación del bean Evento ( y de los getters y setters)
Crear el bean `Evento` en el paquete `es.deusto.masf.appeventos.domain`

```java
package es.deusto.masf.appeventos.domain;

import java.util.Date;

public class Evento {

    private Long idEvento;
    private String nombreEvento;
    private String descripcionEvento;
    private String tipo;
    private Date fecha;
	    
}
```

Crear los correspondientes getters y setters. En Netbeans:

	Botón derecho sobre el código fuente --> refactor --> Encapsulate Fields

###Paso 6.2: Creación de la clase java del mapper EventosMapper
Crear la interfaz `EventosMapper`en el paquete `es.deusto.masf.appeventos.mappers`

```java
package es.deusto.masf.appeventos.mappers;

import es.deusto.masf.appeventos.domain.Evento;
import java.util.List;

public interface EventosMapper {
    
    List<Evento> getListadoEventosByTipo(String tipo);
    
}
```

###Paso 6.3: Creación del fichero XML del mapper EventosMapper
Crear el fichero XML `EventosMapper.xml` en el directorio `src/main/resources/es/deusto/masf/appeventos/mappers/`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="es.deusto.masf.appeventos.mappers.EventosMapper">
    <resultMap id="EventoMap" type="es.deusto.masf.appeventos.domain.Evento">
        <id column="ID_EVENTO" jdbcType="NUMERIC" property="idEvento" />
        <result column="NOMBRE_EVENTO" jdbcType="VARCHAR" property="nombreEvento" />
        <result column="DESCRIPCION_EVENTO" jdbcType="VARCHAR" property="descripcionEvento" />
        <result column="TIPO" jdbcType="VARCHAR" property="tipo" />
        <result column="FECHA" jdbcType="DATE" property="fecha" />
    </resultMap>
    
    <select id="getListadoEventosByTipo" parameterType="java.lang.String" resultMap="EventoMap">
        SELECT ID_EVENTO, NOMBRE_EVENTO, DESCRIPCION_EVENTO, TIPO, FECHA  
        FROM EVENTOS
        WHERE TIPO = #{value}
    </select>
</mapper>
```

###Paso 6.4: Creación del datasource
Hay varias alternativas para crear un datasource:

a. Podemos crear el datasource en el propio fichero de configuración de Spring:

```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value=""/>
    <property name="url" value=""/>
    <property name="username" value=""/>
    <property name="password" value=""/>
</bean>
```

b. También podemos referenciar un datasource creado en el servidor a través de JNDI:
	
```xml	
<jee:jndi-lookup id="dataSource" jndi-name="jdbc/miDatasource" resource-ref="true"/>
```

c. La opción elegida en este caso. Usar una base de datos en memoria que crearemos cada vez que se arranque la aplicación a partir de unos ficheros sql:

```xml
<jdbc:embedded-database id="dataSource"> 
    <jdbc:script location="classpath:database/hsqldb-schema.sql" /> 
    <jdbc:script location="classpath:database/hsqldb-dataload.sql" /> 
</jdbc:embedded-database>
```

Spring soporta varios tipos de base de datos, como HSQLDB (por defecto), H2, Derby etc...

En este caso, al no indicar nada, se usará HSQLDB. Es necesario incluir la dependencia de la librería de HSQLDB en el `pom.xml` 

```xml    
<!--Dependencias para desarrollo -->
<dependency>
    <groupId>org.hsqldb</groupId>
    <artifactId>hsqldb</artifactId>
    <version>2.3.4</version>
</dependency>
```

También será necesario crear los ficheros `hsqldb-dataload.sql` y `hsqldb-schema.sql` en `src/main/resources/database/`

**Fichero `hsqldb-schema.sql`**

```sql
CREATE TABLE EVENTOS(
    ID_EVENTO VARCHAR(10) NOT NULL,
    NOMBRE_EVENTO VARCHAR(100),
    DESCRIPCION_EVENTO VARCHAR(500),
    TIPO VARCHAR(100),
    FECHA TIMESTAMP,
PRIMARY KEY (ID_EVENTO));
```

**Fichero `hsqldb-dataload.sql`**

```sql
INSERT INTO EVENTOS(ID_EVENTO,NOMBRE_EVENTO,DESCRIPCION_EVENTO,TIPO,FECHA) VALUES (1,'SpringIO','Spring IO 2016 Barcelona','IT','2016-05-19 09:00:00');
INSERT INTO EVENTOS(ID_EVENTO,NOMBRE_EVENTO,DESCRIPCION_EVENTO,TIPO,FECHA) VALUES (2,'Apache BigData Europe','Apache BigData Europe 2016','BigData','2016-11-14 09:00:00');
INSERT INTO EVENTOS(ID_EVENTO,NOMBRE_EVENTO,DESCRIPCION_EVENTO,TIPO,FECHA) VALUES (3,'Codemotion','Codemotion 2016 Madrid','IT','2016-11-18 09:00:00');
```

### Paso 6.5: Configuración del módulo de mybatis-spring

En el fichero `spring/application-context.xml` añadir la siguiente configuración (el datasource está configurado en el paso anterior):

```xml
<jdbc:embedded-database id="dataSource"> 
    <jdbc:script location="classpath:database/hsqldb-schema.sql" /> 
    <jdbc:script location="classpath:database/hsqldb-dataload.sql" /> 
</jdbc:embedded-database>

<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
</bean>
   
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="typeAliasesPackage" value="es.deusto.masf.appeventos.domain" />
</bean>

<mybatis:scan base-package="es.deusto.masf.appeventos.mappers" />
```
	
### Paso 6.6: Configuración del visor de H2

Utilizar una base de datos embebida para desarrollar tiene muchas ventajas, sin embargo, no podemos consultar las tablas con las herramientas tradicionales. Afortunadamente H2 ofrece un visor de base de datos embebidas que arranca con la propia aplicación y que es compatible con HSQLDB. Para configurarlo, realizar los siguientes pasos:

Incluir en el fichero `pom.xml` las siguientes líneas:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.192</version>
</dependency>
```

Incluir en el fichero `web.xml` las siguientes líneas:

```xml
<servlet>
    <servlet-name>H2Console</servlet-name>
    <servlet-class>org.h2.server.web.WebServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>H2Console</servlet-name>
    <url-pattern>/console/*</url-pattern>
</servlet-mapping>  
```
 
Para verificar que está funcionando, ejecutar el proyecto e ir a la URL <http://tumaquina:tupuerto/AppEventos/console/>

Los parámetros a configurar son:

* **Driver:** `org.hsqldb.jdbcDriver`
* **URL:** `jdbc:hsqldb:mem:dataSource`


## Paso 7: Creación del servicio de negocio

###Paso 7.1: Creación de la clase del servicio
Crear la clase `EventosService` en el paquete `es.deusto.masf.appeventos.services`

###Paso 7.2: Configuración de Spring
Añadir las siguientes líneas al fichero de configuración de Spring para activar el escaneo de beans, búsqueda de dependencias y transacciones:

```xml
<context:component-scan
    base-package="es.deusto.masf.appeventos.services" />

<tx:annotation-driven />
```

###Paso 7.2: Anotaciones e Inyección de Dependencias
Para que Spring encuentre e interprete que EventosService es un Bean de Spring, usaremos la anotación `@Service`

Por otro lado, creamos una referencia al Mapper `EventosMapper` y le decimos a Spring que la inyecte usando la anotación `@Autowired`

```java
@Service
public class EventosService {
    
    @Autowired
    EventosMapper eventosMapper;
    
    public List<Evento> getEventosByTipo(String tipo){
        return eventosMapper.getListadoEventosByTipo(tipo);
    }
    
}
```

## Paso 8: Creación de la capa web

### Paso 8.1: Creación del controlador EventosController
Crear la clase controladora `EventosController` en el paquete `es.deusto.masf.appeventos.controllers`

```java 
@Controller
public class EventosController {
    
    @Autowired
    EventosService eventosService;
    
    @RequestMapping("/listadoEventos.action")
    public String getEventos(Model model,@RequestParam(name = "tipo",defaultValue = "IT")String tipo){
        model.addAttribute("listadoEventos", eventosService.getEventosByTipo(tipo));
        return "listadoEventos";
    }
}
```

### Paso 8.2: Configuración del servlet de SpringMVC en el fichero web.xml
En el fichero `web.xml` añadir las siguientes líneas:

```xml
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/appeventos-servlet.xml</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>*.action</url-pattern>
</servlet-mapping>
```

Mediante estas líneas estamos arrancando SpringMVC e indicando que su fichero de configuración se encuentra en `/WEB-INF/spring/appeventos-servlet.xml`

### Paso 8.3: Creación del fichero de configuración de SpringMVC

Crear el fichero `/WEB-INF/spring/appeventos-servlet.xml` de configuración de SpringMVC con las siguientes líneas:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">

    <context:component-scan base-package="es.deusto.masf.appeventos.controllers" />

    <mvc:annotation-driven />
	
    <bean id="viewResolver"
              class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass"
                          value="org.springframework.web.servlet.view.JstlView" />
        <property name="prefix" value="/WEB-INF/jsp/" />
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```

###Paso 8.4: Inclusión en el POM de las dependencias de JSTL
Para el ejemplo se utilizará JSTL. En Tomcat, es necesario incluir su dependencia en el `pom.xml`
  
```xml        
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
```

###Paso 8.5: Creación del JSP
Crear el fichero JSP  `/WEB-INF/jsp/listadoEventos.jsp` con el siguiente contenido:

```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aplicación de Eventos</title>
    </head>
    <body>
        <h1>Listado de eventos</h1>
        <c:choose>
            <c:when test="${empty listadoEventos}">
                No hay eventos del tipo seleccionado (${param.tipo})
            </c:when>
            <c:otherwise>
            <table>
                <tr>
                <th>Nombre Evento</th>
                <th>Descripcion Evento</th>
                <th>Tipo Evento</th>
                <th>Fecha Evento</th>
                </tr>
            <c:forEach items="${listadoEventos}" var="item1">
                <tr>
                    <td>${item1.nombreEvento}</td>
                    <td>${item1.descripcionEvento}</td>
                    <td>${item1.tipo}</td>
                    <td><fmt:formatDate pattern="dd/MM/yyyy hh:mm" value="${item1.fecha}" /></td>
                </tr>
            </c:forEach>
            </c:otherwise>
        </c:choose>
        </table>
                <ul>
                    <li><a href="listadoEventos.action?tipo=IT"> Eventos IT</a></li>
        <li><a href="listadoEventos.action?tipo=BigData"> Eventos Big Data</a></li>
        <li><a href="listadoEventos.action?tipo=Otros"> Otros eventos</a></li>
                </ul>
            
    </body>
</html>
```

La página web estará disponible en: <http://localhost:8080/AppEventos/AppEventos/listadoEventos.action>


### Paso 8.6: Mejorar el look&feel de la página de eventos
Para ello utilizaremos Twitter Bootstrap. Necesitaremos incluir el link a la CSS de bootstrap en la página JSP:

```html
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
```

Añadir la clase `table` a la tabla:

```html
<table class="table">
```	

Añadir estilos a los botones de la siguiente manera:

```html
<ul class="nav nav-pills">
    <li role="presentation" <c:if test="${param.tipo eq 'IT'}"> class="active"</c:if> ><a href="listadoEventos.action?tipo=IT"> Eventos IT</a></li>
    <li role="presentation" <c:if test="${param.tipo eq 'BigData'}"> class="active"</c:if>><a href="listadoEventos.action?tipo=BigData"> Eventos Big Data</a></li>
    <li role="presentation" <c:if test="${param.tipo eq 'Otros'}"> class="active"</c:if>><a href="listadoEventos.action?tipo=Otros"> Otros eventos</a></li>
</ul>
```

Cambiar el mensaje de no hay eventos por:

```html
<div class="alert alert-warning" role="alert">No hay eventos del tipo seleccionado (${param.tipo})</div>
```
          
### Paso 8.7: Exposición de los datos en formato JSON
Para completar el ejemplo, vamos a suponer que se desean exponer los datos en formato JSON, para ello:

Crear el controlador `EventosControllerJSON`

```java
@RestController
public class EventosControllerJSON {
	
    @Autowired
    EventosService eventosService;
	
    @RequestMapping("/listadoEventosJSON.action")
    public List<Evento> getEventos(Model model, @RequestParam(name = "tipo", defaultValue = "IT") String tipo) {
        return eventosService.getEventosByTipo(tipo);
    }
	}
```

Añadir las dependencias de `jackson` al `pom.xml`

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.8.4</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.8.4</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.8.4</version>
</dependency>
```

El servicio JSON estará disponible en La página web: <http://localhost:8080/AppEventos/AppEventos/listadoEventosJSON.action>

## Paso 9: Creación de aplicación externa que ofrece información de Eventos vía REST

Vamos a crear una nueva aplicación que muestre información extra de Eventos vía un API REST que consumiremos desde nuestra aplicación de Eventos para enriquecer los datos que tenemos actualmente.

Para ello, nos vamos a <http://start.spring.io/> y creamos una aplicación usando los siguientes datos:

* **Group:** `es.deusto.masf.extrainfoeventos`
* **Artifact:** `ExtraInfoEventos`
* **Dependencias:** `Web`

Generamos el proyecto y lo descomprimimos el ZIP en nuestra carpeta de proyectos. 

Aunque vamos a trabajar poco con este proyecto, por comodidad, lo importamos en el IDE. En Netbeans basta con abrir el proyecto.

Por otro lado, en la carpeta del proyecto, ejecutar 

```bash
mvn install
```
	
para descargar todas las dependencias del mismo.

Crear una nueva clase `ExtraInfoController` en el paquete `es.deusto.masf.extrainfoeventos.controllers` y al igual que en el proyecto AppEventos, definirlo como de tipo REST

```java
@RestController
public class ExtraInfoController {
    
    final static Map<String,Map> resultadosMock = new HashMap<String,Map>();
    static{
        Map springIO= new HashMap();
        springIO.put("localizacion","Barcelona");
        springIO.put("precio","250€");       
        resultadosMock.put("SpringIO", springIO);
        Map abdEurope= new HashMap();
        abdEurope.put("localizacion","Sevilla");
        abdEurope.put("precio","900€");       
        resultadosMock.put("Apache BigData Europe", abdEurope);
        Map codemotion= new HashMap();
        codemotion.put("localizacion","Madrid");
        codemotion.put("precio","Gratis");       
        resultadosMock.put("Codemotion", codemotion);   
    }
    
    @RequestMapping("/eventos/{id}")
    public Map getEvento(@PathVariable("id") String id) {
        return resultadosMock.get(id);
    }
}
```	
	
Nos situamos en el directorio de la aplicación y la ejecutamos con los siguientes comandos:

```bash
mvn clean install
java -jar target/ExtraInfoEventos-0.0.1-SNAPSHOT.jar --server.port=9999
```

Verificamos que la aplicación funciona correctamente accediendo a la URL <http://localhost:9999/eventos/SpringIO>

##Paso 10: Adaptar AppEventos para consumir los datos de la aplicación externa y enriquecer la información mostrada por pantalla

###Paso 10.1: Configuración del bean RestTremplate

En el fichero `application-context.xml` definimos el bean `restTemplate`

```xml
<bean id="restTemplate" class="org.springframework.web.client.RestTemplate" />
```

El fichero `application-context.xml` quedaría de la siguiente forma:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
     http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
     http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd">
	
    <jdbc:embedded-database id="dataSource"> 
        <jdbc:script location="classpath:database/hsqldb-schema.sql" /> 
        <jdbc:script location="classpath:database/hsqldb-dataload.sql" /> 
    </jdbc:embedded-database>
	
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
    
       
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="typeAliasesPackage" value="es.deusto.masf.appeventos.domain" />
    </bean>
	
    <mybatis:scan base-package="es.deusto.masf.appeventos.mappers" />
    
    
    <context:component-scan
        base-package="es.deusto.masf.appeventos.services" />
	
    <tx:annotation-driven />
    
    <bean id="restTemplate" class="org.springframework.web.client.RestTemplate" />
</beans>
```

###Paso 10.2: Adaptación del objeto Evento

Adaptamos el objeto Evento para que pueda almacenar la información extra que nos va a dar el servicio externo. Aunque es mejor trabajar con objetos y atributos que con mapas, para este ejemplo rápido usaremos un Map.

La clase Evento quedaría de la siguiente forma (más sus correspondientes getters y setters):

```java
public class Evento {
	
    private Long idEvento;
    private String nombreEvento;
    private String descripcionEvento;
    private String tipo;
    private Date fecha;
    private Map extraInfo;
	
}
```	

###Paso 10.3: Adaptación del servicio

Inyectar el bean `restTemplate` en el servicio `EventosService`:

```java
@Autowired
RestTemplate restTemplate;
```

Para cada elemento de la lista de Eventos devuelto por el mapper, llamamos al servicio externo para ampliar la información que disponemos del mismo:

```java
public List<Evento> getEventosByTipo(String tipo){
    List<Evento> listadoEventos = eventosMapper.getListadoEventosByTipo(tipo);
    for(Evento evento: listadoEventos){
        try{
            evento.setExtraInfo(restTemplate.getForObject("http://localhost:9999/eventos/"+evento.getNombreEvento(), Map.class));
        }catch(Exception e){
            System.out.println("No hay informacion extra disponible: "  + e.getClass().getName() + " " + e.getMessage());
        }
    }  
      
    return listadoEventos;
}
```

El servicio quedaría de la siguiente manera:

```java
@Autowired
EventosMapper eventosMapper;
    
@Autowired
RestTemplate restTemplate;
    
public List<Evento> getEventosByTipo(String tipo){
    List<Evento> listadoEventos = eventosMapper.getListadoEventosByTipo(tipo);
    for(Evento evento: listadoEventos){
        evento.setExtraInfo(restTemplate.getForObject("http://localhost:9999/eventos/"+evento.getNombreEvento(), Map.class));
    }    
    return listadoEventos;
}
```

Acceder a la URL <http://localhost:8080/AppEventos/listadoEventosJSON.action?tipo=BigData> para comprobar que la aplicación está funcionando correctamente.

###Paso 10.4: Adaptación de la JSP para que muestre la información extra

El `API` JSON expuesto en <http://localhost:8080/AppEventos/listadoEventosJSON.action> funciona de serie porque `jackson` está transformando automáticamente el objeto devuelto a formato JSON, pero para la JSP hay que hacer el trabajo manualmente. Para ello editar la tabla JSP `listadoEventos.jsp`: 

```html
<table class="table">
        <tr>
        <th>Nombre Evento</th>
        <th>Descripcion Evento</th>
        <th>Tipo Evento</th>
        <th>Fecha Evento</th>
        <th>Localizacion</th>
        <th>Precio</th>
        </tr>
    <c:forEach items="${listadoEventos}" var="item1">
        <tr>
            <td>${item1.nombreEvento}</td>
            <td>${item1.descripcionEvento}</td>
            <td>${item1.tipo}</td>
            <td><fmt:formatDate pattern="dd/MM/yyyy hh:mm" value="${item1.fecha}" /></td>
            <td>${item1.extraInfo.localizacion}</td>
            <td>${item1.extraInfo.precio}</td>
        </tr>
    </c:forEach>
    </c:otherwise>
</c:choose>
</table>
'''