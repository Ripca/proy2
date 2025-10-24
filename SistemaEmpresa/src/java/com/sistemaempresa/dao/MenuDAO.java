package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Menu;
import java.sql.*;
import java.util.*;

public class MenuDAO {
    
    /**
     * Obtiene todos los menús principales (sin padre) con sus submenús en estructura jerárquica multinivel
     */
    public List<Menu> obtenerMenusJerarquicos() {
        List<Menu> menusPrincipales = new ArrayList<>();
        String sql = """
            SELECT
                m1.id_menu, m1.nombre, m1.icono, m1.url, m1.id_padre, m1.orden, m1.estado,
                m2.nombre as nombre_padre
            FROM menus m1
            LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
            WHERE m1.estado = 1
            ORDER BY m1.id_padre, m1.orden
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, Menu> mapaMenus = new HashMap<>();

            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setNombre(rs.getString("nombre"));
                menu.setIcono(rs.getString("icono"));
                menu.setUrl(rs.getString("url"));
                menu.setIdPadre(rs.getObject("id_padre", Integer.class));
                menu.setOrden(rs.getInt("orden"));
                menu.setEstado(rs.getBoolean("estado"));
                menu.setNombrePadre(rs.getString("nombre_padre"));

                mapaMenus.put(menu.getIdMenu(), menu);

                // Si es menú principal, agregarlo a la lista
                if (menu.esMenuPrincipal()) {
                    menusPrincipales.add(menu);
                }
            }

            // Construir estructura jerárquica multinivel
            construirArbolJerarquico(mapaMenus, menusPrincipales);

            // Ordenar menús principales
            menusPrincipales.sort(Comparator.comparingInt(Menu::getOrden));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menusPrincipales;
    }

    /**
     * Construye la estructura jerárquica multinivel del árbol de menús
     */
    private void construirArbolJerarquico(Map<Integer, Menu> mapaMenus, List<Menu> menusPrincipales) {
        // Organizar submenús bajo sus padres recursivamente
        for (Menu menu : mapaMenus.values()) {
            if (!menu.esMenuPrincipal()) {
                Menu padre = mapaMenus.get(menu.getIdPadre());
                if (padre != null) {
                    padre.agregarSubmenu(menu);
                }
            }
        }

        // Ordenar recursivamente todos los niveles
        for (Menu menu : menusPrincipales) {
            ordenarSubmenusRecursivo(menu);
        }
    }

    /**
     * Ordena los submenús recursivamente en todos los niveles
     */
    private void ordenarSubmenusRecursivo(Menu menu) {
        if (menu.tieneSubmenus()) {
            menu.getSubmenus().sort(Comparator.comparingInt(Menu::getOrden));
            // Ordenar recursivamente los submenús de los submenús
            for (Menu submenu : menu.getSubmenus()) {
                ordenarSubmenusRecursivo(submenu);
            }
        }
    }
    
    /**
     * Obtiene todos los menús (planos, sin jerarquía)
     */
    public List<Menu> obtenerTodos() {
        List<Menu> menus = new ArrayList<>();
        String sql = """
            SELECT 
                m1.id_menu, m1.nombre, m1.icono, m1.url, m1.id_padre, m1.orden, m1.estado,
                m2.nombre as nombre_padre
            FROM menus m1
            LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
            WHERE m1.estado = 1
            ORDER BY m1.orden
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setNombre(rs.getString("nombre"));
                menu.setIcono(rs.getString("icono"));
                menu.setUrl(rs.getString("url"));
                menu.setIdPadre(rs.getObject("id_padre", Integer.class));
                menu.setOrden(rs.getInt("orden"));
                menu.setEstado(rs.getBoolean("estado"));
                menu.setNombrePadre(rs.getString("nombre_padre"));
                
                menus.add(menu);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene solo los menús principales (sin padre)
     */
    public List<Menu> obtenerMenusPrincipales() {
        List<Menu> menus = new ArrayList<>();
        String sql = """
            SELECT id_menu, nombre, icono, url, id_padre, orden, estado
            FROM menus
            WHERE id_padre IS NULL AND estado = 1
            ORDER BY orden
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setNombre(rs.getString("nombre"));
                menu.setIcono(rs.getString("icono"));
                menu.setUrl(rs.getString("url"));
                menu.setIdPadre(rs.getObject("id_padre", Integer.class));
                menu.setOrden(rs.getInt("orden"));
                menu.setEstado(rs.getBoolean("estado"));
                
                menus.add(menu);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menus;
    }
    
    /**
     * Obtiene los submenús de un menú padre específico
     */
    public List<Menu> obtenerSubmenus(int idPadre) {
        List<Menu> submenus = new ArrayList<>();
        String sql = """
            SELECT id_menu, nombre, icono, url, id_padre, orden, estado
            FROM menus
            WHERE id_padre = ? AND estado = 1
            ORDER BY orden
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPadre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setIdMenu(rs.getInt("id_menu"));
                    menu.setNombre(rs.getString("nombre"));
                    menu.setIcono(rs.getString("icono"));
                    menu.setUrl(rs.getString("url"));
                    menu.setIdPadre(rs.getObject("id_padre", Integer.class));
                    menu.setOrden(rs.getInt("orden"));
                    menu.setEstado(rs.getBoolean("estado"));
                    
                    submenus.add(menu);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return submenus;
    }
    
    /**
     * Obtiene un menú por su ID
     */
    public Menu obtenerPorId(int idMenu) {
        String sql = """
            SELECT 
                m1.id_menu, m1.nombre, m1.icono, m1.url, m1.id_padre, m1.orden, m1.estado,
                m2.nombre as nombre_padre
            FROM menus m1
            LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
            WHERE m1.id_menu = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMenu);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Menu menu = new Menu();
                    menu.setIdMenu(rs.getInt("id_menu"));
                    menu.setNombre(rs.getString("nombre"));
                    menu.setIcono(rs.getString("icono"));
                    menu.setUrl(rs.getString("url"));
                    menu.setIdPadre(rs.getObject("id_padre", Integer.class));
                    menu.setOrden(rs.getInt("orden"));
                    menu.setEstado(rs.getBoolean("estado"));
                    menu.setNombrePadre(rs.getString("nombre_padre"));
                    
                    return menu;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
