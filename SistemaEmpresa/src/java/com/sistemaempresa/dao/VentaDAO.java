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
    /*public List<Venta> obtenerTodos() {
        List<Venta> ventas = new ArrayList<>();
        String sql = """
                                    SELECT 
                v.idVenta,
                v.noFactura,
                v.serie,
                v.fechaFactura,
                v.idCliente,
                v.idEmpleado,
                v.fechaingreso,
                CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente,
                CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado,
                IFNULL(SUM(d.cantidad * d.precio_unitario), 0) AS Total
            FROM ventas v
            LEFT JOIN clientes c ON v.idCliente = c.idCliente
            LEFT JOIN empleados e ON v.idEmpleado = e.idEmpleado
            LEFT JOIN ventas_detalle d ON v.idVenta = d.idVenta
            GROUP BY 
                v.idVenta,
                v.noFactura,
                v.serie,
                v.fechaFactura,
                v.idCliente,
                v.idEmpleado,
                v.fechaingreso,
                nombre_cliente,
                nombre_empleado
            ORDER BY v.fechaFactura DESC;
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("idVenta"));
                venta.setNoFactura(rs.getInt("noFactura"));
                venta.setSerie(rs.getString("serie"));
                venta.setFechaFactura(rs.getDate("fechaFactura").toLocalDate());
                venta.setIdCliente(rs.getInt("idCliente"));
                venta.setIdEmpleado(rs.getInt("idEmpleado"));
                venta.setFechaIngreso(rs.getDate("fechaingreso").toLocalDate());
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setNombreEmpleado(rs.getString("nombre_empleado"));
                venta.setTotal(rs.getDouble("total"));                
                // Calcular total
                venta.setTotal(calcularTotalVenta(venta.getIdVenta()));
                
                ventas.add(venta);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ventas;
    }*/
    
    public List<Venta> obtenerTodos() {
    List<Venta> ventas = new ArrayList<>();
    String sql = """
        SELECT 
            v.idVenta,
            v.noFactura,
            v.serie,
            v.fechaFactura,
            v.idCliente,
            v.idEmpleado,
            v.fechaingreso,
            CONCAT(c.nombres, ' ', c.apellidos) AS nombre_cliente,
            CONCAT(e.nombres, ' ', e.apellidos) AS nombre_empleado,
            IFNULL(SUM(d.cantidad * d.precio_unitario), 0) AS total
        FROM ventas v
        LEFT JOIN clientes c ON v.idCliente = c.idCliente
        LEFT JOIN empleados e ON v.idEmpleado = e.idEmpleado
        LEFT JOIN ventas_detalle d ON v.idVenta = d.idVenta
        GROUP BY 
            v.idVenta,
            v.noFactura,
            v.serie,
            v.fechaFactura,
            v.idCliente,
            v.idEmpleado,
            v.fechaingreso,
            nombre_cliente,
            nombre_empleado
        ORDER BY v.fechaFactura DESC;
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Venta venta = new Venta();
            venta.setIdVenta(rs.getInt("idVenta"));
            venta.setNoFactura(rs.getInt("noFactura"));
            venta.setSerie(rs.getString("serie"));
            venta.setFechaFactura(rs.getDate("fechaFactura").toLocalDate());
            venta.setIdCliente(rs.getInt("idCliente"));
            venta.setIdEmpleado(rs.getInt("idEmpleado"));
            venta.setFechaIngreso(rs.getDate("fechaingreso").toLocalDate());
            venta.setNombreCliente(rs.getString("nombre_cliente"));
            venta.setNombreEmpleado(rs.getString("nombre_empleado"));
            venta.setTotal(rs.getDouble("total"));
            
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
            SELECT v.idVenta, v.noFactura, v.serie, v.fechaFactura, 
                   v.idCliente, v.idEmpleado, v.fechaingreso,
                   CONCAT(c.nombres, ' ', c.apellidos) as nombre_cliente,
                   CONCAT(e.nombres, ' ', e.apellidos) as nombre_empleado
            FROM ventas v
            LEFT JOIN clientes c ON v.idCliente = c.idCliente
            LEFT JOIN empleados e ON v.idEmpleado = e.idEmpleado
            WHERE v.idVenta = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("idVenta"));
                    venta.setNoFactura(rs.getInt("noFactura"));
                    venta.setSerie(rs.getString("serie"));
                    venta.setFechaFactura(rs.getDate("fechaFactura").toLocalDate());
                    venta.setIdCliente(rs.getInt("idCliente"));
                    venta.setIdEmpleado(rs.getInt("idEmpleado"));
                    venta.setFechaIngreso(rs.getDate("fechaingreso").toLocalDate());
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
            String sqlVenta = "INSERT INTO ventas(noFactura, serie, fechaFactura, idCliente, idEmpleado, fechaingreso) " +
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
                String sqlDetalle = "INSERT INTO ventas_detalle(idVenta, idProducto, cantidad, precio_unitario) " +
                                   "VALUES (?, ?, ?, ?)";

                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (VentaDetalle detalle : venta.getDetalles()) {
                        stmtDetalle.setInt(1, idVentaCreada);
                        stmtDetalle.setInt(2, detalle.getIdProducto());
                        stmtDetalle.setString(3, detalle.getCantidad()); 
                        stmtDetalle.setDouble(4, detalle.getPrecioUnitario());

                        int detalleAfectado = stmtDetalle.executeUpdate();
                        if (detalleAfectado == 0) {
                            throw new SQLException("Error al insertar detalle de venta");
                        }

                        // En ventas se RESTA la cantidad
                        try {
                            int cantidadVenta = Integer.parseInt(detalle.getCantidad());
                            String sqlActualizarExistencia = "UPDATE productos SET existencia = existencia - ? WHERE idProducto = ?";
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
            INSERT INTO ventas (noFactura, serie, fechaFactura, idCliente, idEmpleado, fechaingreso)
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
   /* public boolean actualizar(Venta venta) {
        String sql = """
            UPDATE ventas 
            SET noFactura = ?, serie = ?, fechaFactura = ?, idCliente = ?, idEmpleado = ?
            WHERE idVenta = ?
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
    */
    
    
    
    
    
    public boolean actualizar(Venta venta) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Iniciar transacción

        // 1️⃣ Actualizar tabla principal 'ventas'
        String sqlVenta = """
            UPDATE ventas 
            SET noFactura = ?, serie = ?, fechaFactura = ?, idCliente = ?, idEmpleado = ?
            WHERE idVenta = ?
        """;

        try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
            stmtVenta.setInt(1, venta.getNoFactura());
            stmtVenta.setString(2, venta.getSerie());
            stmtVenta.setDate(3, java.sql.Date.valueOf(venta.getFechaFactura()));
            stmtVenta.setInt(4, venta.getIdCliente());
            stmtVenta.setInt(5, venta.getIdEmpleado());
            stmtVenta.setInt(6, venta.getIdVenta());
            if (stmtVenta.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
        }

        // 2️⃣ Procesar detalles de la venta
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {

            String sqlDetallePrevio = """
                SELECT idProducto, cantidad
                FROM ventas_detalle
                WHERE idVenta_detalle = ?
            """;

            String sqlActualizarDetalle = """
                UPDATE ventas_detalle
                SET idProducto = ?, cantidad = ?, precio_unitario = ?
                WHERE idVenta = ? AND idVenta_detalle = ?
            """;

            String sqlInsertarDetalle = """
                INSERT INTO ventas_detalle (idVenta, idProducto, cantidad, precio_unitario)
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

                for (VentaDetalle detalle : venta.getDetalles()) {
                    int idVentaDetalle = detalle.getIdVentaDetalle();
                    int nuevoIdProducto = detalle.getIdProducto();
                    int nuevaCantidad = Integer.parseInt(detalle.getCantidad());
                    double nuevoPrecio = detalle.getPrecioUnitario();

                    // 🔹 Verificar si es un detalle nuevo (sin idVentaDetalle o con valor 0)
                    if (idVentaDetalle == 0) {
                        // ✅ NUEVO DETALLE: Insertar
                        System.out.println("DEBUG - Insertando nuevo detalle: idProducto=" + nuevoIdProducto + ", cantidad=" + nuevaCantidad);
                        stmtInsertar.setInt(1, venta.getIdVenta());
                        stmtInsertar.setInt(2, nuevoIdProducto);
                        stmtInsertar.setInt(3, nuevaCantidad);
                        stmtInsertar.setDouble(4, nuevoPrecio);
                        int inserted = stmtInsertar.executeUpdate();

                        if (inserted == 0) {
                            throw new SQLException("Error al insertar nuevo detalle de venta");
                        }

                        // Descontar del stock del nuevo producto
                        stmtStock.setInt(1, -nuevaCantidad);
                        stmtStock.setInt(2, nuevoIdProducto);
                        stmtStock.executeUpdate();

                    } else {
                        // ✅ DETALLE EXISTENTE: Actualizar
                        System.out.println("DEBUG - Actualizando detalle existente: idVentaDetalle=" + idVentaDetalle);

                        // 🔹 Obtener el estado previo del detalle
                        int idProductoAnterior = 0;
                        int cantidadAnterior = 0;

                        stmtPrevio.setInt(1, idVentaDetalle);
                        try (ResultSet rs = stmtPrevio.executeQuery()) {
                            if (rs.next()) {
                                idProductoAnterior = rs.getInt("idProducto");
                                cantidadAnterior = rs.getInt("cantidad");
                            } else {
                                throw new SQLException("No se encontró el detalle con idVentaDetalle=" + idVentaDetalle);
                            }
                        }

                        // 🔹 Caso A: Cambió el producto
                        if (idProductoAnterior != nuevoIdProducto) {
                            // 1. Devolver existencia al producto anterior
                            stmtStock.setInt(1, cantidadAnterior);  // +cantidad anterior
                            stmtStock.setInt(2, idProductoAnterior);
                            stmtStock.executeUpdate();

                            // 2. Descontar del nuevo producto
                            stmtStock.setInt(1, -nuevaCantidad);
                            stmtStock.setInt(2, nuevoIdProducto);
                            stmtStock.executeUpdate();

                        } else {
                            // 🔹 Caso B: Mismo producto, pero cambió cantidad
                            int diferencia = nuevaCantidad - cantidadAnterior;

                            if (diferencia != 0) {
                                // Si diferencia > 0 → vendió más → restar stock
                                // Si diferencia < 0 → vendió menos → devolver stock
                                stmtStock.setInt(1, -diferencia);
                                stmtStock.setInt(2, nuevoIdProducto);
                                stmtStock.executeUpdate();
                            }
                        }

                        // 🔹 Actualizar la fila del detalle
                        stmtDetalle.setInt(1, nuevoIdProducto);
                        stmtDetalle.setInt(2, nuevaCantidad);
                        stmtDetalle.setDouble(3, nuevoPrecio);
                        stmtDetalle.setInt(4, venta.getIdVenta());
                        stmtDetalle.setInt(5, idVentaDetalle);
                        int updated = stmtDetalle.executeUpdate();

                        if (updated == 0) {
                            throw new SQLException("No se encontró el detalle con idVentaDetalle=" + idVentaDetalle);
                        }
                    }
                }
            }
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;

    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

    
    
    
    /*
    public boolean actualizar(Venta venta) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Iniciar transacción

        // 1. Actualizar la venta principal
        String sqlVenta = """
            UPDATE ventas 
            SET noFactura = ?, serie = ?, fechaFactura = ?, idCliente = ?, idEmpleado = ?
            WHERE idVenta = ?
        """;
        
        try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
            stmtVenta.setInt(1, venta.getNoFactura());
            stmtVenta.setString(2, venta.getSerie());
            stmtVenta.setDate(3, java.sql.Date.valueOf(venta.getFechaFactura()));
            stmtVenta.setInt(4, venta.getIdCliente());
            stmtVenta.setInt(5, venta.getIdEmpleado());
            stmtVenta.setInt(6, venta.getIdVenta());
            
            int filasAfectadas = stmtVenta.executeUpdate();
            if (filasAfectadas == 0) {
                conn.rollback();
                return false;
            }
        }

        // 2. Actualizar los detalles de la venta
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            String sqlDetalle = """
                UPDATE ventas_detalle 
                SET idProducto = ?, cantidad = ?, precio_unitario = ?
                WHERE idVenta = ? AND idVenta_detalle = ?
            """;
            
            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                for (VentaDetalle detalle : venta.getDetalles()) {
                    stmtDetalle.setInt(1, detalle.getIdProducto());
                    stmtDetalle.setInt(2, Integer.parseInt(detalle.getCantidad()));
                    stmtDetalle.setDouble(3, detalle.getPrecioUnitario());
                    stmtDetalle.setInt(4, venta.getIdVenta());
                    stmtDetalle.setInt(5, detalle.getIdVentaDetalle());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }
        }

        conn.commit(); // Confirmar transacción
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback(); // Revertir en caso de error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}  */
    
    /**
     * Elimina una venta y sus detalles
     */
    public boolean eliminar(int idVenta) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Eliminar detalles primero
            String sqlDetalles = "DELETE FROM ventas_detalle WHERE idVenta = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalles)) {
                stmt.setInt(1, idVenta);
                stmt.executeUpdate();
            }
            
            // Eliminar venta
            String sqlVenta = "DELETE FROM ventas WHERE idVenta = ?";
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
            SELECT vd.idVenta_detalle, vd.idVenta, vd.idProducto, 
                   vd.cantidad, vd.precio_unitario,
                   p.producto as nombre_producto,
                   m.marca as marca_producto
            FROM ventas_detalle vd
            LEFT JOIN productos p ON vd.idProducto = p.idProducto
            LEFT JOIN marcas m ON p.idMarca = m.idMarca
            WHERE vd.idVenta = ?
            ORDER BY vd.idVenta_detalle
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setIdVentaDetalle(rs.getInt("idVenta_detalle"));
                    detalle.setIdVenta(rs.getInt("idVenta"));
                    detalle.setIdProducto(rs.getInt("idProducto"));
                    detalle.setCantidad(rs.getString("cantidad")); 
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
            INSERT INTO ventas_detalle (idVenta, idProducto, cantidad, precio_unitario)
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
