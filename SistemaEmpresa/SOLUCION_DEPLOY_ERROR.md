# Solución: "The module has not been deployed" - NetBeans

## 🚨 Problema
Error en línea 1030 de build-impl.xml: "The module has not been deployed"

## 🔧 Causa
El archivo `nbproject/private/private.properties` tiene rutas incorrectas del servidor Tomcat.

## ✅ Solución Paso a Paso

### 1. Configurar Servidor en NetBeans

#### Opción A: Desde NetBeans IDE
1. **Abrir NetBeans**
2. **Tools** → **Servers**
3. **Add Server** → **Apache Tomcat or TomEE**
4. **Seleccionar Tomcat 10+** (IMPORTANTE: NO Tomcat 9)
5. **Browse** → Seleccionar carpeta de instalación de Tomcat
6. **Finish**

#### Opción B: Configuración Manual
1. **Descargar Tomcat 10.1.x** desde: https://tomcat.apache.org/
2. **Extraer** en: `C:\apache-tomcat-10.1.x`
3. **Editar** `nbproject/private/private.properties`:

```properties
deploy.ant.properties.file=${user.home}\\AppData\\Roaming\\NetBeans\\26\\tomcat100.properties
j2ee.server.home=C:/apache-tomcat-10.1.28
j2ee.server.instance=tomcat100:home=C:\\apache-tomcat-10.1.28
user.properties.file=${user.home}\\AppData\\Roaming\\NetBeans\\26\\build.properties
```

### 2. Verificar Configuración del Proyecto

#### En NetBeans:
1. **Click derecho** en proyecto → **Properties**
2. **Run** → **Server**: Seleccionar Tomcat 10+
3. **Context Path**: `/SistemaEmpresa`
4. **Apply** → **OK**

### 3. Limpiar y Reconstruir

```bash
# En NetBeans:
1. Clean and Build (Shift+F11)
2. Run (F6)

# O desde línea de comandos:
cd SistemaEmpresa
ant clean
ant compile
ant dist
```

### 4. Deploy Manual (Si persiste el error)

#### Opción A: Copiar WAR
1. **Generar WAR**: `ant dist`
2. **Copiar** `dist/SistemaEmpresa.war` a `TOMCAT_HOME/webapps/`
3. **Iniciar Tomcat**

#### Opción B: Deploy desde Manager
1. **Acceder**: http://localhost:8080/manager/html
2. **Deploy** → Seleccionar `dist/SistemaEmpresa.war`

## 🔍 Verificaciones Adicionales

### 1. Versión de Tomcat
```bash
# Verificar versión
cd TOMCAT_HOME/bin
catalina.bat version
```
**Debe ser**: 10.x o superior

### 2. Java Version
```bash
java -version
```
**Debe ser**: JDK 17 o superior

### 3. Puertos
- **Tomcat**: 8080 (libre)
- **MySQL**: 3306 (corriendo)

### 4. Librerías
Verificar en `web/WEB-INF/lib/`:
- `mysql-connector-java-8.0.33.jar`
- `jakarta.servlet.jsp.jstl-3.0.1.jar`
- `jakarta.servlet.jsp.jstl-api-3.0.0.jar`

## 🚀 Configuración Recomendada

### Estructura de Directorios:
```
C:\
├── apache-tomcat-10.1.x\
├── mysql-8.0.x\
└── Proyectos\
    └── SistemaEmpresa\
```

### Variables de Entorno:
```
JAVA_HOME=C:\Program Files\Java\jdk-17
CATALINA_HOME=C:\apache-tomcat-10.1.x
PATH=%JAVA_HOME%\bin;%CATALINA_HOME%\bin;%PATH%
```

## 🔧 Comandos de Diagnóstico

### Verificar Configuración:
```bash
# Verificar Java
java -version
javac -version

# Verificar Tomcat
cd %CATALINA_HOME%\bin
catalina.bat version

# Verificar MySQL
mysql --version
```

### Logs de Error:
- **NetBeans**: `View` → `IDE Log`
- **Tomcat**: `CATALINA_HOME/logs/catalina.out`
- **Aplicación**: `CATALINA_HOME/logs/localhost.log`

## 📞 Soporte Adicional

Si el problema persiste:

1. **Verificar** que Tomcat esté corriendo: http://localhost:8080
2. **Revisar logs** de NetBeans y Tomcat
3. **Recrear** configuración del servidor en NetBeans
4. **Usar deploy manual** como alternativa

## ⚡ Solución Rápida

```bash
# 1. Parar Tomcat
cd %CATALINA_HOME%\bin
shutdown.bat

# 2. Limpiar proyecto
cd SistemaEmpresa
ant clean

# 3. Compilar
ant compile

# 4. Generar WAR
ant dist

# 5. Copiar WAR manualmente
copy dist\SistemaEmpresa.war %CATALINA_HOME%\webapps\

# 6. Iniciar Tomcat
cd %CATALINA_HOME%\bin
startup.bat
```

**Acceder**: http://localhost:8080/SistemaEmpresa
