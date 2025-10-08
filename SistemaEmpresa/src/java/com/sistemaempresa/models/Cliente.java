package com.sistemaempresa.models;

import java.util.Date;

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
    private Date fechaIngreso;
    
    // Constructores
    public Cliente() {}
    
    public Cliente(String nombres, String apellidos, String nit, boolean genero, 
                  String telefono, String correoElectronico) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nit = nit;
        this.genero = genero;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
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
    
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
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
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
