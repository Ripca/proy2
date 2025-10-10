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
import com.sistemaempresa.dao.ClienteDAO;
import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.dao.EmpleadoDAO;
import com.sistemaempresa.dao.ProveedorDAO;
import com.sistemaempresa.models.CarruselImagen;
import com.sistemaempresa.models.Menu;

/**
 * Servlet para el dashboard principal del sistema
 */
public class DashboardServlet extends HttpServlet {

    private CarruselDAO carruselDAO = new CarruselDAO();
    private MenuDAO menuDAO = new MenuDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();

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

            // Cargar menús para el sidebar (con manejo de errores)
            try {
                List<Menu> menusJerarquicos = menuDAO.obtenerMenusJerarquicos();
                request.setAttribute("menusJerarquicos", menusJerarquicos);
            } catch (Exception e) {
                // Si hay error cargando menús, el template usará menús por defecto
                System.err.println("Error cargando menús: " + e.getMessage());
                request.setAttribute("menusJerarquicos", null);
            }

            // Cargar contadores para las tarjetas de resumen
            int totalClientes = clienteDAO.obtenerTodos().size();
            int totalProductos = productoDAO.obtenerTodos().size();
            int totalEmpleados = empleadoDAO.obtenerTodos().size();
            int totalProveedores = proveedorDAO.obtenerTodos().size();

            request.setAttribute("totalClientes", totalClientes);
            request.setAttribute("totalProductos", totalProductos);
            request.setAttribute("totalEmpleados", totalEmpleados);
            request.setAttribute("totalProveedores", totalProveedores);

        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, continuar sin datos dinámicos
            request.setAttribute("totalClientes", 0);
            request.setAttribute("totalProductos", 0);
            request.setAttribute("totalEmpleados", 0);
            request.setAttribute("totalProveedores", 0);
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
