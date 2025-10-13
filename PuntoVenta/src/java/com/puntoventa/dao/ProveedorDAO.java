package com.puntoventa.dao;

import com.puntoventa.models.Proveedor;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Proveedor
 */
public class ProveedorDAO {
    
    /**
     * Crea un nuevo proveedor
     * @param proveedor objeto Proveedor a crear
     * @return ID del proveedor creado, 0 si hay error
     */
    public int crear(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedores (proveedor, nit, direccion, telefono, email, contacto, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, proveedor.getProveedor());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getEmail());
            stmt.setString(6, proveedor.getContacto());
            stmt.setBoolean(7, proveedor.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un proveedor por su ID
     * @param idProveedor ID del proveedor
     * @return objeto Proveedor o null si no existe
     */
    public Proveedor obtenerPorId(int idProveedor) {
        String sql = "SELECT * FROM Proveedores WHERE idProveedor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProveedor);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProveedor(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene un proveedor por su NIT
     * @param nit NIT del proveedor
     * @return objeto Proveedor o null si no existe
     */
    public Proveedor obtenerPorNit(String nit) {
        String sql = "SELECT * FROM Proveedores WHERE nit = ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nit);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProveedor(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por NIT: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los proveedores activos
     * @return lista de proveedores
     */
    public List<Proveedor> obtenerTodos() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores WHERE activo = true ORDER BY proveedor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                proveedores.add(mapearProveedor(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los proveedores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return proveedores;
    }
    
    /**
     * Busca proveedores por nombre o NIT
     * @param termino término de búsqueda
     * @return lista de proveedores que coinciden
     */
    public List<Proveedor> buscar(String termino) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores WHERE (proveedor LIKE ? OR nit LIKE ?) " +
                    "AND activo = true ORDER BY proveedor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + termino + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                proveedores.add(mapearProveedor(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar proveedores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return proveedores;
    }
    
    /**
     * Actualiza un proveedor
     * @param proveedor objeto Proveedor con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Proveedor proveedor) {
        String sql = "UPDATE Proveedores SET proveedor = ?, nit = ?, direccion = ?, telefono = ?, " +
                    "email = ?, contacto = ?, activo = ? WHERE idProveedor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, proveedor.getProveedor());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getEmail());
            stmt.setString(6, proveedor.getContacto());
            stmt.setBoolean(7, proveedor.isActivo());
            stmt.setInt(8, proveedor.getIdProveedor());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un proveedor (marca como inactivo)
     * @param idProveedor ID del proveedor a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idProveedor) {
        String sql = "UPDATE Proveedores SET activo = false WHERE idProveedor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProveedor);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de proveedores activos
     * @return número total de proveedores activos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Proveedores WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar proveedores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe un proveedor con el NIT dado
     * @param nit NIT del proveedor
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeNit(String nit, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Proveedores WHERE nit = ? AND idProveedor != ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nit);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de NIT: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica si existe un proveedor con el nombre dado
     * @param nombre nombre del proveedor
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeProveedor(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Proveedores WHERE proveedor = ? AND idProveedor != ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Proveedor
     * @param rs ResultSet con los datos
     * @return objeto Proveedor
     * @throws SQLException si hay error en el mapeo
     */
    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(rs.getInt("idProveedor"));
        proveedor.setProveedor(rs.getString("proveedor"));
        proveedor.setNit(rs.getString("nit"));
        proveedor.setDireccion(rs.getString("direccion"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setEmail(rs.getString("email"));
        proveedor.setContacto(rs.getString("contacto"));
        proveedor.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            proveedor.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        return proveedor;
    }
}
