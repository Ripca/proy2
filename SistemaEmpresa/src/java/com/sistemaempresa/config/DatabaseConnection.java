package com.sistemaempresa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
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
}
