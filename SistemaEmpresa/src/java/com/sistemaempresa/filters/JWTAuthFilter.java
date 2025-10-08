package com.sistemaempresa.filters;

import com.sistemaempresa.utils.JWTUtil;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro para validar autenticación JWT en todas las peticiones
 */
public class JWTAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Rutas que no requieren autenticación
        if (isPublicPath(requestURI, contextPath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar token JWT
        String token = getTokenFromRequest(httpRequest);
        
        if (token != null && JWTUtil.validateToken(token)) {
            // Token válido, agregar información del usuario a la sesión
            HttpSession session = httpRequest.getSession();
            if (session.getAttribute("usuario") == null) {
                String usuario = JWTUtil.getUserFromToken(token);
                int idUsuario = JWTUtil.getUserIdFromToken(token);
                int idEmpleado = JWTUtil.getEmployeeIdFromToken(token);
                
                session.setAttribute("usuario", usuario);
                session.setAttribute("idUsuario", idUsuario);
                session.setAttribute("idEmpleado", idEmpleado);
                session.setAttribute("token", token);
            }
            
            chain.doFilter(request, response);
        } else {
            // Token inválido o no presente, redirigir al login
            httpResponse.sendRedirect(contextPath + "/index.html");
        }
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
    
    /**
     * Verifica si la ruta es pública (no requiere autenticación)
     * @param requestURI URI de la petición
     * @param contextPath contexto de la aplicación
     * @return true si es ruta pública
     */
    private boolean isPublicPath(String requestURI, String contextPath) {
        String path = requestURI.substring(contextPath.length());
        
        return path.equals("/") ||
               path.equals("/index.html") ||
               path.equals("/LoginServlet") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/assets/");
    }
    
    /**
     * Extrae el token JWT de la petición
     * @param request petición HTTP
     * @return token JWT o null si no existe
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // Buscar en header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        // Buscar en parámetro de URL
        String tokenParam = request.getParameter("token");
        if (tokenParam != null) {
            return tokenParam;
        }
        
        // Buscar en sesión
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("token");
        }
        
        return null;
    }
}
