package com.example.santotrack.model;

/**
 * Modelo que representa la entidad EVALUACION según el modelo lógico de SantoTrack.
 */
public class Evaluacion {
    private int idEvaluacion;
    private int idAsignatura;
    private String nombre;
    private double porcentaje;
    private Double nota;
    private String fechaCreacion;

    public Evaluacion() {}

    public Evaluacion(int idEvaluacion, int idAsignatura, String nombre, double porcentaje, Double nota, String fechaCreacion) {
        this.idEvaluacion = idEvaluacion;
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.nota = nota;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(int idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
