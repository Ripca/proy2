package com.sistemaempresa.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Menu {
    private int idMenu;
    private String nombre;
    private String icono;
    private String url;
    private Integer idPadre;
    private int orden;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    
    // Para menús jerárquicos
    private List<Menu> submenus;
    private String nombrePadre;
    
    // Constructores
    public Menu() {
        this.submenus = new ArrayList<>();
    }
    
    public Menu(String nombre, String icono, String url, Integer idPadre, int orden) {
        this();
        this.nombre = nombre;
        this.icono = icono;
        this.url = url;
        this.idPadre = idPadre;
        this.orden = orden;
        this.estado = true;
    }
    
    // Getters y Setters
    public int getIdMenu() {
        return idMenu;
    }
    
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getIcono() {
        return icono;
    }
    
    public void setIcono(String icono) {
        this.icono = icono;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getIdPadre() {
        return idPadre;
    }
    
    public void setIdPadre(Integer idPadre) {
        this.idPadre = idPadre;
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
    
    public List<Menu> getSubmenus() {
        return submenus;
    }
    
    public void setSubmenus(List<Menu> submenus) {
        this.submenus = submenus;
    }
    
    public String getNombrePadre() {
        return nombrePadre;
    }
    
    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }
    
    // Métodos de utilidad
    public boolean esMenuPrincipal() {
        return idPadre == null;
    }
    
    public boolean tieneSubmenus() {
        return submenus != null && !submenus.isEmpty();
    }
    
    public void agregarSubmenu(Menu submenu) {
        if (this.submenus == null) {
            this.submenus = new ArrayList<>();
        }
        this.submenus.add(submenu);
    }
    
    @Override
    public String toString() {
        return "Menu{" +
                "idMenu=" + idMenu +
                ", nombre='" + nombre + '\'' +
                ", icono='" + icono + '\'' +
                ", url='" + url + '\'' +
                ", idPadre=" + idPadre +
                ", orden=" + orden +
                ", estado=" + estado +
                ", submenus=" + (submenus != null ? submenus.size() : 0) +
                '}';
    }
}
