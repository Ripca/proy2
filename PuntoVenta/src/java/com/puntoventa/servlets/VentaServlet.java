package com.puntoventa.servlets;

import com.puntoventa.dao.*;
import com.puntoventa.models.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet para manejo de ventas
 */
@WebServlet(name = "VentaServlet", urlPatterns = {"/ventas"})
public class VentaServlet extends HttpServlet {
    
    private VentaDAO ventaDAO;
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private EmpleadoDAO empleadoDAO;
    
    @Override
    public void init() throws ServletException {
        ventaDAO = new VentaDAO();
        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        empleadoDAO = new EmpleadoDAO();
    }
    
    /**
     * Maneja las peticiones GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar autenticación
        if (!LoginServlet.verificarAutenticacion(request, response)) {
            return;
        }
        
        Usuario usuarioActual = LoginServlet.obtenerUsuarioActual(request);
        
        // Verificar permisos de venta
        if (!usuarioActual.puedeVender()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a ventas");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("list".equals(action) || action == null) {
                listarVentas(request, response);
            } else if ("form".equals(action)) {
                mostrarFormularioVenta(request, response);
            } else if ("view".equals(action)) {
                verVenta(request, response);
            } else if ("searchClientes".equals(action)) {
                buscarClientes(request, response);
            } else if ("searchProductos".equals(action)) {
                buscarProductos(request, response);
            } else if ("getProductoByBarcode".equals(action)) {
                obtenerProductoPorCodigoBarras(request, response);
            } else if ("getSiguienteFactura".equals(action)) {
                obtenerSiguienteNumeroFactura(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
            
        } catch (Exception e) {
            System.err.println("Error en VentaServlet GET: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja las peticiones POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar autenticación
        if (!LoginServlet.verificarAutenticacion(request, response)) {
            return;
        }
        
        Usuario usuarioActual = LoginServlet.obtenerUsuarioActual(request);
        
        // Verificar permisos de venta
        if (!usuarioActual.puedeVender()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("{\"success\": false, \"message\": \"No tiene permisos para realizar ventas\"}");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                crearVenta(request, response, usuarioActual);
            } else if ("cancel".equals(action)) {
                cancelarVenta(request, response);
            } else if ("updateEstado".equals(action)) {
                actualizarEstadoVenta(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
            
        } catch (Exception e) {
            System.err.println("Error en VentaServlet POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"success\": false, \"message\": \"Error interno del servidor\"}");
        }
    }
    
    /**
     * Lista todas las ventas
     */
    private void listarVentas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Venta> ventas = ventaDAO.obtenerTodas();
        
        request.setAttribute("ventas", ventas);
        request.setAttribute("titulo", "Gestión de Ventas");
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/list.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear una nueva venta
     */
    private void mostrarFormularioVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener siguiente número de factura
        int siguienteFactura = ventaDAO.obtenerSiguienteNumeroFactura("A");
        
        request.setAttribute("siguienteFactura", siguienteFactura);
        request.setAttribute("serie", "A");
        request.setAttribute("fechaActual", LocalDate.now());
        request.setAttribute("titulo", "Nueva Venta");
        
        request.getRequestDispatcher("/WEB-INF/views/ventas/form.jsp").forward(request, response);
    }
    
    /**
     * Muestra los detalles de una venta
     */
    private void verVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "ID de venta requerido");
            listarVentas(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Venta venta = ventaDAO.obtenerPorId(id);
            
            if (venta == null) {
                request.setAttribute("error", "Venta no encontrada");
                listarVentas(request, response);
                return;
            }
            
            request.setAttribute("venta", venta);
            request.setAttribute("titulo", "Detalles de Venta #" + venta.getNoFactura());
            
            request.getRequestDispatcher("/WEB-INF/views/ventas/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de venta inválido");
            listarVentas(request, response);
        }
    }
    
    /**
     * Busca clientes para el modal
     */
    private void buscarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String termino = request.getParameter("q");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (termino == null || termino.trim().isEmpty()) {
            response.getWriter().print("[]");
            return;
        }
        
        List<Cliente> clientes = clienteDAO.buscar(termino.trim());
        
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            if (i > 0) json.append(",");
            
            json.append("{")
                .append("\"id\": ").append(cliente.getIdCliente()).append(",")
                .append("\"nit\": \"").append(escapeJson(cliente.getNit())).append("\",")
                .append("\"nombre\": \"").append(escapeJson(cliente.getNombres() + " " + cliente.getApellidos())).append("\",")
                .append("\"direccion\": \"").append(escapeJson(cliente.getDireccion())).append("\",")
                .append("\"telefono\": \"").append(escapeJson(cliente.getTelefono())).append("\"")
                .append("}");
        }
        
        json.append("]");
        response.getWriter().print(json.toString());
    }
    
    /**
     * Busca productos para el modal
     */
    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String termino = request.getParameter("q");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (termino == null || termino.trim().isEmpty()) {
            response.getWriter().print("[]");
            return;
        }
        
        List<Producto> productos = productoDAO.buscar(termino.trim());
        
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            if (i > 0) json.append(",");
            
            json.append("{")
                .append("\"id\": ").append(producto.getIdProducto()).append(",")
                .append("\"nombre\": \"").append(escapeJson(producto.getProducto())).append("\",")
                .append("\"codigoBarras\": \"").append(escapeJson(producto.getCodigoBarras())).append("\",")
                .append("\"marca\": \"").append(escapeJson(producto.getNombreMarca())).append("\",")
                .append("\"precio\": ").append(producto.getPrecioVenta()).append(",")
                .append("\"existencia\": ").append(producto.getExistencia()).append(",")
                .append("\"stockMinimo\": ").append(producto.isStockMinimo())
                .append("}");
        }
        
        json.append("]");
        response.getWriter().print(json.toString());
    }
    
    /**
     * Obtiene producto por código de barras para el scanner
     */
    private void obtenerProductoPorCodigoBarras(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codigo = request.getParameter("codigo");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (codigo == null || codigo.trim().isEmpty()) {
            response.getWriter().print("{\"success\": false, \"message\": \"Código de barras requerido\"}");
            return;
        }
        
        // Procesar formato "cantidad*codigo" o solo "codigo"
        int cantidad = 1;
        String codigoBarras = codigo.trim();
        
        if (codigo.contains("*")) {
            String[] partes = codigo.split("\\*", 2);
            try {
                cantidad = Integer.parseInt(partes[0]);
                codigoBarras = partes[1];
            } catch (NumberFormatException e) {
                response.getWriter().print("{\"success\": false, \"message\": \"Formato de código inválido\"}");
                return;
            }
        }
        
        Producto producto = productoDAO.obtenerPorCodigoBarras(codigoBarras);
        
        if (producto != null) {
            if (producto.getExistencia() < cantidad) {
                response.getWriter().print("{\"success\": false, \"message\": \"Stock insuficiente\"}");
                return;
            }
            
            String json = String.format(
                "{\"success\": true, \"producto\": {" +
                "\"id\": %d, \"nombre\": \"%s\", \"codigoBarras\": \"%s\", " +
                "\"marca\": \"%s\", \"precio\": %.2f, \"existencia\": %d, \"cantidad\": %d}}",
                producto.getIdProducto(),
                escapeJson(producto.getProducto()),
                escapeJson(producto.getCodigoBarras()),
                escapeJson(producto.getNombreMarca()),
                producto.getPrecioVenta(),
                producto.getExistencia(),
                cantidad
            );
            response.getWriter().print(json);
        } else {
            response.getWriter().print("{\"success\": false, \"message\": \"Producto no encontrado\"}");
        }
    }
    
    /**
     * Obtiene el siguiente número de factura
     */
    private void obtenerSiguienteNumeroFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String serie = request.getParameter("serie");
        if (serie == null || serie.isEmpty()) {
            serie = "A";
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        int siguienteFactura = ventaDAO.obtenerSiguienteNumeroFactura(serie);
        
        response.getWriter().print("{\"siguienteFactura\": " + siguienteFactura + "}");
    }
    
    /**
     * Crea una nueva venta
     */
    private void crearVenta(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Obtener parámetros de la venta
            String noFacturaStr = request.getParameter("noFactura");
            String serie = request.getParameter("serie");
            String fechaFacturaStr = request.getParameter("fechaFactura");
            String idClienteStr = request.getParameter("idCliente");
            String subtotalStr = request.getParameter("subtotal");
            String descuentoStr = request.getParameter("descuento");
            String totalStr = request.getParameter("total");
            String metodoPago = request.getParameter("metodoPago");
            String observaciones = request.getParameter("observaciones");
            
            // Validar parámetros requeridos
            if (noFacturaStr == null || serie == null || fechaFacturaStr == null ||
                idClienteStr == null || subtotalStr == null || totalStr == null || metodoPago == null) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Parámetros requeridos faltantes\"}");
                return;
            }
            
            // Crear objeto venta
            Venta venta = new Venta();
            venta.setNoFactura(Integer.parseInt(noFacturaStr));
            venta.setSerie(serie);
            venta.setFechaFactura(LocalDate.parse(fechaFacturaStr));
            venta.setIdCliente(Integer.parseInt(idClienteStr));
            venta.setIdEmpleado(usuario.getIdEmpleado());
            venta.setSubtotal(new BigDecimal(subtotalStr));
            venta.setDescuento(descuentoStr != null ? new BigDecimal(descuentoStr) : BigDecimal.ZERO);
            venta.setTotal(new BigDecimal(totalStr));
            venta.setMetodoPago(metodoPago);
            venta.setEstado("COMPLETADA");
            venta.setObservaciones(observaciones != null ? observaciones : "");
            venta.setIdUsuario(usuario.getIdUsuario());
            
            // Obtener detalles de la venta
            String[] productosIds = request.getParameterValues("detalles[idProducto]");
            String[] cantidades = request.getParameterValues("detalles[cantidad]");
            String[] precios = request.getParameterValues("detalles[precio]");
            String[] descuentosDetalle = request.getParameterValues("detalles[descuento]");
            
            if (productosIds == null || cantidades == null || precios == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Detalles de venta requeridos\"}");
                return;
            }
            
            List<VentaDetalle> detalles = new ArrayList<>();
            for (int i = 0; i < productosIds.length; i++) {
                VentaDetalle detalle = new VentaDetalle();
                detalle.setIdProducto(Integer.parseInt(productosIds[i]));
                detalle.setCantidad(Integer.parseInt(cantidades[i]));
                detalle.setPrecioUnitario(new BigDecimal(precios[i]));
                detalle.setDescuento(descuentosDetalle != null && i < descuentosDetalle.length ? 
                    new BigDecimal(descuentosDetalle[i]) : BigDecimal.ZERO);
                
                // Calcular subtotal del detalle
                BigDecimal subtotalDetalle = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()))
                    .subtract(detalle.getDescuento());
                detalle.setSubtotal(subtotalDetalle);
                
                detalles.add(detalle);
            }
            
            venta.setDetalles(detalles);
            
            // Crear la venta
            int idVenta = ventaDAO.crear(venta);
            
            if (idVenta > 0) {
                out.print("{\"success\": true, \"message\": \"Venta creada exitosamente\", \"idVenta\": " + idVenta + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al crear la venta\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Formato de número inválido\"}");
        } catch (Exception e) {
            System.err.println("Error al crear venta: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno: " + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Cancela una venta
     */
    private void cancelarVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID de venta requerido\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            boolean cancelada = ventaDAO.eliminar(id); // Método eliminar cancela y restaura stock
            
            if (cancelada) {
                out.print("{\"success\": true, \"message\": \"Venta cancelada exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al cancelar la venta\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"ID de venta inválido\"}");
        }
    }
    
    /**
     * Actualiza el estado de una venta
     */
    private void actualizarEstadoVenta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            String nuevoEstado = request.getParameter("estado");
            
            if (idStr == null || nuevoEstado == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID y estado requeridos\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            boolean actualizado = ventaDAO.actualizarEstado(id, nuevoEstado);
            
            if (actualizado) {
                out.print("{\"success\": true, \"message\": \"Estado actualizado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al actualizar el estado\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"ID de venta inválido\"}");
        }
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
