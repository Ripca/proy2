package com.sistemaempresa.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet de prueba para verificar que el sistema funciona sin librerías externas
 */
public class TestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Test Sistema Empresa</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            
            out.println("<div class='container mt-5'>");
            out.println("<div class='row justify-content-center'>");
            out.println("<div class='col-md-8'>");
            
            out.println("<div class='card'>");
            out.println("<div class='card-header bg-success text-white'>");
            out.println("<h3 class='mb-0'><i class='fas fa-check-circle'></i> Sistema Empresa - Test Exitoso</h3>");
            out.println("</div>");
            
            out.println("<div class='card-body'>");
            out.println("<div class='alert alert-success'>");
            out.println("<h4><i class='fas fa-thumbs-up'></i> ¡Felicitaciones!</h4>");
            out.println("<p>El sistema está funcionando correctamente sin librerías externas.</p>");
            out.println("</div>");
            
            out.println("<h5>Estado del Sistema:</h5>");
            out.println("<ul class='list-group list-group-flush'>");
            out.println("<li class='list-group-item d-flex justify-content-between align-items-center'>");
            out.println("Jakarta Servlet API <span class='badge bg-success rounded-pill'>✓ OK</span>");
            out.println("</li>");
            out.println("<li class='list-group-item d-flex justify-content-between align-items-center'>");
            out.println("Tomcat Server <span class='badge bg-success rounded-pill'>✓ OK</span>");
            out.println("</li>");
            out.println("<li class='list-group-item d-flex justify-content-between align-items-center'>");
            out.println("Sistema Empresa <span class='badge bg-success rounded-pill'>✓ OK</span>");
            out.println("</li>");
            out.println("</ul>");
            
            out.println("<div class='mt-4'>");
            out.println("<h5>Información del Sistema:</h5>");
            out.println("<table class='table table-sm'>");
            out.println("<tr><td><strong>Java Version:</strong></td><td>" + System.getProperty("java.version") + "</td></tr>");
            out.println("<tr><td><strong>Servlet Version:</strong></td><td>Jakarta Servlet 5.0+</td></tr>");
            out.println("<tr><td><strong>Server Info:</strong></td><td>" + getServletContext().getServerInfo() + "</td></tr>");
            out.println("<tr><td><strong>Context Path:</strong></td><td>" + request.getContextPath() + "</td></tr>");
            out.println("</table>");
            out.println("</div>");
            
            out.println("<div class='mt-4'>");
            out.println("<h5>Próximos Pasos:</h5>");
            out.println("<ol>");
            out.println("<li>Configurar base de datos MySQL</li>");
            out.println("<li>Ejecutar script SQL: <code>database/sistema_empresa.sql</code></li>");
            out.println("<li>Acceder al sistema: <a href='index.html' class='btn btn-primary btn-sm'>Ir al Login</a></li>");
            out.println("</ol>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
