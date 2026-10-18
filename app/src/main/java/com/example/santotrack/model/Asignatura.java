package com.example.santotrack.model;

/**
 * Modelo que representa la entidad ASIGNATURA según el modelo lógico de SantoTrack.
 */
public class Asignatura {
    private int idAsignatura;
    private int idUsuario;
    private int idColor;
    private String nombre;
    private double notaAprobacion;
    private String fechaCreacion;

    // Campos de apoyo obtenidos mediante JOIN con la tabla COLOR
    private String colorHex;
    private String colorNombre;

    public Asignatura() {}

    public Asignatura(int idAsignatura, int idUsuario, int idColor, String nombre, double notaAprobacion, String fechaCreacion) {
        this.idAsignatura = idAsignatura;
        this.idUsuario = idUsuario;
        this.idColor = idColor;
        this.nombre = nombre;
        this.notaAprobacion = notaAprobacion;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(int idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public double getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(double notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getColorHex() {
        return colorHex != null ? colorHex : "#3525CD";
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getColorNombre() {
        return colorNombre;
    }

    public void setColorNombre(String colorNombre) {
        this.colorNombre = colorNombre;
    }
}
