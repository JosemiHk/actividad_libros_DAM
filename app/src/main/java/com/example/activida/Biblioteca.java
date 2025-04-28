package com.example.activida;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {
    private List<Estudiante> estudiantes = new ArrayList<>();
    private List<Libro> libros = new ArrayList<>();
    private List<Prestamo> prestamos = new ArrayList<>();
    private List<Bibliotecario> bibliotecarios = new ArrayList<>();

    // Singleton
    private static Biblioteca instance;

    // Constructor privado para evitar que se cree más de una instancia
    private Biblioteca() {}

    // Método para obtener la instancia del Singleton
    public static Biblioteca getInstance() {
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }

    // Métodos para agregar estudiantes, libros, y registrar préstamos
    public void agregarEstudiante(Estudiante e) {
        estudiantes.add(e);
    }

    public void agregarLibro(Libro l) {
        libros.add(l);
    }

    // Métodos para bibliotecarios 👇
    public void agregarBibliotecario(Bibliotecario b) {
        bibliotecarios.add(b);
    }

    public void registrarPrestamo(Estudiante e, Libro l, Bibliotecario b, Date fechaDevolucion) {
        if (l.isDisponible()) {
            Date fechaPrestamo = new Date(); // actual
            Prestamo p = new Prestamo(e, l, b, fechaPrestamo, fechaDevolucion);
            prestamos.add(p);
            l.prestar();
        } else {
            System.out.println("Libro no disponible.");
        }
    }

    public Estudiante buscarEstudiantePorCodigo(String codigo) {
        for (Estudiante e : estudiantes) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    public Libro buscarLibroPorCodigo(String codigo) {
        for (Libro l : libros) {
            if (l.getCodigo().equalsIgnoreCase(codigo)) {
                return l;
            }
        }
        return null;
    }

    public Bibliotecario buscarBibliotecarioPorCodigo(String codigo) {
        for (Bibliotecario b : bibliotecarios) {
            if (b.getCodigo().equalsIgnoreCase(codigo)) {
                return b;
            }
        }
        return null;
    }

    public void registrarDevolucion(Prestamo p) {
        p.setDevuelto(true);
        p.getLibro().devolver(); // si tu clase Libro tiene este método
    }
    public String generarReporteGeneral() {
        StringBuilder sb = new StringBuilder();
        int totalPrestados = prestamos.size();
        int totalDevueltos = 0;
        int totalNoDevueltos = 0;

        sb.append("📚✨  *REPORTE GENERAL DE PRÉSTAMOS*  ✨📚\n\n");

        for (Prestamo p : prestamos) {
            sb.append("🔸 *Estudiante:* ").append(p.getEstudiante().getNombre()).append("\n");
            sb.append("📖 *Libro:* ").append(p.getLibro().getTitulo()).append("\n");
            sb.append("🕒 *Fecha de Préstamo:* ").append(p.getFechaPrestamo()).append("\n");
            sb.append("📅 *Fecha de Devolución:* ").append(p.getFechaDevolucion()).append("\n");
            sb.append("💰 *Mora:* S/. ").append(String.format("%.2f", p.getMora())).append("\n");

            if (p.isDevuelto()) {
                sb.append("✅ *Estado:* Devuelto\n");
                totalDevueltos++;
            } else {
                sb.append("❌ *Estado:* No devuelto\n");
                totalNoDevueltos++;
            }
            sb.append("──────────────────────────────\n\n");
        }

        // Cálculo de total prestados (todos los préstamos)
        totalPrestados = prestamos.size();

        // Sección de resumen
        sb.append("📊 *Resumen General*\n");
        sb.append("📌 Total Préstamos: ").append(totalPrestados).append("\n");
        sb.append("📗 Devueltos: ").append(totalDevueltos).append("\n");
        sb.append("📕 No Devueltos: ").append(totalNoDevueltos).append("\n");


        return sb.toString();
    }

    public String generarReporteMoraPorEstudiante() {
        StringBuilder sb = new StringBuilder();
        // Usamos un mapa para almacenar la mora acumulada por cada estudiante
        Map<String, Double> moraPorEstudiante = new HashMap<>();

        // Recorremos todos los préstamos
        for (Prestamo p : prestamos) {
            // Si el préstamo no ha sido devuelto y tiene mora
            if (!p.isDevuelto() && p.getMora() > 0) {
                // Si el estudiante ya está en el mapa, sumamos la mora
                String nombreEstudiante = p.getEstudiante().getNombre();
                moraPorEstudiante.put(nombreEstudiante, moraPorEstudiante.getOrDefault(nombreEstudiante, 0.0) + p.getMora());
            }
        }

        sb.append("📚✨ *REPORTE DE MORAS ACUMULADAS POR ESTUDIANTE* ✨📚\n\n");

        // Recorremos el mapa para mostrar la mora acumulada por estudiante
        for (Map.Entry<String, Double> entry : moraPorEstudiante.entrySet()) {
            sb.append("🔸 *Estudiante:* ").append(entry.getKey()).append("\n");
            sb.append("💰 *Mora acumulada:* S/. ").append(String.format("%.2f", entry.getValue())).append("\n");
            sb.append("──────────────────────────────\n\n");
        }

        return sb.toString();
    }

    // Métodos para obtener los datos
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public List<Bibliotecario> getBibliotecarios() {
        return bibliotecarios;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }
}
