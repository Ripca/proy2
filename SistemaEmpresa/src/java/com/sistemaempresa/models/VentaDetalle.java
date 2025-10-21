package com.sistemaempresa.models;

public class VentaDetalle {
    private int idVentaDetalle;
    private int idVenta;
    private int idProducto;
    private String cantidad; 
    private double precioUnitario;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreProducto;
    private String marcaProducto;
    private double subtotal;
    
    // Constructores
    public VentaDetalle() {
    }
    
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
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public String getMarcaProducto() {
        return marcaProducto;
    }
    
    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    // Métodos de utilidad
    public double calcularSubtotal() {
        try {
            return Integer.parseInt(cantidad) * precioUnitario;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return "VentaDetalle{" +
                "idVentaDetalle=" + idVentaDetalle +
                ", idVenta=" + idVenta +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}
