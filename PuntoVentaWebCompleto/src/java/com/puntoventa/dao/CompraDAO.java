package com.puntoventa.dao;

import com.puntoventa.config.DatabaseConnection;
import com.puntoventa.models.Compra;
import com.puntoventa.models.CompraDetalle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Compra
 * Implementa exactamente la lógica del proyecto C# repp/vista/Compra.h
 */
public class CompraDAO {
    
    /**
     * Crea una nueva compra con sus detalles
     * Implementa exactamente la lógica del método crear() del C#
     * @param compra objeto Compra con sus detalles
     * @return ID de la compra creada, 0 si hay error
     */
    public int crear(Compra compra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar la compra maestro (igual que en C#)
            String sqlCompra = "INSERT INTO Compras(no_order_compra, idproveedor, fecha_order, fecha_ingreso) " +
                              "VALUES (?, ?, ?, ?)";
            
            int idCompraCreada = 0;
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmtCompra.setInt(1, compra.getNoOrderCompra());
                stmtCompra.setInt(2, compra.getIdProveedor());
                stmtCompra.setTimestamp(3, new Timestamp(compra.getFechaOrder().getTime()));
                stmtCompra.setTimestamp(4, new Timestamp(compra.getFechaIngreso().getTime()));
                
                int filasAfectadas = stmtCompra.executeUpdate();
                
                if (filasAfectadas > 0) {
                    // Obtener el ID de la compra recién insertada (LAST_INSERT_ID como en C#)
                    try (ResultSet generatedKeys = stmtCompra.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idCompraCreada = generatedKeys.getInt(1);
                        }
                    }
                }
            }
            
            if (idCompraCreada == 0) {
                throw new SQLException("Error al crear la compra, no se obtuvo el ID");
            }
            
            // 2. Insertar los detalles de compra (igual que en C#)
            if (compra.tieneDetalles()) {
                String sqlDetalle = "INSERT INTO Compras_detalle(idcompra, idproducto, cantidad, precio_unitario) " +
                                   "VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (CompraDetalle detalle : compra.getDetalles()) {
                        stmtDetalle.setInt(1, idCompraCreada);
                        stmtDetalle.setInt(2, detalle.getIdProducto());
                        stmtDetalle.setInt(3, detalle.getCantidad());
                        stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                        
                        int detalleAfectado = stmtDetalle.executeUpdate();
                        if (detalleAfectado == 0) {
                            throw new SQLException("Error al insertar detalle de compra");
                        }
                    }
                }
            }
            
            conn.commit();
            compra.setIdCompra(idCompraCreada);
            return idCompraCreada;
            
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
     * Obtiene todas las compras con información de proveedor
     * Implementa la lógica del método leer() del C#
     */
    public List<Compra> obtenerTodos() {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.idcompra, c.no_order_compra, p.proveedor, c.fecha_order, c.fecha_ingreso " +
                    "FROM Compras AS c INNER JOIN Proveedores AS p ON c.idproveedor = p.idProveedor " +
                    "ORDER BY c.fecha_order DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("idcompra"));
                compra.setNoOrderCompra(rs.getInt("no_order_compra"));
                compra.setNombreProveedor(rs.getString("proveedor"));
                compra.setFechaOrder(rs.getTimestamp("fecha_order"));
                compra.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                
                compras.add(compra);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return compras;
    }
    
    /**
     * Obtiene una compra por ID con sus detalles
     */
    public Compra obtenerPorId(int idCompra) {
        String sql = "SELECT c.idcompra, c.no_order_compra, c.idproveedor, p.proveedor, " +
                    "c.fecha_order, c.fecha_ingreso " +
                    "FROM Compras c INNER JOIN Proveedores p ON c.idproveedor = p.idProveedor " +
                    "WHERE c.idcompra = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("idcompra"));
                compra.setNoOrderCompra(rs.getInt("no_order_compra"));
                compra.setIdProveedor(rs.getInt("idproveedor"));
                compra.setNombreProveedor(rs.getString("proveedor"));
                compra.setFechaOrder(rs.getTimestamp("fecha_order"));
                compra.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                
                // Cargar detalles
                compra.setDetalles(obtenerDetallesPorCompra(idCompra));
                
                return compra;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene los detalles de una compra
     */
    public List<CompraDetalle> obtenerDetallesPorCompra(int idCompra) {
        List<CompraDetalle> detalles = new ArrayList<>();
        String sql = "SELECT cd.idcompra_detalle, cd.idcompra, cd.idproducto, p.producto, " +
                    "cd.cantidad, cd.precio_unitario " +
                    "FROM Compras_detalle cd " +
                    "INNER JOIN Productos p ON cd.idproducto = p.idProducto " +
                    "WHERE cd.idcompra = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CompraDetalle detalle = new CompraDetalle();
                detalle.setIdCompraDetalle(rs.getInt("idcompra_detalle"));
                detalle.setIdCompra(rs.getInt("idcompra"));
                detalle.setIdProducto(rs.getInt("idproducto"));
                detalle.setNombreProducto(rs.getString("producto"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                detalles.add(detalle);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return detalles;
    }
    
    /**
     * Actualiza una compra
     * Implementa la lógica del método actualizar() del C#
     */
    public boolean actualizar(Compra compra) {
        String sql = "UPDATE Compras SET no_order_compra = ?, idproveedor = ?, " +
                    "fecha_order = ?, fecha_ingreso = ? WHERE idcompra = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, compra.getNoOrderCompra());
            stmt.setInt(2, compra.getIdProveedor());
            stmt.setTimestamp(3, new Timestamp(compra.getFechaOrder().getTime()));
            stmt.setTimestamp(4, new Timestamp(compra.getFechaIngreso().getTime()));
            stmt.setInt(5, compra.getIdCompra());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una compra y sus detalles
     */
    public boolean eliminar(int idCompra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Eliminar detalles primero
            String sqlDetalles = "DELETE FROM Compras_detalle WHERE idcompra = ?";
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                stmtDetalles.setInt(1, idCompra);
                stmtDetalles.executeUpdate();
            }
            
            // Eliminar compra
            String sqlCompra = "DELETE FROM Compras WHERE idcompra = ?";
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra)) {
                stmtCompra.setInt(1, idCompra);
                int filasAfectadas = stmtCompra.executeUpdate();
                
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
