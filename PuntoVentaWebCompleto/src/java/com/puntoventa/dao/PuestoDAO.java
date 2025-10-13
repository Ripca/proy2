package com.puntoventa.dao;

import com.puntoventa.config.DatabaseConnection;
import com.puntoventa.models.Puesto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Puesto
 * Copiado exactamente de SistemaEmpresa
 */
public class PuestoDAO {
    
    public List<Puesto> obtenerTodos() {
        List<Puesto> puestos = new ArrayList<>();
        String sql = "SELECT * FROM Puestos ORDER BY puesto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Puesto puesto = new Puesto();
                puesto.setIdPuesto(rs.getInt("idPuesto"));
                puesto.setPuesto(rs.getString("puesto"));
                puestos.add(puesto);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return puestos;
    }
    
    public Puesto obtenerPorId(int id) {
        String sql = "SELECT * FROM Puestos WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Puesto puesto = new Puesto();
                puesto.setIdPuesto(rs.getInt("idPuesto"));
                puesto.setPuesto(rs.getString("puesto"));
                return puesto;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean insertar(Puesto puesto) {
        String sql = "INSERT INTO Puestos (puesto) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, puesto.getPuesto());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    puesto.setIdPuesto(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean actualizar(Puesto puesto) {
        String sql = "UPDATE Puestos SET puesto = ? WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, puesto.getPuesto());
            stmt.setInt(2, puesto.getIdPuesto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Puestos WHERE idPuesto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Puesto> buscar(String termino) {
        List<Puesto> puestos = new ArrayList<>();
        String sql = "SELECT * FROM Puestos WHERE puesto LIKE ? ORDER BY puesto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termino + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Puesto puesto = new Puesto();
                puesto.setIdPuesto(rs.getInt("idPuesto"));
                puesto.setPuesto(rs.getString("puesto"));
                puestos.add(puesto);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return puestos;
    }
}
