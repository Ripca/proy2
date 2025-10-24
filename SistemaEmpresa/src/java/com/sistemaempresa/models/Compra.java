package com.sistemaempresa.models;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Compra {
    private int idCompra;
    private int noOrdenCompra;
    private LocalDate fechaOrden;
    private LocalDate fechaIngreso;
    private int idProveedor;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreProveedor;
    private String nitProveedor;
    private String telefonoProveedor;
    private double total;
    
    // Lista de detalles de compra
    private List<CompraDetalle> detalles;
    
    // Constructores
    public Compra() {
        this.detalles = new ArrayList<>();
    }
    
    public Compra(int noOrdenCompra, LocalDate fechaOrden, int idProveedor) {
        this();
        this.noOrdenCompra = noOrdenCompra;
        this.fechaOrden = fechaOrden;
        this.idProveedor = idProveedor;
        this.fechaIngreso = LocalDate.now();
    }
    
    // Getters y Setters
    public int getIdCompra() {
        return idCompra;
    }
    
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
    
    public int getNoOrdenCompra() {
        return noOrdenCompra;
    }
    
    public void setNoOrdenCompra(int noOrdenCompra) {
        this.noOrdenCompra = noOrdenCompra;
    }
    
    public LocalDate getFechaOrden() {
        return fechaOrden;
    }
    
    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }
    
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public List<CompraDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }
    
    // Métodos de utilidad
    public void agregarDetalle(CompraDetalle detalle) {
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
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioCostoUnitario())
                .sum();
    }
    
    public boolean tieneDetalles() {
        return detalles != null && !detalles.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", noOrdenCompra=" + noOrdenCompra +
                ", fechaOrden=" + fechaOrden +
                ", idProveedor=" + idProveedor +
                ", total=" + total +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
