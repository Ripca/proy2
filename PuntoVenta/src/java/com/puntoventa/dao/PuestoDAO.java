package com.puntoventa.dao;

import com.puntoventa.models.Puesto;
import com.puntoventa.utils.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Puesto
 */
public class PuestoDAO {
    
    /**
     * Crea un nuevo puesto
     * @param puesto objeto Puesto a crear
     * @return ID del puesto creado, 0 si hay error
     */
    public int crear(Puesto puesto) {
        String sql = "INSERT INTO Puestos (puesto, descripcion, salario_base) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, puesto.getPuesto());
            stmt.setString(2, puesto.getDescripcion());
            stmt.setBigDecimal(3, puesto.getSalarioBase());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear puesto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un puesto por su ID
     * @param idPuesto ID del puesto
     * @return objeto Puesto o null si no existe
     */
    public Puesto obtenerPorId(int idPuesto) {
        String sql = "SELECT * FROM Puestos WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPuesto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPuesto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener puesto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los puestos
     * @return lista de puestos
     */
    public List<Puesto> obtenerTodos() {
        List<Puesto> puestos = new ArrayList<>();
        String sql = "SELECT * FROM Puestos ORDER BY puesto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                puestos.add(mapearPuesto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los puestos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return puestos;
    }
    
    /**
     * Actualiza un puesto
     * @param puesto objeto Puesto con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Puesto puesto) {
        String sql = "UPDATE Puestos SET puesto = ?, descripcion = ?, salario_base = ? WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, puesto.getPuesto());
            stmt.setString(2, puesto.getDescripcion());
            stmt.setBigDecimal(3, puesto.getSalarioBase());
            stmt.setInt(4, puesto.getIdPuesto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar puesto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un puesto
     * @param idPuesto ID del puesto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idPuesto) {
        String sql = "DELETE FROM Puestos WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPuesto);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar puesto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca puestos por nombre
     * @param nombre nombre a buscar
     * @return lista de puestos que coinciden
     */
    public List<Puesto> buscarPorNombre(String nombre) {
        List<Puesto> puestos = new ArrayList<>();
        String sql = "SELECT * FROM Puestos WHERE puesto LIKE ? ORDER BY puesto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                puestos.add(mapearPuesto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar puestos por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        
        return puestos;
    }
    
    /**
     * Cuenta el total de puestos
     * @return número total de puestos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Puestos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar puestos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe un puesto con el nombre dado
     * @param nombre nombre del puesto
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existePuesto(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Puestos WHERE puesto = ? AND idPuesto != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de puesto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Puesto
     * @param rs ResultSet con los datos
     * @return objeto Puesto
     * @throws SQLException si hay error en el mapeo
     */
    private Puesto mapearPuesto(ResultSet rs) throws SQLException {
        Puesto puesto = new Puesto();
        puesto.setIdPuesto(rs.getInt("idPuesto"));
        puesto.setPuesto(rs.getString("puesto"));
        puesto.setDescripcion(rs.getString("descripcion"));
        puesto.setSalarioBase(rs.getBigDecimal("salario_base"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            puesto.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return puesto;
    }
}
