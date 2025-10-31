package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Cliente
 */
public class ClienteDAO {
    
    /**
     * Obtiene todos los clientes
     * @return lista de clientes
     */
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombres, apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = mapearResultSet(rs);
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return clientes;
    }
    
    /**
     * Obtiene un cliente por ID
     * @param idCliente ID del cliente
     * @return cliente encontrado o null
     */
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT * FROM clientes WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Inserta un nuevo cliente
     * @param cliente cliente a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombres());
            stmt.setString(2, cliente.getApellidos());
            stmt.setString(3, cliente.getNit());
            stmt.setBoolean(4, cliente.isGenero());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getCorreoElectronico());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza un cliente existente
     * @param cliente cliente a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombres = ?, apellidos = ?, NIT = ?, genero = ?, " +
                    "telefono = ?, correo_electronico = ? WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombres());
            stmt.setString(2, cliente.getApellidos());
            stmt.setString(3, cliente.getNit());
            stmt.setBoolean(4, cliente.isGenero());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getCorreoElectronico());
            stmt.setInt(7, cliente.getIdCliente());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un cliente
     * @param idCliente ID del cliente a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM clientes WHERE idCliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca clientes por nombre o NIT
     * @param termino término de búsqueda
     * @return lista de clientes encontrados
     */
    public List<Cliente> buscar(String termino) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nombres LIKE ? OR apellidos LIKE ? OR NIT LIKE ? " +
                    "ORDER BY nombres, apellidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String patron = "%" + termino + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            stmt.setString(3, patron);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = mapearResultSet(rs);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    /**
     * Busca un cliente por NIT
     * @param nit NIT del cliente
     * @return Cliente encontrado o null
     */
    public Cliente obtenerPorNit(String nit) {
        String sql = "SELECT * FROM Clientes WHERE NIT = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Cliente
     * @param rs ResultSet
     * @return objeto Cliente
     * @throws SQLException si hay error en el mapeo
     */
    private Cliente mapearResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setNit(rs.getString("NIT"));
        cliente.setGenero(rs.getBoolean("genero"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreoElectronico(rs.getString("correo_electronico"));
        cliente.setFechaIngreso(rs.getTimestamp("fechaingreso"));
        return cliente;
    }
}
