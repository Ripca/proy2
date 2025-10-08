package com.sistemaempresa.utils;

import java.util.Map;

/**
 * Utilidad simple para manejo de JSON sin librerías externas
 * Alternativa a Jackson para evitar dependencias
 */
public class SimpleJSONUtil {
    
    /**
     * Convierte un Map a JSON string simple
     * @param map mapa a convertir
     * @return string JSON
     */
    public static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Number) {
                json.append(value);
            } else if (value instanceof Boolean) {
                json.append(value);
            } else {
                json.append("\"").append(value != null ? value.toString() : "null").append("\"");
            }
            
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Parsea un JSON simple a Map
     * @param json string JSON
     * @return Map con los datos
     */
    public static Map<String, Object> jsonToMap(String json) {
        // Implementación simple para casos básicos
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        
        if (json == null || json.trim().isEmpty()) {
            return map;
        }
        
        // Remover llaves
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        
        // Dividir por comas (implementación básica)
        String[] pairs = json.split(",");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");
                
                // Intentar convertir a número
                try {
                    if (value.contains(".")) {
                        map.put(key, Double.parseDouble(value));
                    } else {
                        map.put(key, Long.parseLong(value));
                    }
                } catch (NumberFormatException e) {
                    // Si no es número, mantener como string
                    if ("true".equals(value) || "false".equals(value)) {
                        map.put(key, Boolean.parseBoolean(value));
                    } else {
                        map.put(key, value);
                    }
                }
            }
        }
        
        return map;
    }
}
