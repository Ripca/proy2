package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Venta;
import com.sistemaempresa.models.VentaDetalle;
import java.sql.*;
import java.util.*;

public class VentaDAO {
    
    /**
     * Obtiene todas las ventas con información de cliente y empleado
     */
    public List<Venta> obtenerTodos() {
        List<Venta> ventas = new ArrayList<>();
        String sql = """
            SELECT v.id_venta, v.no_factura, v.serie, v.fecha_factura, 
                   v.id_cliente, v.id_empleado, v.fecha_ingreso,
                   c.cliente as nombre_cliente,
                   CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado
            FROM ventas v
            LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
            LEFT JOIN empleados e ON v.id_empleado = e.id_empleado
            ORDER BY v.fecha_factura DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setNoFactura(rs.getInt("no_factura"));
                venta.setSerie(rs.getString("serie"));
                venta.setFechaFactura(rs.getDate("fecha_factura").toLocalDate());
                venta.setIdCliente(rs.getInt("id_cliente"));
                venta.setIdEmpleado(rs.getInt("id_empleado"));
                venta.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setNombreEmpleado(rs.getString("nombre_empleado"));
                
                // Calcular total
                venta.setTotal(calcularTotalVenta(venta.getIdVenta()));
                
                ventas.add(venta);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }
    
    /**
     * Obtiene una venta por su ID con sus detalles
     */
    public Venta obtenerPorId(int idVenta) {
        String sql = """
            SELECT v.id_venta, v.no_factura, v.serie, v.fecha_factura, 
                   v.id_cliente, v.id_empleado, v.fecha_ingreso,
                   c.cliente as nombre_cliente,
                   CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado
            FROM ventas v
            LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
            LEFT JOIN empleados e ON v.id_empleado = e.id_empleado
            WHERE v.id_venta = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setNoFactura(rs.getInt("no_factura"));
                    venta.setSerie(rs.getString("serie"));
                    venta.setFechaFactura(rs.getDate("fecha_factura").toLocalDate());
                    venta.setIdCliente(rs.getInt("id_cliente"));
                    venta.setIdEmpleado(rs.getInt("id_empleado"));
                    venta.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    venta.setNombreCliente(rs.getString("nombre_cliente"));
                    venta.setNombreEmpleado(rs.getString("nombre_empleado"));
                    
                    // Cargar detalles
                    venta.setDetalles(obtenerDetallesPorVenta(idVenta));
                    venta.setTotal(venta.calcularTotal());
                    
                    return venta;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Crea una nueva venta con sus detalles
     * Implementa exactamente la lógica del método crearVenta() del C# repp/vista/Venta.h
     * Actualiza las existencias de productos automáticamente
     * @param venta objeto Venta con sus detalles
     * @return ID de la venta creada, 0 si hay error
     */
    public int crearVenta(Venta venta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar la venta maestro (igual que en C#)
            String sqlVenta = "INSERT INTO ventas(no_factura, serie, fecha_factura, id_cliente, id_empleado, fecha_ingreso) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";

            int idVentaCreada = 0;
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenta.setInt(1, venta.getNoFactura());
                stmtVenta.setString(2, venta.getSerie());
                stmtVenta.setDate(3, java.sql.Date.valueOf(venta.getFechaFactura()));
                stmtVenta.setInt(4, venta.getIdCliente());
                stmtVenta.setInt(5, venta.getIdEmpleado());
                stmtVenta.setDate(6, java.sql.Date.valueOf(venta.getFechaIngreso()));

                int filasAfectadas = stmtVenta.executeUpdate();

                if (filasAfectadas > 0) {
                    // Obtener el ID de la venta recién insertada (LAST_INSERT_ID como en C#)
                    try (ResultSet generatedKeys = stmtVenta.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idVentaCreada = generatedKeys.getInt(1);
                        }
                    }
                }
            }

            if (idVentaCreada == 0) {
                throw new SQLException("Error al crear la venta, no se obtuvo el ID");
            }

            // 2. Insertar los detalles de venta (igual que en C#)
            if (venta.tieneDetalles()) {
                String sqlDetalle = "INSERT INTO ventas_detalle(id_venta, id_producto, cantidad, precio_unitario) " +
                                   "VALUES (?, ?, ?, ?)";

                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (VentaDetalle detalle : venta.getDetalles()) {
                        stmtDetalle.setInt(1, idVentaCreada);
                        stmtDetalle.setInt(2, detalle.getIdProducto());
                        stmtDetalle.setString(3, detalle.getCantidad()); // VARCHAR(45) como en C#
                        stmtDetalle.setDouble(4, detalle.getPrecioUnitario());

                        int detalleAfectado = stmtDetalle.executeUpdate();
                        if (detalleAfectado == 0) {
                            throw new SQLException("Error al insertar detalle de venta");
                        }

                        // 3. Actualizar existencias de productos (NUEVO - como en C#)
                        // En ventas se RESTA la cantidad
                        try {
                            int cantidadVenta = Integer.parseInt(detalle.getCantidad());
                            String sqlActualizarExistencia = "UPDATE productos SET existencia = existencia - ? WHERE id_producto = ?";
                            try (PreparedStatement stmtExistencia = conn.prepareStatement(sqlActualizarExistencia)) {
                                stmtExistencia.setInt(1, cantidadVenta);
                                stmtExistencia.setInt(2, detalle.getIdProducto());
                                stmtExistencia.executeUpdate();
                            }
                        } catch (NumberFormatException e) {
                            // Si no se puede parsear la cantidad, continuar sin actualizar existencia
                            System.err.println("Error al parsear cantidad de venta: " + detalle.getCantidad());
                        }
                    }
                }
            }

            conn.commit();
            venta.setIdVenta(idVentaCreada);
            return idVentaCreada;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserta una nueva venta con sus detalles (método original)
     */
    public boolean insertar(Venta venta) {
        String sqlVenta = """
            INSERT INTO ventas (no_factura, serie, fecha_factura, id_cliente, id_empleado, fecha_ingreso)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insertar venta
            try (PreparedStatement stmt = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, venta.getNoFactura());
                stmt.setString(2, venta.getSerie());
                stmt.setDate(3, java.sql.Date.valueOf(venta.getFechaFactura()));
                stmt.setInt(4, venta.getIdCliente());
                stmt.setInt(5, venta.getIdEmpleado());
                stmt.setDate(6, java.sql.Date.valueOf(venta.getFechaIngreso()));

                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            venta.setIdVenta(generatedKeys.getInt(1));
                            
                            // Insertar detalles
                            if (venta.tieneDetalles()) {
                                for (VentaDetalle detalle : venta.getDetalles()) {
                                    detalle.setIdVenta(venta.getIdVenta());
                                    if (!insertarDetalle(conn, detalle)) {
                                        conn.rollback();
                                        return false;
                                    }
                                }
                            }
                            
                            conn.commit();
                            return true;
                        }
                    }
                }
            }
            
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Actualiza una venta existente
     */
    public boolean actualizar(Venta venta) {
        String sql = """
            UPDATE ventas 
            SET no_factura = ?, serie = ?, fecha_factura = ?, id_cliente = ?, id_empleado = ?
            WHERE id_venta = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, venta.getNoFactura());
            stmt.setString(2, venta.getSerie());
            stmt.setDate(3, java.sql.Date.valueOf(venta.getFechaFactura()));
            stmt.setInt(4, venta.getIdCliente());
            stmt.setInt(5, venta.getIdEmpleado());
            stmt.setInt(6, venta.getIdVenta());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una venta y sus detalles
     */
    public boolean eliminar(int idVenta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Eliminar detalles primero
            String sqlDetalles = "DELETE FROM ventas_detalle WHERE id_venta = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalles)) {
                stmt.setInt(1, idVenta);
                stmt.executeUpdate();
            }
            
            // Eliminar venta
            String sqlVenta = "DELETE FROM ventas WHERE id_venta = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlVenta)) {
                stmt.setInt(1, idVenta);
                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    conn.commit();
                    return true;
                }
            }
            
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Obtiene los detalles de una venta
     */
    public List<VentaDetalle> obtenerDetallesPorVenta(int idVenta) {
        List<VentaDetalle> detalles = new ArrayList<>();
        String sql = """
            SELECT vd.id_venta_detalle, vd.id_venta, vd.id_producto, 
                   vd.cantidad, vd.precio_unitario,
                   p.producto as nombre_producto,
                   m.marca as marca_producto
            FROM ventas_detalle vd
            LEFT JOIN productos p ON vd.id_producto = p.id_producto
            LEFT JOIN marcas m ON p.id_marca = m.id_marca
            WHERE vd.id_venta = ?
            ORDER BY vd.id_venta_detalle
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setIdVentaDetalle(rs.getInt("id_venta_detalle"));
                    detalle.setIdVenta(rs.getInt("id_venta"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getString("cantidad")); // VARCHAR(45) como en C#
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                    detalle.setMarcaProducto(rs.getString("marca_producto"));
                    detalle.setSubtotal(detalle.calcularSubtotal());
                    
                    detalles.add(detalle);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }
    
    /**
     * Inserta un detalle de venta
     */
    private boolean insertarDetalle(Connection conn, VentaDetalle detalle) throws SQLException {
        String sql = """
            INSERT INTO ventas_detalle (id_venta, id_producto, cantidad, precio_unitario)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setString(3, detalle.getCantidad()); // VARCHAR(45) como en C#
            stmt.setDouble(4, detalle.getPrecioUnitario());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Calcula el total de una venta
     */
    private double calcularTotalVenta(int idVenta) {
        String sql = """
            SELECT SUM(cantidad * precio_unitario) as total
            FROM ventas_detalle
            WHERE id_venta = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
}
