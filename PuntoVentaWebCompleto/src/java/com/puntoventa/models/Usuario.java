package com.puntoventa.models;

import java.time.LocalDate;

/**
 * Modelo para la entidad Usuario del sistema
 * Copiado exactamente de SistemaEmpresa
 */
public class Usuario {
    private int idUsuario;
    private String usuario;
    private String password;
    private int idEmpleado;
    private boolean activo;
    private LocalDate fechaCreacion;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreCompleto;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String usuario, String password, int idEmpleado) {
        this.usuario = usuario;
        this.password = password;
        this.idEmpleado = idEmpleado;
        this.activo = true;
        this.fechaCreacion = LocalDate.now();
    }
    
    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", usuario='" + usuario + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                '}';
    }
}
