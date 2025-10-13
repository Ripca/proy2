package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad CarruselImagen
 */
public class CarruselImagen {
    private int idImagen;
    private String titulo;
    private String descripcion;
    private String imagen;
    private String urlEnlace;
    private int orden;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public CarruselImagen() {
        this.activo = true;
    }
    
    public CarruselImagen(String titulo, String descripcion, String imagen, String urlEnlace, int orden) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.urlEnlace = urlEnlace;
        this.orden = orden;
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
    
    public String getUrlEnlace() {
        return urlEnlace;
    }
    
    public void setUrlEnlace(String urlEnlace) {
        this.urlEnlace = urlEnlace;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    /**
     * Verifica si la imagen tiene enlace
     * @return true si tiene enlace válido
     */
    public boolean tieneEnlace() {
        return urlEnlace != null && !urlEnlace.trim().isEmpty() && !"#".equals(urlEnlace.trim());
    }
    
    /**
     * Obtiene la ruta completa de la imagen
     * @return ruta de la imagen con prefijo assets/img/carousel/
     */
    public String getRutaCompleta() {
        if (imagen != null && !imagen.trim().isEmpty()) {
            return "assets/img/carousel/" + imagen;
        }
        return "assets/img/carousel/default.jpg";
    }
    
    @Override
    public String toString() {
        return "CarruselImagen{" +
                "idImagen=" + idImagen +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", urlEnlace='" + urlEnlace + '\'' +
                ", orden=" + orden +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
