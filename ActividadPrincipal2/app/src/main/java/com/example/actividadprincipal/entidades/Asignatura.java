package com.example.actividadprincipal.entidades;

public class Asignatura {
    private String codigo;
    private String nombre;
    private Programas carrera;

    public Asignatura(String codigo, String nombre, Programas carrera) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Programas getCarrera() {
        return carrera;
    }

    public void setCarrera(Programas carrera) {
        this.carrera = carrera;
    }
}
