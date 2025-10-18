package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.dao.MarcaDAO;
import com.sistemaempresa.models.Producto;
import com.sistemaempresa.models.Marca;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Producto
 */
public class ProductoServlet extends HttpServlet {
    
    private ProductoDAO productoDAO;
    private MarcaDAO marcaDAO;
    
    @Override
    public void init() throws ServletException {
        productoDAO = new ProductoDAO();
        marcaDAO = new MarcaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listarProductos(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarProducto(request, response);
                break;
            case "search":
                buscarProductos(request, response);
                break;
            case "buscarAjax":
                buscarProductosAjax(request, response);
                break;
            case "obtenerTodos":
                obtenerTodosProductos(request, response);
                break;
            default:
                listarProductos(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarProducto(request, response);
        } else if ("update".equals(action)) {
            actualizarProducto(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = productoDAO.obtenerTodos();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/WEB-INF/views/productos/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Marca> marcas = marcaDAO.obtenerTodos();
        request.setAttribute("marcas", marcas);
        request.getRequestDispatcher("/WEB-INF/views/productos/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProducto = Integer.parseInt(request.getParameter("id"));
        Producto producto = productoDAO.obtenerPorId(idProducto);
        List<Marca> marcas = marcaDAO.obtenerTodos();
        
        if (producto != null) {
            request.setAttribute("producto", producto);
            request.setAttribute("marcas", marcas);
            request.getRequestDispatcher("/WEB-INF/views/productos/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("ProductoServlet?action=list&error=Producto no encontrado");
        }
    }
    
    private void guardarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Producto producto = new Producto();
            producto.setProducto(request.getParameter("producto"));
            producto.setIdMarca(Integer.parseInt(request.getParameter("idMarca")));
            producto.setDescripcion(request.getParameter("descripcion"));
            producto.setImagen(request.getParameter("imagen"));
            producto.setPrecioCosto(new BigDecimal(request.getParameter("precioCosto")));
            producto.setPrecioVenta(new BigDecimal(request.getParameter("precioVenta")));
            producto.setExistencia(Integer.parseInt(request.getParameter("existencia")));
            
            if (productoDAO.insertar(producto)) {
                response.sendRedirect("ProductoServlet?action=list&success=Producto guardado correctamente");
            } else {
                response.sendRedirect("ProductoServlet?action=new&error=Error al guardar producto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductoServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Producto producto = new Producto();
            producto.setIdProducto(Integer.parseInt(request.getParameter("idProducto")));
            producto.setProducto(request.getParameter("producto"));
            producto.setIdMarca(Integer.parseInt(request.getParameter("idMarca")));
            producto.setDescripcion(request.getParameter("descripcion"));
            producto.setImagen(request.getParameter("imagen"));
            producto.setPrecioCosto(new BigDecimal(request.getParameter("precioCosto")));
            producto.setPrecioVenta(new BigDecimal(request.getParameter("precioVenta")));
            producto.setExistencia(Integer.parseInt(request.getParameter("existencia")));
            
            if (productoDAO.actualizar(producto)) {
                response.sendRedirect("ProductoServlet?action=list&success=Producto actualizado correctamente");
            } else {
                response.sendRedirect("ProductoServlet?action=edit&id=" + producto.getIdProducto() + "&error=Error al actualizar producto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductoServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idProducto = Integer.parseInt(request.getParameter("id"));
            
            if (productoDAO.eliminar(idProducto)) {
                response.sendRedirect("ProductoServlet?action=list&success=Producto eliminado correctamente");
            } else {
                response.sendRedirect("ProductoServlet?action=list&error=Error al eliminar producto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProductoServlet?action=list&error=Error al eliminar producto");
        }
    }
    
    private void buscarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String termino = request.getParameter("termino");
        List<Producto> productos;

        if (termino != null && !termino.trim().isEmpty()) {
            productos = productoDAO.buscar(termino);
        } else {
            productos = productoDAO.obtenerTodos();
        }

        request.setAttribute("productos", productos);
        request.setAttribute("termino", termino);
        request.getRequestDispatcher("/WEB-INF/views/productos/list_template.jsp").forward(request, response);
    }

    /**
     * Endpoint AJAX para buscar productos y retornar JSON
     */
    private void buscarProductosAjax(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String termino = request.getParameter("termino");
        List<Producto> productos;

        if (termino != null && !termino.trim().isEmpty()) {
            productos = productoDAO.buscar(termino);
        } else {
            productos = productoDAO.obtenerTodos();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            json.append("{");
            json.append("\"idProducto\":").append(p.getIdProducto()).append(",");
            json.append("\"id_producto\":").append(p.getIdProducto()).append(",");
            json.append("\"producto\":\"").append(escapeJson(p.getProducto())).append("\",");
            json.append("\"descripcion\":\"").append(escapeJson(p.getDescripcion())).append("\",");
            json.append("\"existencia\":").append(p.getExistencia()).append(",");
            json.append("\"precio_costo\":").append(p.getPrecioCosto()).append(",");
            json.append("\"precio_venta\":").append(p.getPrecioVenta()).append(",");
            json.append("\"precio_unitario\":").append(p.getPrecioVenta()).append(",");
            json.append("\"nombreMarca\":\"").append(escapeJson(p.getNombreMarca() != null ? p.getNombreMarca() : "")).append("\"");
            json.append("}");
            if (i < productos.size() - 1) json.append(",");
        }
        json.append("]");
        response.getWriter().write(json.toString());
    }

    /**
     * Endpoint AJAX para obtener todos los productos en tabla modal
     */
    private void obtenerTodosProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Producto> productos = productoDAO.obtenerTodos();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            json.append("{");
            json.append("\"id_producto\":").append(p.getIdProducto()).append(",");
            json.append("\"producto\":\"").append(escapeJson(p.getProducto())).append("\",");
            json.append("\"existencia\":").append(p.getExistencia()).append(",");
            json.append("\"precio_costo\":").append(p.getPrecioCosto()).append(",");
            json.append("\"precio_venta\":").append(p.getPrecioVenta()).append("\"");
            json.append("}");
            if (i < productos.size() - 1) json.append(",");
        }
        json.append("]");
        response.getWriter().write(json.toString());
    }

    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
