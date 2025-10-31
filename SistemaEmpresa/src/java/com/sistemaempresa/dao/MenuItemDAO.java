package com.sistemaempresa.dao;

import com.sistemaempresa.models.MenuItem;
import com.sistemaempresa.config.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * DAO para operaciones CRUD de MenuItem
 */
public class MenuItemDAO {
    
    /**
     * Obtiene todos los elementos del menú activos
     */
    public List<MenuItem> obtenerTodos() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menuitems WHERE activo = TRUE ORDER BY padre_id, orden";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                MenuItem item = mapearResultSet(rs);
                menuItems.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menuItems;
    }
    
    /**
     * Construye la estructura de árbol del menú
     */
    public List<MenuItem> obtenerMenuEstructurado() {
        List<MenuItem> todosLosItems = obtenerTodos();
        Map<Integer, MenuItem> itemMap = new HashMap<>();
        List<MenuItem> menuRaiz = new ArrayList<>();

        // Crear mapa de items por ID
        for (MenuItem item : todosLosItems) {
            itemMap.put(item.getIdMenuItem(), item);
        }

        // Construir estructura de árbol
        for (MenuItem item : todosLosItems) {
            if (item.getPadreId() == null) {
                // Es un elemento raíz
                menuRaiz.add(item);
            } else {
                // Es un elemento hijo
                MenuItem padre = itemMap.get(item.getPadreId());
                if (padre != null) {
                    padre.agregarHijo(item);
                }
            }
        }

        // Ordenar elementos raíz y sus hijos recursivamente
        ordenarMenuRecursivo(menuRaiz);

        return menuRaiz;
    }

    /**
     * Obtiene el menú estructurado filtrado por rol del usuario
     */
    public List<MenuItem> obtenerMenuEstructuradoPorRol(int idUsuario) {
        // Obtener el rol del usuario
        UsuarioRolDAO usuarioRolDAO = new UsuarioRolDAO();
        int idRol = usuarioRolDAO.obtenerIdRolPorUsuario(idUsuario);

        if (idRol == -1) {
            // Si no tiene rol asignado, retorna menú vacío
            return new ArrayList<>();
        }

        // Obtener menús permitidos para el rol
        RolMenuDAO rolMenuDAO = new RolMenuDAO();
        List<Integer> menusPorRol = rolMenuDAO.obtenerMenusPorRol(idRol);

        // Obtener todos los menús
        List<MenuItem> todosLosItems = obtenerTodos();

        // Filtrar menús según el rol
        List<MenuItem> itemsFiltrados = new ArrayList<>();
        for (MenuItem item : todosLosItems) {
            if (menusPorRol.contains(item.getIdMenuItem())) {
                itemsFiltrados.add(item);
            }
        }

        // Construir estructura de árbol con items filtrados
        Map<Integer, MenuItem> itemMap = new HashMap<>();
        List<MenuItem> menuRaiz = new ArrayList<>();

        // Crear mapa de items filtrados por ID
        for (MenuItem item : itemsFiltrados) {
            itemMap.put(item.getIdMenuItem(), item);
        }

        // Construir estructura de árbol
        for (MenuItem item : itemsFiltrados) {
            if (item.getPadreId() == null) {
                // Es un elemento raíz
                menuRaiz.add(item);
            } else {
                // Es un elemento hijo
                MenuItem padre = itemMap.get(item.getPadreId());
                if (padre != null) {
                    padre.agregarHijo(item);
                }
            }
        }

        // Ordenar elementos raíz y sus hijos recursivamente
        ordenarMenuRecursivo(menuRaiz);

        return menuRaiz;
    }
    
    /**
     * Ordena el menú recursivamente por el campo orden
     */
    private void ordenarMenuRecursivo(List<MenuItem> items) {
        items.sort(Comparator.comparingInt(MenuItem::getOrden));
        
        for (MenuItem item : items) {
            if (item.tieneHijos()) {
                ordenarMenuRecursivo(item.getHijos());
            }
        }
    }
    
    /**
     * Obtiene elementos hijos de un padre específico
     */
    public List<MenuItem> obtenerHijos(int padreId) {
        List<MenuItem> hijos = new ArrayList<>();
        String sql = "SELECT * FROM menuitems WHERE padre_id = ? AND activo = TRUE ORDER BY orden";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, padreId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = mapearResultSet(rs);
                    hijos.add(item);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return hijos;
    }
    
    /**
     * Obtiene elementos raíz (sin padre)
     */
    public List<MenuItem> obtenerElementosRaiz() {
        List<MenuItem> raiz = new ArrayList<>();
        String sql = "SELECT * FROM menuitems WHERE padre_id IS NULL AND activo = TRUE ORDER BY orden";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                MenuItem item = mapearResultSet(rs);
                raiz.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return raiz;
    }
    
    /**
     * Inserta un nuevo elemento del menú
     */
    public boolean insertar(MenuItem menuItem) {
        String sql = "INSERT INTO menuitems (titulo, url, icono, padre_id, orden, activo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menuItem.getTitulo());
            stmt.setString(2, menuItem.getUrl());
            stmt.setString(3, menuItem.getIcono());
            
            if (menuItem.getPadreId() != null) {
                stmt.setInt(4, menuItem.getPadreId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setInt(5, menuItem.getOrden());
            stmt.setBoolean(6, menuItem.isActivo());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto MenuItem
     */
    private MenuItem mapearResultSet(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setIdMenuItem(rs.getInt("idMenuItem"));
        item.setTitulo(rs.getString("titulo"));
        item.setUrl(rs.getString("url"));
        item.setIcono(rs.getString("icono"));
        
        int padreId = rs.getInt("padre_id");
        if (!rs.wasNull()) {
            item.setPadreId(padreId);
        }
        
        item.setOrden(rs.getInt("orden"));
        item.setActivo(rs.getBoolean("activo"));
        item.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        
        return item;
    }
}
