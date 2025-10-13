package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Marca
 */
public class Marca {
    private int idMarca;
    private String marca;
    private String descripcion;
    private String paisOrigen;
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public Marca() {}
    
    public Marca(String marca, String descripcion, String paisOrigen) {
        this.marca = marca;
        this.descripcion = descripcion;
        this.paisOrigen = paisOrigen;
    }
    
    public Marca(int idMarca, String marca, String descripcion, String paisOrigen) {
        this.idMarca = idMarca;
        this.marca = marca;
        this.descripcion = descripcion;
        this.paisOrigen = paisOrigen;
    }
    
    // Getters y Setters
    public int getIdMarca() {
        return idMarca;
    }
    
    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getPaisOrigen() {
        return paisOrigen;
    }
    
    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "Marca{" +
                "idMarca=" + idMarca +
                ", marca='" + marca + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", paisOrigen='" + paisOrigen + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
