package com.puntoventa.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Filtro para establecer codificación UTF-8 en todas las peticiones
 */
@WebFilter(
    filterName = "CharacterEncodingFilter",
    urlPatterns = {"/*"},
    initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8")
    }
)
public class CharacterEncodingFilter implements Filter {
    
    private String encoding;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Establecer codificación para la petición
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        
        // Establecer codificación para la respuesta
        response.setCharacterEncoding(encoding);
        
        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}
