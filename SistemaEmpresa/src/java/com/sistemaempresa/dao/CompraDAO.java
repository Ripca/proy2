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
            SELECT c.idCompra, c.no_orden_compra, c.fecha_orden, 
                   c.fechaingreso, c.idProveedor,
                   p.proveedor as nombre_proveedor
            FROM compras c
            LEFT JOIN proveedores p ON c.idProveedor = p.idProveedor
            ORDER BY c.fecha_orden DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("idCompra"));
                compra.setNoOrdenCompra(rs.getInt("no_orden_compra"));
                compra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
                compra.setFechaIngreso(rs.getDate("fechaingreso").toLocalDate());
                compra.setIdProveedor(rs.getInt("idProveedor"));
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
            SELECT c.idCompra, c.no_orden_compra, c.fecha_orden,
                   c.fechaingreso, c.idProveedor,
                   p.proveedor as nombre_proveedor, p.nit, p.telefono
            FROM compras c
            LEFT JOIN proveedores p ON c.idProveedor = p.idProveedor
            WHERE c.idCompra = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCompra);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Compra compra = new Compra();
                    compra.setIdCompra(rs.getInt("idCompra"));
                    compra.setNoOrdenCompra(rs.getInt("no_orden_compra"));
                    compra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
                    compra.setFechaIngreso(rs.getDate("fechaingreso").toLocalDate());
                    compra.setIdProveedor(rs.getInt("idProveedor"));
                    compra.setNombreProveedor(rs.getString("nombre_proveedor"));
                    compra.setNitProveedor(rs.getString("nit"));
                    compra.setTelefonoProveedor(rs.getString("telefono"));

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
     * Actualiza las existencias de productos automáticamente
     * @param compra objeto Compra con sus detalles
     * @return ID de la compra creada, 0 si hay error
     */
    public int crear(Compra compra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar la compra maestro (igual que en C#)
            String sqlCompra = "INSERT INTO compras(no_orden_compra, idProveedor, fecha_orden, fechaingreso) " +
                              "VALUES (?, ?, ?, ?)";

            int idCompraCreada = 0;
            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmtCompra.setInt(1, compra.getNoOrdenCompra());
                stmtCompra.setInt(2, compra.getIdProveedor());
                stmtCompra.setDate(3, java.sql.Date.valueOf(compra.getFechaOrden()));
                stmtCompra.setDate(4, java.sql.Date.valueOf(compra.getFechaIngreso()));

                int filasAfectadas = stmtCompra.executeUpdate();

                if (filasAfectadas > 0) {
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
                String sqlDetalle = "INSERT INTO compras_detalle(idCompra, idProducto, cantidad, precio_costo_unitario) " +
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

                        // 3. Actualizar existencias de productos (NUEVO - como en C#)
                        String sqlActualizarExistencia = "UPDATE productos SET existencia = existencia + ? WHERE idProducto = ?";
                        try (PreparedStatement stmtExistencia = conn.prepareStatement(sqlActualizarExistencia)) {
                            stmtExistencia.setInt(1, detalle.getCantidad());
                            stmtExistencia.setInt(2, detalle.getIdProducto());
                            stmtExistencia.executeUpdate();
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
            INSERT INTO compras (no_orden_compra, fecha_orden, fechaingreso, idProveedor)
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
     * Actualiza una compra existente y sus detalles
     */
    public boolean actualizar(Compra compra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1️⃣ Actualizar tabla principal 'compras'
            String sqlCompra = """
                UPDATE compras
                SET no_orden_compra = ?, fecha_orden = ?, idProveedor = ?
                WHERE idCompra = ?
            """;

            try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra)) {
                stmtCompra.setInt(1, compra.getNoOrdenCompra());
                stmtCompra.setDate(2, java.sql.Date.valueOf(compra.getFechaOrden()));
                stmtCompra.setInt(3, compra.getIdProveedor());
                stmtCompra.setInt(4, compra.getIdCompra());
                if (stmtCompra.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2️⃣ Procesar detalles de la compra
            if (compra.getDetalles() != null && !compra.getDetalles().isEmpty()) {

                String sqlDetallePrevio = """
                    SELECT idProducto, cantidad
                    FROM compras_detalle
                    WHERE idCompra_detalle = ?
                """;

                String sqlActualizarDetalle = """
                    UPDATE compras_detalle
                    SET idProducto = ?, cantidad = ?, precio_costo_unitario = ?
                    WHERE idCompra = ? AND idCompra_detalle = ?
                """;

                String sqlInsertarDetalle = """
                    INSERT INTO compras_detalle (idCompra, idProducto, cantidad, precio_costo_unitario)
                    VALUES (?, ?, ?, ?)
                """;

                String sqlActualizarExistencia = """
                    UPDATE productos
                    SET existencia = existencia + ?
                    WHERE idProducto = ?
                """;

                try (
                    PreparedStatement stmtPrevio = conn.prepareStatement(sqlDetallePrevio);
                    PreparedStatement stmtDetalle = conn.prepareStatement(sqlActualizarDetalle);
                    PreparedStatement stmtInsertar = conn.prepareStatement(sqlInsertarDetalle);
                    PreparedStatement stmtStock = conn.prepareStatement(sqlActualizarExistencia)
                ) {

                    for (CompraDetalle detalle : compra.getDetalles()) {
                        int idCompraDetalle = detalle.getIdCompraDetalle();
                        int nuevoIdProducto = detalle.getIdProducto();
                        int nuevaCantidad = detalle.getCantidad();
                        double nuevoPrecio = detalle.getPrecioCostoUnitario();

                        // 🔹 Verificar si es un detalle nuevo (sin idCompraDetalle o con valor 0)
                        if (idCompraDetalle == 0) {
                            // ✅ NUEVO DETALLE: Insertar
                            System.out.println("DEBUG - Insertando nuevo detalle de compra: idProducto=" + nuevoIdProducto + ", cantidad=" + nuevaCantidad);
                            stmtInsertar.setInt(1, compra.getIdCompra());
                            stmtInsertar.setInt(2, nuevoIdProducto);
                            stmtInsertar.setInt(3, nuevaCantidad);
                            stmtInsertar.setDouble(4, nuevoPrecio);
                            int inserted = stmtInsertar.executeUpdate();

                            if (inserted == 0) {
                                throw new SQLException("Error al insertar nuevo detalle de compra");
                            }

                            // Sumar al stock del nuevo producto (en compras se SUMA)
                            stmtStock.setInt(1, nuevaCantidad);
                            stmtStock.setInt(2, nuevoIdProducto);
                            stmtStock.executeUpdate();

                        } else {
                            // ✅ DETALLE EXISTENTE: Actualizar
                            System.out.println("DEBUG - Actualizando detalle existente de compra: idCompraDetalle=" + idCompraDetalle);

                            // 🔹 Obtener el estado previo del detalle
                            int idProductoAnterior = 0;
                            int cantidadAnterior = 0;

                            stmtPrevio.setInt(1, idCompraDetalle);
                            try (ResultSet rs = stmtPrevio.executeQuery()) {
                                if (rs.next()) {
                                    idProductoAnterior = rs.getInt("idProducto");
                                    cantidadAnterior = rs.getInt("cantidad");
                                } else {
                                    throw new SQLException("No se encontró el detalle con idCompraDetalle=" + idCompraDetalle);
                                }
                            }

                            // 🔹 Caso A: Cambió el producto
                            if (idProductoAnterior != nuevoIdProducto) {
                                // 1. Devolver existencia al producto anterior
                                stmtStock.setInt(1, -cantidadAnterior);  // -cantidad anterior
                                stmtStock.setInt(2, idProductoAnterior);
                                stmtStock.executeUpdate();

                                // 2. Sumar al nuevo producto
                                stmtStock.setInt(1, nuevaCantidad);
                                stmtStock.setInt(2, nuevoIdProducto);
                                stmtStock.executeUpdate();

                            } else {
                                // 🔹 Caso B: Mismo producto, pero cambió cantidad
                                int diferencia = nuevaCantidad - cantidadAnterior;

                                if (diferencia != 0) {
                                    // Si diferencia > 0 → compró más → sumar stock
                                    // Si diferencia < 0 → compró menos → restar stock
                                    stmtStock.setInt(1, diferencia);
                                    stmtStock.setInt(2, nuevoIdProducto);
                                    stmtStock.executeUpdate();
                                }
                            }

                            // 🔹 Actualizar la fila del detalle
                            stmtDetalle.setInt(1, nuevoIdProducto);
                            stmtDetalle.setInt(2, nuevaCantidad);
                            stmtDetalle.setDouble(3, nuevoPrecio);
                            stmtDetalle.setInt(4, compra.getIdCompra());
                            stmtDetalle.setInt(5, idCompraDetalle);
                            int updated = stmtDetalle.executeUpdate();

                            if (updated == 0) {
                                throw new SQLException("No se encontró el detalle con idCompraDetalle=" + idCompraDetalle);
                            }
                        }
                    }
                }
            }

            conn.commit();
            return true;

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
     * Elimina una compra y sus detalles
     * Devuelve las existencias de los productos al eliminar
     */
    public boolean eliminar(int idCompra) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener los detalles de la compra para devolver existencias
            String sqlObtenerDetalles = """
                SELECT idProducto, cantidad
                FROM compras_detalle
                WHERE idCompra = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlObtenerDetalles)) {
                stmt.setInt(1, idCompra);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int idProducto = rs.getInt("idProducto");
                        int cantidad = rs.getInt("cantidad");

                        // Devolver existencias (restar la cantidad comprada)
                        String sqlActualizarExistencia = """
                            UPDATE productos
                            SET existencia = existencia - ?
                            WHERE idProducto = ?
                        """;
                        try (PreparedStatement stmtExistencia = conn.prepareStatement(sqlActualizarExistencia)) {
                            stmtExistencia.setInt(1, cantidad);
                            stmtExistencia.setInt(2, idProducto);
                            stmtExistencia.executeUpdate();
                        }
                    }
                }
            }

            // 2. Eliminar detalles
            String sqlDetalles = "DELETE FROM compras_detalle WHERE idCompra = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalles)) {
                stmt.setInt(1, idCompra);
                stmt.executeUpdate();
            }

            // 3. Eliminar compra
            String sqlCompra = "DELETE FROM compras WHERE idCompra = ?";
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
            SELECT cd.idCompra_detalle, cd.idCompra, cd.idProducto, 
                   cd.cantidad, cd.precio_costo_unitario,
                   p.producto as nombre_producto,
                   m.marca as marca_producto
            FROM compras_detalle cd
            LEFT JOIN productos p ON cd.idProducto = p.idProducto
            LEFT JOIN marcas m ON p.idMarca = m.idMarca
            WHERE cd.idCompra = ?
            ORDER BY cd.idCompra_detalle
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCompra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CompraDetalle detalle = new CompraDetalle();
                    detalle.setIdCompraDetalle(rs.getInt("idCompra_detalle"));
                    detalle.setIdCompra(rs.getInt("idCompra"));
                    detalle.setIdProducto(rs.getInt("idProducto"));
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
            INSERT INTO compras_detalle (idCompra, idProducto, cantidad, precio_costo_unitario)
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
            WHERE idCompra = ?
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
