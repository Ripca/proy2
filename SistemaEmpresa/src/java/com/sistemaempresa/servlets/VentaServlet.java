package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.VentaDAO;
import com.sistemaempresa.dao.ClienteDAO;
import com.sistemaempresa.dao.EmpleadoDAO;
import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.models.Venta;
import com.sistemaempresa.models.VentaDetalle;
import com.sistemaempresa.models.Cliente;
import com.sistemaempresa.models.Empleado;
import com.sistemaempresa.models.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class VentaServlet extends HttpServlet {
    
    private VentaDAO ventaDAO = new VentaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listarVentas(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarVenta(request, response);
                break;
            case "view":
                verVenta(request, response);
                break;
            default:
                listarVentas(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarVenta(request, response);
        } else if ("update".equals(action)) {
            actualizarVenta(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarVentas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Venta> ventas = ventaDAO.obtenerTodos();
        request.setAttribute("ventas", ventas);
        request.getRequestDispatcher("/WEB-INF/views/ventas/list.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar datos para los dropdowns
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        
        request.setAttribute("clientes", clientes);
        request.setAttribute("empleados", empleados);
        request.setAttribute("productos", productos);
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/form.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idVenta = Integer.parseInt(request.getParameter("id"));
        Venta venta = ventaDAO.obtenerPorId(idVenta);
        
        if (venta != null) {
            // Cargar datos para los dropdowns
            List<Cliente> clientes = clienteDAO.obtenerTodos();
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            List<Producto> productos = productoDAO.obtenerTodos();
            
            request.setAttribute("venta", venta);
            request.setAttribute("clientes", clientes);
            request.setAttribute("empleados", empleados);
            request.setAttribute("productos", productos);
            
            request.getRequestDispatcher("/WEB-INF/views/ventas/form.jsp").forward(request, response);
        } else {
            response.sendRedirect("VentaServlet?error=Venta no encontrada");
        }
    }
    
    private void verVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idVenta = Integer.parseInt(request.getParameter("id"));
        Venta venta = ventaDAO.obtenerPorId(idVenta);
        
        if (venta != null) {
            request.setAttribute("venta", venta);
            request.getRequestDispatcher("/WEB-INF/views/ventas/view.jsp").forward(request, response);
        } else {
            response.sendRedirect("VentaServlet?error=Venta no encontrada");
        }
    }
    
    private void guardarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos de la venta
            int noFactura = Integer.parseInt(request.getParameter("noFactura"));
            String serie = request.getParameter("serie");
            LocalDate fechaFactura = LocalDate.parse(request.getParameter("fechaFactura"));
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));
            int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
            
            Venta venta = new Venta(noFactura, serie, fechaFactura, idCliente, idEmpleado);
            
            // Obtener detalles de la venta
            String[] productosIds = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] precios = request.getParameterValues("precioUnitario");
            
            if (productosIds != null && cantidades != null && precios != null) {
                for (int i = 0; i < productosIds.length; i++) {
                    if (!productosIds[i].isEmpty() && !cantidades[i].isEmpty() && !precios[i].isEmpty()) {
                        VentaDetalle detalle = new VentaDetalle();
                        detalle.setIdProducto(Integer.parseInt(productosIds[i]));
                        detalle.setCantidad(Integer.parseInt(cantidades[i]));
                        detalle.setPrecioUnitario(Double.parseDouble(precios[i]));
                        venta.agregarDetalle(detalle);
                    }
                }
            }
            
            if (ventaDAO.insertar(venta)) {
                response.sendRedirect("VentaServlet?success=Venta guardada exitosamente");
            } else {
                response.sendRedirect("VentaServlet?error=Error al guardar la venta");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("VentaServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void actualizarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idVenta = Integer.parseInt(request.getParameter("idVenta"));
            int noFactura = Integer.parseInt(request.getParameter("noFactura"));
            String serie = request.getParameter("serie");
            LocalDate fechaFactura = LocalDate.parse(request.getParameter("fechaFactura"));
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));
            int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
            
            Venta venta = new Venta(noFactura, serie, fechaFactura, idCliente, idEmpleado);
            venta.setIdVenta(idVenta);
            
            if (ventaDAO.actualizar(venta)) {
                response.sendRedirect("VentaServlet?success=Venta actualizada exitosamente");
            } else {
                response.sendRedirect("VentaServlet?error=Error al actualizar la venta");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("VentaServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void eliminarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idVenta = Integer.parseInt(request.getParameter("id"));
            
            if (ventaDAO.eliminar(idVenta)) {
                response.sendRedirect("VentaServlet?success=Venta eliminada exitosamente");
            } else {
                response.sendRedirect("VentaServlet?error=Error al eliminar la venta");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("VentaServlet?error=Error al procesar la eliminación: " + e.getMessage());
        }
    }
}
