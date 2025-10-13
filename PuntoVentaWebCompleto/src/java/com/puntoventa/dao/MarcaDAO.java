package com.puntoventa.dao;

import com.puntoventa.config.DatabaseConnection;
import com.puntoventa.models.Marca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Marca
 * Copiado exactamente de SistemaEmpresa
 */
public class MarcaDAO {
    
    public List<Marca> obtenerTodos() {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marcas ORDER BY marca";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setMarca(rs.getString("marca"));
                marcas.add(marca);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return marcas;
    }
    
    public Marca obtenerPorId(int id) {
        String sql = "SELECT * FROM Marcas WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setMarca(rs.getString("marca"));
                return marca;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean insertar(Marca marca) {
        String sql = "INSERT INTO Marcas (marca) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, marca.getMarca());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    marca.setIdMarca(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean actualizar(Marca marca) {
        String sql = "UPDATE Marcas SET marca = ? WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, marca.getMarca());
            stmt.setInt(2, marca.getIdMarca());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Marcas WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Marca> buscar(String termino) {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marcas WHERE marca LIKE ? ORDER BY marca";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termino + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setMarca(rs.getString("marca"));
                marcas.add(marca);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return marcas;
    }
}
