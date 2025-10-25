package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.PuestoDAO;
import com.sistemaempresa.models.Puesto;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Puesto
 */
public class PuestoServlet extends HttpServlet {
    
    private PuestoDAO puestoDAO;
    
    @Override
    public void init() throws ServletException {
        puestoDAO = new PuestoDAO();
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
                listarPuestos(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                // Operación no permitida - información sensible
                response.sendRedirect("PuestoServlet?action=list&error=No permitido");
                break;
            default:
                listarPuestos(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            guardarPuesto(request, response);
        } else if ("update".equals(action)) {
            actualizarPuesto(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarPuestos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Puesto> puestos = puestoDAO.obtenerTodos();
        request.setAttribute("puestos", puestos);
        request.getRequestDispatcher("/WEB-INF/views/puestos/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/puestos/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idPuesto = Integer.parseInt(request.getParameter("id"));
        Puesto puesto = puestoDAO.obtenerPorId(idPuesto);
        
        if (puesto != null) {
            request.setAttribute("puesto", puesto);
            request.getRequestDispatcher("/WEB-INF/views/puestos/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("PuestoServlet?action=list&error=Puesto no encontrado");
        }
    }
    
    private void guardarPuesto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Puesto puesto = new Puesto();
            puesto.setPuesto(request.getParameter("puesto"));
            
            if (puestoDAO.insertar(puesto)) {
                response.sendRedirect("PuestoServlet?action=list&success=Puesto guardado correctamente");
            } else {
                response.sendRedirect("PuestoServlet?action=new&error=Error al guardar puesto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("PuestoServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarPuesto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Puesto puesto = new Puesto();
            puesto.setIdPuesto(Integer.parseInt(request.getParameter("idPuesto")));
            puesto.setPuesto(request.getParameter("puesto"));
            
            if (puestoDAO.actualizar(puesto)) {
                response.sendRedirect("PuestoServlet?action=list&success=Puesto actualizado correctamente");
            } else {
                response.sendRedirect("PuestoServlet?action=edit&id=" + puesto.getIdPuesto() + "&error=Error al actualizar puesto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("PuestoServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarPuesto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idPuesto = Integer.parseInt(request.getParameter("id"));
            
            if (puestoDAO.eliminar(idPuesto)) {
                response.sendRedirect("PuestoServlet?action=list&success=Puesto eliminado correctamente");
            } else {
                response.sendRedirect("PuestoServlet?action=list&error=Error al eliminar puesto");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("PuestoServlet?action=list&error=Error al eliminar puesto");
        }
    }
}
