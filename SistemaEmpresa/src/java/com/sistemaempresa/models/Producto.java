package com.sistemaempresa.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Modelo para la entidad Producto
 */
public class Producto {
    private int idProducto;
    private String producto;
    private int idMarca;
    private String nombreMarca; // Para mostrar en vistas
    private String descripcion;
    private String imagen;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private int existencia;
    private Date fechaIngreso;
    
    // Constructores
    public Producto() {}
    
    public Producto(String producto, int idMarca, String descripcion, String imagen,
                   BigDecimal precioCosto, BigDecimal precioVenta, int existencia) {
        this.producto = producto;
        this.idMarca = idMarca;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.existencia = existencia;
    }
    
    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getProducto() {
        return producto;
    }
    
    public void setProducto(String producto) {
        this.producto = producto;
    }
    
    public int getIdMarca() {
        return idMarca;
    }
    
    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
    
    public String getNombreMarca() {
        return nombreMarca;
    }
    
    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }
    
    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public int getExistencia() {
        return existencia;
    }
    
    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }
    
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    // Métodos de utilidad
    public BigDecimal getMargenGanancia() {
        if (precioCosto != null && precioVenta != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            return precioVenta.subtract(precioCosto);
        }
        return BigDecimal.ZERO;
    }
    
    public double getPorcentajeGanancia() {
        if (precioCosto != null && precioVenta != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal margen = getMargenGanancia();
            return margen.divide(precioCosto, 4, RoundingMode.HALF_UP)
                         .multiply(new BigDecimal("100")).doubleValue();
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", producto='" + producto + '\'' +
                ", idMarca=" + idMarca +
                ", nombreMarca='" + nombreMarca + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precioCosto=" + precioCosto +
                ", precioVenta=" + precioVenta +
                ", existencia=" + existencia +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
