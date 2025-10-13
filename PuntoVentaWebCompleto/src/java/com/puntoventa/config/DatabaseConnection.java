package com.puntoventa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 * Copiada exactamente de SistemaEmpresa
 */
public class DatabaseConnection {
    
    // Variables de entorno para configuración de base de datos
    private static final String DB_HOST = System.getenv("DB_HOST") != null ?
        System.getenv("DB_HOST") : "localhost";
    private static final String DB_PORT = System.getenv("DB_PORT") != null ?
        System.getenv("DB_PORT") : "3306";
    private static final String DB_NAME = System.getenv("DB_NAME") != null ?
        System.getenv("DB_NAME") : "PuntoVentaWebCompleto";
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String USERNAME = System.getenv("DB_USERNAME") != null ?
        System.getenv("DB_USERNAME") : "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ?
        System.getenv("DB_PASSWORD") : "";
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
        System.out.println("  DB_HOST: " + (System.getenv("DB_HOST") != null ? "✓ configurado (" + System.getenv("DB_HOST") + ")" : "✗ usando default (localhost)"));
        System.out.println("  DB_PORT: " + (System.getenv("DB_PORT") != null ? "✓ configurado (" + System.getenv("DB_PORT") + ")" : "✗ usando default (3306)"));
        System.out.println("  DB_NAME: " + (System.getenv("DB_NAME") != null ? "✓ configurado (" + System.getenv("DB_NAME") + ")" : "✗ usando default (PuntoVentaWebCompleto)"));
        System.out.println("  DB_USERNAME: " + (System.getenv("DB_USERNAME") != null ? "✓ configurado" : "✗ usando default"));
        System.out.println("  DB_PASSWORD: " + (System.getenv("DB_PASSWORD") != null ? "✓ configurado" : "✗ usando default"));
        System.out.println("=====================================");
    }
}
