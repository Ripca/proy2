package com.puntoventa.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

/**
 * Utilidad para manejo de tokens JWT
 */
public class JWTUtil {
    
    private static final String SECRET_KEY = "PuntoVentaSecretKey2024!@#$%^&*()_+";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    /**
     * Genera un token JWT para un usuario
     * @param usuario nombre de usuario
     * @param idUsuario ID del usuario
     * @param idEmpleado ID del empleado asociado
     * @param rol rol del usuario
     * @return token JWT generado
     */
    public static String generateToken(String usuario, int idUsuario, int idEmpleado, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setSubject(usuario)
                .claim("idUsuario", idUsuario)
                .claim("idEmpleado", idEmpleado)
                .claim("rol", rol)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Valida un token JWT
     * @param token token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrae el nombre de usuario del token
     * @param token token JWT
     * @return nombre de usuario
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    
    /**
     * Extrae el ID del usuario del token
     * @param token token JWT
     * @return ID del usuario
     */
    public static int getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("idUsuario", Integer.class);
    }
    
    /**
     * Extrae el ID del empleado del token
     * @param token token JWT
     * @return ID del empleado
     */
    public static int getEmployeeIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("idEmpleado", Integer.class);
    }
    
    /**
     * Extrae el rol del usuario del token
     * @param token token JWT
     * @return rol del usuario
     */
    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rol", String.class);
    }
    
    /**
     * Verifica si el token ha expirado
     * @param token token JWT
     * @return true si ha expirado, false en caso contrario
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
