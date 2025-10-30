package com.sistemaempresa.dao;

import com.sistemaempresa.models.Rol;
import com.sistemaempresa.config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Rol
 */
public class RolDAO {
    
    /**
     * Obtiene todos los roles activos
     */
    public List<Rol> obtenerTodos() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles WHERE estado = 1 ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Rol rol = mapearResultSet(rs);
                roles.add(rol);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return roles;
    }
    
    /**
     * Obtiene un rol por su ID
     */
    public Rol obtenerPorId(int idRol) {
        String sql = "SELECT * FROM roles WHERE idRol = ? AND estado = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene el rol de un usuario específico
     */
    public Rol obtenerRolPorUsuario(int idUsuario) {
        String sql = "SELECT r.* FROM roles r " +
                     "INNER JOIN usuario_rol ur ON r.idRol = ur.idRol " +
                     "WHERE ur.idUsuario = ? AND ur.estado = 1 AND r.estado = 1 " +
                     "LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Inserta un nuevo rol
     */
    public int insertar(Rol rol) {
        String sql = "INSERT INTO roles (nombre, estado, fecha_creacion) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, rol.getNombre());
            stmt.setInt(2, rol.getEstado());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Actualiza un rol existente
     */
    public boolean actualizar(Rol rol) {
        String sql = "UPDATE roles SET nombre = ?, estado = ? WHERE idRol = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, rol.getNombre());
            stmt.setInt(2, rol.getEstado());
            stmt.setInt(3, rol.getIdRol());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Rol
     */
    private Rol mapearResultSet(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setIdRol(rs.getInt("idRol"));
        rol.setNombre(rs.getString("nombre"));
        rol.setEstado(rs.getInt("estado"));
        
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            rol.setFechaCreacion(ts.toLocalDateTime());
        }
        
        return rol;
    }
}

