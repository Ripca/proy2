package com.puntoventa.dao;

import com.puntoventa.models.Menu;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para la entidad Menu
 */
public class MenuDAO {
    
    /**
     * Crea un nuevo menú
     * @param menu objeto Menu a crear
     * @return ID del menú creado, 0 si hay error
     */
    public int crear(Menu menu) {
        String sql = "INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, menu.getNombre());
            stmt.setString(2, menu.getUrl());
            stmt.setString(3, menu.getIcono());
            stmt.setInt(4, menu.getOrden());
            if (menu.getIdMenuPadre() > 0) {
                stmt.setInt(5, menu.getIdMenuPadre());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, menu.getRolesPermitidos());
            stmt.setBoolean(7, menu.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear menú: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un menú por su ID
     * @param idMenu ID del menú
     * @return objeto Menu o null si no existe
     */
    public Menu obtenerPorId(int idMenu) {
        String sql = "SELECT * FROM Menus WHERE idMenu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMenu);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearMenu(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener menú por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los menús activos
     * @return lista de menús
     */
    public List<Menu> obtenerTodos() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menus WHERE activo = true ORDER BY orden, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                menus.add(mapearMenu(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los menús: " + e.getMessage());
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene menús principales (sin padre)
     * @return lista de menús principales
     */
    public List<Menu> obtenerMenusPrincipales() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menus WHERE idMenuPadre IS NULL AND activo = true ORDER BY orden, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                menus.add(mapearMenu(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener menús principales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene submenús de un menú padre
     * @param idMenuPadre ID del menú padre
     * @return lista de submenús
     */
    public List<Menu> obtenerSubmenus(int idMenuPadre) {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menus WHERE idMenuPadre = ? AND activo = true ORDER BY orden, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMenuPadre);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                menus.add(mapearMenu(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener submenús: " + e.getMessage());
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene menús accesibles por un rol específico
     * @param rol rol del usuario
     * @return lista de menús accesibles
     */
    public List<Menu> obtenerMenusPorRol(String rol) {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menus WHERE (roles_permitidos LIKE ? OR roles_permitidos = 'ALL') " +
                    "AND activo = true ORDER BY orden, nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + rol + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Menu menu = mapearMenu(rs);
                if (menu.tieneAcceso(rol)) {
                    menus.add(menu);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener menús por rol: " + e.getMessage());
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene la estructura jerárquica completa de menús para un rol
     * @param rol rol del usuario
     * @return lista de menús principales con sus submenús
     */
    public List<Menu> obtenerMenusJerarquicos(String rol) {
        List<Menu> menusPrincipales = new ArrayList<>();
        
        // Obtener todos los menús accesibles por el rol
        List<Menu> todosLosMenus = obtenerMenusPorRol(rol);
        
        // Crear un mapa para acceso rápido por ID
        Map<Integer, Menu> mapaMenus = new HashMap<>();
        for (Menu menu : todosLosMenus) {
            mapaMenus.put(menu.getIdMenu(), menu);
        }
        
        // Construir la jerarquía
        for (Menu menu : todosLosMenus) {
            if (menu.getIdMenuPadre() == 0) {
                // Es un menú principal
                menusPrincipales.add(menu);
            } else {
                // Es un submenú, agregarlo a su padre
                Menu padre = mapaMenus.get(menu.getIdMenuPadre());
                if (padre != null) {
                    padre.agregarSubmenu(menu);
                }
            }
        }
        
        return menusPrincipales;
    }
    
    /**
     * Actualiza un menú
     * @param menu objeto Menu con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Menu menu) {
        String sql = "UPDATE Menus SET nombre = ?, url = ?, icono = ?, orden = ?, " +
                    "idMenuPadre = ?, roles_permitidos = ?, activo = ? WHERE idMenu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getNombre());
            stmt.setString(2, menu.getUrl());
            stmt.setString(3, menu.getIcono());
            stmt.setInt(4, menu.getOrden());
            if (menu.getIdMenuPadre() > 0) {
                stmt.setInt(5, menu.getIdMenuPadre());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, menu.getRolesPermitidos());
            stmt.setBoolean(7, menu.isActivo());
            stmt.setInt(8, menu.getIdMenu());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar menú: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un menú (marca como inactivo)
     * @param idMenu ID del menú a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idMenu) {
        String sql = "UPDATE Menus SET activo = false WHERE idMenu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMenu);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar menú: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de menús activos
     * @return número total de menús activos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Menus WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar menús: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Mapea un ResultSet a un objeto Menu
     * @param rs ResultSet con los datos
     * @return objeto Menu
     * @throws SQLException si hay error en el mapeo
     */
    private Menu mapearMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setIdMenu(rs.getInt("idMenu"));
        menu.setNombre(rs.getString("nombre"));
        menu.setUrl(rs.getString("url"));
        menu.setIcono(rs.getString("icono"));
        menu.setOrden(rs.getInt("orden"));
        menu.setIdMenuPadre(rs.getInt("idMenuPadre"));
        menu.setRolesPermitidos(rs.getString("roles_permitidos"));
        menu.setActivo(rs.getBoolean("activo"));
        
        return menu;
    }
}
