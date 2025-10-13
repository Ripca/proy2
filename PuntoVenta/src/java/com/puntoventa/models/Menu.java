package com.puntoventa.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modelo para la entidad Menu del sistema
 */
public class Menu {
    private int idMenu;
    private String nombre;
    private String url;
    private String icono;
    private int orden;
    private Integer idMenuPadre;
    private String nombreMenuPadre; // Para mostrar en vistas
    private boolean activo;
    private String rolesPermitidos;
    private LocalDateTime fechaCreacion;
    
    // Lista de submenús
    private List<Menu> submenus;
    
    // Constructores
    public Menu() {
        this.submenus = new ArrayList<>();
        this.activo = true;
    }
    
    public Menu(String nombre, String url, String icono, int orden, Integer idMenuPadre, String rolesPermitidos) {
        this();
        this.nombre = nombre;
        this.url = url;
        this.icono = icono;
        this.orden = orden;
        this.idMenuPadre = idMenuPadre;
        this.rolesPermitidos = rolesPermitidos;
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
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public Integer getIdMenuPadre() {
        return idMenuPadre;
    }
    
    public void setIdMenuPadre(Integer idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
    }
    
    public String getNombreMenuPadre() {
        return nombreMenuPadre;
    }
    
    public void setNombreMenuPadre(String nombreMenuPadre) {
        this.nombreMenuPadre = nombreMenuPadre;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getRolesPermitidos() {
        return rolesPermitidos;
    }
    
    public void setRolesPermitidos(String rolesPermitidos) {
        this.rolesPermitidos = rolesPermitidos;
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
    
    /**
     * Agrega un submenú a este menú
     * @param submenu submenú a agregar
     */
    public void agregarSubmenu(Menu submenu) {
        if (this.submenus == null) {
            this.submenus = new ArrayList<>();
        }
        this.submenus.add(submenu);
    }
    
    /**
     * Verifica si este menú es un menú padre (tiene submenús)
     * @return true si tiene submenús
     */
    public boolean tieneSubmenus() {
        return submenus != null && !submenus.isEmpty();
    }
    
    /**
     * Verifica si este menú es un submenú (tiene padre)
     * @return true si tiene menú padre
     */
    public boolean esSubmenu() {
        return idMenuPadre != null;
    }
    
    /**
     * Verifica si un rol específico tiene acceso a este menú
     * @param rol rol a verificar
     * @return true si el rol tiene acceso
     */
    public boolean tieneAcceso(String rol) {
        if (rolesPermitidos == null || rolesPermitidos.trim().isEmpty()) {
            return false;
        }
        
        List<String> roles = Arrays.asList(rolesPermitidos.split(","));
        return roles.contains(rol.trim());
    }
    
    /**
     * Obtiene la lista de roles permitidos como lista
     * @return lista de roles
     */
    public List<String> getListaRoles() {
        if (rolesPermitidos == null || rolesPermitidos.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(rolesPermitidos.split(","));
    }
    
    /**
     * Verifica si el menú tiene URL (es navegable)
     * @return true si tiene URL
     */
    public boolean esNavegable() {
        return url != null && !url.trim().isEmpty() && !"#".equals(url.trim());
    }
    
    @Override
    public String toString() {
        return "Menu{" +
                "idMenu=" + idMenu +
                ", nombre='" + nombre + '\'' +
                ", url='" + url + '\'' +
                ", icono='" + icono + '\'' +
                ", orden=" + orden +
                ", idMenuPadre=" + idMenuPadre +
                ", nombreMenuPadre='" + nombreMenuPadre + '\'' +
                ", activo=" + activo +
                ", rolesPermitidos='" + rolesPermitidos + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", submenus=" + (submenus != null ? submenus.size() : 0) +
                '}';
    }
}
