@echo off
echo ========================================
echo    SISTEMA EMPRESA - INSTALACION
echo ========================================
echo.

echo 1. Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no esta instalado o no esta en el PATH
    pause
    exit /b 1
)

echo.
echo 2. Creando directorios necesarios...
if not exist "web\WEB-INF\lib" mkdir "web\WEB-INF\lib"

echo.
echo 3. Verificando MySQL...
echo IMPORTANTE: Asegurese de que MySQL este ejecutandose en localhost:3306
echo Usuario: root, Password: (vacio)
echo.

echo 4. Para completar la instalacion:
echo    a) Ejecute el script database/sistema_empresa.sql en MySQL
echo    b) Descargue las siguientes librerias en web/WEB-INF/lib/:
echo       - mysql-connector-java-8.0.33.jar
echo       - jakarta.servlet.jsp.jstl-3.0.1.jar
echo       - jakarta.servlet.jsp.jstl-api-3.0.0.jar
echo    c) Abra el proyecto en NetBeans
echo    d) Haga Clean and Build
echo    e) Deploy en Tomcat 10+ (IMPORTANTE: Tomcat 10 o superior)
echo.

echo 5. Acceso al sistema:
echo    URL: http://localhost:8080/SistemaEmpresa
echo    Usuario: admin
echo    Password: 123456
echo.

echo ========================================
echo    INSTALACION COMPLETADA
echo ========================================
pause
