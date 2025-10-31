package com.sistemaempresa.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Servlet para subir imágenes de productos
 */
@WebServlet("/UploadImagenServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 5 * 5
)
public class UploadImagenServlet extends HttpServlet {
    
    private static final String UPLOAD_DIR = "assets/productos";
    private static final String[] TIPOS_PERMITIDOS = {"image/png", "image/jpeg", "image/jpg"};
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try {
            // Obtener el archivo del formulario
            Part filePart = request.getPart("imagen");
            
            if (filePart == null || filePart.getSize() == 0) {
                response.getWriter().write("{\"success\": false, \"message\": \"No se seleccionó archivo\"}");
                return;
            }
            
            // Validar tipo de archivo
            String contentType = filePart.getContentType();
            boolean tipoValido = false;
            for (String tipo : TIPOS_PERMITIDOS) {
                if (contentType.equals(tipo)) {
                    tipoValido = true;
                    break;
                }
            }
            
            if (!tipoValido) {
                response.getWriter().write("{\"success\": false, \"message\": \"Solo se permiten archivos PNG, JPEG o JPG\"}");
                return;
            }
            
            // Validar tamaño (máximo 5MB)
            if (filePart.getSize() > 1024 * 1024 * 5) {
                response.getWriter().write("{\"success\": false, \"message\": \"El archivo es demasiado grande (máximo 5MB)\"}");
                return;
            }
            
            // Obtener la ruta real del directorio web
            String realPath = getServletContext().getRealPath("/");
            System.out.println("Real path del contexto: " + realPath);

            // Reemplazar "build/web" por "web" si es necesario
            String uploadPath;
            if (realPath.contains("build" + File.separator + "web")) {
                uploadPath = realPath.replace("build" + File.separator + "web", "web");
            } else {
                uploadPath = realPath;
            }

            uploadPath = uploadPath + File.separator + UPLOAD_DIR + File.separator;
            System.out.println("Ruta final de upload: " + uploadPath);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean dirCreated = uploadDir.mkdirs();
                System.out.println("Directorio creado: " + dirCreated + " en: " + uploadPath);
            } else {
                System.out.println("Directorio ya existe: " + uploadPath);
            }

            // Generar nombre único para el archivo
            String nombreOriginal = filePart.getSubmittedFileName();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            String nombreArchivo = UUID.randomUUID().toString() + extension;

            // Guardar archivo usando InputStream y FileOutputStream
            String rutaArchivo = uploadPath + nombreArchivo;
            System.out.println("Intentando guardar archivo en: " + rutaArchivo);

            try (InputStream input = filePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(rutaArchivo)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();
            }

            // Verificar que el archivo se guardó correctamente
            File archivoGuardado = new File(rutaArchivo);
            if (!archivoGuardado.exists() || archivoGuardado.length() == 0) {
                System.out.println("ERROR: El archivo no se guardó. Existe: " + archivoGuardado.exists() + ", Tamaño: " + archivoGuardado.length());
                response.getWriter().write("{\"success\": false, \"message\": \"Error: El archivo no se guardó correctamente en: " + rutaArchivo + "\"}");
                return;
            }

            System.out.println("Archivo guardado exitosamente en: " + rutaArchivo + " (Tamaño: " + archivoGuardado.length() + " bytes)");

            // Retornar ruta con "web/" incluido para guardar en BD
            String rutaRelativa = "web/" + UPLOAD_DIR + "/" + nombreArchivo;

            response.getWriter().write("{\"success\": true, \"imagen\": \"" + rutaRelativa + "\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Error al subir archivo: " + e.getMessage() + "\"}");
        }
    }
}

