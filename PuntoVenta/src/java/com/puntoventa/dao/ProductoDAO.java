package com.puntoventa.dao;

import com.puntoventa.models.Producto;
import com.puntoventa.utils.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Producto
 */
public class ProductoDAO {
    
    /**
     * Crea un nuevo producto
     * @param producto objeto Producto a crear
     * @return ID del producto creado, 0 si hay error
     */
    public int crear(Producto producto) {
        String sql = "INSERT INTO Productos (producto, codigo_barras, idMarca, descripcion, imagen, " +
                    "precio_costo, precio_venta, existencia, stock_minimo, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getProducto());
            stmt.setString(2, producto.getCodigoBarras());
            stmt.setInt(3, producto.getIdMarca());
            stmt.setString(4, producto.getDescripcion());
            stmt.setString(5, producto.getImagen());
            stmt.setBigDecimal(6, producto.getPrecioCosto());
            stmt.setBigDecimal(7, producto.getPrecioVenta());
            stmt.setInt(8, producto.getExistencia());
            stmt.setInt(9, producto.getStockMinimo());
            stmt.setBoolean(10, producto.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene un producto por su ID con información de marca
     * @param idProducto ID del producto
     * @return objeto Producto o null si no existe
     */
    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene un producto por su código de barras
     * @param codigoBarras código de barras del producto
     * @return objeto Producto o null si no existe
     */
    public Producto obtenerPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.codigo_barras = ? AND p.activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoBarras);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por código de barras: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los productos activos con información de marca
     * @return lista de productos
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.activo = true " +
                    "ORDER BY p.producto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Obtiene productos con stock bajo (menor o igual al stock mínimo)
     * @return lista de productos con stock bajo
     */
    public List<Producto> obtenerProductosStockBajo() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.existencia <= p.stock_minimo AND p.activo = true " +
                    "ORDER BY p.existencia ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos con stock bajo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Busca productos por nombre o código de barras
     * @param termino término de búsqueda
     * @return lista de productos que coinciden
     */
    public List<Producto> buscar(String termino) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE (p.producto LIKE ? OR p.codigo_barras LIKE ? OR p.descripcion LIKE ?) " +
                    "AND p.activo = true " +
                    "ORDER BY p.producto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + termino + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Obtiene productos por marca
     * @param idMarca ID de la marca
     * @return lista de productos de la marca
     */
    public List<Producto> obtenerPorMarca(int idMarca) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, m.marca as nombre_marca " +
                    "FROM Productos p " +
                    "LEFT JOIN Marcas m ON p.idMarca = m.idMarca " +
                    "WHERE p.idMarca = ? AND p.activo = true " +
                    "ORDER BY p.producto";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    /**
     * Actualiza un producto
     * @param producto objeto Producto con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE Productos SET producto = ?, codigo_barras = ?, idMarca = ?, " +
                    "descripcion = ?, imagen = ?, precio_costo = ?, precio_venta = ?, " +
                    "existencia = ?, stock_minimo = ?, activo = ? " +
                    "WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getProducto());
            stmt.setString(2, producto.getCodigoBarras());
            stmt.setInt(3, producto.getIdMarca());
            stmt.setString(4, producto.getDescripcion());
            stmt.setString(5, producto.getImagen());
            stmt.setBigDecimal(6, producto.getPrecioCosto());
            stmt.setBigDecimal(7, producto.getPrecioVenta());
            stmt.setInt(8, producto.getExistencia());
            stmt.setInt(9, producto.getStockMinimo());
            stmt.setBoolean(10, producto.isActivo());
            stmt.setInt(11, producto.getIdProducto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza solo la existencia de un producto
     * @param idProducto ID del producto
     * @param nuevaExistencia nueva cantidad en existencia
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarExistencia(int idProducto, int nuevaExistencia) {
        String sql = "UPDATE Productos SET existencia = ? WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevaExistencia);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar existencia del producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Incrementa la existencia de un producto (para compras)
     * @param idProducto ID del producto
     * @param cantidad cantidad a incrementar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean incrementarExistencia(int idProducto, int cantidad) {
        String sql = "UPDATE Productos SET existencia = existencia + ? WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al incrementar existencia del producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Decrementa la existencia de un producto (para ventas)
     * @param idProducto ID del producto
     * @param cantidad cantidad a decrementar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean decrementarExistencia(int idProducto, int cantidad) {
        String sql = "UPDATE Productos SET existencia = existencia - ? WHERE idProducto = ? AND existencia >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);
            stmt.setInt(3, cantidad);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al decrementar existencia del producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina un producto (marca como inactivo)
     * @param idProducto ID del producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idProducto) {
        String sql = "UPDATE Productos SET activo = false WHERE idProducto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de productos activos
     * @return número total de productos activos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Productos WHERE activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe un producto con el código de barras dado
     * @param codigoBarras código de barras del producto
     * @param idExcluir ID a excluir de la búsqueda (para actualizaciones)
     * @return true si existe, false en caso contrario
     */
    public boolean existeCodigoBarras(String codigoBarras, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM Productos WHERE codigo_barras = ? AND idProducto != ? AND activo = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoBarras);
            stmt.setInt(2, idExcluir);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de código de barras: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Producto
     * @param rs ResultSet con los datos
     * @return objeto Producto
     * @throws SQLException si hay error en el mapeo
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setProducto(rs.getString("producto"));
        producto.setCodigoBarras(rs.getString("codigo_barras"));
        producto.setIdMarca(rs.getInt("idMarca"));
        producto.setNombreMarca(rs.getString("nombre_marca"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setImagen(rs.getString("imagen"));
        producto.setPrecioCosto(rs.getBigDecimal("precio_costo"));
        producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        producto.setExistencia(rs.getInt("existencia"));
        producto.setStockMinimo(rs.getInt("stock_minimo"));
        producto.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            producto.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        return producto;
    }
}
