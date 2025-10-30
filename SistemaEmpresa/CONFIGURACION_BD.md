# Configuración de Base de Datos - Variables de Entorno

## Variables de Entorno Requeridas

El sistema utiliza las siguientes variables de entorno para la configuración de la base de datos:

### Variables Disponibles:

1. **DB_URL** - URL completa de conexión a MySQL
   - Formato: `jdbc:mysql://[host]:[puerto]/[nombre_bd]`
   - Default: `jdbc:mysql://localhost:3306/sistema-empresa`

2. **DB_USERNAME** - Usuario de la base de datos
   - Default: `root`

3. **DB_PASSWORD** - Contraseña de la base de datos
   - Default: `admin`

## Configuración por Sistema Operativo

### Windows (PowerShell)
```powershell
# Configurar variables de entorno temporalmente (solo para la sesión actual)
$env:DB_URL = "jdbc:mysql://localhost:3306/sistema-empresa"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "tu_password_aqui"

# Configurar variables de entorno permanentemente
[Environment]::SetEnvironmentVariable("DB_URL", "jdbc:mysql://localhost:3306/sistema-empresa", "User")
[Environment]::SetEnvironmentVariable("DB_USERNAME", "root", "User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "tu_password_aqui", "User")
```

### Windows (CMD)
```cmd
# Configurar variables de entorno temporalmente
set DB_URL=jdbc:mysql://localhost:3306/sistema-empresa
set DB_USERNAME=root
set DB_PASSWORD=tu_password_aqui

# Configurar variables de entorno permanentemente
setx DB_URL "jdbc:mysql://localhost:3306/sistema-empresa"
setx DB_USERNAME "root"
setx DB_PASSWORD "tu_password_aqui"
```

### Linux/macOS (Bash)
```bash
# Configurar variables de entorno temporalmente
export DB_URL="jdbc:mysql://localhost:3306/sistema-empresa"
export DB_USERNAME="root"
export DB_PASSWORD="tu_password_aqui"

# Configurar variables de entorno permanentemente (agregar al ~/.bashrc o ~/.profile)
echo 'export DB_URL="jdbc:mysql://localhost:3306/sistema-empresa"' >> ~/.bashrc
echo 'export DB_USERNAME="root"' >> ~/.bashrc
echo 'export DB_PASSWORD="tu_password_aqui"' >> ~/.bashrc
source ~/.bashrc
```

## Configuración en Tomcat

### Opción 1: Variables de Sistema
Agregar al archivo `setenv.bat` (Windows) o `setenv.sh` (Linux) en la carpeta `bin` de Tomcat:

**Windows (setenv.bat):**
```batch
set CATALINA_OPTS=%CATALINA_OPTS% -DDB_URL=jdbc:mysql://localhost:3306/sistema-empresa
set CATALINA_OPTS=%CATALINA_OPTS% -DDB_USERNAME=root
set CATALINA_OPTS=%CATALINA_OPTS% -DDB_PASSWORD=tu_password_aqui
```

**Linux (setenv.sh):**
```bash
export CATALINA_OPTS="$CATALINA_OPTS -DDB_URL=jdbc:mysql://localhost:3306/sistema-empresa"
export CATALINA_OPTS="$CATALINA_OPTS -DDB_USERNAME=root"
export CATALINA_OPTS="$CATALINA_OPTS -DDB_PASSWORD=tu_password_aqui"
```

### Opción 2: Variables de Entorno en el Sistema
Configurar las variables de entorno a nivel del sistema operativo antes de iniciar Tomcat.

## Configuración en IDE (NetBeans/Eclipse/IntelliJ)

### NetBeans
1. Click derecho en el proyecto → Properties
2. Run → VM Options
3. Agregar: `-DDB_URL=jdbc:mysql://localhost:3306/sistema-empresa -DDB_USERNAME=root -DDB_PASSWORD=tu_password_aqui`

### Eclipse
1. Run → Run Configurations
2. Seleccionar tu aplicación → Environment tab
3. Agregar las variables:
   - DB_URL = jdbc:mysql://localhost:3306/sistema-empresa
   - DB_USERNAME = root
   - DB_PASSWORD = tu_password_aqui

### IntelliJ IDEA
1. Run → Edit Configurations
2. Environment variables
3. Agregar las variables como en Eclipse

## Ejemplos de Configuración

### Desarrollo Local
```
DB_URL=jdbc:mysql://localhost:3306/sistema-empresa
DB_USERNAME=root
DB_PASSWORD=admin
```

### Servidor de Producción
```
DB_URL=jdbc:mysql://servidor-prod:3306/sistema-empresa_prod
DB_USERNAME=app_user
DB_PASSWORD=password_seguro_123
```

### Servidor de Pruebas
```
DB_URL=jdbc:mysql://servidor-test:3306/sistema-empresa_test
DB_USERNAME=test_user
DB_PASSWORD=test_password
```

## Verificación de Configuración

El sistema incluye un método para verificar la configuración actual. Para usarlo, puedes agregar esta línea en cualquier servlet o clase:

```java
DatabaseConnection.printConnectionInfo();
```

Esto mostrará en la consola:
- La URL actual de conexión
- El usuario configurado
- Si la contraseña está configurada (sin mostrarla)
- Qué variables de entorno están configuradas

## Troubleshooting

### Error: "Access denied for user"
- Verificar que DB_USERNAME y DB_PASSWORD sean correctos
- Verificar que el usuario tenga permisos en la base de datos

### Error: "Communications link failure"
- Verificar que DB_URL tenga el host y puerto correctos
- Verificar que MySQL esté ejecutándose
- Verificar conectividad de red

### Error: "Unknown database"
- Verificar que la base de datos especificada en DB_URL exista
- Crear la base de datos si es necesario

## Seguridad

⚠️ **IMPORTANTE**: Nunca hardcodees contraseñas en el código fuente. Siempre usa variables de entorno para información sensible.

✅ **Buenas prácticas**:
- Usar variables de entorno diferentes para cada ambiente (dev, test, prod)
- Usar contraseñas fuertes en producción
- Restringir permisos del usuario de base de datos
- No commitear archivos con contraseñas al repositorio
