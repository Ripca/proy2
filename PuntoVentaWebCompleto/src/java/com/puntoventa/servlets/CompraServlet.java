package com.puntoventa.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.puntoventa.dao.CompraDAO;
import com.puntoventa.dao.ProveedorDAO;
import com.puntoventa.dao.ProductoDAO;
import com.puntoventa.models.Compra;
import com.puntoventa.models.CompraDetalle;
import com.puntoventa.models.Proveedor;
import com.puntoventa.models.Producto;
import com.puntoventa.models.Usuario;

/**
 * Servlet para gestión de Compras
 * Implementa exactamente la funcionalidad del proyecto C# repp/vista/Compra.h
 */
public class CompraServlet extends HttpServlet {

    private CompraDAO compraDAO = new CompraDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listarCompras(request, response);
                break;
            case "new":
                mostrarFormularioNuevaCompra(request, response);
                break;
            case "view":
                verCompra(request, response);
                break;
            case "edit":
                editarCompra(request, response);
                break;
            case "delete":
                eliminarCompra(request, response);
                break;
            default:
                listarCompras(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                crearCompra(request, response);
                break;
            case "update":
                actualizarCompra(request, response);
                break;
            default:
                crearCompra(request, response);
                break;
        }
    }

    /**
     * Lista todas las compras
     */
    private void listarCompras(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Compra> compras = compraDAO.obtenerTodos();
        request.setAttribute("compras", compras);
        request.setAttribute("titulo", "Gestión de Compras");
        
        request.getRequestDispatcher("/WEB-INF/views/compras/list.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para nueva compra
     */
    private void mostrarFormularioNuevaCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar proveedores y productos para los selects
        List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("productos", productos);
        request.setAttribute("titulo", "Nueva Compra");
        request.setAttribute("fechaActual", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        request.getRequestDispatcher("/WEB-INF/views/compras/form.jsp").forward(request, response);
    }

    /**
     * Crea una nueva compra con sus detalles
     * Implementa exactamente la lógica del método crear() del C#
     */
    private void crearCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Obtener datos de la compra maestro
            String noOrderCompraStr = request.getParameter("noOrderCompra");
            String idProveedorStr = request.getParameter("idProveedor");
            String fechaOrderStr = request.getParameter("fechaOrder");
            String fechaIngresoStr = request.getParameter("fechaIngreso");
            
            // Validar datos requeridos
            if (noOrderCompraStr == null || idProveedorStr == null || 
                fechaOrderStr == null || fechaIngresoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Faltan datos requeridos\"}");
                return;
            }
            
            // Crear objeto compra
            Compra compra = new Compra();
            compra.setNoOrderCompra(Integer.parseInt(noOrderCompraStr));
            compra.setIdProveedor(Integer.parseInt(idProveedorStr));
            compra.setFechaOrder(new SimpleDateFormat("yyyy-MM-dd").parse(fechaOrderStr));
            compra.setFechaIngreso(new SimpleDateFormat("yyyy-MM-dd").parse(fechaIngresoStr));
            
            // Obtener detalles de compra
            String[] idProductos = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] preciosUnitarios = request.getParameterValues("precioUnitario");
            
            if (idProductos != null && cantidades != null && preciosUnitarios != null) {
                List<CompraDetalle> detalles = new ArrayList<>();
                
                for (int i = 0; i < idProductos.length; i++) {
                    if (idProductos[i] != null && !idProductos[i].trim().isEmpty()) {
                        CompraDetalle detalle = new CompraDetalle();
                        detalle.setIdProducto(Integer.parseInt(idProductos[i]));
                        detalle.setCantidad(Integer.parseInt(cantidades[i]));
                        detalle.setPrecioUnitario(Double.parseDouble(preciosUnitarios[i]));
                        
                        detalles.add(detalle);
                    }
                }
                
                compra.setDetalles(detalles);
            }
            
            // Crear la compra (igual que en C#)
            int idCompra = compraDAO.crear(compra);
            
            if (idCompra > 0) {
                out.print("{\"success\": true, \"message\": \"Compra creada exitosamente\", \"idCompra\": " + idCompra + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al crear la compra\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Ver detalles de una compra
     */
    private void verCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("CompraServlet?action=list");
            return;
        }
        
        try {
            int idCompra = Integer.parseInt(idStr);
            Compra compra = compraDAO.obtenerPorId(idCompra);
            
            if (compra == null) {
                response.sendRedirect("CompraServlet?action=list");
                return;
            }
            
            request.setAttribute("compra", compra);
            request.setAttribute("titulo", "Detalles de Compra #" + compra.getIdCompra());
            
            request.getRequestDispatcher("/WEB-INF/views/compras/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("CompraServlet?action=list");
        }
    }

    /**
     * Editar una compra
     */
    private void editarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("CompraServlet?action=list");
            return;
        }
        
        try {
            int idCompra = Integer.parseInt(idStr);
            Compra compra = compraDAO.obtenerPorId(idCompra);
            
            if (compra == null) {
                response.sendRedirect("CompraServlet?action=list");
                return;
            }
            
            // Cargar proveedores y productos para los selects
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            List<Producto> productos = productoDAO.obtenerTodos();
            
            request.setAttribute("compra", compra);
            request.setAttribute("proveedores", proveedores);
            request.setAttribute("productos", productos);
            request.setAttribute("titulo", "Editar Compra #" + compra.getIdCompra());
            
            request.getRequestDispatcher("/WEB-INF/views/compras/form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("CompraServlet?action=list");
        }
    }

    /**
     * Actualizar una compra
     */
    private void actualizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idCompraStr = request.getParameter("idCompra");
            String noOrderCompraStr = request.getParameter("noOrderCompra");
            String idProveedorStr = request.getParameter("idProveedor");
            String fechaOrderStr = request.getParameter("fechaOrder");
            String fechaIngresoStr = request.getParameter("fechaIngreso");
            
            if (idCompraStr == null || noOrderCompraStr == null || idProveedorStr == null || 
                fechaOrderStr == null || fechaIngresoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Faltan datos requeridos\"}");
                return;
            }
            
            Compra compra = new Compra();
            compra.setIdCompra(Integer.parseInt(idCompraStr));
            compra.setNoOrderCompra(Integer.parseInt(noOrderCompraStr));
            compra.setIdProveedor(Integer.parseInt(idProveedorStr));
            compra.setFechaOrder(new SimpleDateFormat("yyyy-MM-dd").parse(fechaOrderStr));
            compra.setFechaIngreso(new SimpleDateFormat("yyyy-MM-dd").parse(fechaIngresoStr));
            
            boolean actualizado = compraDAO.actualizar(compra);
            
            if (actualizado) {
                out.print("{\"success\": true, \"message\": \"Compra actualizada exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al actualizar la compra\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Eliminar una compra
     */
    private void eliminarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID de compra requerido\"}");
                return;
            }
            
            int idCompra = Integer.parseInt(idStr);
            boolean eliminado = compraDAO.eliminar(idCompra);
            
            if (eliminado) {
                out.print("{\"success\": true, \"message\": \"Compra eliminada exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al eliminar la compra\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }
}
