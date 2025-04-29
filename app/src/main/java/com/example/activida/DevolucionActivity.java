package com.example.activida;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DevolucionActivity extends AppCompatActivity {

    private EditText etBuscarPrestamo;
    private Button btnBuscarPrestamo, btnConfirmarDevolucion, btnVolver;
    private TextView tvInfoPrestamo, tvResultado;
    private Biblioteca biblioteca = Biblioteca.getInstance();
    private Prestamo prestamoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        // Obtener referencias a los elementos del layout
        etBuscarPrestamo = findViewById(R.id.etBuscarPrestamo);
        btnBuscarPrestamo = findViewById(R.id.btnBuscarPrestamo);
        btnConfirmarDevolucion = findViewById(R.id.btnConfirmarDevolucion);
        tvInfoPrestamo = findViewById(R.id.tvInfoPrestamo);
        tvResultado = findViewById(R.id.tvResultado);
        btnVolver = findViewById(R.id.btnVolver);

        // Configurar el listener para el botón Buscar
        btnBuscarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarPrestamo();
            }
        });

        // Configurar el listener para el botón Confirmar Devolución
        btnConfirmarDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarDevolucion();
            }
        });
        // Configurar el listener para el botón Volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y regresa a la anterior
            }
        });
    }

    private void buscarPrestamo() {
        String idPrestamoStr = etBuscarPrestamo.getText().toString().trim();
        if (idPrestamoStr.isEmpty()) {
            tvResultado.setText("Por favor, ingrese un ID de préstamo.");
            return;
        }

        int idPrestamo;
        try {
            idPrestamo = Integer.parseInt(idPrestamoStr);
        } catch (NumberFormatException e) {
            tvResultado.setText("ID de préstamo inválido. Debe ser un número.");
            return;
        }

        // Buscar el préstamo en la lista de préstamos de la biblioteca
        prestamoSeleccionado = null;
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            if (prestamo.getIdPrestamo() == idPrestamo) {
                prestamoSeleccionado = prestamo;
                break;
            }
        }

        if (prestamoSeleccionado != null) {
            // Mostrar la información del préstamo
            mostrarInfoPrestamo();
        } else {
            tvResultado.setText("Préstamo no encontrado.");
            tvInfoPrestamo.setText("Información del Préstamo");
        }
    }

    private void mostrarInfoPrestamo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String info = "ID Préstamo: " + prestamoSeleccionado.getIdPrestamo() + "\n" +
                "Estudiante: " + prestamoSeleccionado.getEstudiante().getNombre() + "\n" +
                "Libro: " + prestamoSeleccionado.getLibro().getTitulo() + "\n" +
                "Fecha de Préstamo: " + sdf.format(prestamoSeleccionado.getFechaPrestamo()) + "\n" +
                "Fecha de Devolución: " + sdf.format(prestamoSeleccionado.getFechaDevolucion()) + "\n" +
                "Mora: S/. " + String.format("%.2f", prestamoSeleccionado.getMora());
        tvInfoPrestamo.setText(info);
        tvResultado.setText("");
    }

    private void confirmarDevolucion() {
        if (prestamoSeleccionado == null) {
            tvResultado.setText("Primero debe buscar un préstamo.");
            return;
        }
        if (prestamoSeleccionado.isDevuelto()) {
            tvResultado.setText("El libro ya fue devuelto");
            return;
        }

        // Realizar la devolución
        Date fechaDevolucion = new Date();
        prestamoSeleccionado.devolverLibro(fechaDevolucion);
        prestamoSeleccionado.setDevuelto(true);
        tvResultado.setText("Devolución confirmada.");
        tvInfoPrestamo.setText("Información del Préstamo");
        etBuscarPrestamo.setText("");
        Toast.makeText(this, "¡Devolución registrada con éxito!", Toast.LENGTH_SHORT).show();
    }
}