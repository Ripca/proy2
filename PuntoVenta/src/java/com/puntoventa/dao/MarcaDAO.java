package com.puntoventa.dao;

import com.puntoventa.models.Marca;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Marca
 */
public class MarcaDAO {
    
    /**
     * Crea una nueva marca
     * @param marca objeto Marca a crear
     * @return ID de la marca creada, 0 si hay error
     */
    public int crear(Marca marca) {
        String sql = "INSERT INTO Marcas (marca, descripcion, pais_origen) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, marca.getMarca());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getPaisOrigen());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene una marca por su ID
     * @param idMarca ID de la marca
     * @return objeto Marca o null si no existe
     */
    public Marca obtenerPorId(int idMarca) {
        String sql = "SELECT * FROM Marcas WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearMarca(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener marca por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las marcas
     * @return lista de marcas
     */
    public List<Marca> obtenerTodas() {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marcas ORDER BY marca";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                marcas.add(mapearMarca(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las marcas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return marcas;
    }
    
    /**
     * Actualiza una marca
     * @param marca objeto Marca con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Marca marca) {
        String sql = "UPDATE Marcas SET marca = ?, descripcion = ?, pais_origen = ? WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, marca.getMarca());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getPaisOrigen());
            stmt.setInt(4, marca.getIdMarca());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una marca
     * @param idMarca ID de la marca a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idMarca) {
        String sql = "DELETE FROM Marcas WHERE idMarca = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMarca);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca marcas por nombre
     * @param nombre nombre a buscar
     * @return lista de marcas que coinciden
     */
    public List<Marca> buscarPorNombre(String nombre) {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marcas WHERE marca LIKE ? ORDER BY marca";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                marcas.add(mapearMarca(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar marcas por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        
        return marcas;
    }
    
    /**
     * Cuenta el total de marcas
     * @return número total de marcas
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Marcas";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar marcas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe una marca con el nombre dado
     * @param nombre nombre de la marca
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeMarca(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Marcas WHERE marca = ? AND idMarca != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtiene marcas con productos asociados
     * @return lista de marcas que tienen productos
     */
    public List<Marca> obtenerMarcasConProductos() {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM Marcas m " +
                    "INNER JOIN Productos p ON m.idMarca = p.idMarca " +
                    "WHERE p.activo = true ORDER BY m.marca";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                marcas.add(mapearMarca(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener marcas con productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return marcas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Marca
     * @param rs ResultSet con los datos
     * @return objeto Marca
     * @throws SQLException si hay error en el mapeo
     */
    private Marca mapearMarca(ResultSet rs) throws SQLException {
        Marca marca = new Marca();
        marca.setIdMarca(rs.getInt("idMarca"));
        marca.setMarca(rs.getString("marca"));
        marca.setDescripcion(rs.getString("descripcion"));
        marca.setPaisOrigen(rs.getString("pais_origen"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            marca.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return marca;
    }
}
