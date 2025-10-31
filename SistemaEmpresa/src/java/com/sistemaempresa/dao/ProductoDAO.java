package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Producto
 */
public class ProductoDAO {
    
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, m.marca as nombreMarca FROM productos p " +
                    "LEFT JOIN marcas m ON p.idMarca = m.idMarca " +
                    "ORDER BY p.producto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = mapearResultSet(rs);
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productos;
    }
    
    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT p.*, m.marca as nombreMarca FROM productos p " +
                    "LEFT JOIN marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (producto, idMarca, descripcion, imagen, " +
                    "precio_costo, precio_venta, existencia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getProducto());
            stmt.setInt(2, producto.getIdMarca());
            stmt.setString(3, producto.getDescripcion());
            stmt.setString(4, producto.getImagen());
            stmt.setBigDecimal(5, producto.getPrecioCosto());
            stmt.setBigDecimal(6, producto.getPrecioVenta());
            stmt.setInt(7, producto.getExistencia());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    producto.setIdProducto(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET producto = ?, idMarca = ?, descripcion = ?, imagen = ?, " +
                    "precio_costo = ?, precio_venta = ?, existencia = ? WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getProducto());
            stmt.setInt(2, producto.getIdMarca());
            stmt.setString(3, producto.getDescripcion());
            stmt.setString(4, producto.getImagen());
            stmt.setBigDecimal(5, producto.getPrecioCosto());
            stmt.setBigDecimal(6, producto.getPrecioVenta());
            stmt.setInt(7, producto.getExistencia());
            stmt.setInt(8, producto.getIdProducto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
public List<Producto> buscar(String termino) {
    List<Producto> productos = new ArrayList<>();
    String sql = "SELECT p.*, m.marca AS nombreMarca FROM productos p " +
                 "LEFT JOIN marcas m ON p.idMarca = m.idMarca " +
                 "WHERE p.idProducto = ? " +
                 "ORDER BY p.producto";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, termino);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Producto producto = mapearResultSet(rs);
            productos.add(producto);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return productos;
}

    
    

    /**
     * Actualiza la existencia de un producto (para compras y ventas)
     * @param idProducto ID del producto
     * @param cantidad cantidad a sumar (positivo para compras, negativo para ventas)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarExistencia(int idProducto, int cantidad) {
        String sql = "UPDATE productos SET existencia = existencia + ? WHERE idProducto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Producto mapearResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setProducto(rs.getString("producto"));
        producto.setIdMarca(rs.getInt("idMarca"));
        producto.setNombreMarca(rs.getString("nombreMarca"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setImagen(rs.getString("imagen"));
        producto.setPrecioCosto(rs.getBigDecimal("precio_costo"));
        producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        producto.setExistencia(rs.getInt("existencia"));
        producto.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
        return producto;
    }
}
