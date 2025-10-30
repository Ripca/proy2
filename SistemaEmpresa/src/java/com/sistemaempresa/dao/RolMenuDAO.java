package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones de relación Rol-Menu
 */
public class RolMenuDAO {
    
    /**
     * Obtiene todos los IDs de menús permitidos para un rol específico
     */
    public List<Integer> obtenerMenusPorRol(int idRol) {
        List<Integer> menuIds = new ArrayList<>();
        String sql = "SELECT idMenu FROM rol_menu WHERE idRol = ? AND estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menuIds.add(rs.getInt("idMenu"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menuIds;
    }
    
    /**
     * Asigna un menú a un rol
     */
    public boolean asignarMenuARol(int idRol, int idMenu) {
        String sql = "INSERT INTO rol_menu (idRol, idMenu, estado) VALUES (?, ?, 1) " +
                     "ON DUPLICATE KEY UPDATE estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            stmt.setInt(2, idMenu);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Desasigna un menú de un rol
     */
    public boolean desasignarMenuDeRol(int idRol, int idMenu) {
        String sql = "UPDATE rol_menu SET estado = 0 WHERE idRol = ? AND idMenu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            stmt.setInt(2, idMenu);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica si un rol tiene acceso a un menú específico
     */
    public boolean tieneAcceso(int idRol, int idMenu) {
        String sql = "SELECT COUNT(*) as count FROM rol_menu WHERE idRol = ? AND idMenu = ? AND estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            stmt.setInt(2, idMenu);
            
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
    
    /**
     * Obtiene todos los menús asignados a un rol
     */
    public List<Integer> obtenerTodosMenusPorRol(int idRol) {
        return obtenerMenusPorRol(idRol);
    }
}

