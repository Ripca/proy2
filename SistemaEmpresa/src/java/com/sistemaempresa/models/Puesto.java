package com.sistemaempresa.models;

/**
 * Modelo para la entidad Puesto
 */
public class Puesto {
    private int idPuesto;
    private String puesto;
    
    // Constructores
    public Puesto() {}
    
    public Puesto(String puesto) {
        this.puesto = puesto;
    }
    
    public Puesto(int idPuesto, String puesto) {
        this.idPuesto = idPuesto;
        this.puesto = puesto;
    }
    
    // Getters y Setters
    public int getIdPuesto() {
        return idPuesto;
    }
    
    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }
    
    public String getPuesto() {
        return puesto;
    }
    
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    
    @Override
    public String toString() {
        return "Puesto{" +
                "idPuesto=" + idPuesto +
                ", puesto='" + puesto + '\'' +
                '}';
    }
}
