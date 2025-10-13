package com.puntoventa.dao;

import com.puntoventa.config.DatabaseConnection;
import com.puntoventa.models.Venta;
import com.puntoventa.models.VentaDetalle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Venta
 * Implementa exactamente la lógica del proyecto C# repp/vista/Venta.h
 */
public class VentaDAO {
    
    /**
     * Crea una nueva venta con sus detalles
     * Implementa exactamente la lógica del método crearVenta() del C#
     * @param venta objeto Venta con sus detalles
     * @return ID de la venta creada, 0 si hay error
     */
    public int crearVenta(Venta venta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar la venta maestro (igual que en C#)
            String sqlVenta = "INSERT INTO Ventas(nofactura, serie, fechafactura, idcliente, idempleado, fecha_ingreso) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";
            
            int idVentaCreada = 0;
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenta.setInt(1, venta.getNoFactura());
                stmtVenta.setString(2, venta.getSerie());
                stmtVenta.setTimestamp(3, new Timestamp(venta.getFechaFactura().getTime()));
                stmtVenta.setInt(4, venta.getIdCliente());
                stmtVenta.setInt(5, venta.getIdEmpleado());
                stmtVenta.setTimestamp(6, new Timestamp(venta.getFechaIngreso().getTime()));
                
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
                String sqlDetalle = "INSERT INTO Ventas_detalle(idventa, idProducto, cantidad, precio_unitario) " +
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
     * Obtiene todas las ventas con información de cliente y empleado
     * Implementa la lógica del método leer() del C#
     */
    public List<Venta> obtenerTodos() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.idVenta, v.nofactura, v.serie, v.fechafactura, " +
                    "c.nombres as nombreCliente, c.apellidos as apellidoCliente, " +
                    "e.nombres as nombreEmpleado, e.apellidos as apellidoEmpleado, " +
                    "v.fecha_ingreso " +
                    "FROM Ventas v " +
                    "INNER JOIN Clientes c ON v.idcliente = c.idCliente " +
                    "INNER JOIN Empleados e ON v.idempleado = e.idEmpleado " +
                    "ORDER BY v.fechafactura DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("idVenta"));
                venta.setNoFactura(rs.getInt("nofactura"));
                venta.setSerie(rs.getString("serie"));
                venta.setFechaFactura(rs.getTimestamp("fechafactura"));
                venta.setNombreCliente(rs.getString("nombreCliente") + " " + rs.getString("apellidoCliente"));
                venta.setNombreEmpleado(rs.getString("nombreEmpleado") + " " + rs.getString("apellidoEmpleado"));
                venta.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                
                ventas.add(venta);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }
    
    /**
     * Obtiene una venta por ID con sus detalles
     */
    public Venta obtenerPorId(int idVenta) {
        String sql = "SELECT v.idVenta, v.nofactura, v.serie, v.fechafactura, v.idcliente, v.idempleado, " +
                    "c.nombres as nombreCliente, c.apellidos as apellidoCliente, " +
                    "e.nombres as nombreEmpleado, e.apellidos as apellidoEmpleado, " +
                    "v.fecha_ingreso " +
                    "FROM Ventas v " +
                    "INNER JOIN Clientes c ON v.idcliente = c.idCliente " +
                    "INNER JOIN Empleados e ON v.idempleado = e.idEmpleado " +
                    "WHERE v.idVenta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("idVenta"));
                venta.setNoFactura(rs.getInt("nofactura"));
                venta.setSerie(rs.getString("serie"));
                venta.setFechaFactura(rs.getTimestamp("fechafactura"));
                venta.setIdCliente(rs.getInt("idcliente"));
                venta.setIdEmpleado(rs.getInt("idempleado"));
                venta.setNombreCliente(rs.getString("nombreCliente") + " " + rs.getString("apellidoCliente"));
                venta.setNombreEmpleado(rs.getString("nombreEmpleado") + " " + rs.getString("apellidoEmpleado"));
                venta.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                
                // Cargar detalles
                venta.setDetalles(obtenerDetallesPorVenta(idVenta));
                
                return venta;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene los detalles de una venta
     */
    public List<VentaDetalle> obtenerDetallesPorVenta(int idVenta) {
        List<VentaDetalle> detalles = new ArrayList<>();
        String sql = "SELECT vd.idventa_detalle, vd.idventa, vd.idProducto, p.producto, " +
                    "vd.cantidad, vd.precio_unitario " +
                    "FROM Ventas_detalle vd " +
                    "INNER JOIN Productos p ON vd.idProducto = p.idProducto " +
                    "WHERE vd.idventa = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VentaDetalle detalle = new VentaDetalle();
                detalle.setIdVentaDetalle(rs.getInt("idventa_detalle"));
                detalle.setIdVenta(rs.getInt("idventa"));
                detalle.setIdProducto(rs.getInt("idProducto"));
                detalle.setNombreProducto(rs.getString("producto"));
                detalle.setCantidad(rs.getString("cantidad")); // VARCHAR(45) como en C#
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                detalles.add(detalle);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }
    
    /**
     * Actualiza una venta
     * Implementa la lógica del método actualizar() del C#
     */
    public boolean actualizar(Venta venta) {
        String sql = "UPDATE Ventas SET nofactura = ?, serie = ?, fechafactura = ?, " +
                    "idcliente = ?, idempleado = ?, fecha_ingreso = ? WHERE idVenta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, venta.getNoFactura());
            stmt.setString(2, venta.getSerie());
            stmt.setTimestamp(3, new Timestamp(venta.getFechaFactura().getTime()));
            stmt.setInt(4, venta.getIdCliente());
            stmt.setInt(5, venta.getIdEmpleado());
            stmt.setTimestamp(6, new Timestamp(venta.getFechaIngreso().getTime()));
            stmt.setInt(7, venta.getIdVenta());
            
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
            String sqlDetalles = "DELETE FROM Ventas_detalle WHERE idventa = ?";
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                stmtDetalles.setInt(1, idVenta);
                stmtDetalles.executeUpdate();
            }
            
            // Eliminar venta
            String sqlVenta = "DELETE FROM Ventas WHERE idVenta = ?";
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
                stmtVenta.setInt(1, idVenta);
                int filasAfectadas = stmtVenta.executeUpdate();
                
                if (filasAfectadas > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
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
}
