package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.MarcaDAO;
import com.sistemaempresa.models.Marca;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Marca
 */
public class MarcaServlet extends HttpServlet {
    
    private MarcaDAO marcaDAO;
    
    @Override
    public void init() throws ServletException {
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
                listarMarcas(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                // Operación no permitida - información sensible
                response.sendRedirect("MarcaServlet?action=list&error=No permitido");
                break;
            default:
                listarMarcas(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            guardarMarca(request, response);
        } else if ("update".equals(action)) {
            actualizarMarca(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarMarcas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Marca> marcas = marcaDAO.obtenerTodos();
        request.setAttribute("marcas", marcas);
        request.getRequestDispatcher("/WEB-INF/views/marcas/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/marcas/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idMarca = Integer.parseInt(request.getParameter("id"));
        Marca marca = marcaDAO.obtenerPorId(idMarca);
        
        if (marca != null) {
            request.setAttribute("marca", marca);
            request.getRequestDispatcher("/WEB-INF/views/marcas/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("MarcaServlet?action=list&error=Marca no encontrada");
        }
    }
    
    private void guardarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Marca marca = new Marca();
            marca.setMarca(request.getParameter("marca"));
            
            if (marcaDAO.insertar(marca)) {
                response.sendRedirect("MarcaServlet?action=list&success=Marca guardada correctamente");
            } else {
                response.sendRedirect("MarcaServlet?action=new&error=Error al guardar marca");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MarcaServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Marca marca = new Marca();
            marca.setIdMarca(Integer.parseInt(request.getParameter("idMarca")));
            marca.setMarca(request.getParameter("marca"));
            
            if (marcaDAO.actualizar(marca)) {
                response.sendRedirect("MarcaServlet?action=list&success=Marca actualizada correctamente");
            } else {
                response.sendRedirect("MarcaServlet?action=edit&id=" + marca.getIdMarca() + "&error=Error al actualizar marca");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MarcaServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarMarca(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idMarca = Integer.parseInt(request.getParameter("id"));
            
            if (marcaDAO.eliminar(idMarca)) {
                response.sendRedirect("MarcaServlet?action=list&success=Marca eliminada correctamente");
            } else {
                response.sendRedirect("MarcaServlet?action=list&error=Error al eliminar marca");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MarcaServlet?action=list&error=Error al eliminar marca");
        }
    }
}
