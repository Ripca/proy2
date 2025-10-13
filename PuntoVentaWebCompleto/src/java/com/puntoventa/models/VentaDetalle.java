package com.puntoventa.models;

/**
 * Modelo para la entidad VentaDetalle
 * Basado exactamente en la estructura DetalleVenta del proyecto C# repp/vista/Venta.h
 * NOTA: cantidad es VARCHAR(45) en la base de datos como en el C#
 */
public class VentaDetalle {
    private int idVentaDetalle;
    private int idVenta;
    private int idProducto;
    private String nombreProducto; // Para mostrar en vistas
    private String cantidad; // VARCHAR(45) como en el C# - NO es int
    private double precioUnitario;
    
    // Constructores
    public VentaDetalle() {}
    
    public VentaDetalle(int idVenta, int idProducto, String cantidad, double precioUnitario) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public int getIdVentaDetalle() {
        return idVentaDetalle;
    }
    
    public void setIdVentaDetalle(int idVentaDetalle) {
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
    
    public String getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    // Métodos de utilidad
    public double getSubtotal() {
        try {
            return Integer.parseInt(cantidad) * precioUnitario;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public int getCantidadNumerica() {
        try {
            return Integer.parseInt(cantidad);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "VentaDetalle{" +
                "idVentaDetalle=" + idVentaDetalle +
                ", idVenta=" + idVenta +
                ", idProducto=" + idProducto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
