package com.puntoventa.servlets;

import com.puntoventa.dao.*;
import com.puntoventa.models.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet para el dashboard principal del sistema
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard", "/", "/index"})
public class DashboardServlet extends HttpServlet {
    
    private ProductoDAO productoDAO;
    private ClienteDAO clienteDAO;
    private ProveedorDAO proveedorDAO;
    private EmpleadoDAO empleadoDAO;
    private VentaDAO ventaDAO;
    private CompraDAO compraDAO;
    private CarruselDAO carruselDAO;
    private MenuDAO menuDAO;
    
    @Override
    public void init() throws ServletException {
        productoDAO = new ProductoDAO();
        clienteDAO = new ClienteDAO();
        proveedorDAO = new ProveedorDAO();
        empleadoDAO = new EmpleadoDAO();
        ventaDAO = new VentaDAO();
        compraDAO = new CompraDAO();
        carruselDAO = new CarruselDAO();
        menuDAO = new MenuDAO();
    }
    
    /**
     * Maneja las peticiones GET (mostrar dashboard)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar autenticación
        if (!LoginServlet.verificarAutenticacion(request, response)) {
            return;
        }
        
        Usuario usuarioActual = LoginServlet.obtenerUsuarioActual(request);
        
        try {
            // Obtener estadísticas generales
            int totalProductos = productoDAO.contarTotal();
            int totalClientes = clienteDAO.contarTotal();
            int totalProveedores = proveedorDAO.contarTotal();
            int totalEmpleados = empleadoDAO.contarTotal();
            int totalVentas = ventaDAO.contarTotal();
            int totalCompras = compraDAO.contarTotal();
            
            // Obtener productos con stock bajo
            List<Producto> productosStockBajo = productoDAO.obtenerProductosStockBajo();
            
            // Obtener ventas del día
            LocalDate hoy = LocalDate.now();
            BigDecimal ventasDelDia = ventaDAO.obtenerTotalVentasDelDia(hoy);
            
            // Obtener top clientes
            List<Cliente> topClientes = clienteDAO.obtenerTopClientes(5);
            
            // Obtener compras pendientes
            List<Compra> comprasPendientes = compraDAO.obtenerPorEstado("PENDIENTE");
            
            // Obtener imágenes del carrusel
            List<CarruselImagen> imagenesCarrusel = carruselDAO.obtenerImagenesActivas(5);
            
            // Obtener menús para el usuario actual
            List<Menu> menusUsuario = menuDAO.obtenerMenusJerarquicos(usuarioActual.getRol());
            
            // Establecer atributos para la vista
            request.setAttribute("totalProductos", totalProductos);
            request.setAttribute("totalClientes", totalClientes);
            request.setAttribute("totalProveedores", totalProveedores);
            request.setAttribute("totalEmpleados", totalEmpleados);
            request.setAttribute("totalVentas", totalVentas);
            request.setAttribute("totalCompras", totalCompras);
            request.setAttribute("productosStockBajo", productosStockBajo);
            request.setAttribute("ventasDelDia", ventasDelDia);
            request.setAttribute("topClientes", topClientes);
            request.setAttribute("comprasPendientes", comprasPendientes);
            request.setAttribute("imagenesCarrusel", imagenesCarrusel);
            request.setAttribute("menusUsuario", menusUsuario);
            request.setAttribute("usuarioActual", usuarioActual);
            
            // Calcular estadísticas adicionales
            calcularEstadisticasAdicionales(request);
            
            // Mostrar dashboard
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error en dashboard: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Calcula estadísticas adicionales para el dashboard
     */
    private void calcularEstadisticasAdicionales(HttpServletRequest request) {
        try {
            // Productos más vendidos (simulado - requeriría consulta más compleja)
            List<Producto> productosPopulares = productoDAO.obtenerTodos();
            if (productosPopulares.size() > 5) {
                productosPopulares = productosPopulares.subList(0, 5);
            }
            request.setAttribute("productosPopulares", productosPopulares);
            
            // Estadísticas de inventario
            List<Producto> todosProductos = productoDAO.obtenerTodos();
            int productosStockBajo = 0;
            int productosStockNormal = 0;
            BigDecimal valorInventario = BigDecimal.ZERO;
            
            for (Producto producto : todosProductos) {
                if (producto.isStockMinimo()) {
                    productosStockBajo++;
                } else {
                    productosStockNormal++;
                }
                
                BigDecimal valorProducto = producto.getPrecioCosto()
                    .multiply(BigDecimal.valueOf(producto.getExistencia()));
                valorInventario = valorInventario.add(valorProducto);
            }
            
            request.setAttribute("productosStockBajo", productosStockBajo);
            request.setAttribute("productosStockNormal", productosStockNormal);
            request.setAttribute("valorInventario", valorInventario);
            
            // Estadísticas de ventas por período
            LocalDate inicioSemana = LocalDate.now().minusDays(7);
            LocalDate finSemana = LocalDate.now();
            List<Venta> ventasSemana = ventaDAO.obtenerPorRangoFechas(inicioSemana, finSemana);
            
            BigDecimal totalVentasSemana = BigDecimal.ZERO;
            for (Venta venta : ventasSemana) {
                totalVentasSemana = totalVentasSemana.add(venta.getTotal());
            }
            
            request.setAttribute("ventasSemana", ventasSemana.size());
            request.setAttribute("totalVentasSemana", totalVentasSemana);
            
            // Estadísticas de compras
            List<Compra> todasCompras = compraDAO.obtenerTodas();
            int comprasPendientes = 0;
            int comprasRecibidas = 0;
            int comprasCanceladas = 0;
            
            for (Compra compra : todasCompras) {
                switch (compra.getEstado()) {
                    case "PENDIENTE":
                        comprasPendientes++;
                        break;
                    case "RECIBIDA":
                        comprasRecibidas++;
                        break;
                    case "CANCELADA":
                        comprasCanceladas++;
                        break;
                }
            }
            
            request.setAttribute("comprasPendientesCount", comprasPendientes);
            request.setAttribute("comprasRecibidasCount", comprasRecibidas);
            request.setAttribute("comprasCanceladasCount", comprasCanceladas);
            
        } catch (Exception e) {
            System.err.println("Error al calcular estadísticas adicionales: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Maneja las peticiones POST (acciones del dashboard)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar autenticación
        if (!LoginServlet.verificarAutenticacion(request, response)) {
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("getStats".equals(action)) {
            getStatsAjax(request, response);
        } else if ("getNotifications".equals(action)) {
            getNotificationsAjax(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
    
    /**
     * Obtiene estadísticas vía AJAX
     */
    private void getStatsAjax(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener estadísticas actualizadas
            int totalProductos = productoDAO.contarTotal();
            int totalClientes = clienteDAO.contarTotal();
            int totalVentas = ventaDAO.contarTotal();
            BigDecimal ventasDelDia = ventaDAO.obtenerTotalVentasDelDia(LocalDate.now());
            
            String json = String.format(
                "{\"totalProductos\": %d, \"totalClientes\": %d, \"totalVentas\": %d, \"ventasDelDia\": %.2f}",
                totalProductos, totalClientes, totalVentas, ventasDelDia
            );
            
            response.getWriter().print(json);
            
        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\": \"Error al obtener estadísticas\"}");
        }
    }
    
    /**
     * Obtiene notificaciones vía AJAX
     */
    private void getNotificationsAjax(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener productos con stock bajo
            List<Producto> productosStockBajo = productoDAO.obtenerProductosStockBajo();
            
            // Obtener compras pendientes
            List<Compra> comprasPendientes = compraDAO.obtenerPorEstado("PENDIENTE");
            
            StringBuilder json = new StringBuilder();
            json.append("{\"notifications\": [");
            
            // Notificaciones de stock bajo
            for (int i = 0; i < productosStockBajo.size(); i++) {
                Producto producto = productosStockBajo.get(i);
                if (i > 0) json.append(",");
                json.append(String.format(
                    "{\"type\": \"warning\", \"message\": \"Stock bajo: %s (Existencia: %d)\"}",
                    producto.getProducto(), producto.getExistencia()
                ));
            }
            
            // Notificaciones de compras pendientes
            for (Compra compra : comprasPendientes) {
                if (json.length() > 20) json.append(",");
                json.append(String.format(
                    "{\"type\": \"info\", \"message\": \"Compra pendiente: Orden #%d\"}",
                    compra.getNoOrdenCompra()
                ));
            }
            
            json.append("]}");
            
            response.getWriter().print(json.toString());
            
        } catch (Exception e) {
            System.err.println("Error al obtener notificaciones: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\": \"Error al obtener notificaciones\"}");
        }
    }
}
