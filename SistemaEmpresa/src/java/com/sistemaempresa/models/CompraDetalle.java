package com.sistemaempresa.models;

public class CompraDetalle {
    private int idCompraDetalle;
    private int idCompra;
    private int idProducto;
    private int cantidad;
    private double precioCostoUnitario;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreProducto;
    private String marcaProducto;
    private double subtotal;
    
    // Constructores
    public CompraDetalle() {
    }
    
    public CompraDetalle(int idCompra, int idProducto, int cantidad, double precioCostoUnitario) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioCostoUnitario = precioCostoUnitario;
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
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecioCostoUnitario() {
        return precioCostoUnitario;
    }
    
    public void setPrecioCostoUnitario(double precioCostoUnitario) {
        this.precioCostoUnitario = precioCostoUnitario;
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
        return cantidad * precioCostoUnitario;
    }
    
    @Override
    public String toString() {
        return "CompraDetalle{" +
                "idCompraDetalle=" + idCompraDetalle +
                ", idCompra=" + idCompra +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioCostoUnitario=" + precioCostoUnitario +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}
