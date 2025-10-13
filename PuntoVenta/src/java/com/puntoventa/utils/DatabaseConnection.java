package com.puntoventa.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/PuntoVenta";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
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
     * Cierra una conexión de manera segura
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
     * Verifica si la conexión está disponible
     * @return true si la conexión es exitosa, false en caso contrario
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
