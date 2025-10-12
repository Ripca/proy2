package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.MenuItemDAO;
import com.sistemaempresa.models.MenuItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para manejar el menú dinámico
 */
public class MenuServlet extends HttpServlet {
    
    private MenuItemDAO menuItemDAO;
    
    @Override
    public void init() throws ServletException {
        menuItemDAO = new MenuItemDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "getMenu";
        }
        
        switch (action) {
            case "getMenu":
                obtenerMenuEstructurado(request, response);
                break;
            case "getMenuJson":
                obtenerMenuJson(request, response);
                break;
            default:
                obtenerMenuEstructurado(request, response);
                break;
        }
    }
    
    /**
     * Obtiene el menú estructurado y lo pasa como atributo
     */
    private void obtenerMenuEstructurado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<MenuItem> menuEstructurado = menuItemDAO.obtenerMenuEstructurado();
        request.setAttribute("menuItems", menuEstructurado);
        
        // Redirigir a la página que solicitó el menú
        String returnPage = request.getParameter("returnPage");
        if (returnPage != null && !returnPage.isEmpty()) {
            request.getRequestDispatcher(returnPage).forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/menu/menu_test.jsp").forward(request, response);
        }
    }
    
    /**
     * Devuelve el menú en formato JSON para AJAX (implementación manual sin Gson)
     */
    private void obtenerMenuJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<MenuItem> menuEstructurado = menuItemDAO.obtenerMenuEstructurado();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        for (int i = 0; i < menuEstructurado.size(); i++) {
            MenuItem item = menuEstructurado.get(i);
            if (i > 0) jsonBuilder.append(",");
            jsonBuilder.append(menuItemToJson(item));
        }

        jsonBuilder.append("]");

        PrintWriter out = response.getWriter();
        out.print(jsonBuilder.toString());
        out.flush();
    }

    /**
     * Convierte un MenuItem a JSON manualmente
     */
    private String menuItemToJson(MenuItem item) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"idMenuItem\":").append(item.getIdMenuItem()).append(",");
        json.append("\"titulo\":\"").append(escapeJson(item.getTitulo())).append("\",");
        json.append("\"url\":\"").append(item.getUrl() != null ? escapeJson(item.getUrl()) : "").append("\",");
        json.append("\"icono\":\"").append(item.getIcono() != null ? escapeJson(item.getIcono()) : "").append("\",");
        json.append("\"orden\":").append(item.getOrden()).append(",");
        json.append("\"activo\":").append(item.isActivo()).append(",");

        // Agregar hijos si existen
        json.append("\"hijos\":[");
        if (item.tieneHijos()) {
            for (int i = 0; i < item.getHijos().size(); i++) {
                if (i > 0) json.append(",");
                json.append(menuItemToJson(item.getHijos().get(i)));
            }
        }
        json.append("]");

        json.append("}");
        return json.toString();
    }

    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
