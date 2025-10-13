package com.puntoventa.dao;

import com.puntoventa.models.Cliente;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Cliente
 */
public class ClienteDAO {
    
    /**
     * Crea un nuevo cliente
     * @param cliente objeto Cliente a crear
     * @return ID del cliente creado, 0 si hay error
     */
    public int crear(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nit, nombres, apellidos, direccion, telefono, email, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNit());
            stmt.setString(2, cliente.getNombres());
            stmt.setString(3, cliente.getApellidos());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
            stmt.setBoolean(7, cliente.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un cliente por su ID
     * @param idCliente ID del cliente
     * @return objeto Cliente o null si no existe
     */
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT * FROM Clientes WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene un cliente por su NIT
     * @param nit NIT del cliente
     * @return objeto Cliente o null si no existe
     */
    public Cliente obtenerPorNit(String nit) {
        String sql = "SELECT * FROM Clientes WHERE nit = ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nit);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente por NIT: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los clientes activos
     * @return lista de clientes
     */
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE activo = true ORDER BY nombres, apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clientes;
    }
    
    /**
     * Busca clientes por nombre, apellido o NIT
     * @param termino término de búsqueda
     * @return lista de clientes que coinciden
     */
    public List<Cliente> buscar(String termino) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes WHERE (nombres LIKE ? OR apellidos LIKE ? OR nit LIKE ?) " +
                    "AND activo = true ORDER BY nombres, apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + termino + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clientes;
    }
    
    /**
     * Actualiza un cliente
     * @param cliente objeto Cliente con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Clientes SET nit = ?, nombres = ?, apellidos = ?, direccion = ?, " +
                    "telefono = ?, email = ?, activo = ? WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNit());
            stmt.setString(2, cliente.getNombres());
            stmt.setString(3, cliente.getApellidos());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
            stmt.setBoolean(7, cliente.isActivo());
            stmt.setInt(8, cliente.getIdCliente());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un cliente (marca como inactivo)
     * @param idCliente ID del cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idCliente) {
        String sql = "UPDATE Clientes SET activo = false WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de clientes activos
     * @return número total de clientes activos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Clientes WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe un cliente con el NIT dado
     * @param nit NIT del cliente
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeNit(String nit, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Clientes WHERE nit = ? AND idCliente != ? AND activo = true";
        
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
     * Obtiene clientes con más compras (top clientes)
     * @param limite número máximo de clientes a retornar
     * @return lista de clientes ordenados por número de compras
     */
    public List<Cliente> obtenerTopClientes(int limite) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(v.idVenta) as total_compras " +
                    "FROM Clientes c " +
                    "LEFT JOIN Ventas v ON c.idCliente = v.idCliente " +
                    "WHERE c.activo = true " +
                    "GROUP BY c.idCliente " +
                    "ORDER BY total_compras DESC " +
                    "LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = mapearCliente(rs);
                cliente.setTotalCompras(rs.getInt("total_compras"));
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener top clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clientes;
    }
    
    /**
     * Mapea un ResultSet a un objeto Cliente
     * @param rs ResultSet con los datos
     * @return objeto Cliente
     * @throws SQLException si hay error en el mapeo
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNit(rs.getString("nit"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        cliente.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            cliente.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        return cliente;
    }
}
