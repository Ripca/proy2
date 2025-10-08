# 📚 Librerías Requeridas para Sistema Empresa

## 🚨 ERROR ACTUAL
```
FALLO - Aplicación desplegada en la ruta de contexto [/SistemaEmpresa], 
pero el contexto no pudo arrancar
```

**CAUSA**: Faltan las librerías JAR en `web/WEB-INF/lib/`

## 📥 Librerías a Descargar

### 1. MySQL Connector/J
**Archivo**: `mysql-connector-java-8.0.33.jar`
**Descarga**: https://dev.mysql.com/downloads/connector/j/
**Tamaño**: ~2.4 MB

### 2. Jakarta JSTL Core
**Archivo**: `jakarta.servlet.jsp.jstl-3.0.1.jar`
**Descarga**: https://mvnrepository.com/artifact/org.glassfish.web/jakarta.servlet.jsp.jstl/3.0.1
**Tamaño**: ~300 KB

### 3. Jakarta JSTL API
**Archivo**: `jakarta.servlet.jsp.jstl-api-3.0.0.jar`
**Descarga**: https://mvnrepository.com/artifact/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api/3.0.0
**Tamaño**: ~100 KB

## 📂 Ubicación
Copiar todos los archivos JAR a:
```
SistemaEmpresa/web/WEB-INF/lib/
```

## 🔗 Enlaces Directos de Descarga

### MySQL Connector
```
https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
```

### Jakarta JSTL
```
https://repo1.maven.org/maven2/org/glassfish/web/jakarta.servlet.jsp.jstl/3.0.1/jakarta.servlet.jsp.jstl-3.0.1.jar
```

### Jakarta JSTL API
```
https://repo1.maven.org/maven2/jakarta/servlet/jsp/jstl/jakarta.servlet.jsp.jstl-api/3.0.0/jakarta.servlet.jsp.jstl-api-3.0.0.jar
```

## ⚡ Instalación Rápida

### Opción 1: Descarga Manual
1. **Crear carpeta**: `SistemaEmpresa/web/WEB-INF/lib/`
2. **Descargar** los 3 archivos JAR de los enlaces arriba
3. **Copiar** todos los JAR a la carpeta `lib/`
4. **Clean and Build** en NetBeans
5. **Deploy** nuevamente

### Opción 2: Usando wget (si tienes)
```bash
cd SistemaEmpresa/web/WEB-INF/lib/

# MySQL Connector
wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# Jakarta JSTL
wget https://repo1.maven.org/maven2/org/glassfish/web/jakarta.servlet.jsp.jstl/3.0.1/jakarta.servlet.jsp.jstl-3.0.1.jar

# Jakarta JSTL API
wget https://repo1.maven.org/maven2/jakarta/servlet/jsp/jstl/jakarta.servlet.jsp.jstl-api/3.0.0/jakarta.servlet.jsp.jstl-api-3.0.0.jar
```

### Opción 3: PowerShell
```powershell
cd "SistemaEmpresa\web\WEB-INF\lib"

# MySQL Connector
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" -OutFile "mysql-connector-java-8.0.33.jar"

# Jakarta JSTL
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/glassfish/web/jakarta.servlet.jsp.jstl/3.0.1/jakarta.servlet.jsp.jstl-3.0.1.jar" -OutFile "jakarta.servlet.jsp.jstl-3.0.1.jar"

# Jakarta JSTL API
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/jakarta/servlet/jsp/jstl/jakarta.servlet.jsp.jstl-api/3.0.0/jakarta.servlet.jsp.jstl-api-3.0.0.jar" -OutFile "jakarta.servlet.jsp.jstl-api-3.0.0.jar"
```

## ✅ Verificación

Después de copiar las librerías, la carpeta debe verse así:
```
SistemaEmpresa/web/WEB-INF/lib/
├── mysql-connector-java-8.0.33.jar
├── jakarta.servlet.jsp.jstl-3.0.1.jar
└── jakarta.servlet.jsp.jstl-api-3.0.0.jar
```

## 🔄 Pasos Después de Instalar Librerías

1. **En NetBeans**:
   - Clean and Build (Shift+F11)
   - Run (F6)

2. **Si persiste el error**:
   - Verificar logs de Tomcat
   - Verificar que MySQL esté corriendo
   - Verificar configuración de base de datos

## 🔍 Verificar Logs

### Logs de Tomcat:
```
CATALINA_HOME/logs/catalina.out
CATALINA_HOME/logs/localhost.log
```

### Logs de NetBeans:
- View → IDE Log

## 📞 Problemas Comunes

### Error: ClassNotFoundException
**Causa**: Falta alguna librería
**Solución**: Verificar que los 3 JAR estén en `lib/`

### Error: SQLException
**Causa**: MySQL no está corriendo o configuración incorrecta
**Solución**: Iniciar MySQL y verificar credenciales

### Error: 404 Not Found
**Causa**: Aplicación no se desplegó correctamente
**Solución**: Clean and Build, luego Deploy

## 🎯 Próximos Pasos

1. **Descargar librerías** (enlaces arriba)
2. **Copiar a** `web/WEB-INF/lib/`
3. **Clean and Build** en NetBeans
4. **Deploy** nuevamente
5. **Acceder**: http://localhost:8080/SistemaEmpresa

¡Una vez que tengas las librerías, el sistema debería funcionar perfectamente!
