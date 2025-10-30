package com.sistemaempresa.filters;

import com.sistemaempresa.utils.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Filtro de autenticación que valida sesiones y tokens JWT
 * Permite acceso si:
 * 1. La sesión es válida y tiene usuario
 * 2. El token JWT es válido (incluso si la sesión expiró)
 */
public class AuthenticationFilter implements Filter {

    // Rutas que no requieren autenticación
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/index.html",
        "/login.html",
        "/index.jsp",
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
        boolean isAuthenticated = false;

        // Opción 1: Verificar si la sesión es válida
        if (session != null && session.getAttribute("usuario") != null) {
            isAuthenticated = true;
        }
        // Opción 2: Verificar si hay un token JWT válido en la cookie o parámetro
        else {
            String token = getTokenFromRequest(httpRequest);
            if (token != null && JWTUtil.validateToken(token)) {
                // Token válido, restaurar la sesión
                try {
                    Map<String, Object> payload = JWTUtil.getPayloadFromToken(token);

                    // Crear nueva sesión con los datos del token
                    session = httpRequest.getSession(true);
                    session.setAttribute("usuario", payload.get("usuario"));
                    session.setAttribute("idUsuario", ((Number) payload.get("idUsuario")).intValue());
                    session.setAttribute("idEmpleado", ((Number) payload.get("idEmpleado")).intValue());
                    session.setAttribute("token", token);

                    isAuthenticated = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!isAuthenticated) {
            // Usuario no autenticado, redirigir al login
            httpResponse.sendRedirect(contextPath + "/index.jsp");
            return;
        }

        // Usuario autenticado, continuar
        chain.doFilter(request, response);
    }

    /**
     * Obtiene el token JWT del request (desde cookie o parámetro)
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // Intentar obtener del parámetro token
        String token = request.getParameter("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // Intentar obtener de la cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
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
