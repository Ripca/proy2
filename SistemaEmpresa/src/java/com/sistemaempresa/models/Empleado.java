package com.sistemaempresa.models;

import java.util.Date;

/**
 * Modelo para la entidad Empleado
 */
public class Empleado {
    private int idEmpleado;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String dpi;
    private boolean genero; // true = masculino, false = femenino
    private Date fechaNacimiento;
    private int idPuesto;
    private String nombrePuesto; // Para mostrar en vistas
    private Date fechaInicioLabores;
    private Date fechaIngreso;
    
    // Constructores
    public Empleado() {}
    
    public Empleado(String nombres, String apellidos, String direccion, String telefono,
                   String dpi, boolean genero, Date fechaNacimiento, int idPuesto,
                   Date fechaInicioLabores) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dpi = dpi;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.idPuesto = idPuesto;
        this.fechaInicioLabores = fechaInicioLabores;
    }
    
    // Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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
    
    public String getDpi() {
        return dpi;
    }
    
    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
    
    public boolean isGenero() {
        return genero;
    }
    
    public void setGenero(boolean genero) {
        this.genero = genero;
    }
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public int getIdPuesto() {
        return idPuesto;
    }
    
    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }
    
    public String getNombrePuesto() {
        return nombrePuesto;
    }
    
    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }
    
    public Date getFechaInicioLabores() {
        return fechaInicioLabores;
    }
    
    public void setFechaInicioLabores(Date fechaInicioLabores) {
        this.fechaInicioLabores = fechaInicioLabores;
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
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dpi='" + dpi + '\'' +
                ", genero=" + genero +
                ", fechaNacimiento=" + fechaNacimiento +
                ", idPuesto=" + idPuesto +
                ", nombrePuesto='" + nombrePuesto + '\'' +
                ", fechaInicioLabores=" + fechaInicioLabores +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
