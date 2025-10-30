# Sistema de Token JWT - Autenticación Multi-Pestaña

## Descripción

Se ha implementado un sistema completo de autenticación basado en tokens JWT que permite:

1. **Persistencia de sesión entre pestañas**: Si abres la aplicación en múltiples pestañas con el mismo usuario, todas reconocerán el token válido
2. **Validación automática**: El sistema valida el token cada 60 segundos
3. **Recuperación de sesión**: Si la sesión HTTP expira pero el token JWT sigue siendo válido, se restaura automáticamente
4. **Logout seguro**: Al cerrar sesión, se invalida tanto la sesión como el token

## Componentes Implementados

### 1. **LoginServlet** (Actualizado)
- Genera un token JWT al iniciar sesión
- Guarda el token en una cookie HTTP-only llamada `authToken`
- Limpia la cookie al hacer logout

### 2. **AuthenticationFilter** (Mejorado)
- Valida la sesión HTTP
- Si la sesión expira, intenta validar el token JWT
- Si el token es válido, restaura la sesión automáticamente
- Redirige al login si no hay sesión ni token válido

### 3. **ValidateTokenServlet** (Nuevo)
- Endpoint para validar el token desde el cliente
- Retorna JSON con el estado de validación
- Accesible en: `/SistemaEmpresa/ValidateTokenServlet`

### 4. **token-validator.js** (Nuevo)
- Script JavaScript que valida el token automáticamente
- Se ejecuta cada 60 segundos
- También valida cuando la ventana recupera el foco
- Redirige al login si el token expira

## Cómo Funciona

### Flujo de Login
```
1. Usuario ingresa credenciales
2. LoginServlet valida en BD
3. Se genera token JWT (válido 24 horas)
4. Se crea sesión HTTP
5. Se guarda token en cookie "authToken"
6. Se redirige al dashboard
```

### Flujo de Validación (Multi-Pestaña)
```
1. Usuario abre nueva pestaña con la URL de la aplicación
2. AuthenticationFilter verifica sesión (no existe)
3. AuthenticationFilter obtiene token de la cookie
4. AuthenticationFilter valida el token JWT
5. Si es válido, restaura la sesión
6. Usuario accede a la página sin necesidad de login
```

### Flujo de Logout
```
1. Usuario hace clic en "Cerrar Sesión"
2. LoginServlet invalida la sesión HTTP
3. LoginServlet limpia la cookie "authToken"
4. Se redirige al login con mensaje de logout
```

## Características de Seguridad

- **Token JWT**: Firmado con HMAC-SHA256
- **Cookie HTTP-only**: No accesible desde JavaScript (protege contra XSS)
- **Expiración**: Token válido por 24 horas
- **Validación automática**: Se valida cada 60 segundos
- **Sincronización entre pestañas**: Todas las pestañas usan el mismo token

## Uso en Páginas

El validador de token se carga automáticamente en todas las páginas que usan el template principal.

### Acceso desde JavaScript
```javascript
// Verificar si el usuario está autenticado
if (window.tokenValidator.isAuthenticated()) {
    console.log('Usuario autenticado');
}

// Obtener el token
const token = window.tokenValidator.getTokenFromCookie();

// Validar manualmente
window.tokenValidator.validateToken();
```

## Endpoints

### ValidateTokenServlet
- **URL**: `/SistemaEmpresa/ValidateTokenServlet`
- **Método**: GET
- **Respuesta (válido)**:
```json
{
    "valid": true,
    "message": "Token válido",
    "usuario": "admin",
    "idUsuario": 1,
    "idEmpleado": 1
}
```

- **Respuesta (inválido)**:
```json
{
    "valid": false,
    "message": "Token inválido o expirado"
}
```

## Configuración

### Tiempo de Expiración del Token
Editar en `JWTUtil.java`:
```java
private static final long EXPIRATION_TIME = 86400000; // 24 horas en ms
```

### Intervalo de Validación
Editar en `token-validator.js`:
```javascript
this.validationInterval = 60000; // 60 segundos
```

### Clave Secreta
Editar en `JWTUtil.java`:
```java
private static final String SECRET_KEY = "sistemaEmpresaSecretKey2024";
```

## Pruebas

### Prueba 1: Multi-Pestaña
1. Inicia sesión en la aplicación
2. Abre una nueva pestaña
3. Accede a la URL de la aplicación
4. Deberías acceder sin necesidad de login

### Prueba 2: Expiración de Sesión
1. Inicia sesión
2. Espera a que expire la sesión HTTP (30 minutos por defecto)
3. Recarga la página
4. Deberías acceder sin necesidad de login (si el token aún es válido)

### Prueba 3: Logout
1. Inicia sesión
2. Haz clic en "Cerrar Sesión"
3. Deberías ser redirigido al login
4. Intenta acceder a una página protegida
5. Deberías ser redirigido al login

## Troubleshooting

### El token no se valida
- Verifica que la cookie `authToken` esté presente
- Comprueba que el token no haya expirado (24 horas)
- Revisa la consola del navegador para errores

### La sesión no se restaura
- Verifica que `AuthenticationFilter` esté configurado en `web.xml`
- Comprueba que el token sea válido
- Revisa los logs del servidor

### El logout no funciona
- Verifica que la cookie se esté limpiando
- Comprueba que la sesión se invalide correctamente
- Revisa que no haya caché del navegador

## Próximos Pasos

1. Compilar el proyecto
2. Probar el sistema de token en múltiples pestañas
3. Verificar que el logout funcione correctamente
4. Ajustar tiempos de expiración según necesidades

