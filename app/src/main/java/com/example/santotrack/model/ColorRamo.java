package com.example.santotrack.model;

/**
 * Modelo que representa la entidad COLOR según el modelo lógico de SantoTrack.
 */
public class ColorRamo {
    private int idColor;
    private String nombre;
    private String codigoHex;

    public ColorRamo() {}

    public ColorRamo(int idColor, String nombre, String codigoHex) {
        this.idColor = idColor;
        this.nombre = nombre;
        this.codigoHex = codigoHex;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoHex() {
        return codigoHex;
    }

    public void setCodigoHex(String codigoHex) {
        this.codigoHex = codigoHex;
    }
}
