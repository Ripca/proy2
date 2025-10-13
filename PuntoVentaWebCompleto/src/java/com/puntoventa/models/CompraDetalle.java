package com.puntoventa.models;

/**
 * Modelo para la entidad CompraDetalle
 * Basado exactamente en la estructura DetalleCompra del proyecto C# repp/vista/Compra.h
 */
public class CompraDetalle {
    private int idCompraDetalle;
    private int idCompra;
    private int idProducto;
    private String nombreProducto; // Para mostrar en vistas
    private int cantidad;
    private double precioUnitario;
    
    // Constructores
    public CompraDetalle() {}
    
    public CompraDetalle(int idCompra, int idProducto, int cantidad, double precioUnitario) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public int getIdCompraDetalle() {
        return idCompraDetalle;
    }
    
    public void setIdCompraDetalle(int idCompraDetalle) {
        this.idCompraDetalle = idCompraDetalle;
    }
    
    public int getIdCompra() {
        return idCompra;
    }
    
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
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
        return cantidad * precioUnitario;
    }
    
    @Override
    public String toString() {
        return "CompraDetalle{" +
                "idCompraDetalle=" + idCompraDetalle +
                ", idCompra=" + idCompra +
                ", idProducto=" + idProducto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
