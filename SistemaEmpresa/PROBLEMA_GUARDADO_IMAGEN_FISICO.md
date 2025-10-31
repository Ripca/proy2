# PROBLEMA: Guardado Físico de Imágenes - Análisis Detallado

## Resumen del Problema
La ruta de la imagen se guarda correctamente en la base de datos (`web/assets/productos/uuid.jpg`), pero el archivo físico **NO se crea** en la carpeta `SistemaEmpresa/web/assets/productos/`.

---

## Flujo de Subida de Imagen

### 1. **Formulario (form_content.jsp)**
**Archivo:** `SistemaEmpresa/web/WEB-INF/views/productos/form_content.jsp`

```jsp
<!-- Líneas 120-121: Input de archivo -->
<input type="file" class="form-control" id="imagenFile" name="imagenFile"
       accept=".png,.jpg,.jpeg" onchange="previewImagen(event)">

<!-- Líneas 130: Input oculto donde se guarda la ruta -->
<input type="hidden" id="imagen" name="imagen"
       value="<%= esEdicion && producto.getImagen() != null ? producto.getImagen() : "" %>">
```

### 2. **JavaScript - Envío AJAX (form_content.jsp)**
**Archivo:** `SistemaEmpresa/web/WEB-INF/views/productos/form_content.jsp` (líneas 234-257)

```javascript
// Línea 239: Llamada al servlet
fetch('UploadImagenServlet', {
    method: 'POST',
    body: formData  // formData contiene el archivo
})
.then(response => response.json())
.then(data => {
    if (data.success) {
        // Línea 247: Guarda la ruta en el input oculto
        document.getElementById('imagen').value = data.imagen;
        // Línea 249: Envía el formulario
        document.getElementById('formProducto').submit();
    }
})
```

### 3. **Servlet de Upload (UploadImagenServlet.java)**
**Archivo:** `SistemaEmpresa/src/java/com/sistemaempresa/servlets/UploadImagenServlet.java`

```java
// Línea 27: Constante de directorio
private static final String UPLOAD_DIR = "assets/productos";

// Línea 38: Obtiene el archivo del formulario
Part filePart = request.getPart("imagen");

// Línea 67: Obtiene la ruta real del contexto
String realPath = getServletContext().getRealPath("/");

// Línea 71: Construye la ruta completa
String uploadPath = realPath + File.separator + UPLOAD_DIR + File.separator;

// Línea 72-78: Crea el directorio si no existe
File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
    boolean dirCreated = uploadDir.mkdirs();
    System.out.println("Directorio creado: " + dirCreated + " en: " + uploadPath);
}

// Línea 86: Ruta completa del archivo
String rutaArchivo = uploadPath + nombreArchivo;

// Línea 89-98: Intenta guardar el archivo
try (InputStream input = filePart.getInputStream();
     FileOutputStream output = new FileOutputStream(rutaArchivo)) {
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = input.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
    }
    output.flush();
}

// Línea 111: Retorna la ruta para guardar en BD
String rutaRelativa = "web/" + UPLOAD_DIR + "/" + nombreArchivo;
response.getWriter().write("{\"success\": true, \"imagen\": \"" + rutaRelativa + "\"}");
```

### 4. **Servlet de Producto (ProductoServlet.java)**
**Archivo:** `SistemaEmpresa/src/java/com/sistemaempresa/servlets/ProductoServlet.java` (líneas 137-141)

```java
// Obtiene la imagen del input oculto (ya fue subida por AJAX)
String imagen = request.getParameter("imagen");
if (imagen != null && !imagen.isEmpty()) {
    producto.setImagen(imagen);
}
```

---

## Posibles Causas del Problema

### 1. **`getRealPath()` devuelve null**
- En algunos servidores (Tomcat en ciertos modos), `getRealPath("/")` puede devolver `null`
- Esto causaría que `uploadPath` sea `null + File.separator + ...` = error

### 2. **Permisos de Escritura**
- La carpeta `SistemaEmpresa/web/assets/productos/` podría no tener permisos de escritura
- El usuario que ejecuta Tomcat podría no tener permisos

### 3. **Ruta Incorrecta**
- `realPath` podría no ser la ruta esperada
- En desarrollo vs producción, las rutas pueden ser diferentes

### 4. **FileOutputStream no está escribiendo**
- El `try-with-resources` cierra el stream, pero podría haber un error silencioso
- No hay manejo de excepciones específico para `FileOutputStream`

### 5. **El archivo se crea pero en otra ubicación**
- `getRealPath()` podría devolver una ruta diferente a la esperada
- El archivo podría estar en `build/web/assets/productos/` en lugar de `web/assets/productos/`

---

## Recomendaciones para Investigar

1. **Revisar los logs de Tomcat** para ver qué ruta imprime:
   - `System.out.println("Real path del contexto: " + realPath);`
   - `System.out.println("Intentando guardar archivo en: " + rutaArchivo);`

2. **Verificar permisos** de la carpeta `SistemaEmpresa/web/assets/productos/`

3. **Buscar el archivo** en otras ubicaciones:
   - `SistemaEmpresa/build/web/assets/productos/`
   - Carpeta temporal de Tomcat
   - Directorio raíz del proyecto

4. **Agregar manejo de excepciones** más específico en el servlet

---

## Archivos Involucrados

| Archivo | Líneas | Función |
|---------|--------|---------|
| `form_content.jsp` | 120-121, 130, 234-257 | Formulario y AJAX |
| `UploadImagenServlet.java` | 27, 38, 67-98, 111 | Guardado físico |
| `ProductoServlet.java` | 137-141 | Guardado en BD |
| `ProductoDAO.java` | - | Inserta en tabla `productos` |


