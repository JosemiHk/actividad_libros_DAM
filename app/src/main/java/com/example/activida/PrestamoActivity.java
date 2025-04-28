package com.example.activida;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrestamoActivity extends AppCompatActivity {

    private EditText etCodigoEstudiante, etCodigoLibro, etCodigoBibliotecario, etFechaDevolucion;
    private Button btnBuscarEstudiante, btnBuscarLibro, btnBuscarBibliotecario, btnGenerarPrestamo, btnVolver2;
    private TextView tvResultadoBusqueda;

    private Estudiante estudianteSeleccionado = null;
    private Libro libroSeleccionado = null;
    private Bibliotecario bibliotecarioSeleccionado = null;

    private Biblioteca biblioteca = Biblioteca.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prestamo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCodigoEstudiante = findViewById(R.id.input_codigo_estudiante);
        etCodigoLibro = findViewById(R.id.input_codigo_libro);
        etCodigoBibliotecario = findViewById(R.id.input_codigo_bibliotecario);
        etFechaDevolucion = findViewById(R.id.et_fecha_devolucion);

        btnBuscarEstudiante = findViewById(R.id.btn_buscar_estudiante);
        btnBuscarLibro = findViewById(R.id.btn_buscar_libro);
        btnBuscarBibliotecario = findViewById(R.id.btn_buscar_bibliotecario);
        btnGenerarPrestamo = findViewById(R.id.btn_generarprestamo);
        btnVolver2 = findViewById(R.id.btnVolver2);
        tvResultadoBusqueda = findViewById(R.id.tv_resultado_busqueda);

        btnBuscarEstudiante.setOnClickListener(v -> {
            String codigo = etCodigoEstudiante.getText().toString().trim();
            estudianteSeleccionado = biblioteca.buscarEstudiantePorCodigo(codigo);

            if (estudianteSeleccionado == null) {
                tvResultadoBusqueda.setText("Estudiante no encontrado");
            } else {
                tvResultadoBusqueda.setText("Estudiante Encontrado: " + estudianteSeleccionado.getNombre());
            }
        });
        btnBuscarLibro.setOnClickListener(v -> {
            String codigo = etCodigoLibro.getText().toString().trim();
            libroSeleccionado = biblioteca.buscarLibroPorCodigo(codigo);

            if (libroSeleccionado == null) {
                tvResultadoBusqueda.setText("Libro no encontrado");
            } else {
                tvResultadoBusqueda.setText("Libro Encontrado: " + libroSeleccionado.getTitulo());
            }
        });

        btnBuscarBibliotecario.setOnClickListener(v -> {
            String codigo = etCodigoBibliotecario.getText().toString().trim();
            bibliotecarioSeleccionado = biblioteca.buscarBibliotecarioPorCodigo(codigo);

            if (bibliotecarioSeleccionado == null) {
                tvResultadoBusqueda.setText("Bibliotecario no encontrado");
            } else {
                tvResultadoBusqueda.setText("Bibliotecario Encontrado: " + bibliotecarioSeleccionado.getNombre());
            }
        });

        btnGenerarPrestamo.setOnClickListener(v -> {
            if (estudianteSeleccionado == null || libroSeleccionado == null || bibliotecarioSeleccionado == null) {
                tvResultadoBusqueda.setText("Faltan datos válidos para registrar el préstamo");
                return;
            }

            String fechaDevolucionStr = etFechaDevolucion.getText().toString().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            try {
                // Convertir la fecha de devolución en objeto Date
                Date fechaDevolucion = sdf.parse(fechaDevolucionStr);

                // Obtener la fecha del préstamo (esto depende de tu implementación, por ejemplo, podría ser la fecha actual)
                Date fechaPrestamo = new Date();  // Suponiendo que el préstamo se hace en el momento actual

                // Validar si la fecha de devolución es posterior a la fecha del préstamo
                if (!fechaDevolucion.after(fechaPrestamo)) {
                    tvResultadoBusqueda.setText("La fecha de devolución debe ser posterior a la fecha del préstamo.");
                    return;
                }

                // Si la validación pasa, registrar el préstamo
                biblioteca.registrarPrestamo(estudianteSeleccionado, libroSeleccionado, bibliotecarioSeleccionado, fechaDevolucion);

                // Obtener el último préstamo para mostrar los datos
                Prestamo ultimo = biblioteca.getPrestamos().get(biblioteca.getPrestamos().size() - 1);

                String mensaje = "Nombre: " + estudianteSeleccionado.getNombre() + "\n" +
                        "Libro: " + libroSeleccionado.getTitulo() + "\n" +
                        "Fecha del Préstamo: " + sdf.format(ultimo.getFechaPrestamo()) + "\n" +
                        "Fecha de Devolución: " + sdf.format(ultimo.getFechaDevolucion()) + "\n" +
                        "Mora: S/. " + String.format("%.2f", ultimo.getMora());
                tvResultadoBusqueda.setText(mensaje);

                Toast.makeText(this, "¡Préstamo registrado con éxito!", Toast.LENGTH_SHORT).show();
                limpiarCampos();

            } catch (ParseException e) {
                tvResultadoBusqueda.setText("Formato de fecha inválido. Usa: dd/MM/yyyy HH:mm");
            }
        });


        // Acción del botón Volver: regresa al Activity anterior
        btnVolver2.setOnClickListener(v -> finish());
    }

    private void limpiarCampos() {
        etCodigoEstudiante.setText("");
        etCodigoLibro.setText("");
        etCodigoBibliotecario.setText("");
        etFechaDevolucion.setText("");
        estudianteSeleccionado = null;
        libroSeleccionado = null;
        bibliotecarioSeleccionado = null;
    }

}