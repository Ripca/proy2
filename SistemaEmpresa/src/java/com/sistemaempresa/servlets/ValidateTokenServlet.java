package com.sistemaempresa.servlets;

import com.sistemaempresa.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet para validar tokens JWT desde el cliente
 * Retorna JSON indicando si el token es válido
 */
@WebServlet("/ValidateTokenServlet")
public class ValidateTokenServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try {
            // Verificar si hay sesión activa
            HttpSession session = request.getSession(false);
            
            if (session != null && session.getAttribute("usuario") != null) {
                // Sesión válida
                response.getWriter().write("{\"valid\": true, \"message\": \"Sesión activa\"}");
                return;
            }
            
            // Intentar validar token
            String token = getTokenFromRequest(request);
            
            if (token != null && JWTUtil.validateToken(token)) {
                // Token válido, retornar datos del usuario
                Map<String, Object> payload = JWTUtil.getPayloadFromToken(token);
                
                String jsonResponse = "{\"valid\": true, \"message\": \"Token válido\", " +
                    "\"usuario\": \"" + payload.get("usuario") + "\", " +
                    "\"idUsuario\": " + payload.get("idUsuario") + ", " +
                    "\"idEmpleado\": " + payload.get("idEmpleado") + "}";
                
                response.getWriter().write(jsonResponse);
            } else {
                // Token inválido o expirado
                response.getWriter().write("{\"valid\": false, \"message\": \"Token inválido o expirado\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"valid\": false, \"message\": \"Error al validar token\"}");
        }
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
}

