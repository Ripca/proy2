package com.sistemaempresa.models;

/**
 * Modelo para la entidad Proveedor
 */
public class Proveedor {
    private int idProveedor;
    private String proveedor;
    private String nit;
    private String direccion;
    private String telefono;
    
    // Constructores
    public Proveedor() {}
    
    public Proveedor(String proveedor, String nit, String direccion, String telefono) {
        this.proveedor = proveedor;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
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
    
    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", proveedor='" + proveedor + '\'' +
                ", nit='" + nit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
