package com.puntoventa.servlets;

import com.puntoventa.dao.ProductoDAO;
import com.puntoventa.dao.MarcaDAO;
import com.puntoventa.models.Producto;
import com.puntoventa.models.Marca;
import com.puntoventa.models.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet para manejo de productos
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {
    
    private ProductoDAO productoDAO;
    private MarcaDAO marcaDAO;
    
    @Override
    public void init() throws ServletException {
        productoDAO = new ProductoDAO();
        marcaDAO = new MarcaDAO();
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
        String action = request.getParameter("action");
        
        try {
            if ("list".equals(action) || action == null) {
                listarProductos(request, response);
            } else if ("form".equals(action)) {
                mostrarFormulario(request, response);
            } else if ("view".equals(action)) {
                verProducto(request, response);
            } else if ("search".equals(action)) {
                buscarProductos(request, response);
            } else if ("searchByBarcode".equals(action)) {
                buscarPorCodigoBarras(request, response);
            } else if ("stockBajo".equals(action)) {
                listarProductosStockBajo(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
            
        } catch (Exception e) {
            System.err.println("Error en ProductoServlet GET: " + e.getMessage());
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
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                crearProducto(request, response);
            } else if ("update".equals(action)) {
                actualizarProducto(request, response);
            } else if ("delete".equals(action)) {
                eliminarProducto(request, response);
            } else if ("updateStock".equals(action)) {
                actualizarStock(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
            
        } catch (Exception e) {
            System.err.println("Error en ProductoServlet POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"success\": false, \"message\": \"Error interno del servidor\"}");
        }
    }
    
    /**
     * Lista todos los productos
     */
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = productoDAO.obtenerTodos();
        List<Marca> marcas = marcaDAO.obtenerTodas();
        
        request.setAttribute("productos", productos);
        request.setAttribute("marcas", marcas);
        request.setAttribute("titulo", "Gestión de Productos");
        
        request.getRequestDispatcher("/WEB-INF/views/productos/list.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear/editar producto
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        Producto producto = null;
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                producto = productoDAO.obtenerPorId(id);
                if (producto == null) {
                    request.setAttribute("error", "Producto no encontrado");
                    listarProductos(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de producto inválido");
                listarProductos(request, response);
                return;
            }
        }
        
        List<Marca> marcas = marcaDAO.obtenerTodas();
        
        request.setAttribute("producto", producto);
        request.setAttribute("marcas", marcas);
        request.setAttribute("titulo", producto == null ? "Nuevo Producto" : "Editar Producto");
        
        request.getRequestDispatcher("/WEB-INF/views/productos/form.jsp").forward(request, response);
    }
    
    /**
     * Muestra los detalles de un producto
     */
    private void verProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "ID de producto requerido");
            listarProductos(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            Producto producto = productoDAO.obtenerPorId(id);
            
            if (producto == null) {
                request.setAttribute("error", "Producto no encontrado");
                listarProductos(request, response);
                return;
            }
            
            request.setAttribute("producto", producto);
            request.setAttribute("titulo", "Detalles del Producto");
            
            request.getRequestDispatcher("/WEB-INF/views/productos/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de producto inválido");
            listarProductos(request, response);
        }
    }
    
    /**
     * Busca productos por término
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
                .append("\"existencia\": ").append(producto.getExistencia())
                .append("}");
        }
        
        json.append("]");
        response.getWriter().print(json.toString());
    }
    
    /**
     * Busca producto por código de barras
     */
    private void buscarPorCodigoBarras(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codigoBarras = request.getParameter("codigo");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            response.getWriter().print("{\"success\": false, \"message\": \"Código de barras requerido\"}");
            return;
        }
        
        Producto producto = productoDAO.obtenerPorCodigoBarras(codigoBarras.trim());
        
        if (producto != null) {
            String json = String.format(
                "{\"success\": true, \"producto\": {" +
                "\"id\": %d, \"nombre\": \"%s\", \"codigoBarras\": \"%s\", " +
                "\"marca\": \"%s\", \"precio\": %.2f, \"existencia\": %d}}",
                producto.getIdProducto(),
                escapeJson(producto.getProducto()),
                escapeJson(producto.getCodigoBarras()),
                escapeJson(producto.getNombreMarca()),
                producto.getPrecioVenta(),
                producto.getExistencia()
            );
            response.getWriter().print(json);
        } else {
            response.getWriter().print("{\"success\": false, \"message\": \"Producto no encontrado\"}");
        }
    }
    
    /**
     * Lista productos con stock bajo
     */
    private void listarProductosStockBajo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = productoDAO.obtenerProductosStockBajo();
        
        request.setAttribute("productos", productos);
        request.setAttribute("titulo", "Productos con Stock Bajo");
        
        request.getRequestDispatcher("/WEB-INF/views/productos/stock_bajo.jsp").forward(request, response);
    }
    
    /**
     * Crea un nuevo producto
     */
    private void crearProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Validar parámetros
            String nombre = request.getParameter("nombre");
            String codigoBarras = request.getParameter("codigoBarras");
            String idMarcaStr = request.getParameter("idMarca");
            String descripcion = request.getParameter("descripcion");
            String imagen = request.getParameter("imagen");
            String precioCostoStr = request.getParameter("precioCosto");
            String precioVentaStr = request.getParameter("precioVenta");
            String existenciaStr = request.getParameter("existencia");
            String stockMinimoStr = request.getParameter("stockMinimo");
            
            if (nombre == null || nombre.trim().isEmpty() ||
                codigoBarras == null || codigoBarras.trim().isEmpty() ||
                idMarcaStr == null || precioCostoStr == null || precioVentaStr == null) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Campos requeridos faltantes\"}");
                return;
            }
            
            // Verificar si el código de barras ya existe
            if (productoDAO.existeCodigoBarras(codigoBarras.trim(), 0)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("{\"success\": false, \"message\": \"El código de barras ya existe\"}");
                return;
            }
            
            // Crear producto
            Producto producto = new Producto();
            producto.setProducto(nombre.trim());
            producto.setCodigoBarras(codigoBarras.trim());
            producto.setIdMarca(Integer.parseInt(idMarcaStr));
            producto.setDescripcion(descripcion != null ? descripcion.trim() : "");
            producto.setImagen(imagen != null ? imagen.trim() : "");
            producto.setPrecioCosto(new BigDecimal(precioCostoStr));
            producto.setPrecioVenta(new BigDecimal(precioVentaStr));
            producto.setExistencia(existenciaStr != null ? Integer.parseInt(existenciaStr) : 0);
            producto.setStockMinimo(stockMinimoStr != null ? Integer.parseInt(stockMinimoStr) : 5);
            producto.setActivo(true);
            
            int id = productoDAO.crear(producto);
            
            if (id > 0) {
                out.print("{\"success\": true, \"message\": \"Producto creado exitosamente\", \"id\": " + id + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al crear el producto\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Formato de número inválido\"}");
        }
    }
    
    /**
     * Actualiza un producto existente
     */
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID de producto requerido\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            Producto producto = productoDAO.obtenerPorId(id);
            
            if (producto == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"success\": false, \"message\": \"Producto no encontrado\"}");
                return;
            }
            
            // Actualizar campos
            String nombre = request.getParameter("nombre");
            String codigoBarras = request.getParameter("codigoBarras");
            String idMarcaStr = request.getParameter("idMarca");
            String descripcion = request.getParameter("descripcion");
            String imagen = request.getParameter("imagen");
            String precioCostoStr = request.getParameter("precioCosto");
            String precioVentaStr = request.getParameter("precioVenta");
            String existenciaStr = request.getParameter("existencia");
            String stockMinimoStr = request.getParameter("stockMinimo");
            
            // Verificar código de barras duplicado
            if (codigoBarras != null && !codigoBarras.equals(producto.getCodigoBarras())) {
                if (productoDAO.existeCodigoBarras(codigoBarras.trim(), id)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.print("{\"success\": false, \"message\": \"El código de barras ya existe\"}");
                    return;
                }
            }
            
            if (nombre != null) producto.setProducto(nombre.trim());
            if (codigoBarras != null) producto.setCodigoBarras(codigoBarras.trim());
            if (idMarcaStr != null) producto.setIdMarca(Integer.parseInt(idMarcaStr));
            if (descripcion != null) producto.setDescripcion(descripcion.trim());
            if (imagen != null) producto.setImagen(imagen.trim());
            if (precioCostoStr != null) producto.setPrecioCosto(new BigDecimal(precioCostoStr));
            if (precioVentaStr != null) producto.setPrecioVenta(new BigDecimal(precioVentaStr));
            if (existenciaStr != null) producto.setExistencia(Integer.parseInt(existenciaStr));
            if (stockMinimoStr != null) producto.setStockMinimo(Integer.parseInt(stockMinimoStr));
            
            boolean actualizado = productoDAO.actualizar(producto);
            
            if (actualizado) {
                out.print("{\"success\": true, \"message\": \"Producto actualizado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al actualizar el producto\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Formato de número inválido\"}");
        }
    }
    
    /**
     * Elimina un producto
     */
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID de producto requerido\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            boolean eliminado = productoDAO.eliminar(id);
            
            if (eliminado) {
                out.print("{\"success\": true, \"message\": \"Producto eliminado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al eliminar el producto\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"ID de producto inválido\"}");
        }
    }
    
    /**
     * Actualiza el stock de un producto
     */
    private void actualizarStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String idStr = request.getParameter("id");
            String existenciaStr = request.getParameter("existencia");
            
            if (idStr == null || existenciaStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"ID y existencia requeridos\"}");
                return;
            }
            
            int id = Integer.parseInt(idStr);
            int existencia = Integer.parseInt(existenciaStr);
            
            boolean actualizado = productoDAO.actualizarExistencia(id, existencia);
            
            if (actualizado) {
                out.print("{\"success\": true, \"message\": \"Stock actualizado exitosamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"success\": false, \"message\": \"Error al actualizar el stock\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Formato de número inválido\"}");
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
