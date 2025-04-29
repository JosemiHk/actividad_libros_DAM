package com.example.activida;
import java.util.Date;

public class Prestamo {

    private static int contadorPrestamos = 0; // Contador estático para generar IDs únicos
    private int idPrestamo; // Nuevo campo para el ID del préstamo
    private Estudiante estudiante;
    private Libro libro;
    private Bibliotecario bibliotecario;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private double mora;
    private boolean devuelto = false;

    public Prestamo(Estudiante estudiante, Libro libro, Bibliotecario bibliotecario, Date fechaPrestamo, Date fechaDevolucion) {
        this.idPrestamo = ++contadorPrestamos; // Asignar un ID único al crear el préstamo
        this.estudiante = estudiante;
        this.libro = libro;
        this.bibliotecario = bibliotecario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        calcularMora();
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }
    private void calcularMora() {
        long diferenciaMillis = fechaDevolucion.getTime() - fechaPrestamo.getTime();
        long dias = (long) Math.ceil((double) diferenciaMillis / (1000 * 60 * 60 * 24));
        this.mora = dias * 0.10;
    }

    public void devolverLibro(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
        libro.devolver();
    }

    public Bibliotecario getBibliotecario() {
        return bibliotecario;
    }

    public void setBibliotecario(Bibliotecario bibliotecario) {
        this.bibliotecario = bibliotecario;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public double getMora() {
        return mora;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}
