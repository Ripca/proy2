package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Proveedor
 */
public class Proveedor {
    private int idProveedor;
    private String proveedor;
    private String nit;
    private String direccion;
    private String telefono;
    private String email;
    private String contactoPrincipal;
    private boolean activo;
    private LocalDateTime fechaIngreso;
    
    // Constructores
    public Proveedor() {}
    
    public Proveedor(String proveedor, String nit, String direccion, String telefono, String email, String contactoPrincipal) {
        this.proveedor = proveedor;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contactoPrincipal = contactoPrincipal;
        this.activo = true;
    }
    
    // Getters y Setters
    public int getIdProveedor() {
        return idProveedor;
    }
    
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    public String getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    
    public String getNit() {
        return nit;
    }
    
    public void setNit(String nit) {
        this.nit = nit;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getContactoPrincipal() {
        return contactoPrincipal;
    }
    
    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", proveedor='" + proveedor + '\'' +
                ", nit='" + nit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", contactoPrincipal='" + contactoPrincipal + '\'' +
                ", activo=" + activo +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
