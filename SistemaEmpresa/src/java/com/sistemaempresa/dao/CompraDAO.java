package com.sistemaempresa.dao;

import com.sistemaempresa.config.DatabaseConnection;
import com.sistemaempresa.models.Compra;
import com.sistemaempresa.models.CompraDetalle;
import java.sql.*;
import java.util.*;

public class CompraDAO {
    
    /**
     * Obtiene todas las compras con información de proveedor
     */
    public List<Compra> obtenerTodos() {
        List<Compra> compras = new ArrayList<>();
        String sql = """
            SELECT c.id_compra, c.no_orden_compra, c.fecha_orden, 
                   c.fecha_ingreso, c.id_proveedor,
                   p.proveedor as nombre_proveedor
            FROM compras c
            LEFT JOIN proveedores p ON c.id_proveedor = p.id_proveedor
            ORDER BY c.fecha_orden DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("id_compra"));
                compra.setNoOrdenCompra(rs.getInt("no_orden_compra"));
                compra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
                compra.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                compra.setIdProveedor(rs.getInt("id_proveedor"));
                compra.setNombreProveedor(rs.getString("nombre_proveedor"));
                
                // Calcular total
                compra.setTotal(calcularTotalCompra(compra.getIdCompra()));
                
                compras.add(compra);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return compras;
    }
    
    /**
     * Obtiene una compra por su ID con sus detalles
     */
    public Compra obtenerPorId(int idCompra) {
        String sql = """
            SELECT c.id_compra, c.no_orden_compra, c.fecha_orden, 
                   c.fecha_ingreso, c.id_proveedor,
                   p.proveedor as nombre_proveedor
            FROM compras c
            LEFT JOIN proveedores p ON c.id_proveedor = p.id_proveedor
            WHERE c.id_compra = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Compra compra = new Compra();
                    compra.setIdCompra(rs.getInt("id_compra"));
                    compra.setNoOrdenCompra(rs.getInt("no_orden_compra"));
                    compra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
                    compra.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    compra.setIdProveedor(rs.getInt("id_proveedor"));
                    compra.setNombreProveedor(rs.getString("nombre_proveedor"));
                    
                    // Cargar detalles
                    compra.setDetalles(obtenerDetallesPorCompra(idCompra));
                    compra.setTotal(compra.calcularTotal());
                    
                    return compra;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Crea una nueva compra con sus detalles
     * Implementa exactamente la lógica del método crear() del C# repp/vista/Compra.h
     * @param compra objeto Compra con sus detalles
     * @return ID de la compra creada, 0 si hay error
     */
    public int crear(Compra compra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar la compra maestro (igual que en C#)
            String sqlCompra = "INSERT INTO compras(no_orden_compra, id_proveedor, fecha_orden, fecha_ingreso) " +
                              "VALUES (?, ?, ?, ?)";

            int idCompraCreada = 0;
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmtCompra.setInt(1, compra.getNoOrdenCompra());
                stmtCompra.setInt(2, compra.getIdProveedor());
                stmtCompra.setDate(3, java.sql.Date.valueOf(compra.getFechaOrden()));
                stmtCompra.setDate(4, java.sql.Date.valueOf(compra.getFechaIngreso()));

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
                String sqlDetalle = "INSERT INTO compras_detalle(id_compra, id_producto, cantidad, precio_costo_unitario) " +
                                   "VALUES (?, ?, ?, ?)";

                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (CompraDetalle detalle : compra.getDetalles()) {
                        stmtDetalle.setInt(1, idCompraCreada);
                        stmtDetalle.setInt(2, detalle.getIdProducto());
                        stmtDetalle.setInt(3, detalle.getCantidad());
                        stmtDetalle.setDouble(4, detalle.getPrecioCostoUnitario());

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
     * Inserta una nueva compra con sus detalles (método original)
     */
    public boolean insertar(Compra compra) {
        String sqlCompra = """
            INSERT INTO compras (no_orden_compra, fecha_orden, fecha_ingreso, id_proveedor)
            VALUES (?, ?, ?, ?)
        """;
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insertar compra
            try (PreparedStatement stmt = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, compra.getNoOrdenCompra());
            stmt.setDate(2, java.sql.Date.valueOf(compra.getFechaOrden()));
            stmt.setDate(3, java.sql.Date.valueOf(compra.getFechaIngreso()));

                stmt.setInt(4, compra.getIdProveedor());
                
                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            compra.setIdCompra(generatedKeys.getInt(1));
                            
                            // Insertar detalles
                            if (compra.tieneDetalles()) {
                                for (CompraDetalle detalle : compra.getDetalles()) {
                                    detalle.setIdCompra(compra.getIdCompra());
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
     * Actualiza una compra existente
     */
    public boolean actualizar(Compra compra) {
        String sql = """
            UPDATE compras 
            SET no_orden_compra = ?, fecha_orden = ?, id_proveedor = ?
            WHERE id_compra = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, compra.getNoOrdenCompra());
            stmt.setDate(2, java.sql.Date.valueOf(compra.getFechaOrden()));
            stmt.setInt(3, compra.getIdProveedor());
            stmt.setInt(4, compra.getIdCompra());
            
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
            String sqlDetalles = "DELETE FROM compras_detalle WHERE id_compra = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalles)) {
                stmt.setInt(1, idCompra);
                stmt.executeUpdate();
            }
            
            // Eliminar compra
            String sqlCompra = "DELETE FROM compras WHERE id_compra = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCompra)) {
                stmt.setInt(1, idCompra);
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
     * Obtiene los detalles de una compra
     */
    public List<CompraDetalle> obtenerDetallesPorCompra(int idCompra) {
        List<CompraDetalle> detalles = new ArrayList<>();
        String sql = """
            SELECT cd.id_compra_detalle, cd.id_compra, cd.id_producto, 
                   cd.cantidad, cd.precio_costo_unitario,
                   p.producto as nombre_producto,
                   m.marca as marca_producto
            FROM compras_detalle cd
            LEFT JOIN productos p ON cd.id_producto = p.id_producto
            LEFT JOIN marcas m ON p.id_marca = m.id_marca
            WHERE cd.id_compra = ?
            ORDER BY cd.id_compra_detalle
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CompraDetalle detalle = new CompraDetalle();
                    detalle.setIdCompraDetalle(rs.getInt("id_compra_detalle"));
                    detalle.setIdCompra(rs.getInt("id_compra"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioCostoUnitario(rs.getDouble("precio_costo_unitario"));
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
     * Inserta un detalle de compra
     */
    private boolean insertarDetalle(Connection conn, CompraDetalle detalle) throws SQLException {
        String sql = """
            INSERT INTO compras_detalle (id_compra, id_producto, cantidad, precio_costo_unitario)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdCompra());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioCostoUnitario());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Calcula el total de una compra
     */
    private double calcularTotalCompra(int idCompra) {
        String sql = """
            SELECT SUM(cantidad * precio_costo_unitario) as total
            FROM compras_detalle
            WHERE id_compra = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
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
