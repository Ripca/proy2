package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.ClienteDAO;
import com.sistemaempresa.models.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet para operaciones CRUD de Cliente
 */
public class ClienteServlet extends HttpServlet {
    
    private ClienteDAO clienteDAO;
    
    @Override
    public void init() throws ServletException {
        clienteDAO = new ClienteDAO();
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
                listarClientes(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarCliente(request, response);
                break;
            case "search":
                buscarClientes(request, response);
                break;
            default:
                listarClientes(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarCliente(request, response);
        } else if ("update".equals(action)) {
            actualizarCliente(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("/WEB-INF/views/clientes/list.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/clientes/form.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idCliente = Integer.parseInt(request.getParameter("id"));
        Cliente cliente = clienteDAO.obtenerPorId(idCliente);
        
        if (cliente != null) {
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("/WEB-INF/views/clientes/form.jsp").forward(request, response);
        } else {
            response.sendRedirect("ClienteServlet?action=list&error=Cliente no encontrado");
        }
    }
    
    private void guardarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Cliente cliente = new Cliente();
            cliente.setNombres(request.getParameter("nombres"));
            cliente.setApellidos(request.getParameter("apellidos"));
            cliente.setNit(request.getParameter("nit"));
            cliente.setGenero("M".equals(request.getParameter("genero")));
            cliente.setTelefono(request.getParameter("telefono"));
            cliente.setCorreoElectronico(request.getParameter("correoElectronico"));
            
            if (clienteDAO.insertar(cliente)) {
                response.sendRedirect("ClienteServlet?action=list&success=Cliente guardado correctamente");
            } else {
                response.sendRedirect("ClienteServlet?action=new&error=Error al guardar cliente");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ClienteServlet?action=new&error=Error en los datos");
        }
    }
    
    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(request.getParameter("idCliente")));
            cliente.setNombres(request.getParameter("nombres"));
            cliente.setApellidos(request.getParameter("apellidos"));
            cliente.setNit(request.getParameter("nit"));
            cliente.setGenero("M".equals(request.getParameter("genero")));
            cliente.setTelefono(request.getParameter("telefono"));
            cliente.setCorreoElectronico(request.getParameter("correoElectronico"));
            
            if (clienteDAO.actualizar(cliente)) {
                response.sendRedirect("ClienteServlet?action=list&success=Cliente actualizado correctamente");
            } else {
                response.sendRedirect("ClienteServlet?action=edit&id=" + cliente.getIdCliente() + "&error=Error al actualizar cliente");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ClienteServlet?action=list&error=Error en los datos");
        }
    }
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCliente = Integer.parseInt(request.getParameter("id"));
            
            if (clienteDAO.eliminar(idCliente)) {
                response.sendRedirect("ClienteServlet?action=list&success=Cliente eliminado correctamente");
            } else {
                response.sendRedirect("ClienteServlet?action=list&error=Error al eliminar cliente");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ClienteServlet?action=list&error=Error al eliminar cliente");
        }
    }
    
    private void buscarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String termino = request.getParameter("termino");
        List<Cliente> clientes;
        
        if (termino != null && !termino.trim().isEmpty()) {
            clientes = clienteDAO.buscar(termino);
        } else {
            clientes = clienteDAO.obtenerTodos();
        }
        
        request.setAttribute("clientes", clientes);
        request.setAttribute("termino", termino);
        request.getRequestDispatcher("/WEB-INF/views/clientes/list.jsp").forward(request, response);
    }
}
