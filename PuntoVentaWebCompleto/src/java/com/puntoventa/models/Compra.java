package com.puntoventa.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo para la entidad Compra
 * Basado exactamente en la estructura del proyecto C# repp/vista/Compra.h
 */
public class Compra {
    private int idCompra;
    private int noOrderCompra;
    private int idProveedor;
    private String nombreProveedor; // Para mostrar en vistas
    private Date fechaOrder;
    private Date fechaIngreso;
    
    // Lista de detalles de compra (equivalente al vector<DetalleCompra> del C#)
    private List<CompraDetalle> detalles;
    
    // Constructores
    public Compra() {
        this.detalles = new ArrayList<>();
    }
    
    public Compra(int noOrderCompra, int idProveedor, Date fechaOrder, Date fechaIngreso) {
        this();
        this.noOrderCompra = noOrderCompra;
        this.idProveedor = idProveedor;
        this.fechaOrder = fechaOrder;
        this.fechaIngreso = fechaIngreso;
    }
    
    // Getters y Setters (equivalentes a los métodos get/set del C#)
    public int getIdCompra() {
        return idCompra;
    }
    
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
    
    public int getNoOrderCompra() {
        return noOrderCompra;
    }
    
    public void setNoOrderCompra(int noOrderCompra) {
        this.noOrderCompra = noOrderCompra;
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
    
    public Date getFechaOrder() {
        return fechaOrder;
    }
    
    public void setFechaOrder(Date fechaOrder) {
        this.fechaOrder = fechaOrder;
    }
    
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public List<CompraDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }
    
    // Métodos de utilidad (equivalentes a los del C#)
    public void agregarDetalle(CompraDetalle detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }
    
    public void limpiarDetalles() {
        if (this.detalles != null) {
            this.detalles.clear();
        }
    }
    
    public boolean tieneDetalles() {
        return detalles != null && !detalles.isEmpty();
    }
    
    public double calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            return 0.0;
        }
        
        return detalles.stream()
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                .sum();
    }
    
    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", noOrderCompra=" + noOrderCompra +
                ", idProveedor=" + idProveedor +
                ", nombreProveedor='" + nombreProveedor + '\'' +
                ", fechaOrder=" + fechaOrder +
                ", fechaIngreso=" + fechaIngreso +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
