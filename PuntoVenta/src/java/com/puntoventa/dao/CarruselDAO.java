package com.puntoventa.dao;

import com.puntoventa.models.CarruselImagen;
import com.puntoventa.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad CarruselImagen
 */
public class CarruselDAO {
    
    /**
     * Crea una nueva imagen de carrusel
     * @param imagen objeto CarruselImagen a crear
     * @return ID de la imagen creada, 0 si hay error
     */
    public int crear(CarruselImagen imagen) {
        String sql = "INSERT INTO CarruselImagenes (titulo, descripcion, imagen_url, enlace_url, orden, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, imagen.getTitulo());
            stmt.setString(2, imagen.getDescripcion());
            stmt.setString(3, imagen.getImagenUrl());
            stmt.setString(4, imagen.getEnlaceUrl());
            stmt.setInt(5, imagen.getOrden());
            stmt.setBoolean(6, imagen.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear imagen de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene una imagen de carrusel por su ID
     * @param idImagen ID de la imagen
     * @return objeto CarruselImagen o null si no existe
     */
    public CarruselImagen obtenerPorId(int idImagen) {
        String sql = "SELECT * FROM CarruselImagenes WHERE idImagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idImagen);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCarruselImagen(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener imagen de carrusel por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las imágenes de carrusel activas ordenadas por orden
     * @return lista de imágenes de carrusel
     */
    public List<CarruselImagen> obtenerTodas() {
        List<CarruselImagen> imagenes = new ArrayList<>();
        String sql = "SELECT * FROM CarruselImagenes WHERE activo = true ORDER BY orden, idImagen";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                imagenes.add(mapearCarruselImagen(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las imágenes de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return imagenes;
    }
    
    /**
     * Obtiene las imágenes de carrusel activas para mostrar en el dashboard
     * @param limite número máximo de imágenes a retornar
     * @return lista de imágenes de carrusel
     */
    public List<CarruselImagen> obtenerImagenesActivas(int limite) {
        List<CarruselImagen> imagenes = new ArrayList<>();
        String sql = "SELECT * FROM CarruselImagenes WHERE activo = true ORDER BY orden, idImagen LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                imagenes.add(mapearCarruselImagen(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener imágenes activas de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return imagenes;
    }
    
    /**
     * Actualiza una imagen de carrusel
     * @param imagen objeto CarruselImagen con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(CarruselImagen imagen) {
        String sql = "UPDATE CarruselImagenes SET titulo = ?, descripcion = ?, imagen_url = ?, " +
                    "enlace_url = ?, orden = ?, activo = ? WHERE idImagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, imagen.getTitulo());
            stmt.setString(2, imagen.getDescripcion());
            stmt.setString(3, imagen.getImagenUrl());
            stmt.setString(4, imagen.getEnlaceUrl());
            stmt.setInt(5, imagen.getOrden());
            stmt.setBoolean(6, imagen.isActivo());
            stmt.setInt(7, imagen.getIdImagen());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar imagen de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza el orden de una imagen de carrusel
     * @param idImagen ID de la imagen
     * @param nuevoOrden nuevo orden
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarOrden(int idImagen, int nuevoOrden) {
        String sql = "UPDATE CarruselImagenes SET orden = ? WHERE idImagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevoOrden);
            stmt.setInt(2, idImagen);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar orden de imagen: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una imagen de carrusel (marca como inactiva)
     * @param idImagen ID de la imagen a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idImagen) {
        String sql = "UPDATE CarruselImagenes SET activo = false WHERE idImagen = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idImagen);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar imagen de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de imágenes de carrusel activas
     * @return número total de imágenes activas
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM CarruselImagenes WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar imágenes de carrusel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene el siguiente número de orden disponible
     * @return siguiente número de orden
     */
    public int obtenerSiguienteOrden() {
        String sql = "SELECT COALESCE(MAX(orden), 0) + 1 FROM CarruselImagenes";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente orden: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 1;
    }
    
    /**
     * Reordena las imágenes de carrusel
     * @param imagenesOrdenadas lista de IDs en el nuevo orden
     * @return true si se reordenó correctamente, false en caso contrario
     */
    public boolean reordenar(List<Integer> imagenesOrdenadas) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            String sql = "UPDATE CarruselImagenes SET orden = ? WHERE idImagen = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < imagenesOrdenadas.size(); i++) {
                    stmt.setInt(1, i + 1);
                    stmt.setInt(2, imagenesOrdenadas.get(i));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al reordenar imágenes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto CarruselImagen
     * @param rs ResultSet con los datos
     * @return objeto CarruselImagen
     * @throws SQLException si hay error en el mapeo
     */
    private CarruselImagen mapearCarruselImagen(ResultSet rs) throws SQLException {
        CarruselImagen imagen = new CarruselImagen();
        imagen.setIdImagen(rs.getInt("idImagen"));
        imagen.setTitulo(rs.getString("titulo"));
        imagen.setDescripcion(rs.getString("descripcion"));
        imagen.setImagenUrl(rs.getString("imagen_url"));
        imagen.setEnlaceUrl(rs.getString("enlace_url"));
        imagen.setOrden(rs.getInt("orden"));
        imagen.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            imagen.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return imagen;
    }
}
