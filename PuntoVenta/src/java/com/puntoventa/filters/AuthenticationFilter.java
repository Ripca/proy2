package com.puntoventa.filters;

import com.puntoventa.servlets.LoginServlet;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro para verificar autenticación en páginas protegidas
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Verificar autenticación usando el método del LoginServlet
        if (LoginServlet.verificarAutenticacion(httpRequest, httpResponse)) {
            // Usuario autenticado, continuar con la cadena de filtros
            chain.doFilter(request, response);
        }
        // Si no está autenticado, LoginServlet.verificarAutenticacion ya redirige al login
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}
