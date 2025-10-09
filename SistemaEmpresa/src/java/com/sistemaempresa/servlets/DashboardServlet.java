package com.sistemaempresa.servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.sistemaempresa.dao.CarruselDAO;
import com.sistemaempresa.dao.MenuDAO;
import com.sistemaempresa.models.CarruselImagen;
import com.sistemaempresa.models.Menu;

/**
 * Servlet para el dashboard principal del sistema
 */
public class DashboardServlet extends HttpServlet {

    private CarruselDAO carruselDAO = new CarruselDAO();
    private MenuDAO menuDAO = new MenuDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.html");
            return;
        }

        // Cargar datos dinámicos para el dashboard
        try {
            // Cargar imágenes del carrusel
            List<CarruselImagen> imagenesCarrusel = carruselDAO.obtenerImagenesActivas();
            request.setAttribute("imagenesCarrusel", imagenesCarrusel);

            // Cargar menús para el sidebar
            List<Menu> menusJerarquicos = menuDAO.obtenerMenusJerarquicos();
            request.setAttribute("menusJerarquicos", menusJerarquicos);

        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, continuar sin datos dinámicos
        }

        // Redirigir al dashboard JSP simple (sin JSTL)
        request.getRequestDispatcher("/WEB-INF/views/dashboard_simple.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
