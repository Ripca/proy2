package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad CarruselImagen
 * Copiado exactamente de SistemaEmpresa
 */
public class CarruselImagen {
    private int idImagen;
    private String titulo;
    private String urlImagen;
    private String descripcion;
    private int orden;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public CarruselImagen() {
    }
    
    public CarruselImagen(String titulo, String urlImagen, String descripcion, int orden) {
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.descripcion = descripcion;
        this.orden = orden;
        this.estado = true;
    }
    
    // Getters y Setters
    public int getIdImagen() {
        return idImagen;
    }
    
    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getUrlImagen() {
        return urlImagen;
    }
    
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public boolean isEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "CarruselImagen{" +
                "idImagen=" + idImagen +
                ", titulo='" + titulo + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", orden=" + orden +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
