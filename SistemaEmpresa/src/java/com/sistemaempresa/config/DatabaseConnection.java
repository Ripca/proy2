package com.sistemaempresa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 */
public class DatabaseConnection {
    
    // Variables de entorno para configuración de base de datos
    private static final String URL = System.getenv("DB_URL") != null ?
        System.getenv("DB_URL") : "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USERNAME = System.getenv("DB_USERNAME") != null ?
        System.getenv("DB_USERNAME") : "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ?
        System.getenv("DB_PASSWORD") : "admin";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver de MySQL", e);
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si hay error en la conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    /**
     * Cierra la conexión de forma segura
     * @param connection conexión a cerrar
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Prueba la conexión a la base de datos
     * @return true si la conexión es exitosa
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Muestra la configuración actual de la base de datos (sin mostrar la contraseña)
     * Útil para debugging
     */
    public static void printConnectionInfo() {
        System.out.println("=== CONFIGURACIÓN DE BASE DE DATOS ===");
        System.out.println("URL: " + URL);
        System.out.println("Usuario: " + USERNAME);
        System.out.println("Password: " + (PASSWORD != null && !PASSWORD.isEmpty() ? "***configurado***" : "***no configurado***"));
        System.out.println("Variables de entorno:");
        System.out.println("  DB_URL: " + (System.getenv("DB_URL") != null ? "✓ configurado" : "✗ usando default"));
        System.out.println("  DB_USERNAME: " + (System.getenv("DB_USERNAME") != null ? "✓ configurado" : "✗ usando default"));
        System.out.println("  DB_PASSWORD: " + (System.getenv("DB_PASSWORD") != null ? "✓ configurado" : "✗ usando default"));
        System.out.println("=====================================");
    }
}
