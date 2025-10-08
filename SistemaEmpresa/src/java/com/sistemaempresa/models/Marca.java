package com.sistemaempresa.models;

/**
 * Modelo para la entidad Marca
 */
public class Marca {
    private int idMarca;
    private String marca;
    
    // Constructores
    public Marca() {}
    
    public Marca(String marca) {
        this.marca = marca;
    }
    
    public Marca(int idMarca, String marca) {
        this.idMarca = idMarca;
        this.marca = marca;
    }
    
    // Getters y Setters
    public int getIdMarca() {
        return idMarca;
    }
    
    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    @Override
    public String toString() {
        return "Marca{" +
                "idMarca=" + idMarca +
                ", marca='" + marca + '\'' +
                '}';
    }
}
