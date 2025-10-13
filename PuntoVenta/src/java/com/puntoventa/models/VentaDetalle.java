package com.puntoventa.models;

import java.math.BigDecimal;

/**
 * Modelo para la entidad VentaDetalle
 */
public class VentaDetalle {
    private long idVentaDetalle;
    private int idVenta;
    private int idProducto;
    private String nombreProducto; // Para mostrar en vistas
    private String codigoBarras; // Para mostrar en vistas
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    
    // Constructores
    public VentaDetalle() {
        this.descuento = BigDecimal.ZERO;
    }
    
    public VentaDetalle(int idVenta, int idProducto, int cantidad, BigDecimal precioUnitario) {
        this();
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    public VentaDetalle(int idVenta, int idProducto, int cantidad, BigDecimal precioUnitario, BigDecimal descuento) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento != null ? descuento : BigDecimal.ZERO;
        calcularSubtotal();
    }
    
    // Getters y Setters
    public long getIdVentaDetalle() {
        return idVentaDetalle;
    }
    
    public void setIdVentaDetalle(long idVentaDetalle) {
        this.idVentaDetalle = idVentaDetalle;
    }
    
    public int getIdVenta() {
        return idVenta;
    }
    
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
    
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    public BigDecimal getDescuento() {
        return descuento;
    }
    
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento != null ? descuento : BigDecimal.ZERO;
        calcularSubtotal();
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    /**
     * Calcula el subtotal del detalle
     * Fórmula: (cantidad * precioUnitario) - descuento
     */
    public void calcularSubtotal() {
        if (precioUnitario != null) {
            BigDecimal total = precioUnitario.multiply(new BigDecimal(cantidad));
            if (descuento != null) {
                total = total.subtract(descuento);
            }
            this.subtotal = total;
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
    
    /**
     * Obtiene el total sin descuento
     * @return cantidad * precioUnitario
     */
    public BigDecimal getTotalSinDescuento() {
        if (precioUnitario != null) {
            return precioUnitario.multiply(new BigDecimal(cantidad));
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Calcula el porcentaje de descuento aplicado
     * @return porcentaje de descuento
     */
    public BigDecimal getPorcentajeDescuento() {
        BigDecimal totalSinDescuento = getTotalSinDescuento();
        if (totalSinDescuento.compareTo(BigDecimal.ZERO) > 0 && descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {
            return descuento.divide(totalSinDescuento, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "VentaDetalle{" +
                "idVentaDetalle=" + idVentaDetalle +
                ", idVenta=" + idVenta +
                ", idProducto=" + idProducto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", descuento=" + descuento +
                ", subtotal=" + subtotal +
                '}';
    }
}
