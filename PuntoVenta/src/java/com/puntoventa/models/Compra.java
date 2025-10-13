package com.puntoventa.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para la entidad Compra
 */
public class Compra {
    private int idCompra;
    private int noOrdenCompra;
    private int idProveedor;
    private String nombreProveedor; // Para mostrar en vistas
    private LocalDate fechaOrden;
    private LocalDate fechaEntrega;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String estado;
    private String observaciones;
    private int idUsuario;
    private LocalDateTime fechaIngreso;
    
    // Lista de detalles de compra
    private List<CompraDetalle> detalles;
    
    // Constructores
    public Compra() {
        this.detalles = new ArrayList<>();
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.estado = "PENDIENTE";
    }
    
    public Compra(int noOrdenCompra, int idProveedor, LocalDate fechaOrden, int idUsuario) {
        this();
        this.noOrdenCompra = noOrdenCompra;
        this.idProveedor = idProveedor;
        this.fechaOrden = fechaOrden;
        this.idUsuario = idUsuario;
    }
    
    // Getters y Setters
    public int getIdCompra() {
        return idCompra;
    }
    
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
    
    public int getNoOrdenCompra() {
        return noOrdenCompra;
    }
    
    public void setNoOrdenCompra(int noOrdenCompra) {
        this.noOrdenCompra = noOrdenCompra;
    }
    
    public int getIdProveedor() {
        return idProveedor;
    }
    
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    public String getNombreProveedor() {
        return nombreProveedor;
    }
    
    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
    
    public LocalDate getFechaOrden() {
        return fechaOrden;
    }
    
    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }
    
    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }
    
    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public List<CompraDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Agrega un detalle a la compra
     * @param detalle detalle a agregar
     */
    public void agregarDetalle(CompraDetalle detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
        calcularTotales();
    }
    
    /**
     * Elimina un detalle de la compra
     * @param detalle detalle a eliminar
     */
    public void eliminarDetalle(CompraDetalle detalle) {
        if (this.detalles != null) {
            this.detalles.remove(detalle);
            calcularTotales();
        }
    }
    
    /**
     * Calcula los totales de la compra basado en los detalles
     */
    public void calcularTotales() {
        if (detalles != null && !detalles.isEmpty()) {
            subtotal = detalles.stream()
                    .map(CompraDetalle::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            subtotal = BigDecimal.ZERO;
        }
        
        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }
        
        total = subtotal.subtract(descuento);
    }
    
    /**
     * Obtiene la cantidad total de productos en la compra
     * @return cantidad total de productos
     */
    public int getCantidadTotalProductos() {
        if (detalles != null) {
            return detalles.stream()
                    .mapToInt(CompraDetalle::getCantidad)
                    .sum();
        }
        return 0;
    }
    
    /**
     * Verifica si la compra está pendiente de entrega
     * @return true si está pendiente
     */
    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }
    
    /**
     * Verifica si la compra ha sido recibida
     * @return true si ha sido recibida
     */
    public boolean isRecibida() {
        return "RECIBIDA".equals(estado);
    }
    
    /**
     * Verifica si la compra ha sido cancelada
     * @return true si ha sido cancelada
     */
    public boolean isCancelada() {
        return "CANCELADA".equals(estado);
    }
    
    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", noOrdenCompra=" + noOrdenCompra +
                ", idProveedor=" + idProveedor +
                ", nombreProveedor='" + nombreProveedor + '\'' +
                ", fechaOrden=" + fechaOrden +
                ", fechaEntrega=" + fechaEntrega +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", idUsuario=" + idUsuario +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
