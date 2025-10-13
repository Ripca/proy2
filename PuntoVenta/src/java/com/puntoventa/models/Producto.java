package com.puntoventa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo para la entidad Producto
 */
public class Producto {
    private int idProducto;
    private String producto;
    private String codigoBarras;
    private int idMarca;
    private String nombreMarca; // Para mostrar en vistas
    private String descripcion;
    private String imagen;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private int existencia;
    private int stockMinimo;
    private boolean activo;
    private LocalDateTime fechaIngreso;
    
    // Constructores
    public Producto() {}
    
    public Producto(String producto, String codigoBarras, int idMarca, String descripcion, 
                   String imagen, BigDecimal precioCosto, BigDecimal precioVenta, 
                   int existencia, int stockMinimo) {
        this.producto = producto;
        this.codigoBarras = codigoBarras;
        this.idMarca = idMarca;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.existencia = existencia;
        this.stockMinimo = stockMinimo;
        this.activo = true;
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
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
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
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    /**
     * Calcula el margen de ganancia del producto
     * @return margen de ganancia como porcentaje
     */
    public BigDecimal calcularMargenGanancia() {
        if (precioCosto != null && precioVenta != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ganancia = precioVenta.subtract(precioCosto);
            return ganancia.divide(precioCosto, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Verifica si el producto está en stock mínimo
     * @return true si está en stock mínimo o menor
     */
    public boolean isStockMinimo() {
        return existencia <= stockMinimo;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", producto='" + producto + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", idMarca=" + idMarca +
                ", nombreMarca='" + nombreMarca + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precioCosto=" + precioCosto +
                ", precioVenta=" + precioVenta +
                ", existencia=" + existencia +
                ", stockMinimo=" + stockMinimo +
                ", activo=" + activo +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
