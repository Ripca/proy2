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
import com.puntoventa.dao.VentaDAO;
import com.puntoventa.dao.ClienteDAO;
import com.puntoventa.dao.EmpleadoDAO;
import com.puntoventa.dao.ProductoDAO;
import com.puntoventa.models.Venta;
import com.puntoventa.models.VentaDetalle;
import com.puntoventa.models.Cliente;
import com.puntoventa.models.Empleado;
import com.puntoventa.models.Producto;
import com.puntoventa.models.Usuario;

/**
 * Servlet para gestión de Ventas
 * Implementa exactamente la funcionalidad del proyecto C# repp/vista/Venta.h
 */
public class VentaServlet extends HttpServlet {

    private VentaDAO ventaDAO = new VentaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
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
                listarVentas(request, response);
                break;
            case "new":
                mostrarFormularioNuevaVenta(request, response);
                break;
            case "view":
                verVenta(request, response);
                break;
            case "edit":
                editarVenta(request, response);
                break;
            case "delete":
                eliminarVenta(request, response);
                break;
            default:
                listarVentas(request, response);
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
                crearVenta(request, response);
                break;
            case "update":
                actualizarVenta(request, response);
                break;
            default:
                crearVenta(request, response);
                break;
        }
    }

    /**
     * Lista todas las ventas
     */
    private void listarVentas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Venta> ventas = ventaDAO.obtenerTodos();
        request.setAttribute("ventas", ventas);
        request.setAttribute("titulo", "Gestión de Ventas");
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/list.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para nueva venta
     */
    private void mostrarFormularioNuevaVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar clientes, empleados y productos para los selects
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        
        request.setAttribute("clientes", clientes);
        request.setAttribute("empleados", empleados);
        request.setAttribute("productos", productos);
        request.setAttribute("titulo", "Nueva Venta");
        request.setAttribute("fechaActual", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        request.setAttribute("serieDefecto", "A"); // Serie por defecto como en C#
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/form.jsp").forward(request, response);
    }

    /**
     * Crea una nueva venta con sus detalles
     * Implementa exactamente la lógica del método crearVenta() del C#
     */
    private void crearVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Obtener datos de la venta maestro
            String noFacturaStr = request.getParameter("noFactura");
            String serie = request.getParameter("serie");
            String fechaFacturaStr = request.getParameter("fechaFactura");
            String idClienteStr = request.getParameter("idCliente");
            String idEmpleadoStr = request.getParameter("idEmpleado");
            String fechaIngresoStr = request.getParameter("fechaIngreso");
            
            // Validar datos requeridos
            if (noFacturaStr == null || serie == null || fechaFacturaStr == null || 
                idClienteStr == null || idEmpleadoStr == null || fechaIngresoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Faltan datos requeridos\"}");
                return;
            }
            
            // Crear objeto venta
            Venta venta = new Venta();
            venta.setNoFactura(Integer.parseInt(noFacturaStr));
            venta.setSerie(serie);
            venta.setFechaFactura(new SimpleDateFormat("yyyy-MM-dd").parse(fechaFacturaStr));
            venta.setIdCliente(Integer.parseInt(idClienteStr));
            venta.setIdEmpleado(Integer.parseInt(idEmpleadoStr));
            venta.setFechaIngreso(new SimpleDateFormat("yyyy-MM-dd").parse(fechaIngresoStr));
            
            // Obtener detalles de venta
            String[] idProductos = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] preciosUnitarios = request.getParameterValues("precioUnitario");
            
            if (idProductos != null && cantidades != null && preciosUnitarios != null) {
                List<VentaDetalle> detalles = new ArrayList<>();
                
                for (int i = 0; i < idProductos.length; i++) {
                    if (idProductos[i] != null && !idProductos[i].trim().isEmpty()) {
                        VentaDetalle detalle = new VentaDetalle();
                        detalle.setIdProducto(Integer.parseInt(idProductos[i]));
                        detalle.setCantidad(cantidades[i]); // VARCHAR(45) como en C#
                        detalle.setPrecioUnitario(Double.parseDouble(preciosUnitarios[i]));
                        
                        detalles.add(detalle);
                    }
                }
                
                venta.setDetalles(detalles);
            }
            
            // Crear la venta (igual que en C#)
            int idVenta = ventaDAO.crearVenta(venta);
            
            if (idVenta > 0) {
                out.print("{\"success\": true, \"message\": \"Venta creada exitosamente\", \"idVenta\": " + idVenta + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al crear la venta\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Ver detalles de una venta
     */
    private void verVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("VentaServlet?action=list");
            return;
        }
        
        try {
            int idVenta = Integer.parseInt(idStr);
            Venta venta = ventaDAO.obtenerPorId(idVenta);
            
            if (venta == null) {
                response.sendRedirect("VentaServlet?action=list");
                return;
            }
            
            request.setAttribute("venta", venta);
            request.setAttribute("titulo", "Detalles de Venta #" + venta.getIdVenta());
            
            request.getRequestDispatcher("/WEB-INF/views/ventas/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("VentaServlet?action=list");
        }
    }

    /**
     * Editar una venta
     */
    private void editarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("VentaServlet?action=list");
            return;
        }
        
        try {
            int idVenta = Integer.parseInt(idStr);
            Venta venta = ventaDAO.obtenerPorId(idVenta);
            
            if (venta == null) {
                response.sendRedirect("VentaServlet?action=list");
                return;
            }
            
            // Cargar clientes, empleados y productos para los selects
            List<Cliente> clientes = clienteDAO.obtenerTodos();
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            List<Producto> productos = productoDAO.obtenerTodos();
            
            request.setAttribute("venta", venta);
            request.setAttribute("clientes", clientes);
            request.setAttribute("empleados", empleados);
            request.setAttribute("productos", productos);
            request.setAttribute("titulo", "Editar Venta #" + venta.getIdVenta());
            
            request.getRequestDispatcher("/WEB-INF/views/ventas/form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("VentaServlet?action=list");
        }
    }

    /**
     * Actualizar una venta
     */
    private void actualizarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idVentaStr = request.getParameter("idVenta");
            String noFacturaStr = request.getParameter("noFactura");
            String serie = request.getParameter("serie");
            String fechaFacturaStr = request.getParameter("fechaFactura");
            String idClienteStr = request.getParameter("idCliente");
            String idEmpleadoStr = request.getParameter("idEmpleado");
            String fechaIngresoStr = request.getParameter("fechaIngreso");
            
            if (idVentaStr == null || noFacturaStr == null || serie == null || 
                fechaFacturaStr == null || idClienteStr == null || idEmpleadoStr == null || 
                fechaIngresoStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Faltan datos requeridos\"}");
                return;
            }
            
            Venta venta = new Venta();
            venta.setIdVenta(Integer.parseInt(idVentaStr));
            venta.setNoFactura(Integer.parseInt(noFacturaStr));
            venta.setSerie(serie);
            venta.setFechaFactura(new SimpleDateFormat("yyyy-MM-dd").parse(fechaFacturaStr));
            venta.setIdCliente(Integer.parseInt(idClienteStr));
            venta.setIdEmpleado(Integer.parseInt(idEmpleadoStr));
            venta.setFechaIngreso(new SimpleDateFormat("yyyy-MM-dd").parse(fechaIngresoStr));
            
            boolean actualizado = ventaDAO.actualizar(venta);
            
            if (actualizado) {
                out.print("{\"success\": true, \"message\": \"Venta actualizada exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al actualizar la venta\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Eliminar una venta
     */
    private void eliminarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID de venta requerido\"}");
                return;
            }
            
            int idVenta = Integer.parseInt(idStr);
            boolean eliminado = ventaDAO.eliminar(idVenta);
            
            if (eliminado) {
                out.print("{\"success\": true, \"message\": \"Venta eliminada exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al eliminar la venta\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor: " + e.getMessage() + "\"}");
        }
    }
}
