package com.puntoventa.dao;

import com.puntoventa.models.Venta;
import com.puntoventa.models.VentaDetalle;
import com.puntoventa.utils.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Venta con patrón maestro-detalle
 */
public class VentaDAO {
    
    /**
     * Crea una nueva venta con sus detalles (transacción completa)
     * @param venta objeto Venta con sus detalles
     * @return ID de la venta creada, 0 si hay error
     */
    public int crear(Venta venta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar la venta maestro
            String sqlVenta = "INSERT INTO Ventas (noFactura, serie, fechaFactura, idCliente, " +
                             "idEmpleado, subtotal, descuento, total, metodo_pago, estado, " +
                             "observaciones, idUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            int idVenta = 0;
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenta.setInt(1, venta.getNoFactura());
                stmtVenta.setString(2, venta.getSerie());
                stmtVenta.setDate(3, Date.valueOf(venta.getFechaFactura()));
                stmtVenta.setInt(4, venta.getIdCliente());
                stmtVenta.setInt(5, venta.getIdEmpleado());
                stmtVenta.setBigDecimal(6, venta.getSubtotal());
                stmtVenta.setBigDecimal(7, venta.getDescuento());
                stmtVenta.setBigDecimal(8, venta.getTotal());
                stmtVenta.setString(9, venta.getMetodoPago());
                stmtVenta.setString(10, venta.getEstado());
                stmtVenta.setString(11, venta.getObservaciones());
                stmtVenta.setInt(12, venta.getIdUsuario());
                
                int filasAfectadas = stmtVenta.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet generatedKeys = stmtVenta.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idVenta = generatedKeys.getInt(1);
                        }
                    }
                }
            }
            
            if (idVenta == 0) {
                throw new SQLException("Error al crear la venta, no se obtuvo el ID");
            }
            
            // 2. Insertar los detalles de venta
            String sqlDetalle = "INSERT INTO Ventas_detalle (idVenta, idProducto, cantidad, " +
                               "precio_unitario, descuento, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                for (VentaDetalle detalle : venta.getDetalles()) {
                    stmtDetalle.setInt(1, idVenta);
                    stmtDetalle.setInt(2, detalle.getIdProducto());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                    stmtDetalle.setBigDecimal(5, detalle.getDescuento());
                    stmtDetalle.setBigDecimal(6, detalle.getSubtotal());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }
            
            // 3. Actualizar existencias de productos
            ProductoDAO productoDAO = new ProductoDAO();
            for (VentaDetalle detalle : venta.getDetalles()) {
                if (!productoDAO.decrementarExistencia(detalle.getIdProducto(), detalle.getCantidad())) {
                    throw new SQLException("Error al actualizar existencia del producto ID: " + detalle.getIdProducto());
                }
            }
            
            conn.commit(); // Confirmar transacción
            return idVenta;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Error al crear venta: " + e.getMessage());
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
     * Obtiene una venta por su ID con sus detalles
     * @param idVenta ID de la venta
     * @return objeto Venta con sus detalles o null si no existe
     */
    public Venta obtenerPorId(int idVenta) {
        String sqlVenta = "SELECT v.*, " +
                         "CONCAT(c.nombres, ' ', c.apellidos) as nombre_cliente, " +
                         "CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                         "FROM Ventas v " +
                         "LEFT JOIN Clientes c ON v.idCliente = c.idCliente " +
                         "LEFT JOIN Empleados e ON v.idEmpleado = e.idEmpleado " +
                         "WHERE v.idVenta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlVenta)) {
            
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Venta venta = mapearVenta(rs);
                
                // Cargar detalles
                venta.setDetalles(obtenerDetallesPorVenta(idVenta));
                
                return venta;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener venta por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las ventas con información resumida
     * @return lista de ventas
     */
    public List<Venta> obtenerTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, " +
                    "CONCAT(c.nombres, ' ', c.apellidos) as nombre_cliente, " +
                    "CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Ventas v " +
                    "LEFT JOIN Clientes c ON v.idCliente = c.idCliente " +
                    "LEFT JOIN Empleados e ON v.idEmpleado = e.idEmpleado " +
                    "ORDER BY v.fechaFactura DESC, v.idVenta DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ventas.add(mapearVenta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ventas;
    }
    
    /**
     * Obtiene ventas por rango de fechas
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de ventas en el rango
     */
    public List<Venta> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, " +
                    "CONCAT(c.nombres, ' ', c.apellidos) as nombre_cliente, " +
                    "CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Ventas v " +
                    "LEFT JOIN Clientes c ON v.idCliente = c.idCliente " +
                    "LEFT JOIN Empleados e ON v.idEmpleado = e.idEmpleado " +
                    "WHERE v.fechaFactura BETWEEN ? AND ? " +
                    "ORDER BY v.fechaFactura DESC, v.idVenta DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapearVenta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por rango de fechas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ventas;
    }
    
    /**
     * Obtiene ventas por cliente
     * @param idCliente ID del cliente
     * @return lista de ventas del cliente
     */
    public List<Venta> obtenerPorCliente(int idCliente) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, " +
                    "CONCAT(c.nombres, ' ', c.apellidos) as nombre_cliente, " +
                    "CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado " +
                    "FROM Ventas v " +
                    "LEFT JOIN Clientes c ON v.idCliente = c.idCliente " +
                    "LEFT JOIN Empleados e ON v.idEmpleado = e.idEmpleado " +
                    "WHERE v.idCliente = ? " +
                    "ORDER BY v.fechaFactura DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapearVenta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ventas;
    }
    
    /**
     * Obtiene los detalles de una venta
     * @param idVenta ID de la venta
     * @return lista de detalles de la venta
     */
    public List<VentaDetalle> obtenerDetallesPorVenta(int idVenta) {
        List<VentaDetalle> detalles = new ArrayList<>();
        String sql = "SELECT vd.*, p.producto as nombre_producto, p.codigo_barras " +
                    "FROM Ventas_detalle vd " +
                    "LEFT JOIN Productos p ON vd.idProducto = p.idProducto " +
                    "WHERE vd.idVenta = ? " +
                    "ORDER BY vd.idVentaDetalle";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapearVentaDetalle(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de venta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return detalles;
    }
    
    /**
     * Actualiza el estado de una venta
     * @param idVenta ID de la venta
     * @param nuevoEstado nuevo estado
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarEstado(int idVenta, String nuevoEstado) {
        String sql = "UPDATE Ventas SET estado = ? WHERE idVenta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idVenta);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de venta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de ventas
     * @return número total de ventas
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Ventas";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar ventas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene el total de ventas del día
     * @param fecha fecha a consultar
     * @return total de ventas del día
     */
    public BigDecimal obtenerTotalVentasDelDia(LocalDate fecha) {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM Ventas WHERE fechaFactura = ? AND estado = 'COMPLETADA'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener total de ventas del día: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Obtiene el siguiente número de factura disponible
     * @param serie serie de la factura
     * @return siguiente número de factura
     */
    public int obtenerSiguienteNumeroFactura(String serie) {
        String sql = "SELECT COALESCE(MAX(noFactura), 0) + 1 FROM Ventas WHERE serie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, serie);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente número de factura: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 1;
    }

    /**
     * Elimina una venta (marca como cancelada y restaura existencias)
     * @param idVenta ID de la venta a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idVenta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener detalles de la venta para restaurar existencias
            List<VentaDetalle> detalles = obtenerDetallesPorVenta(idVenta);

            // 2. Restaurar existencias de productos
            ProductoDAO productoDAO = new ProductoDAO();
            for (VentaDetalle detalle : detalles) {
                if (!productoDAO.incrementarExistencia(detalle.getIdProducto(), detalle.getCantidad())) {
                    throw new SQLException("Error al restaurar existencia del producto ID: " + detalle.getIdProducto());
                }
            }

            // 3. Marcar venta como cancelada
            String sql = "UPDATE Ventas SET estado = 'CANCELADA' WHERE idVenta = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idVenta);
                stmt.executeUpdate();
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
            System.err.println("Error al eliminar venta: " + e.getMessage());
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
     * Mapea un ResultSet a un objeto Venta
     * @param rs ResultSet con los datos
     * @return objeto Venta
     * @throws SQLException si hay error en el mapeo
     */
    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setIdVenta(rs.getInt("idVenta"));
        venta.setNoFactura(rs.getInt("noFactura"));
        venta.setSerie(rs.getString("serie"));
        venta.setFechaFactura(rs.getDate("fechaFactura").toLocalDate());
        venta.setIdCliente(rs.getInt("idCliente"));
        venta.setNombreCliente(rs.getString("nombre_cliente"));
        venta.setIdEmpleado(rs.getInt("idEmpleado"));
        venta.setNombreEmpleado(rs.getString("nombre_empleado"));
        venta.setSubtotal(rs.getBigDecimal("subtotal"));
        venta.setDescuento(rs.getBigDecimal("descuento"));
        venta.setTotal(rs.getBigDecimal("total"));
        venta.setMetodoPago(rs.getString("metodo_pago"));
        venta.setEstado(rs.getString("estado"));
        venta.setObservaciones(rs.getString("observaciones"));
        venta.setIdUsuario(rs.getInt("idUsuario"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            venta.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        return venta;
    }
    
    /**
     * Mapea un ResultSet a un objeto VentaDetalle
     * @param rs ResultSet con los datos
     * @return objeto VentaDetalle
     * @throws SQLException si hay error en el mapeo
     */
    private VentaDetalle mapearVentaDetalle(ResultSet rs) throws SQLException {
        VentaDetalle detalle = new VentaDetalle();
        detalle.setIdVentaDetalle(rs.getLong("idVentaDetalle"));
        detalle.setIdVenta(rs.getInt("idVenta"));
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
