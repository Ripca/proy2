package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.ClienteDAO;
import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.dao.ProveedorDAO;
import com.sistemaempresa.dao.UsuarioDAO;
import com.sistemaempresa.models.Cliente;
import com.sistemaempresa.models.Producto;
import com.sistemaempresa.models.Proveedor;
import com.sistemaempresa.models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Servlet para búsquedas AJAX en tiempo real
 */
@WebServlet("/BusquedaServlet")
public class BusquedaServlet extends HttpServlet {
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String tipo = request.getParameter("tipo");
        String termino = request.getParameter("termino");
        
        PrintWriter out = response.getWriter();
        
        try {
            switch (tipo) {
                case "clientes":
                    buscarClientes(termino, out);
                    break;
                case "productos":
                    buscarProductos(termino, out);
                    break;
                case "proveedores":
                    buscarProveedores(termino, out);
                    break;
                case "usuarios":
                    buscarUsuarios(termino, out);
                    break;
                default:
                    out.print("{\"error\": \"Tipo de búsqueda no válido\"}");
            }
        } catch (Exception e) {
            out.print("{\"error\": \"Error en la búsqueda: " + e.getMessage() + "\"}");
        }
    }
    
    private void buscarClientes(String termino, PrintWriter out) {
        try {
            List<Cliente> clientes;
            if (termino == null || termino.trim().isEmpty()) {
                clientes = clienteDAO.obtenerTodos();
            } else {
                clientes = clienteDAO.buscar(termino);
            }
            
            // Convertir a formato JSON simplificado
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                if (i > 0) json.append(",");
                json.append("{")
                    .append("\"id\":").append(cliente.getIdCliente()).append(",")
                    .append("\"nombre\":\"").append(escapeJson(cliente.getNombreCompleto())).append("\",")
                    .append("\"nit\":\"").append(escapeJson(cliente.getNit() != null ? cliente.getNit() : "")).append("\",")
                    .append("\"telefono\":\"").append(escapeJson(cliente.getTelefono() != null ? cliente.getTelefono() : "")).append("\"")
                    .append("}");
            }
            json.append("]");
            
            out.print(json.toString());
        } catch (Exception e) {
            out.print("{\"error\": \"Error al buscar clientes: " + e.getMessage() + "\"}");
        }
    }
    
    private void buscarProductos(String termino, PrintWriter out) {
        try {
            List<Producto> productos;
            if (termino == null || termino.trim().isEmpty()) {
                productos = productoDAO.obtenerTodos();
            } else {
                productos = productoDAO.buscar(termino);
            }
            
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                if (i > 0) json.append(",");
                json.append("{")
                    .append("\"id\":").append(producto.getIdProducto()).append(",")
                    .append("\"nombre\":\"").append(escapeJson(producto.getProducto())).append("\",")
                    .append("\"marca\":\"").append(escapeJson(producto.getNombreMarca() != null ? producto.getNombreMarca() : "")).append("\",")
                    .append("\"precio\":").append(producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0).append(",")
                    .append("\"precioCosto\":").append(producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0).append(",")
                    .append("\"existencia\":").append(producto.getExistencia())
                    .append("}");
            }
            json.append("]");
            
            out.print(json.toString());
        } catch (Exception e) {
            out.print("{\"error\": \"Error al buscar productos: " + e.getMessage() + "\"}");
        }
    }
    
    private void buscarProveedores(String termino, PrintWriter out) {
        try {
            List<Proveedor> proveedores;
            if (termino == null || termino.trim().isEmpty()) {
                proveedores = proveedorDAO.obtenerTodos();
            } else {
                proveedores = proveedorDAO.buscar(termino);
            }
            
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < proveedores.size(); i++) {
                Proveedor proveedor = proveedores.get(i);
                if (i > 0) json.append(",");
                json.append("{")
                    .append("\"id\":").append(proveedor.getIdProveedor()).append(",")
                    .append("\"nombre\":\"").append(escapeJson(proveedor.getProveedor())).append("\",")
                    .append("\"nit\":\"").append(escapeJson(proveedor.getNit() != null ? proveedor.getNit() : "")).append("\",")
                    .append("\"telefono\":\"").append(escapeJson(proveedor.getTelefono() != null ? proveedor.getTelefono() : "")).append("\"")
                    .append("}");
            }
            json.append("]");
            
            out.print(json.toString());
        } catch (Exception e) {
            out.print("{\"error\": \"Error al buscar proveedores: " + e.getMessage() + "\"}");
        }
    }
    
    private void buscarUsuarios(String termino, PrintWriter out) {
        try {
            List<Usuario> usuarios;
            if (termino == null || termino.trim().isEmpty()) {
                usuarios = usuarioDAO.obtenerTodos();
            } else {
                usuarios = usuarioDAO.buscar(termino);
            }

            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuario = usuarios.get(i);
                if (i > 0) json.append(",");
                json.append("{")
                    .append("\"id\":").append(usuario.getIdUsuario()).append(",")
                    .append("\"usuario\":\"").append(escapeJson(usuario.getUsuario())).append("\",")
                    .append("\"nombre\":\"").append(escapeJson(usuario.getNombreCompleto())).append("\",")
                    .append("\"email\":\"").append(escapeJson(usuario.getEmail() != null ? usuario.getEmail() : "")).append("\",")
                    .append("\"rol\":\"").append(escapeJson(usuario.getRol())).append("\",")
                    .append("\"activo\":").append(usuario.isActivo())
                    .append("}");
            }
            json.append("]");

            out.print(json.toString());
        } catch (Exception e) {
            out.print("{\"error\": \"Error al buscar usuarios: " + e.getMessage() + "\"}");
        }
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
