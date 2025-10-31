package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Empleado
 */
public class EmpleadoDAO {
    
    /**
     * Obtiene todos los empleados con información del puesto
     * @return lista de empleados
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.*, p.puesto as nombrePuesto FROM empleados e " +
                    "LEFT JOIN puestos p ON e.idPuesto = p.idPuesto " +
                    "ORDER BY e.nombres, e.apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Empleado empleado = mapearResultSet(rs);
                empleados.add(empleado);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return empleados;
    }
    
    /**
     * Obtiene un empleado por ID
     * @param idEmpleado ID del empleado
     * @return empleado encontrado o null
     */
    public Empleado obtenerPorId(int idEmpleado) {
        String sql = "SELECT e.*, p.puesto as nombrePuesto FROM empleados e " +
                    "LEFT JOIN puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
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
     * Inserta un nuevo empleado
     * @param empleado empleado a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombres, apellidos, direccion, telefono, DPI, genero, " +
                    "fecha_nacimiento, idPuesto, fecha_inicio_labores) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getDireccion());
            stmt.setString(4, empleado.getTelefono());
            stmt.setString(5, empleado.getDpi());
            stmt.setBoolean(6, empleado.isGenero());
            stmt.setDate(7, empleado.getFechaNacimiento() != null ? 
                        new java.sql.Date(empleado.getFechaNacimiento().getTime()) : null);
            stmt.setInt(8, empleado.getIdPuesto());
            stmt.setDate(9, empleado.getFechaInicioLabores() != null ? 
                        new java.sql.Date(empleado.getFechaInicioLabores().getTime()) : null);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    empleado.setIdEmpleado(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza un empleado existente
     * @param empleado empleado a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombres = ?, apellidos = ?, direccion = ?, telefono = ?, " +
                    "DPI = ?, genero = ?, fecha_nacimiento = ?, idPuesto = ?, fecha_inicio_labores = ? " +
                    "WHERE idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getDireccion());
            stmt.setString(4, empleado.getTelefono());
            stmt.setString(5, empleado.getDpi());
            stmt.setBoolean(6, empleado.isGenero());
            stmt.setDate(7, empleado.getFechaNacimiento() != null ? 
                        new java.sql.Date(empleado.getFechaNacimiento().getTime()) : null);
            stmt.setInt(8, empleado.getIdPuesto());
            stmt.setDate(9, empleado.getFechaInicioLabores() != null ? 
                        new java.sql.Date(empleado.getFechaInicioLabores().getTime()) : null);
            stmt.setInt(10, empleado.getIdEmpleado());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un empleado
     * @param idEmpleado ID del empleado a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int idEmpleado) {
        String sql = "DELETE FROM empleados WHERE idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca empleados por nombre o DPI
     * @param termino término de búsqueda
     * @return lista de empleados encontrados
     */
    public List<Empleado> buscar(String termino) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.*, p.puesto as nombrePuesto FROM empleados e " +
                    "LEFT JOIN puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.nombres LIKE ? OR e.apellidos LIKE ? OR e.DPI LIKE ? " +
                    "ORDER BY e.nombres, e.apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + termino + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);
            stmt.setString(3, patron);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Empleado empleado = mapearResultSet(rs);
                empleados.add(empleado);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return empleados;
    }
    
    /**
     * Mapea un ResultSet a un objeto Empleado
     * @param rs ResultSet
     * @return objeto Empleado
     * @throws SQLException si hay error en el mapeo
     */
    private Empleado mapearResultSet(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(rs.getInt("idEmpleado"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setDpi(rs.getString("DPI"));
        empleado.setGenero(rs.getBoolean("genero"));
        empleado.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        empleado.setIdPuesto(rs.getInt("idPuesto"));
        empleado.setNombrePuesto(rs.getString("nombrePuesto"));
        empleado.setFechaInicioLabores(rs.getDate("fecha_inicio_labores"));
        empleado.setFechaIngreso(rs.getTimestamp("fechaingreso"));
        return empleado;
    }
}
