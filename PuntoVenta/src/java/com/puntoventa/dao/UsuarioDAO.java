package com.puntoventa.dao;

import com.puntoventa.models.Usuario;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Usuario
 */
public class UsuarioDAO {
    
    /**
     * Crea un nuevo usuario
     * @param usuario objeto Usuario a crear
     * @return ID del usuario creado, 0 si hay error
     */
    public int crear(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (usuario, password, idEmpleado, rol, activo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getPassword());
            stmt.setInt(3, usuario.getIdEmpleado());
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un usuario por su ID con información del empleado
     * @param idUsuario ID del usuario
     * @return objeto Usuario o null si no existe
     */
    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT u.*, CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Usuarios u " +
                    "LEFT JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene un usuario por nombre de usuario
     * @param nombreUsuario nombre de usuario
     * @return objeto Usuario o null si no existe
     */
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT u.*, CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Usuarios u " +
                    "LEFT JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Valida las credenciales de un usuario
     * @param nombreUsuario nombre de usuario
     * @param password contraseña
     * @return objeto Usuario si las credenciales son válidas, null en caso contrario
     */
    public Usuario validarCredenciales(String nombreUsuario, String password) {
        String sql = "SELECT u.*, CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Usuarios u " +
                    "LEFT JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.usuario = ? AND u.password = ? AND u.activo = true AND u.bloqueado = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, password); // En producción debería usar hash
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = mapearUsuario(rs);
                // Actualizar fecha de último acceso
                actualizarUltimoAcceso(usuario.getIdUsuario());
                // Resetear intentos fallidos
                resetearIntentosFallidos(usuario.getIdUsuario());
                return usuario;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los usuarios activos
     * @return lista de usuarios
     */
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Usuarios u " +
                    "LEFT JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.activo = true " +
                    "ORDER BY u.usuario";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
    /**
     * Obtiene usuarios por rol
     * @param rol rol de los usuarios
     * @return lista de usuarios con el rol especificado
     */
    public List<Usuario> obtenerPorRol(String rol) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Usuarios u " +
                    "LEFT JOIN Empleados e ON u.idEmpleado = e.idEmpleado " +
                    "WHERE u.rol = ? AND u.activo = true " +
                    "ORDER BY u.usuario";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, rol);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios por rol: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
    /**
     * Actualiza un usuario
     * @param usuario objeto Usuario con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE Usuarios SET usuario = ?, password = ?, idEmpleado = ?, rol = ?, activo = ? WHERE idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getPassword());
            stmt.setInt(3, usuario.getIdEmpleado());
            stmt.setString(4, usuario.getRol());
            stmt.setBoolean(5, usuario.isActivo());
            stmt.setInt(6, usuario.getIdUsuario());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza la fecha de último acceso de un usuario
     * @param idUsuario ID del usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE Usuarios SET fecha_ultimo_acceso = CURRENT_TIMESTAMP WHERE idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Incrementa los intentos fallidos de un usuario
     * @param nombreUsuario nombre de usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean incrementarIntentosFallidos(String nombreUsuario) {
        String sql = "UPDATE Usuarios SET intentos_fallidos = intentos_fallidos + 1, " +
                    "bloqueado = CASE WHEN intentos_fallidos >= 2 THEN true ELSE false END " +
                    "WHERE usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al incrementar intentos fallidos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Resetea los intentos fallidos de un usuario
     * @param idUsuario ID del usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean resetearIntentosFallidos(int idUsuario) {
        String sql = "UPDATE Usuarios SET intentos_fallidos = 0, bloqueado = false WHERE idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al resetear intentos fallidos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un usuario (marca como inactivo)
     * @param idUsuario ID del usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idUsuario) {
        String sql = "UPDATE Usuarios SET activo = false WHERE idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verifica si existe un usuario con el nombre dado
     * @param nombreUsuario nombre de usuario
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeUsuario(String nombreUsuario, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE usuario = ? AND idUsuario != ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Usuario
     * @param rs ResultSet con los datos
     * @return objeto Usuario
     * @throws SQLException si hay error en el mapeo
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setPassword(rs.getString("password"));
        usuario.setIdEmpleado(rs.getInt("idEmpleado"));
        usuario.setNombreEmpleado(rs.getString("nombre_empleado"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        usuario.setBloqueado(rs.getBoolean("bloqueado"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp fechaUltimoAcceso = rs.getTimestamp("fecha_ultimo_acceso");
        if (fechaUltimoAcceso != null) {
            usuario.setFechaUltimoAcceso(fechaUltimoAcceso.toLocalDateTime());
        }
        
        return usuario;
    }
}
