package com.puntoventa.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para la entidad Venta
 */
public class Venta {
    private int idVenta;
    private int noFactura;
    private String serie;
    private LocalDate fechaFactura;
    private int idCliente;
    private String nombreCliente; // Para mostrar en vistas
    private int idEmpleado;
    private String nombreEmpleado; // Para mostrar en vistas
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private String observaciones;
    private int idUsuario;
    private LocalDateTime fechaIngreso;
    
    // Lista de detalles de venta
    private List<VentaDetalle> detalles;
    
    // Constructores
    public Venta() {
        this.detalles = new ArrayList<>();
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.serie = "A";
        this.metodoPago = "EFECTIVO";
        this.estado = "COMPLETADA";
    }
    
    public Venta(int noFactura, String serie, LocalDate fechaFactura, int idCliente, 
                int idEmpleado, String metodoPago, int idUsuario) {
        this();
        this.noFactura = noFactura;
        this.serie = serie;
        this.fechaFactura = fechaFactura;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.metodoPago = metodoPago;
        this.idUsuario = idUsuario;
    }
    
    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }
    
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
    
    public int getNoFactura() {
        return noFactura;
    }
    
    public void setNoFactura(int noFactura) {
        this.noFactura = noFactura;
    }
    
    public String getSerie() {
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public LocalDate getFechaFactura() {
        return fechaFactura;
    }
    
    public void setFechaFactura(LocalDate fechaFactura) {
        this.fechaFactura = fechaFactura;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public int getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }
    
    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
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
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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
    
    public List<VentaDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Agrega un detalle a la venta
     * @param detalle detalle a agregar
     */
    public void agregarDetalle(VentaDetalle detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
        calcularTotales();
    }
    
    /**
     * Elimina un detalle de la venta
     * @param detalle detalle a eliminar
     */
    public void eliminarDetalle(VentaDetalle detalle) {
        if (this.detalles != null) {
            this.detalles.remove(detalle);
            calcularTotales();
        }
    }
    
    /**
     * Calcula los totales de la venta basado en los detalles
     */
    public void calcularTotales() {
        if (detalles != null && !detalles.isEmpty()) {
            subtotal = detalles.stream()
                    .map(VentaDetalle::getSubtotal)
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
     * Obtiene la cantidad total de productos en la venta
     * @return cantidad total de productos
     */
    public int getCantidadTotalProductos() {
        if (detalles != null) {
            return detalles.stream()
                    .mapToInt(VentaDetalle::getCantidad)
                    .sum();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", noFactura=" + noFactura +
                ", serie='" + serie + '\'' +
                ", fechaFactura=" + fechaFactura +
                ", idCliente=" + idCliente +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                ", estado='" + estado + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", idUsuario=" + idUsuario +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
