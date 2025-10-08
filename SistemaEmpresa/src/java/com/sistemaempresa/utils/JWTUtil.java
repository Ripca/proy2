package com.sistemaempresa.utils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidad para manejo de JWT (JSON Web Tokens)
 * Implementación simple sin librerías externas
 */
public class JWTUtil {
    
    private static final String SECRET_KEY = "sistemaEmpresaSecretKey2024";
    private static final String ALGORITHM = "HmacSHA256";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    
    /**
     * Genera un token JWT
     * @param usuario nombre de usuario
     * @param idUsuario ID del usuario
     * @param idEmpleado ID del empleado
     * @return token JWT
     */
    public static String generateToken(String usuario, int idUsuario, int idEmpleado) {
        try {
            // Header
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            
            // Payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("usuario", usuario);
            payload.put("idUsuario", idUsuario);
            payload.put("idEmpleado", idEmpleado);
            payload.put("iat", System.currentTimeMillis() / 1000);
            payload.put("exp", (System.currentTimeMillis() + EXPIRATION_TIME) / 1000);
            
            String headerJson = SimpleJSONUtil.mapToJson(header);
            String payloadJson = SimpleJSONUtil.mapToJson(payload);
            
            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes());
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
            
            String signature = createSignature(encodedHeader + "." + encodedPayload);
            
            return encodedHeader + "." + encodedPayload + "." + signature;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar token JWT", e);
        }
    }
    
    /**
     * Valida un token JWT
     * @param token token a validar
     * @return true si el token es válido
     */
    public static boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }
            
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            
            // Verificar firma
            String expectedSignature = createSignature(header + "." + payload);
            if (!signature.equals(expectedSignature)) {
                return false;
            }
            
            // Verificar expiración
            Map<String, Object> payloadData = getPayloadFromToken(token);
            long exp = ((Number) payloadData.get("exp")).longValue();
            long currentTime = System.currentTimeMillis() / 1000;
            
            return currentTime < exp;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrae el payload del token
     * @param token token JWT
     * @return Map con los datos del payload
     */
    public static Map<String, Object> getPayloadFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = parts[1];
            
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String payloadJson = new String(decodedBytes);

            return SimpleJSONUtil.jsonToMap(payloadJson);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al extraer payload del token", e);
        }
    }
    
    /**
     * Obtiene el usuario del token
     * @param token token JWT
     * @return nombre de usuario
     */
    public static String getUserFromToken(String token) {
        Map<String, Object> payload = getPayloadFromToken(token);
        return (String) payload.get("usuario");
    }
    
    /**
     * Obtiene el ID de usuario del token
     * @param token token JWT
     * @return ID de usuario
     */
    public static int getUserIdFromToken(String token) {
        Map<String, Object> payload = getPayloadFromToken(token);
        return ((Number) payload.get("idUsuario")).intValue();
    }
    
    /**
     * Obtiene el ID de empleado del token
     * @param token token JWT
     * @return ID de empleado
     */
    public static int getEmployeeIdFromToken(String token) {
        Map<String, Object> payload = getPayloadFromToken(token);
        return ((Number) payload.get("idEmpleado")).intValue();
    }
    
    /**
     * Crea la firma HMAC SHA256
     * @param data datos a firmar
     * @return firma en Base64
     */
    private static String createSignature(String data) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            mac.init(secretKeySpec);
            
            byte[] signature = mac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al crear firma", e);
        }
    }
}
