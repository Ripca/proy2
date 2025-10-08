# Configuración Jakarta EE para Sistema Empresa

## ⚠️ IMPORTANTE: Este proyecto usa Jakarta EE, NO JavaEE

### Diferencias Clave:

**Jakarta EE (Este proyecto)**
- `jakarta.servlet.*` 
- `jakarta.servlet.http.*`
- `jakarta.servlet.jsp.jstl.*`
- Tomcat 10+
- Web.xml versión 5.0

**JavaEE (Versión anterior)**
- `javax.servlet.*`
- `javax.servlet.http.*` 
- `javax.servlet.jsp.jstl.*`
- Tomcat 9 y anteriores
- Web.xml versión 4.0

## Librerías Requeridas

### 1. MySQL Connector
```
mysql-connector-java-8.0.33.jar
```

### 2. Jakarta JSTL (NO usar javax.servlet.jstl)
```
jakarta.servlet.jsp.jstl-3.0.1.jar
jakarta.servlet.jsp.jstl-api-3.0.0.jar
```

### 3. Descargar desde:
- **Maven Central**: https://mvnrepository.com/
- **Jakarta EE**: https://jakarta.ee/

## Configuración de NetBeans

### 1. Servidor
- **Usar**: Apache Tomcat 10.x o superior
- **NO usar**: Tomcat 9.x o inferior

### 2. Proyecto
- **Java Version**: 17+
- **Web Version**: 5.0 (Jakarta EE)

### 3. Librerías
1. Crear carpeta `web/WEB-INF/lib/` si no existe
2. Copiar los JAR mencionados arriba
3. En NetBeans: Click derecho en proyecto → Properties → Libraries → Add JAR/Folder

## Verificación de Configuración

### 1. Verificar web.xml
```xml
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee 
         https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">
```

### 2. Verificar Importaciones en Java
```java
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
```

### 3. Verificar JSP
```jsp
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
```

## Solución de Problemas Comunes

### Error: ClassNotFoundException javax.servlet
**Causa**: Usando Tomcat 10+ con código javax
**Solución**: Cambiar todas las importaciones a jakarta

### Error: Cannot resolve taglib jakarta.tags.core
**Causa**: Falta librería JSTL de Jakarta
**Solución**: Agregar jakarta.servlet.jsp.jstl-*.jar

### Error: HTTP Status 404
**Causa**: Configuración incorrecta de servlets
**Solución**: Verificar web.xml y mappings

### Error: HTTP Status 500 - ClassCastException
**Causa**: Mezcla de javax y jakarta
**Solución**: Usar solo jakarta en todo el proyecto

## Migración desde JavaEE

Si tienes código con javax.servlet:

### 1. Buscar y Reemplazar
```
javax.servlet → jakarta.servlet
javax.servlet.http → jakarta.servlet.http
```

### 2. Actualizar web.xml
- Cambiar namespace a jakarta.ee
- Cambiar versión a 5.0

### 3. Actualizar JSP
```
uri="http://java.sun.com/jsp/jstl/core" → uri="jakarta.tags.core"
uri="http://java.sun.com/jsp/jstl/fmt" → uri="jakarta.tags.fmt"
```

### 4. Actualizar Librerías
- Remover javax.servlet.jstl-*.jar
- Agregar jakarta.servlet.jsp.jstl-*.jar

## Recursos Útiles

- **Jakarta EE**: https://jakarta.ee/
- **Tomcat 10 Migration**: https://tomcat.apache.org/migration-10.html
- **Maven Repository**: https://mvnrepository.com/artifact/org.glassfish.web/jakarta.servlet.jsp.jstl
