package com.sistemaempresa.utils;

import java.util.List;
import java.util.Map;

/**
 * Alternativa simple a JSTL para evitar dependencias externas
 * Proporciona funciones básicas para JSP sin necesidad de librerías JSTL
 */
public class JSTLAlternative {
    
    /**
     * Verifica si una lista está vacía
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
    
    /**
     * Verifica si una cadena está vacía
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Obtiene el tamaño de una lista
     */
    public static int size(List<?> list) {
        return list != null ? list.size() : 0;
    }
    
    /**
     * Escapa caracteres HTML para prevenir XSS
     */
    public static String escapeHtml(String input) {
        if (input == null) return "";
        
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
    
    /**
     * Formatea un número con separadores de miles
     */
    public static String formatNumber(Number number) {
        if (number == null) return "0";
        
        String str = number.toString();
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        
        for (int i = str.length() - 1; i >= 0; i--) {
            if (count == 3) {
                formatted.insert(0, ",");
                count = 0;
            }
            formatted.insert(0, str.charAt(i));
            count++;
        }
        
        return formatted.toString();
    }
    
    /**
     * Trunca una cadena a una longitud específica
     */
    public static String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length) + "...";
    }
    
    /**
     * Convierte la primera letra a mayúscula
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
