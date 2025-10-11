package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.EmpleadoDAO;
import com.sistemaempresa.dao.PuestoDAO;
import com.sistemaempresa.models.Empleado;
import com.sistemaempresa.models.Puesto;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Empleado
 */
public class EmpleadoServlet extends HttpServlet {
    
    private EmpleadoDAO empleadoDAO;
    private PuestoDAO puestoDAO;
    
    @Override
    public void init() throws ServletException {
        empleadoDAO = new EmpleadoDAO();
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
                listarEmpleados(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarEmpleado(request, response);
                break;
            default:
                listarEmpleados(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarEmpleado(request, response);
        } else if ("update".equals(action)) {
            actualizarEmpleado(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        request.setAttribute("empleados", empleados);
        request.getRequestDispatcher("/WEB-INF/views/empleados/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Puesto> puestos = puestoDAO.obtenerTodos();
        request.setAttribute("puestos", puestos);
        request.getRequestDispatcher("/WEB-INF/views/empleados/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idEmpleado = Integer.parseInt(request.getParameter("id"));
        Empleado empleado = empleadoDAO.obtenerPorId(idEmpleado);
        List<Puesto> puestos = puestoDAO.obtenerTodos();
        
        if (empleado != null) {
            request.setAttribute("empleado", empleado);
            request.setAttribute("puestos", puestos);
            request.getRequestDispatcher("/WEB-INF/views/empleados/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("EmpleadoServlet?action=list&error=Empleado no encontrado");
        }
    }
    
    private void guardarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Empleado empleado = new Empleado();
            empleado.setNombres(request.getParameter("nombres"));
            empleado.setApellidos(request.getParameter("apellidos"));
            empleado.setDireccion(request.getParameter("direccion"));
            empleado.setTelefono(request.getParameter("telefono"));
            empleado.setDpi(request.getParameter("dpi"));
            empleado.setGenero("true".equals(request.getParameter("genero")));
            empleado.setIdPuesto(Integer.parseInt(request.getParameter("idPuesto")));
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (request.getParameter("fecha_nacimiento") != null && !request.getParameter("fecha_nacimiento").isEmpty()) {
                empleado.setFechaNacimiento(sdf.parse(request.getParameter("fecha_nacimiento")));
            }
            if (request.getParameter("fecha_inicio_labores") != null && !request.getParameter("fecha_inicio_labores").isEmpty()) {
                empleado.setFechaInicioLabores(sdf.parse(request.getParameter("fecha_inicio_labores")));
            }
            
            if (empleadoDAO.insertar(empleado)) {
                response.sendRedirect("EmpleadoServlet?action=list&success=Empleado guardado correctamente");
            } else {
                response.sendRedirect("EmpleadoServlet?action=new&error=Error al guardar empleado");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("EmpleadoServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Empleado empleado = new Empleado();
            empleado.setIdEmpleado(Integer.parseInt(request.getParameter("idEmpleado")));
            empleado.setNombres(request.getParameter("nombres"));
            empleado.setApellidos(request.getParameter("apellidos"));
            empleado.setDireccion(request.getParameter("direccion"));
            empleado.setTelefono(request.getParameter("telefono"));
            empleado.setDpi(request.getParameter("dpi"));
            empleado.setGenero("true".equals(request.getParameter("genero")));
            empleado.setIdPuesto(Integer.parseInt(request.getParameter("idPuesto")));
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (request.getParameter("fecha_nacimiento") != null && !request.getParameter("fecha_nacimiento").isEmpty()) {
                empleado.setFechaNacimiento(sdf.parse(request.getParameter("fecha_nacimiento")));
            }
            if (request.getParameter("fecha_inicio_labores") != null && !request.getParameter("fecha_inicio_labores").isEmpty()) {
                empleado.setFechaInicioLabores(sdf.parse(request.getParameter("fecha_inicio_labores")));
            }
            
            if (empleadoDAO.actualizar(empleado)) {
                response.sendRedirect("EmpleadoServlet?action=list&success=Empleado actualizado correctamente");
            } else {
                response.sendRedirect("EmpleadoServlet?action=edit&id=" + empleado.getIdEmpleado() + "&error=Error al actualizar empleado");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("EmpleadoServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idEmpleado = Integer.parseInt(request.getParameter("id"));
            
            if (empleadoDAO.eliminar(idEmpleado)) {
                response.sendRedirect("EmpleadoServlet?action=list&success=Empleado eliminado correctamente");
            } else {
                response.sendRedirect("EmpleadoServlet?action=list&error=Error al eliminar empleado");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("EmpleadoServlet?action=list&error=Error al eliminar empleado");
        }
    }
}
