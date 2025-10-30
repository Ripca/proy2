package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import java.sql.*;

/**
 * DAO para operaciones de relación Usuario-Rol
 */
public class UsuarioRolDAO {
    
    /**
     * Obtiene el ID del rol de un usuario específico
     */
    public int obtenerIdRolPorUsuario(int idUsuario) {
        String sql = "SELECT idRol FROM usuario_rol WHERE idUsuario = ? AND estado = 1 LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idRol");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1; // Retorna -1 si no encuentra rol
    }
    
    /**
     * Asigna un rol a un usuario
     */
    public boolean asignarRolAUsuario(int idUsuario, int idRol) {
        String sql = "INSERT INTO usuario_rol (idRol, idUsuario, estado) VALUES (?, ?, 1) " +
                     "ON DUPLICATE KEY UPDATE estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            stmt.setInt(2, idUsuario);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Desasigna un rol de un usuario
     */
    public boolean desasignarRolDeUsuario(int idUsuario, int idRol) {
        String sql = "UPDATE usuario_rol SET estado = 0 WHERE idUsuario = ? AND idRol = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idRol);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean tieneRol(int idUsuario, int idRol) {
        String sql = "SELECT COUNT(*) as count FROM usuario_rol WHERE idUsuario = ? AND idRol = ? AND estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idRol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}

