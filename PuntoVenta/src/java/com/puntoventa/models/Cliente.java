package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Cliente
 */
public class Cliente {
    private int idCliente;
    private String nombres;
    private String apellidos;
    private String nit;
    private boolean genero; // true = masculino, false = femenino
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private boolean activo;
    private LocalDateTime fechaIngreso;
    
    // Constructores
    public Cliente() {}
    
    public Cliente(String nombres, String apellidos, String nit, boolean genero, 
                  String telefono, String correoElectronico, String direccion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nit = nit;
        this.genero = genero;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.activo = true;
    }
    
    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getNit() {
        return nit;
    }
    
    public void setNit(String nit) {
        this.nit = nit;
    }
    
    public boolean isGenero() {
        return genero;
    }
    
    public void setGenero(boolean genero) {
        this.genero = genero;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    
    /**
     * Obtiene el nombre completo del cliente
     * @return nombres + apellidos
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    /**
     * Obtiene el género como texto
     * @return "Masculino" o "Femenino"
     */
    public String getGeneroTexto() {
        return genero ? "Masculino" : "Femenino";
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", nit='" + nit + '\'' +
                ", genero=" + genero +
                ", telefono='" + telefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
