package com.puntoventa.dao;

import com.puntoventa.models.Compra;
import com.puntoventa.models.CompraDetalle;
import com.puntoventa.utils.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Compra con patrón maestro-detalle
 */
public class CompraDAO {
    
    /**
     * Crea una nueva compra con sus detalles (transacción completa)
     * @param compra objeto Compra con sus detalles
     * @return ID de la compra creada, 0 si hay error
     */
    public int crear(Compra compra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar la compra maestro
            String sqlCompra = "INSERT INTO Compras (no_orden_compra, idProveedor, fecha_orden, " +
                              "fecha_entrega, subtotal, descuento, total, estado, observaciones, idUsuario) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            int idCompra = 0;
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmtCompra.setInt(1, compra.getNoOrdenCompra());
                stmtCompra.setInt(2, compra.getIdProveedor());
                stmtCompra.setDate(3, Date.valueOf(compra.getFechaOrden()));
                stmtCompra.setDate(4, compra.getFechaEntrega() != null ? Date.valueOf(compra.getFechaEntrega()) : null);
                stmtCompra.setBigDecimal(5, compra.getSubtotal());
                stmtCompra.setBigDecimal(6, compra.getDescuento());
                stmtCompra.setBigDecimal(7, compra.getTotal());
                stmtCompra.setString(8, compra.getEstado());
                stmtCompra.setString(9, compra.getObservaciones());
                stmtCompra.setInt(10, compra.getIdUsuario());
                
                int filasAfectadas = stmtCompra.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet generatedKeys = stmtCompra.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idCompra = generatedKeys.getInt(1);
                        }
                    }
                }
            }
            
            if (idCompra == 0) {
                throw new SQLException("Error al crear la compra, no se obtuvo el ID");
            }
            
            // 2. Insertar los detalles de compra
            String sqlDetalle = "INSERT INTO Compras_detalle (idCompra, idProducto, cantidad, " +
                               "precio_unitario, descuento, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                for (CompraDetalle detalle : compra.getDetalles()) {
                    stmtDetalle.setInt(1, idCompra);
                    stmtDetalle.setInt(2, detalle.getIdProducto());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                    stmtDetalle.setBigDecimal(5, detalle.getDescuento());
                    stmtDetalle.setBigDecimal(6, detalle.getSubtotal());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }
            
            // 3. Si la compra está recibida, actualizar existencias de productos
            if ("RECIBIDA".equals(compra.getEstado())) {
                ProductoDAO productoDAO = new ProductoDAO();
                for (CompraDetalle detalle : compra.getDetalles()) {
                    if (!productoDAO.incrementarExistencia(detalle.getIdProducto(), detalle.getCantidad())) {
                        throw new SQLException("Error al actualizar existencia del producto ID: " + detalle.getIdProducto());
                    }
                }
            }
            
            conn.commit(); // Confirmar transacción
            return idCompra;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al crear compra: " + e.getMessage());
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
        
        return 0;
    }
    
    /**
     * Obtiene una compra por su ID con sus detalles
     * @param idCompra ID de la compra
     * @return objeto Compra con sus detalles o null si no existe
     */
    public Compra obtenerPorId(int idCompra) {
        String sqlCompra = "SELECT c.*, p.proveedor as nombre_proveedor " +
                          "FROM Compras c " +
                          "LEFT JOIN Proveedores p ON c.idProveedor = p.idProveedor " +
                          "WHERE c.idCompra = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlCompra)) {
            
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Compra compra = mapearCompra(rs);
                
                // Cargar detalles
                compra.setDetalles(obtenerDetallesPorCompra(idCompra));
                
                return compra;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener compra por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las compras con información resumida
     * @return lista de compras
     */
    public List<Compra> obtenerTodas() {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.*, p.proveedor as nombre_proveedor " +
                    "FROM Compras c " +
                    "LEFT JOIN Proveedores p ON c.idProveedor = p.idProveedor " +
                    "ORDER BY c.fecha_orden DESC, c.idCompra DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                compras.add(mapearCompra(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las compras: " + e.getMessage());
            e.printStackTrace();
        }
        
        return compras;
    }
    
    /**
     * Obtiene compras por estado
     * @param estado estado de las compras
     * @return lista de compras con el estado especificado
     */
    public List<Compra> obtenerPorEstado(String estado) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.*, p.proveedor as nombre_proveedor " +
                    "FROM Compras c " +
                    "LEFT JOIN Proveedores p ON c.idProveedor = p.idProveedor " +
                    "WHERE c.estado = ? " +
                    "ORDER BY c.fecha_orden DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                compras.add(mapearCompra(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener compras por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return compras;
    }
    
    /**
     * Obtiene compras por proveedor
     * @param idProveedor ID del proveedor
     * @return lista de compras del proveedor
     */
    public List<Compra> obtenerPorProveedor(int idProveedor) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.*, p.proveedor as nombre_proveedor " +
                    "FROM Compras c " +
                    "LEFT JOIN Proveedores p ON c.idProveedor = p.idProveedor " +
                    "WHERE c.idProveedor = ? " +
                    "ORDER BY c.fecha_orden DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProveedor);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                compras.add(mapearCompra(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener compras por proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return compras;
    }
    
    /**
     * Obtiene los detalles de una compra
     * @param idCompra ID de la compra
     * @return lista de detalles de la compra
     */
    public List<CompraDetalle> obtenerDetallesPorCompra(int idCompra) {
        List<CompraDetalle> detalles = new ArrayList<>();
        String sql = "SELECT cd.*, p.producto as nombre_producto, p.codigo_barras " +
                    "FROM Compras_detalle cd " +
                    "LEFT JOIN Productos p ON cd.idProducto = p.idProducto " +
                    "WHERE cd.idCompra = ? " +
                    "ORDER BY cd.idCompraDetalle";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapearCompraDetalle(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de compra: " + e.getMessage());
            e.printStackTrace();
        }
        
        return detalles;
    }
    
    /**
     * Actualiza el estado de una compra y maneja existencias si es necesario
     * @param idCompra ID de la compra
     * @param nuevoEstado nuevo estado
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarEstado(int idCompra, String nuevoEstado) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Obtener estado actual
            String sqlEstadoActual = "SELECT estado FROM Compras WHERE idCompra = ?";
            String estadoActual = null;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEstadoActual)) {
                stmt.setInt(1, idCompra);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    estadoActual = rs.getString("estado");
                }
            }
            
            if (estadoActual == null) {
                throw new SQLException("Compra no encontrada");
            }
            
            // Actualizar estado
            String sql = "UPDATE Compras SET estado = ? WHERE idCompra = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nuevoEstado);
                stmt.setInt(2, idCompra);
                stmt.executeUpdate();
            }
            
            // Si cambia de PENDIENTE a RECIBIDA, incrementar existencias
            if ("PENDIENTE".equals(estadoActual) && "RECIBIDA".equals(nuevoEstado)) {
                List<CompraDetalle> detalles = obtenerDetallesPorCompra(idCompra);
                ProductoDAO productoDAO = new ProductoDAO();
                for (CompraDetalle detalle : detalles) {
                    if (!productoDAO.incrementarExistencia(detalle.getIdProducto(), detalle.getCantidad())) {
                        throw new SQLException("Error al incrementar existencia del producto ID: " + detalle.getIdProducto());
                    }
                }
            }
            // Si cambia de RECIBIDA a CANCELADA, decrementar existencias
            else if ("RECIBIDA".equals(estadoActual) && "CANCELADA".equals(nuevoEstado)) {
                List<CompraDetalle> detalles = obtenerDetallesPorCompra(idCompra);
                ProductoDAO productoDAO = new ProductoDAO();
                for (CompraDetalle detalle : detalles) {
                    if (!productoDAO.decrementarExistencia(detalle.getIdProducto(), detalle.getCantidad())) {
                        throw new SQLException("Error al decrementar existencia del producto ID: " + detalle.getIdProducto());
                    }
                }
            }
            
            conn.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al actualizar estado de compra: " + e.getMessage());
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
     * Cuenta el total de compras
     * @return número total de compras
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Compras";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar compras: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Obtiene el siguiente número de orden de compra disponible
     * @return siguiente número de orden de compra
     */
    public int obtenerSiguienteNumeroOrden() {
        String sql = "SELECT COALESCE(MAX(no_orden_compra), 0) + 1 FROM Compras";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente número de orden: " + e.getMessage());
            e.printStackTrace();
        }

        return 1001; // Número inicial
    }

    /**
     * Mapea un ResultSet a un objeto Compra
     * @param rs ResultSet con los datos
     * @return objeto Compra
     * @throws SQLException si hay error en el mapeo
     */
    private Compra mapearCompra(ResultSet rs) throws SQLException {
        Compra compra = new Compra();
        compra.setIdCompra(rs.getInt("idCompra"));
        compra.setNoOrdenCompra(rs.getInt("no_orden_compra"));
        compra.setIdProveedor(rs.getInt("idProveedor"));
        compra.setNombreProveedor(rs.getString("nombre_proveedor"));
        compra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());

        Date fechaEntrega = rs.getDate("fecha_entrega");
        if (fechaEntrega != null) {
            compra.setFechaEntrega(fechaEntrega.toLocalDate());
        }

        compra.setSubtotal(rs.getBigDecimal("subtotal"));
        compra.setDescuento(rs.getBigDecimal("descuento"));
        compra.setTotal(rs.getBigDecimal("total"));
        compra.setEstado(rs.getString("estado"));
        compra.setObservaciones(rs.getString("observaciones"));
        compra.setIdUsuario(rs.getInt("idUsuario"));

        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            compra.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }

        return compra;
    }

    /**
     * Mapea un ResultSet a un objeto CompraDetalle
     * @param rs ResultSet con los datos
     * @return objeto CompraDetalle
     * @throws SQLException si hay error en el mapeo
     */
    private CompraDetalle mapearCompraDetalle(ResultSet rs) throws SQLException {
        CompraDetalle detalle = new CompraDetalle();
        detalle.setIdCompraDetalle(rs.getLong("idCompraDetalle"));
        detalle.setIdCompra(rs.getInt("idCompra"));
        detalle.setIdProducto(rs.getInt("idProducto"));
        detalle.setNombreProducto(rs.getString("nombre_producto"));
        detalle.setCodigoBarras(rs.getString("codigo_barras"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setDescuento(rs.getBigDecimal("descuento"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));

        return detalle;
    }
}
