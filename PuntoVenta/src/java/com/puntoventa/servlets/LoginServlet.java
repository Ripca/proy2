package com.puntoventa.servlets;

import com.puntoventa.dao.UsuarioDAO;
import com.puntoventa.models.Usuario;
import com.puntoventa.utils.JWTUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Servlet para manejo de autenticación de usuarios
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Maneja las peticiones GET (mostrar formulario de login o logout)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        if ("/logout".equals(action)) {
            logout(request, response);
        } else {
            // Verificar si ya está autenticado
            if (isAuthenticated(request)) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
            
            // Mostrar página de login
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja las peticiones POST (procesar login)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("login".equals(action)) {
            processLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
    
    /**
     * Procesa el login del usuario
     */
    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String recordarme = request.getParameter("recordarme");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Validar parámetros
            if (usuario == null || usuario.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Usuario y contraseña son requeridos\"}");
                return;
            }
            
            // Hash de la contraseña (en producción usar bcrypt o similar)
            String hashedPassword = hashPassword(password);
            
            // Validar credenciales
            Usuario usuarioObj = usuarioDAO.validarCredenciales(usuario.trim(), hashedPassword);
            
            if (usuarioObj != null) {
                // Login exitoso
                
                // Generar token JWT
                String token = JWTUtil.generateToken(
                    usuarioObj.getUsuario(),
                    usuarioObj.getIdUsuario(),
                    usuarioObj.getIdEmpleado(),
                    usuarioObj.getRol()
                );
                
                // Crear sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuarioObj);
                session.setAttribute("token", token);
                session.setMaxInactiveInterval(24 * 60 * 60); // 24 horas
                
                // Si marcó "recordarme", crear cookie
                if ("on".equals(recordarme)) {
                    Cookie tokenCookie = new Cookie("authToken", token);
                    tokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 días
                    tokenCookie.setPath(request.getContextPath());
                    tokenCookie.setHttpOnly(true);
                    response.addCookie(tokenCookie);
                }
                
                // Respuesta exitosa
                out.print("{\"success\": true, \"message\": \"Login exitoso\", " +
                         "\"redirect\": \"" + request.getContextPath() + "/dashboard\"}");
                
            } else {
                // Login fallido
                usuarioDAO.incrementarIntentosFallidos(usuario.trim());
                
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\": false, \"message\": \"Usuario o contraseña incorrectos\"}");
            }
            
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error interno del servidor\"}");
        }
    }
    
    /**
     * Procesa el logout del usuario
     */
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Invalidar sesión
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // Eliminar cookie de autenticación
        Cookie tokenCookie = new Cookie("authToken", "");
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath(request.getContextPath());
        response.addCookie(tokenCookie);
        
        // Redirigir al login
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    /**
     * Verifica si el usuario está autenticado
     */
    private boolean isAuthenticated(HttpServletRequest request) {
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            return true;
        }
        
        // Verificar cookie JWT
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (JWTUtil.validateToken(token)) {
                        // Token válido, recrear sesión
                        try {
                            String username = JWTUtil.getUsernameFromToken(token);
                            Usuario usuario = usuarioDAO.obtenerPorNombreUsuario(username);
                            
                            if (usuario != null && usuario.isActivo() && !usuario.isBloqueado()) {
                                HttpSession newSession = request.getSession(true);
                                newSession.setAttribute("usuario", usuario);
                                newSession.setAttribute("token", token);
                                return true;
                            }
                        } catch (Exception e) {
                            System.err.println("Error al validar token: " + e.getMessage());
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Genera hash SHA-256 de la contraseña
     * En producción se debería usar bcrypt o similar
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar hash de contraseña", e);
        }
    }
    
    /**
     * Filtro para verificar autenticación en otras páginas
     */
    public static boolean verificarAutenticacion(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        Usuario usuario = null;
        
        if (session != null) {
            usuario = (Usuario) session.getAttribute("usuario");
        }
        
        // Si no hay usuario en sesión, verificar cookie JWT
        if (usuario == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        if (JWTUtil.validateToken(token)) {
                            try {
                                String username = JWTUtil.getUsernameFromToken(token);
                                UsuarioDAO usuarioDAO = new UsuarioDAO();
                                usuario = usuarioDAO.obtenerPorNombreUsuario(username);
                                
                                if (usuario != null && usuario.isActivo() && !usuario.isBloqueado()) {
                                    // Recrear sesión
                                    HttpSession newSession = request.getSession(true);
                                    newSession.setAttribute("usuario", usuario);
                                    newSession.setAttribute("token", token);
                                }
                            } catch (Exception e) {
                                System.err.println("Error al validar token: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        
        if (usuario == null || !usuario.isActivo() || usuario.isBloqueado()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene el usuario actual de la sesión
     */
    public static Usuario obtenerUsuarioActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }
}
