package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.ProveedorDAO;
import com.sistemaempresa.models.Proveedor;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Proveedor
 */
public class ProveedorServlet extends HttpServlet {
    
    private ProveedorDAO proveedorDAO;
    
    @Override
    public void init() throws ServletException {
        proveedorDAO = new ProveedorDAO();
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
                listarProveedores(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarProveedor(request, response);
                break;
            case "search":
                buscarProveedores(request, response);
                break;
            default:
                listarProveedores(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarProveedor(request, response);
        } else if ("update".equals(action)) {
            actualizarProveedor(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarProveedores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
        request.setAttribute("proveedores", proveedores);
        request.getRequestDispatcher("/WEB-INF/views/proveedores/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/proveedores/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idProveedor = Integer.parseInt(request.getParameter("id"));
        Proveedor proveedor = proveedorDAO.obtenerPorId(idProveedor);
        
        if (proveedor != null) {
            request.setAttribute("proveedor", proveedor);
            request.getRequestDispatcher("/WEB-INF/views/proveedores/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("ProveedorServlet?action=list&error=Proveedor no encontrado");
        }
    }
    
    private void guardarProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setProveedor(request.getParameter("proveedor"));
            proveedor.setNit(request.getParameter("nit"));
            proveedor.setDireccion(request.getParameter("direccion"));
            proveedor.setTelefono(request.getParameter("telefono"));
            
            if (proveedorDAO.insertar(proveedor)) {
                response.sendRedirect("ProveedorServlet?action=list&success=Proveedor guardado correctamente");
            } else {
                response.sendRedirect("ProveedorServlet?action=new&error=Error al guardar proveedor");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProveedorServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setIdProveedor(Integer.parseInt(request.getParameter("idProveedor")));
            proveedor.setProveedor(request.getParameter("proveedor"));
            proveedor.setNit(request.getParameter("nit"));
            proveedor.setDireccion(request.getParameter("direccion"));
            proveedor.setTelefono(request.getParameter("telefono"));
            
            if (proveedorDAO.actualizar(proveedor)) {
                response.sendRedirect("ProveedorServlet?action=list&success=Proveedor actualizado correctamente");
            } else {
                response.sendRedirect("ProveedorServlet?action=edit&id=" + proveedor.getIdProveedor() + "&error=Error al actualizar proveedor");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProveedorServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idProveedor = Integer.parseInt(request.getParameter("id"));
            
            if (proveedorDAO.eliminar(idProveedor)) {
                response.sendRedirect("ProveedorServlet?action=list&success=Proveedor eliminado correctamente");
            } else {
                response.sendRedirect("ProveedorServlet?action=list&error=Error al eliminar proveedor");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProveedorServlet?action=list&error=Error al eliminar proveedor");
        }
    }
    
    private void buscarProveedores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String termino = request.getParameter("termino");
        List<Proveedor> proveedores;
        
        if (termino != null && !termino.trim().isEmpty()) {
            proveedores = proveedorDAO.buscar(termino);
        } else {
            proveedores = proveedorDAO.obtenerTodos();
        }
        
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("termino", termino);
        request.getRequestDispatcher("/WEB-INF/views/proveedores/list_template.jsp").forward(request, response);
    }
}
