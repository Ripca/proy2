package com.sistemaempresa.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para mostrar la página de reportes disponibles
 */
public class ReporteListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar atributos para la plantilla
        request.setAttribute("pageTitle", "Reportes");
        request.setAttribute("pageIcon", "fas fa-chart-bar");
        
        // Redirigir a la página de reportes
        request.getRequestDispatcher("/WEB-INF/views/reportes/list.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

