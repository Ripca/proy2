package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.CompraDAO;
import com.sistemaempresa.dao.ProveedorDAO;
import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.models.Compra;
import com.sistemaempresa.models.CompraDetalle;
import com.sistemaempresa.models.Proveedor;
import com.sistemaempresa.models.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class CompraServlet extends HttpServlet {
    
    private CompraDAO compraDAO = new CompraDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
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
                listarCompras(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarCompra(request, response);
                break;
            case "view":
                verCompra(request, response);
                break;
            default:
                listarCompras(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarCompra(request, response);
        } else if ("update".equals(action)) {
            actualizarCompra(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarCompras(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Compra> compras = compraDAO.obtenerTodos();
        request.setAttribute("compras", compras);
        request.getRequestDispatcher("/WEB-INF/views/compras/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar datos para los dropdowns
        List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("productos", productos);
        
        request.getRequestDispatcher("/WEB-INF/views/compras/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idCompra = Integer.parseInt(request.getParameter("id"));
        Compra compra = compraDAO.obtenerPorId(idCompra);

        if (compra != null) {
            // Cargar datos para los dropdowns
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            List<Producto> productos = productoDAO.obtenerTodos();

            // Obtener los detalles de la compra
            List<CompraDetalle> detalles = compraDAO.obtenerDetallesPorCompra(idCompra);
            compra.setDetalles(detalles);

            request.setAttribute("compra", compra);
            request.setAttribute("proveedores", proveedores);
            request.setAttribute("productos", productos);
            request.setAttribute("detalles", detalles);

            request.getRequestDispatcher("/WEB-INF/views/compras/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("CompraServlet?error=Compra no encontrada");
        }
    }
    
    private void verCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idCompra = Integer.parseInt(request.getParameter("id"));
        Compra compra = compraDAO.obtenerPorId(idCompra);
        
        if (compra != null) {
            request.setAttribute("compra", compra);
            request.getRequestDispatcher("/WEB-INF/views/compras/view_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("CompraServlet?error=Compra no encontrada");
        }
    }
    
    private void guardarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener datos de la compra desde el formulario
            int noOrdenCompra = Integer.parseInt(request.getParameter("noOrdenCompra"));

            // El parámetro se llama "fechaOrden", no "fecha"
            String fechaStr = request.getParameter("fechaOrden");
            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                response.sendRedirect("CompraServlet?error=Debe ingresar la fecha de la orden");
                return;
            }
            LocalDate fechaOrden = LocalDate.parse(fechaStr);

            // Obtener idProveedor del campo oculto
            String idProveedorStr = request.getParameter("idProveedor");
            if (idProveedorStr == null || idProveedorStr.isEmpty()) {
                response.sendRedirect("CompraServlet?error=Debe seleccionar un proveedor");
                return;
            }

            int idProveedor = Integer.parseInt(idProveedorStr);

            Compra compra = new Compra(noOrdenCompra, fechaOrden, idProveedor);

            // Obtener detalles de la compra
            String[] productosIds = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] precios = request.getParameterValues("precioCostoUnitario");

            System.out.println("DEBUG - Guardando compra: noOrden=" + noOrdenCompra + ", fecha=" + fechaOrden + ", idProveedor=" + idProveedor);
            System.out.println("DEBUG - idProductos: " + java.util.Arrays.toString(productosIds));
            System.out.println("DEBUG - cantidades: " + java.util.Arrays.toString(cantidades));
            System.out.println("DEBUG - precios: " + java.util.Arrays.toString(precios));

            if (productosIds != null && cantidades != null && precios != null) {
                for (int i = 0; i < productosIds.length; i++) {
                    String idProductoStr = productosIds[i] != null ? productosIds[i].trim() : "";
                    String cantidadStr = cantidades != null && i < cantidades.length ? cantidades[i].trim() : "";
                    String precioStr = precios != null && i < precios.length ? precios[i].trim() : "";

                    // Validar que no sean "undefined" ni valores vacíos
                    if (!idProductoStr.isEmpty() && !cantidadStr.isEmpty() && !precioStr.isEmpty() &&
                        !"undefined".equals(idProductoStr) && !"undefined".equals(cantidadStr) && !"undefined".equals(precioStr)) {
                        try {
                            CompraDetalle detalle = new CompraDetalle();
                            detalle.setIdProducto(Integer.parseInt(idProductoStr));
                            detalle.setCantidad(Integer.parseInt(cantidadStr));
                            detalle.setPrecioCostoUnitario(Double.parseDouble(precioStr));
                            compra.agregarDetalle(detalle);
                            System.out.println("DEBUG - Detalle agregado: idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                        } catch (NumberFormatException e) {
                            System.out.println("DEBUG - Error parseando detalle " + i + ": " + e.getMessage());
                            System.out.println("DEBUG - Valores: idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                        }
                    } else {
                        System.out.println("DEBUG - Saltando fila " + i + " por valores inválidos: idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                    }
                }
            }

            if (!compra.tieneDetalles()) {
                response.sendRedirect("CompraServlet?error=Debe agregar al menos un producto");
                return;
            }

            // Usar el método crear() que implementa la lógica del C#
            int idCompra = compraDAO.crear(compra);
            if (idCompra > 0) {
                response.sendRedirect("CompraServlet?success=Compra creada exitosamente");
            } else {
                response.sendRedirect("CompraServlet?error=Error al crear la compra");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void actualizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int idCompra = Integer.parseInt(request.getParameter("idCompra"));
            int noOrdenCompra = Integer.parseInt(request.getParameter("noOrdenCompra"));

            String fechaStr = request.getParameter("fechaOrden");
            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                response.sendRedirect("CompraServlet?error=Debe ingresar la fecha de la orden");
                return;
            }
            LocalDate fechaOrden = LocalDate.parse(fechaStr);

            int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));

            Compra compra = new Compra(noOrdenCompra, fechaOrden, idProveedor);
            compra.setIdCompra(idCompra);

            // Obtener detalles de la compra
            String[] productosIds = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] precios = request.getParameterValues("precioCostoUnitario");
            String[] detalleIds = request.getParameterValues("idCompraDetalle");

            System.out.println("DEBUG - Actualizando compra: idCompra=" + idCompra + ", noOrden=" + noOrdenCompra + ", fecha=" + fechaOrden + ", idProveedor=" + idProveedor);
            System.out.println("DEBUG - idProductos: " + java.util.Arrays.toString(productosIds));
            System.out.println("DEBUG - cantidades: " + java.util.Arrays.toString(cantidades));
            System.out.println("DEBUG - precios: " + java.util.Arrays.toString(precios));
            System.out.println("DEBUG - detalleIds: " + java.util.Arrays.toString(detalleIds));

            if (productosIds != null && cantidades != null && precios != null) {
                for (int i = 0; i < productosIds.length; i++) {
                    String idProductoStr = productosIds[i] != null ? productosIds[i].trim() : "";
                    String cantidadStr = cantidades != null && i < cantidades.length ? cantidades[i].trim() : "";
                    String precioStr = precios != null && i < precios.length ? precios[i].trim() : "";

                    // Validar que no sean "undefined" ni valores vacíos
                    if (!idProductoStr.isEmpty() && !cantidadStr.isEmpty() && !precioStr.isEmpty() &&
                        !"undefined".equals(idProductoStr) && !"undefined".equals(cantidadStr) && !"undefined".equals(precioStr)) {
                        try {
                            CompraDetalle detalle = new CompraDetalle();

                            // Si existe idCompraDetalle, es un detalle existente
                            if (detalleIds != null && i < detalleIds.length && detalleIds[i] != null) {
                                String idDetalleStr = detalleIds[i].trim();
                                if (!idDetalleStr.isEmpty() && !"undefined".equals(idDetalleStr)) {
                                    detalle.setIdCompraDetalle(Integer.parseInt(idDetalleStr));
                                }
                            }

                            detalle.setIdProducto(Integer.parseInt(idProductoStr));
                            detalle.setCantidad(Integer.parseInt(cantidadStr));
                            detalle.setPrecioCostoUnitario(Double.parseDouble(precioStr));
                            compra.agregarDetalle(detalle);
                            System.out.println("DEBUG - Detalle agregado: idCompraDetalle=" + detalle.getIdCompraDetalle() + ", idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                        } catch (NumberFormatException e) {
                            System.out.println("DEBUG - Error parseando detalle " + i + ": " + e.getMessage());
                            System.out.println("DEBUG - Valores: idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                        }
                    } else {
                        System.out.println("DEBUG - Saltando fila " + i + " por valores inválidos: idProducto=" + idProductoStr + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                    }
                }
            }

            if (!compra.tieneDetalles()) {
                response.sendRedirect("CompraServlet?error=Debe agregar al menos un producto");
                return;
            }

            if (compraDAO.actualizar(compra)) {
                response.sendRedirect("CompraServlet?success=Compra actualizada exitosamente");
            } else {
                response.sendRedirect("CompraServlet?error=Error al actualizar la compra");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void eliminarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCompra = Integer.parseInt(request.getParameter("id"));
            
            if (compraDAO.eliminar(idCompra)) {
                response.sendRedirect("CompraServlet?success=Compra eliminada exitosamente");
            } else {
                response.sendRedirect("CompraServlet?error=Error al eliminar la compra");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar la eliminación: " + e.getMessage());
        }
    }
}
