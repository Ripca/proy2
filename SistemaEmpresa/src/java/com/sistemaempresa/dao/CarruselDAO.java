package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.CarruselImagen;
import java.sql.*;
import java.util.*;

public class CarruselDAO {
    
    /**
     * Obtiene todas las imágenes activas del carrusel ordenadas por orden
     */
    public List<CarruselImagen> obtenerImagenesActivas() {
        List<CarruselImagen> imagenes = new ArrayList<>();
        String sql = """
            SELECT id_imagen, titulo, url_imagen, descripcion, orden, estado, fecha_creacion
            FROM carrusel_imagenes
            WHERE estado = 1
            ORDER BY orden ASC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                CarruselImagen imagen = new CarruselImagen();
                imagen.setIdImagen(rs.getInt("id_imagen"));
                imagen.setTitulo(rs.getString("titulo"));
                imagen.setUrlImagen(rs.getString("url_imagen"));
                imagen.setDescripcion(rs.getString("descripcion"));
                imagen.setOrden(rs.getInt("orden"));
                imagen.setEstado(rs.getBoolean("estado"));
                
                Timestamp timestamp = rs.getTimestamp("fecha_creacion");
                if (timestamp != null) {
                    imagen.setFechaCreacion(timestamp.toLocalDateTime());
                }
                
                imagenes.add(imagen);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return imagenes;
    }
    
    /**
     * Obtiene todas las imágenes del carrusel (activas e inactivas)
     */
    public List<CarruselImagen> obtenerTodas() {
        List<CarruselImagen> imagenes = new ArrayList<>();
        String sql = """
            SELECT id_imagen, titulo, url_imagen, descripcion, orden, estado, fecha_creacion
            FROM carrusel_imagenes
            ORDER BY orden ASC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                CarruselImagen imagen = new CarruselImagen();
                imagen.setIdImagen(rs.getInt("id_imagen"));
                imagen.setTitulo(rs.getString("titulo"));
                imagen.setUrlImagen(rs.getString("url_imagen"));
                imagen.setDescripcion(rs.getString("descripcion"));
                imagen.setOrden(rs.getInt("orden"));
                imagen.setEstado(rs.getBoolean("estado"));
                
                Timestamp timestamp = rs.getTimestamp("fecha_creacion");
                if (timestamp != null) {
                    imagen.setFechaCreacion(timestamp.toLocalDateTime());
                }
                
                imagenes.add(imagen);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return imagenes;
    }
    
    /**
     * Obtiene una imagen por su ID
     */
    public CarruselImagen obtenerPorId(int idImagen) {
        String sql = """
            SELECT id_imagen, titulo, url_imagen, descripcion, orden, estado, fecha_creacion
            FROM carrusel_imagenes
            WHERE id_imagen = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idImagen);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CarruselImagen imagen = new CarruselImagen();
                    imagen.setIdImagen(rs.getInt("id_imagen"));
                    imagen.setTitulo(rs.getString("titulo"));
                    imagen.setUrlImagen(rs.getString("url_imagen"));
                    imagen.setDescripcion(rs.getString("descripcion"));
                    imagen.setOrden(rs.getInt("orden"));
                    imagen.setEstado(rs.getBoolean("estado"));
                    
                    Timestamp timestamp = rs.getTimestamp("fecha_creacion");
                    if (timestamp != null) {
                        imagen.setFechaCreacion(timestamp.toLocalDateTime());
                    }
                    
                    return imagen;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Inserta una nueva imagen en el carrusel
     */
    public boolean insertar(CarruselImagen imagen) {
        String sql = """
            INSERT INTO carrusel_imagenes (titulo, url_imagen, descripcion, orden, estado)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, imagen.getTitulo());
            stmt.setString(2, imagen.getUrlImagen());
            stmt.setString(3, imagen.getDescripcion());
            stmt.setInt(4, imagen.getOrden());
            stmt.setBoolean(5, imagen.isEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        imagen.setIdImagen(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza una imagen existente
     */
    public boolean actualizar(CarruselImagen imagen) {
        String sql = """
            UPDATE carrusel_imagenes 
            SET titulo = ?, url_imagen = ?, descripcion = ?, orden = ?, estado = ?
            WHERE id_imagen = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, imagen.getTitulo());
            stmt.setString(2, imagen.getUrlImagen());
            stmt.setString(3, imagen.getDescripcion());
            stmt.setInt(4, imagen.getOrden());
            stmt.setBoolean(5, imagen.isEstado());
            stmt.setInt(6, imagen.getIdImagen());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una imagen del carrusel
     */
    public boolean eliminar(int idImagen) {
        String sql = "DELETE FROM carrusel_imagenes WHERE id_imagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idImagen);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cambia el estado de una imagen (activar/desactivar)
     */
    public boolean cambiarEstado(int idImagen, boolean estado) {
        String sql = "UPDATE carrusel_imagenes SET estado = ? WHERE id_imagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, estado);
            stmt.setInt(2, idImagen);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
