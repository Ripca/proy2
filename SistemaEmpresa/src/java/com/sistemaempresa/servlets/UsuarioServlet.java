package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.UsuarioDAO;
import com.sistemaempresa.models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet para operaciones CRUD de Usuario
 */
@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
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
                listarUsuarios(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarUsuario(request, response);
                break;
            case "search":
                buscarUsuarios(request, response);
                break;
            case "changePassword":
                mostrarFormularioCambioPassword(request, response);
                break;
            default:
                listarUsuarios(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "save":
                guardarUsuario(request, response);
                break;
            case "update":
                actualizarUsuario(request, response);
                break;
            case "updatePassword":
                actualizarPassword(request, response);
                break;
            default:
                listarUsuarios(request, response);
        }
    }
    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/usuarios/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/usuarios/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.obtenerPorId(id);
        
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/WEB-INF/views/usuarios/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("UsuarioServlet?error=Usuario no encontrado");
        }
    }
    
    private void mostrarFormularioCambioPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.obtenerPorId(id);
        
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/WEB-INF/views/usuarios/password_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("UsuarioServlet?error=Usuario no encontrado");
        }
    }
    
    private void guardarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Usuario usuario = new Usuario();
            usuario.setUsuario(request.getParameter("usuario"));
            usuario.setPassword(request.getParameter("password"));
            usuario.setNombres(request.getParameter("nombres"));
            usuario.setApellidos(request.getParameter("apellidos"));
            usuario.setEmail(request.getParameter("email"));
            usuario.setRol(request.getParameter("rol"));
            usuario.setActivo(Boolean.parseBoolean(request.getParameter("activo")));
            usuario.setFechaCreacion(LocalDate.now());
            
            if (usuarioDAO.insertar(usuario)) {
                response.sendRedirect("UsuarioServlet?success=Usuario creado correctamente");
            } else {
                response.sendRedirect("UsuarioServlet?error=Error al crear usuario");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UsuarioServlet?error=Error al procesar datos: " + e.getMessage());
        }
    }
    
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("idUsuario"));
            Usuario usuario = usuarioDAO.obtenerPorId(id);
            
            if (usuario != null) {
                usuario.setUsuario(request.getParameter("usuario"));
                usuario.setNombres(request.getParameter("nombres"));
                usuario.setApellidos(request.getParameter("apellidos"));
                usuario.setEmail(request.getParameter("email"));
                usuario.setRol(request.getParameter("rol"));
                usuario.setActivo(Boolean.parseBoolean(request.getParameter("activo")));
                
                if (usuarioDAO.actualizar(usuario)) {
                    response.sendRedirect("UsuarioServlet?success=Usuario actualizado correctamente");
                } else {
                    response.sendRedirect("UsuarioServlet?error=Error al actualizar usuario");
                }
            } else {
                response.sendRedirect("UsuarioServlet?error=Usuario no encontrado");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UsuarioServlet?error=Error al procesar datos: " + e.getMessage());
        }
    }
    
    private void actualizarPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("idUsuario"));
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            if (!nuevaPassword.equals(confirmarPassword)) {
                response.sendRedirect("UsuarioServlet?action=changePassword&id=" + id + "&error=Las contraseñas no coinciden");
                return;
            }
            
            if (usuarioDAO.actualizarPassword(id, nuevaPassword)) {
                response.sendRedirect("UsuarioServlet?success=Contraseña actualizada correctamente");
            } else {
                response.sendRedirect("UsuarioServlet?action=changePassword&id=" + id + "&error=Error al actualizar contraseña");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UsuarioServlet?error=Error al procesar datos: " + e.getMessage());
        }
    }
    
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            if (usuarioDAO.eliminar(id)) {
                response.sendRedirect("UsuarioServlet?success=Usuario eliminado correctamente");
            } else {
                response.sendRedirect("UsuarioServlet?error=Error al eliminar usuario");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UsuarioServlet?error=Error al procesar datos: " + e.getMessage());
        }
    }
    
    private void buscarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String termino = request.getParameter("termino");
        List<Usuario> usuarios;
        
        if (termino != null && !termino.trim().isEmpty()) {
            usuarios = usuarioDAO.buscar(termino);
        } else {
            usuarios = usuarioDAO.obtenerTodos();
        }
        
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("termino", termino);
        request.getRequestDispatcher("/WEB-INF/views/usuarios/list_template.jsp").forward(request, response);
    }
}
