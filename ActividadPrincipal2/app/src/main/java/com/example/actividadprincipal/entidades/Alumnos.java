package com.example.actividadprincipal.entidades;

public class Alumnos {
    private String codigo;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Programas carrera;

    public Alumnos(String codigo, String nombre, String apellido) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Programas getCarrera() {
        return carrera;
    }

    public void setCarrera(Programas carrera) {
        this.carrera = carrera;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
