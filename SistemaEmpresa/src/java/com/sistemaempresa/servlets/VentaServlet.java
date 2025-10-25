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
import jakarta.servlet.http.HttpSession;
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
        request.getRequestDispatcher("/WEB-INF/views/ventas/list_template.jsp").forward(request, response);
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
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/form_template.jsp").forward(request, response);
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
            
               // Obtener los detalles de la venta
            List<VentaDetalle> detalles = ventaDAO.obtenerDetallesPorVenta(idVenta);
            venta.setDetalles(detalles);
            
            request.setAttribute("venta", venta);
            request.setAttribute("clientes", clientes);
            request.setAttribute("empleados", empleados);
            request.setAttribute("productos", productos);
            request.setAttribute("detalles", detalles); 

            
            request.getRequestDispatcher("/WEB-INF/views/ventas/form_template.jsp").forward(request, response);
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
            request.getRequestDispatcher("/WEB-INF/views/ventas/view_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("VentaServlet?error=Venta no encontrada");
        }
    }
    
    private void guardarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener datos de la venta desde el formulario
            int noFactura = Integer.parseInt(request.getParameter("noFactura"));
            String serie = request.getParameter("serie");
            LocalDate fechaFactura = LocalDate.parse(request.getParameter("fecha"));

            // Obtener idCliente del campo oculto
            String idClienteStr = request.getParameter("idCliente");
            if (idClienteStr == null || idClienteStr.isEmpty()) {
                response.sendRedirect("VentaServlet?error=Debe seleccionar un cliente");
                return;
            }

            int idCliente = Integer.parseInt(idClienteStr);

            // Obtener idEmpleado (por ahora usar 1 como empleado por defecto)
            int idEmpleado = 1;
            String idEmpleadoStr = request.getParameter("idEmpleado");
            if (idEmpleadoStr != null && !idEmpleadoStr.isEmpty()) {
                idEmpleado = Integer.parseInt(idEmpleadoStr);
            }

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
                        detalle.setCantidad(cantidades[i]); 
                        detalle.setPrecioUnitario(Double.parseDouble(precios[i]));
                        venta.agregarDetalle(detalle);
                    }
                }
            }

            if (!venta.tieneDetalles()) {
                response.sendRedirect("VentaServlet?error=Debe agregar al menos un producto");
                return;
            }

            // Usar el método crearVenta() que implementa la lógica del C#
            int idVenta = ventaDAO.crearVenta(venta);
            if (idVenta > 0) {
                response.sendRedirect("VentaServlet?success=Venta creada exitosamente");
            } else {
                response.sendRedirect("VentaServlet?error=Error al crear la venta");
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
        
        // CORRECCIÓN: El parámetro se llama "fecha", no "fechaFactura"
        String fechaStr = request.getParameter("fecha");
        LocalDate fechaFactura = null;
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            fechaFactura = LocalDate.parse(fechaStr);
        } else {
            fechaFactura = LocalDate.now(); // O maneja el error como prefieras
        }
        
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        // CORRECCIÓN: Si no tienes idEmpleado en el formulario, obténlo de la sesión o usa un valor por defecto
        HttpSession session = request.getSession();
        Integer idEmpleadoSession = (Integer) session.getAttribute("idEmpleado");
        int idEmpleado = (idEmpleadoSession != null) ? idEmpleadoSession : 1; // Valor por defecto si no hay sesión
        
        Venta venta = new Venta(noFactura, serie, fechaFactura, idCliente, idEmpleado);
        venta.setIdVenta(idVenta);
        
        // CORRECCIÓN: También necesitas procesar los detalles de la venta
        String[] idProductos = request.getParameterValues("idProducto");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] preciosUnitarios = request.getParameterValues("precioUnitario");
        String[] idVentaDetalles = request.getParameterValues("idVentaDetalle");

        // Debug: Imprimir los valores recibidos
        System.out.println("DEBUG - idVentaDetalles recibidos: " + java.util.Arrays.toString(idVentaDetalles));
        System.out.println("DEBUG - idProductos recibidos: " + java.util.Arrays.toString(idProductos));
        System.out.println("DEBUG - cantidades recibidas: " + java.util.Arrays.toString(cantidades));
        System.out.println("DEBUG - preciosUnitarios recibidos: " + java.util.Arrays.toString(preciosUnitarios));

        List<VentaDetalle> detalles = new ArrayList<>();
        if (idProductos != null && idProductos.length > 0) {
            for (int i = 0; i < idProductos.length; i++) {
                // Validar y limpiar valores
                String idProductoStr = idProductos[i] != null ? idProductos[i].trim() : "";
                String cantidadStr = cantidades != null && i < cantidades.length ? cantidades[i].trim() : "";
                String precioStr = preciosUnitarios != null && i < preciosUnitarios.length ? preciosUnitarios[i].trim() : "";

                // Saltar filas vacías
                if (idProductoStr.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
                    System.out.println("DEBUG - Saltando fila " + i + " por valores vacíos");
                    continue;
                }

                try {
                    int idProducto = Integer.parseInt(idProductoStr);
                    String cantidad = cantidadStr;
                    double precioUnitario = Double.parseDouble(precioStr);

                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setIdVenta(idVenta);
                    detalle.setIdProducto(idProducto);
                    detalle.setCantidad(cantidad);
                    detalle.setPrecioUnitario(precioUnitario);

                    // Si existe idVentaDetalle, es un detalle existente que se está editando
                    if (idVentaDetalles != null && i < idVentaDetalles.length) {
                        String idVentaDetalleStr = idVentaDetalles[i] != null ? idVentaDetalles[i].trim() : "";
                        if (!idVentaDetalleStr.isEmpty()) {
                            int idVentaDetalle = Integer.parseInt(idVentaDetalleStr);
                            detalle.setIdVentaDetalle(idVentaDetalle);
                            System.out.println("DEBUG - Detalle " + i + " con idVentaDetalle: " + idVentaDetalle);
                        }
                    }

                    detalles.add(detalle);
                    System.out.println("DEBUG - Detalle " + i + " agregado: idProducto=" + idProducto + ", cantidad=" + cantidad + ", precio=" + precioUnitario);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG - Error parseando detalle " + i + ": " + e.getMessage());
                    System.out.println("DEBUG - Valores: idProducto='" + idProductoStr + "', cantidad='" + cantidadStr + "', precio='" + precioStr + "'");
                    throw e;
                }
            }
        }

        if (detalles.isEmpty()) {
            throw new Exception("No hay detalles válidos para la venta");
        }

        venta.setDetalles(detalles);
        
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
    /*
    private void actualizarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idVenta = Integer.parseInt(request.getParameter("idVenta"));
            int noFactura = Integer.parseInt(request.getParameter("noFactura"));
            String serie = request.getParameter("serie");
            LocalDate fechaFactura = LocalDate.parse(request.getParameter("fecha"));
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
   */ 
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
