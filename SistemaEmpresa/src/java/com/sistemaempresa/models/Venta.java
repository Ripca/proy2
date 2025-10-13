package com.sistemaempresa.models;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Venta {
    private int idVenta;
    private int noFactura;
    private String serie;
    private LocalDate fechaFactura;
    private int idCliente;
    private int idEmpleado;
    private LocalDate fechaIngreso;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreCliente;
    private String nombreEmpleado;
    private double total;
    
    // Lista de detalles de venta
    private List<VentaDetalle> detalles;
    
    // Constructores
    public Venta() {
        this.detalles = new ArrayList<>();
    }
    
    public Venta(int noFactura, String serie, LocalDate fechaFactura, int idCliente, int idEmpleado) {
        this();
        this.noFactura = noFactura;
        this.serie = serie;
        this.fechaFactura = fechaFactura;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaIngreso = LocalDate.now();
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
    
    public int getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }
    
    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public List<VentaDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
    
    // Métodos de utilidad
    public void agregarDetalle(VentaDetalle detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }
    
    public double calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            return 0.0;
        }

        return detalles.stream()
                .mapToDouble(detalle -> {
                    try {
                        return Integer.parseInt(detalle.getCantidad()) * detalle.getPrecioUnitario();
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();
    }
    
    public boolean tieneDetalles() {
        return detalles != null && !detalles.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", noFactura=" + noFactura +
                ", serie='" + serie + '\'' +
                ", fechaFactura=" + fechaFactura +
                ", idCliente=" + idCliente +
                ", idEmpleado=" + idEmpleado +
                ", total=" + total +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
