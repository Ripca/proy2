package com.puntoventa.dao;

import com.puntoventa.models.Empleado;
import com.puntoventa.utils.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Empleado
 */
public class EmpleadoDAO {
    
    /**
     * Crea un nuevo empleado
     * @param empleado objeto Empleado a crear
     * @return ID del empleado creado, 0 si hay error
     */
    public int crear(Empleado empleado) {
        String sql = "INSERT INTO Empleados (nombres, apellidos, dpi, direccion, telefono, email, " +
                    "fecha_nacimiento, idPuesto, salario, fecha_contratacion, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getDpi());
            stmt.setString(4, empleado.getDireccion());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(6, empleado.getEmail());
            stmt.setDate(7, Date.valueOf(empleado.getFechaNacimiento()));
            stmt.setInt(8, empleado.getIdPuesto());
            stmt.setBigDecimal(9, empleado.getSalario());
            stmt.setDate(10, Date.valueOf(empleado.getFechaContratacion()));
            stmt.setBoolean(11, empleado.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear empleado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un empleado por su ID con información del puesto
     * @param idEmpleado ID del empleado
     * @return objeto Empleado o null si no existe
     */
    public Empleado obtenerPorId(int idEmpleado) {
        String sql = "SELECT e.*, p.puesto as nombre_puesto " +
                    "FROM Empleados e " +
                    "LEFT JOIN Puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEmpleado(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleado por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene un empleado por su DPI
     * @param dpi DPI del empleado
     * @return objeto Empleado o null si no existe
     */
    public Empleado obtenerPorDpi(String dpi) {
        String sql = "SELECT e.*, p.puesto as nombre_puesto " +
                    "FROM Empleados e " +
                    "LEFT JOIN Puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.dpi = ? AND e.activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dpi);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEmpleado(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleado por DPI: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los empleados activos
     * @return lista de empleados
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.*, p.puesto as nombre_puesto " +
                    "FROM Empleados e " +
                    "LEFT JOIN Puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.activo = true " +
                    "ORDER BY e.nombres, e.apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los empleados: " + e.getMessage());
            e.printStackTrace();
        }
        
        return empleados;
    }
    
    /**
     * Obtiene empleados por puesto
     * @param idPuesto ID del puesto
     * @return lista de empleados del puesto
     */
    public List<Empleado> obtenerPorPuesto(int idPuesto) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.*, p.puesto as nombre_puesto " +
                    "FROM Empleados e " +
                    "LEFT JOIN Puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE e.idPuesto = ? AND e.activo = true " +
                    "ORDER BY e.nombres, e.apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPuesto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados por puesto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return empleados;
    }
    
    /**
     * Busca empleados por nombre, apellido o DPI
     * @param termino término de búsqueda
     * @return lista de empleados que coinciden
     */
    public List<Empleado> buscar(String termino) {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.*, p.puesto as nombre_puesto " +
                    "FROM Empleados e " +
                    "LEFT JOIN Puestos p ON e.idPuesto = p.idPuesto " +
                    "WHERE (e.nombres LIKE ? OR e.apellidos LIKE ? OR e.dpi LIKE ?) " +
                    "AND e.activo = true " +
                    "ORDER BY e.nombres, e.apellidos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + termino + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar empleados: " + e.getMessage());
            e.printStackTrace();
        }
        
        return empleados;
    }
    
    /**
     * Actualiza un empleado
     * @param empleado objeto Empleado con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE Empleados SET nombres = ?, apellidos = ?, dpi = ?, direccion = ?, " +
                    "telefono = ?, email = ?, fecha_nacimiento = ?, idPuesto = ?, salario = ?, " +
                    "fecha_contratacion = ?, activo = ? WHERE idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getDpi());
            stmt.setString(4, empleado.getDireccion());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(6, empleado.getEmail());
            stmt.setDate(7, Date.valueOf(empleado.getFechaNacimiento()));
            stmt.setInt(8, empleado.getIdPuesto());
            stmt.setBigDecimal(9, empleado.getSalario());
            stmt.setDate(10, Date.valueOf(empleado.getFechaContratacion()));
            stmt.setBoolean(11, empleado.isActivo());
            stmt.setInt(12, empleado.getIdEmpleado());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un empleado (marca como inactivo)
     * @param idEmpleado ID del empleado a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idEmpleado) {
        String sql = "UPDATE Empleados SET activo = false WHERE idEmpleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de empleados activos
     * @return número total de empleados activos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Empleados WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar empleados: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe un empleado con el DPI dado
     * @param dpi DPI del empleado
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeDpi(String dpi, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Empleados WHERE dpi = ? AND idEmpleado != ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dpi);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de DPI: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Empleado
     * @param rs ResultSet con los datos
     * @return objeto Empleado
     * @throws SQLException si hay error en el mapeo
     */
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(rs.getInt("idEmpleado"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setDpi(rs.getString("dpi"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setEmail(rs.getString("email"));
        empleado.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        empleado.setIdPuesto(rs.getInt("idPuesto"));
        empleado.setNombrePuesto(rs.getString("nombre_puesto"));
        empleado.setSalario(rs.getBigDecimal("salario"));
        empleado.setFechaContratacion(rs.getDate("fecha_contratacion").toLocalDate());
        empleado.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            empleado.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        return empleado;
    }
}
