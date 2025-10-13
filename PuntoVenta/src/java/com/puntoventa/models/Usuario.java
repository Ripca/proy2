package com.puntoventa.models;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Usuario del sistema
 */
public class Usuario {
    private int idUsuario;
    private String usuario;
    private String password;
    private int idEmpleado;
    private String nombreEmpleado; // Para mostrar en vistas
    private String rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimoAcceso;
    private int intentosFallidos;
    private boolean bloqueado;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String usuario, String password, int idEmpleado, String rol) {
        this.usuario = usuario;
        this.password = password;
        this.idEmpleado = idEmpleado;
        this.rol = rol;
        this.activo = true;
        this.intentosFallidos = 0;
        this.bloqueado = false;
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
    
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }
    
    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }
    
    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
    
    public int getIntentosFallidos() {
        return intentosFallidos;
    }
    
    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }
    
    public boolean isBloqueado() {
        return bloqueado;
    }
    
    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
    
    /**
     * Verifica si el usuario es administrador
     * @return true si es administrador
     */
    public boolean isAdmin() {
        return "ADMIN".equals(rol);
    }
    
    /**
     * Verifica si el usuario es supervisor
     * @return true si es supervisor
     */
    public boolean isSupervisor() {
        return "SUPERVISOR".equals(rol);
    }
    
    /**
     * Verifica si el usuario es vendedor
     * @return true si es vendedor
     */
    public boolean isVendedor() {
        return "VENDEDOR".equals(rol);
    }
    
    /**
     * Verifica si el usuario es cajero
     * @return true si es cajero
     */
    public boolean isCajero() {
        return "CAJERO".equals(rol);
    }
    
    /**
     * Verifica si el usuario tiene permisos de administración
     * @return true si es admin o supervisor
     */
    public boolean tienePermisosAdmin() {
        return isAdmin() || isSupervisor();
    }
    
    /**
     * Verifica si el usuario puede realizar ventas
     * @return true si puede realizar ventas
     */
    public boolean puedeVender() {
        return isAdmin() || isSupervisor() || isVendedor() || isCajero();
    }
    
    /**
     * Verifica si el usuario puede realizar compras
     * @return true si puede realizar compras
     */
    public boolean puedeComprar() {
        return isAdmin() || isSupervisor();
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", usuario='" + usuario + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaUltimoAcceso=" + fechaUltimoAcceso +
                ", intentosFallidos=" + intentosFallidos +
                ", bloqueado=" + bloqueado +
                '}';
    }
}
