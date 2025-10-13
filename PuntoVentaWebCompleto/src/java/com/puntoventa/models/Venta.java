package com.puntoventa.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo para la entidad Venta
 * Basado exactamente en la estructura del proyecto C# repp/vista/Venta.h
 */
public class Venta {
    private int idVenta;
    private int noFactura;
    private String serie;
    private Date fechaFactura;
    private int idCliente;
    private String nombreCliente; // Para mostrar en vistas
    private int idEmpleado;
    private String nombreEmpleado; // Para mostrar en vistas
    private Date fechaIngreso;
    
    // Lista de detalles de venta (equivalente al vector<DetalleVenta> del C#)
    private List<VentaDetalle> detalles;
    
    // Constructores
    public Venta() {
        this.detalles = new ArrayList<>();
        this.serie = "A"; // Serie por defecto como en el C#
    }
    
    public Venta(int noFactura, String serie, Date fechaFactura, int idCliente, int idEmpleado, Date fechaIngreso) {
        this();
        this.noFactura = noFactura;
        this.serie = serie;
        this.fechaFactura = fechaFactura;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaIngreso = fechaIngreso;
    }
    
    // Getters y Setters (equivalentes a los métodos get/set del C#)
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
    
    public Date getFechaFactura() {
        return fechaFactura;
    }
    
    public void setFechaFactura(Date fechaFactura) {
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
    
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public List<VentaDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
    
    // Métodos de utilidad (equivalentes a los del C#)
    public void agregarDetalle(VentaDetalle detalle) {
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
                .mapToDouble(detalle -> Integer.parseInt(detalle.getCantidad()) * detalle.getPrecioUnitario())
                .sum();
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
                ", fechaIngreso=" + fechaIngreso +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
