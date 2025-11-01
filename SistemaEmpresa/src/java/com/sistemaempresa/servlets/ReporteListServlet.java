package com.sistemaempresa.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para mostrar la página de reportes disponibles
 */
@WebServlet(
  name = "ReporteListServlet",
  description = "Servlet que maneja las rutas URL para el controlador de reportes (ReporteServlet)",
  urlPatterns = {"/reportes"}
)
public class ReporteListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirigir a la página de reportes con plantilla
        request.getRequestDispatcher("/WEB-INF/views/reportes/list_template.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

