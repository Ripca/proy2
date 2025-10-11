package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.MenuItemDAO;
import com.sistemaempresa.models.MenuItem;
import com.google.gson.Gson;

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
@WebServlet("/MenuServlet")
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
     * Devuelve el menú en formato JSON para AJAX
     */
    private void obtenerMenuJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<MenuItem> menuEstructurado = menuItemDAO.obtenerMenuEstructurado();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Gson gson = new Gson();
        String jsonMenu = gson.toJson(menuEstructurado);
        
        PrintWriter out = response.getWriter();
        out.print(jsonMenu);
        out.flush();
    }
}
