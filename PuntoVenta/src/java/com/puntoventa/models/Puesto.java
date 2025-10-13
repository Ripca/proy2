package com.puntoventa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo para la entidad Puesto
 */
public class Puesto {
    private int idPuesto;
    private String puesto;
    private String descripcion;
    private BigDecimal salarioBase;
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public Puesto() {}
    
    public Puesto(String puesto, String descripcion, BigDecimal salarioBase) {
        this.puesto = puesto;
        this.descripcion = descripcion;
        this.salarioBase = salarioBase;
    }
    
    public Puesto(int idPuesto, String puesto, String descripcion, BigDecimal salarioBase) {
        this.idPuesto = idPuesto;
        this.puesto = puesto;
        this.descripcion = descripcion;
        this.salarioBase = salarioBase;
    }
    
    // Getters y Setters
    public int getIdPuesto() {
        return idPuesto;
    }
    
    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }
    
    public String getPuesto() {
        return puesto;
    }
    
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public BigDecimal getSalarioBase() {
        return salarioBase;
    }
    
    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "Puesto{" +
                "idPuesto=" + idPuesto +
                ", puesto='" + puesto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", salarioBase=" + salarioBase +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
