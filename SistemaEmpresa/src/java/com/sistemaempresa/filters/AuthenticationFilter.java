package com.sistemaempresa.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro de autenticación básica para verificar sesiones de usuario
 */
public class AuthenticationFilter implements Filter {
    
    // Rutas que no requieren autenticación
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/index.html",
        "/login.html",
        "/LoginServlet",
        "/css/",
        "/js/",
        "/images/",
        "/assets/"
    );
    
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
        
        // Verificar si es una ruta pública
        if (isPublicPath(requestURI, contextPath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar autenticación
        HttpSession session = httpRequest.getSession(false);
        
        if (session == null || session.getAttribute("usuario") == null) {
            // Usuario no autenticado, redirigir al login
            httpResponse.sendRedirect(contextPath + "/index.html");
            return;
        }
        
        // Usuario autenticado, continuar
        chain.doFilter(request, response);
    }
    
    /**
     * Verifica si la ruta es pública (no requiere autenticación)
     */
    private boolean isPublicPath(String requestURI, String contextPath) {
        String path = requestURI.substring(contextPath.length());
        
        return PUBLIC_PATHS.stream().anyMatch(publicPath -> 
            path.equals(publicPath) || path.startsWith(publicPath)
        );
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}
