package com.sistemaempresa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Rol del sistema
 */
public class Rol {
    private int idRol;
    private String nombre;
    private int estado;
    private LocalDateTime fechaCreacion;
    
    // Constructores
    public Rol() {}
    
    public Rol(String nombre) {
        this.nombre = nombre;
        this.estado = 1;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Rol(int idRol, String nombre, int estado) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.estado = estado;
    }
    
    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }
    
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    // Métodos de utilidad
    public boolean isActivo() {
        return estado == 1;
    }
    
    public String getEstadoTexto() {
        return estado == 1 ? "Activo" : "Inactivo";
    }
    
    @Override
    public String toString() {
        return "Rol{" +
                "idRol=" + idRol +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

