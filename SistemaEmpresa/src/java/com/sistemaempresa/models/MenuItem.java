package com.sistemaempresa.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modelo para elementos del menú dinámico
 */
public class MenuItem {
    private int idMenuItem;
    private String titulo;
    private String url;
    private String icono;
    private Integer padreId;
    private int orden;
    private boolean activo;
    private Date fechaCreacion;
    
    // Lista de elementos hijos para estructura de árbol
    private List<MenuItem> hijos;
    
    // Constructor por defecto
    public MenuItem() {
        this.hijos = new ArrayList<>();
    }
    
    // Constructor con parámetros principales
    public MenuItem(String titulo, String url, String icono, Integer padreId, int orden) {
        this();
        this.titulo = titulo;
        this.url = url;
        this.icono = icono;
        this.padreId = padreId;
        this.orden = orden;
        this.activo = true;
    }
    
    // Getters y Setters
    public int getIdMenuItem() {
        return idMenuItem;
    }
    
    public void setIdMenuItem(int idMenuItem) {
        this.idMenuItem = idMenuItem;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getIcono() {
        return icono;
    }
    
    public void setIcono(String icono) {
        this.icono = icono;
    }
    
    public Integer getPadreId() {
        return padreId;
    }
    
    public void setPadreId(Integer padreId) {
        this.padreId = padreId;
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
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<MenuItem> getHijos() {
        return hijos;
    }
    
    public void setHijos(List<MenuItem> hijos) {
        this.hijos = hijos;
    }
    
    // Métodos de utilidad
    public void agregarHijo(MenuItem hijo) {
        this.hijos.add(hijo);
    }
    
    public boolean tieneHijos() {
        return hijos != null && !hijos.isEmpty();
    }
    
    public boolean esElementoPadre() {
        return url == null || url.trim().isEmpty();
    }
    
    public boolean esElementoHoja() {
        return !tieneHijos();
    }
    
    @Override
    public String toString() {
        return "MenuItem{" +
                "idMenuItem=" + idMenuItem +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", icono='" + icono + '\'' +
                ", padreId=" + padreId +
                ", orden=" + orden +
                ", activo=" + activo +
                ", hijos=" + hijos.size() +
                '}';
    }
}
