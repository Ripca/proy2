package com.sistemaempresa.servlets;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.utils.JWTUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet para manejar el login de usuarios
 */
@WebServlet(
    name = "LoginServlet",
    description = "Servlet que maneja la autenticación de usuarios",
    urlPatterns = {"/LoginServlet", "/login"}
)
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/?error=Datos incompletos");
            return;
        }

        try {
            // Validar credenciales en base de datos
            UserData userData = validateUser(usuario, password);

            if (userData != null) {
                // Generar token JWT
                String token = JWTUtil.generateToken(userData.usuario, userData.idUsuario, userData.idEmpleado);

                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", userData.usuario);
                session.setAttribute("idUsuario", userData.idUsuario);
                session.setAttribute("idEmpleado", userData.idEmpleado);
                session.setAttribute("nombreCompleto", userData.nombreCompleto);
                session.setAttribute("token", token);

                // Guardar token en cookie para persistencia entre pestañas
                Cookie tokenCookie = new Cookie("authToken", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath(request.getContextPath() + "/");
                tokenCookie.setMaxAge(86400); // 24 horas
                response.addCookie(tokenCookie);

                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/DashboardServlet");

            } else {
                response.sendRedirect(request.getContextPath() + "/?error=Credenciales incorrectas");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/?error=Error del sistema");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            // Cerrar sesión
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Limpiar cookie de token
            Cookie tokenCookie = new Cookie("authToken", "");
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath(request.getContextPath() + "/");
            tokenCookie.setMaxAge(0); // Eliminar cookie
            response.addCookie(tokenCookie);

            // Redirigir a la raíz de la aplicación
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    /**
     * Valida las credenciales del usuario en la base de datos
     * @param usuario nombre de usuario
     * @param password contraseña
     * @return datos del usuario si es válido, null si no
     */
    private UserData validateUser(String usuario, String password) {
        String sql = "SELECT u.idUsuario, u.usuario, u.idEmpleado, " +
                    "CONCAT(e.nombres, ' ', e.apellidos) as nombreCompleto " +
                    "FROM Usuarios u " +
                    "INNER JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.usuario = ? AND u.password = ? AND u.activo = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, password); // En producción debería usar hash
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                UserData userData = new UserData();
                userData.idUsuario = rs.getInt("idUsuario");
                userData.usuario = rs.getString("usuario");
                userData.idEmpleado = rs.getInt("idEmpleado");
                userData.nombreCompleto = rs.getString("nombreCompleto");
                return userData;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Clase interna para almacenar datos del usuario
     */
    private static class UserData {
        int idUsuario;
        String usuario;
        int idEmpleado;
        String nombreCompleto;
    }
}
