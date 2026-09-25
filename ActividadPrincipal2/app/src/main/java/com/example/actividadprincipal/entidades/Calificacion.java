package com.example.actividadprincipal.entidades;

public class Calificacion {
    private Alumnos estudiante ;
    private Asignatura materia;
    private float nota1,peso1,nota2,peso2,nota3,peso3;

    public Calificacion(){

    }

    public Calificacion(Alumnos estudiante, Asignatura materia) {
        this.estudiante = estudiante;
        this.materia = materia;
    }

    public Alumnos getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Alumnos estudiante) {
        this.estudiante = estudiante;
    }

    public Asignatura getMateria() {
        return materia;
    }

    public void setMateria(Asignatura materia) {
        this.materia = materia;
    }

    public float getNota1() {
        return nota1;
    }

    public void setNota1(float nota1) {
        this.nota1 = nota1;
    }
}
