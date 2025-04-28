package com.example.activida;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class EstudianteActivity extends AppCompatActivity {
    private EditText editCodigo, editNombre, editCarrera;
    private Button btnAgregar, btnEliminar, btnModificar, btnVolver;
    private ListView listViewEstudiantes;
    private ArrayAdapter<String> adapter;
    private int indexSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estudiante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        editCodigo = findViewById(R.id.editCodigo);
        editNombre = findViewById(R.id.editNombre);
        editCarrera = findViewById(R.id.editCarrera);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        btnVolver = findViewById(R.id.btnVolver);
        listViewEstudiantes = findViewById(R.id.listViewEstudiantes);

        // Obtener la instancia del Singleton Biblioteca
        Biblioteca biblioteca = Biblioteca.getInstance();
        ArrayList<String> listaEstudiantes = new ArrayList<>();

        // Agregar los estudiantes existentes a la lista
        for (Estudiante estudiante : biblioteca.getEstudiantes()) {
            String estudianteStr = "Código: " + estudiante.getCodigo() + "\nNombre: " + estudiante.getNombre() + "\nCarrera: " + estudiante.getCarrera();
            listaEstudiantes.add(estudianteStr);
        }

        // Adaptador para el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaEstudiantes);
        listViewEstudiantes.setAdapter(adapter);

        // Agregar Estudiante
        btnAgregar.setOnClickListener(v -> {
            String codigo = editCodigo.getText().toString().trim();
            String nombre = editNombre.getText().toString().trim();
            String carrera = editCarrera.getText().toString().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || carrera.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validarCodigo(codigo)) {
                editCodigo.setError("Código inválido (consonante + 4 dígitos, ej: N0278)");
                return;
            }

            if (!validarNombre(nombre)) {
                editNombre.setError("Nombre inválido. Solo letras y espacios (máx. 30 caracteres)");
                return;
            }

            if (!validarCarrera(carrera)) {
                editCarrera.setError("Carrera inválida. Solo letras y espacios (máx. 40 caracteres)");
                return;
            }

            Estudiante nuevoEstudiante = new Estudiante(codigo, nombre, carrera);
            biblioteca.agregarEstudiante(nuevoEstudiante);

            String estudianteStr = "Código: " + codigo + "\nNombre: " + nombre + "\nCarrera: " + carrera;
            listaEstudiantes.add(estudianteStr);
            adapter.notifyDataSetChanged();
            limpiarCampos();
        });

        // Eliminar Estudiante
        btnEliminar.setOnClickListener(v -> {
            if (indexSeleccionado >= 0) {
                // Eliminar del Singleton
                Estudiante estudianteSeleccionado = biblioteca.getEstudiantes().get(indexSeleccionado);
                biblioteca.getEstudiantes().remove(estudianteSeleccionado);

                // Eliminar de la lista visual
                listaEstudiantes.remove(indexSeleccionado);
                adapter.notifyDataSetChanged();
                limpiarCampos();
            } else {
                Toast.makeText(EstudianteActivity.this, "Selecciona un estudiante para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Modificar Estudiante
        btnModificar.setOnClickListener(v -> {
            if (indexSeleccionado >= 0) {
                String codigo = editCodigo.getText().toString().trim();
                String nombre = editNombre.getText().toString().trim();
                String carrera = editCarrera.getText().toString().trim();

                if (codigo.isEmpty() || nombre.isEmpty() || carrera.isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarCodigo(codigo)) {
                    editCodigo.setError("Código inválido (consonante + 4 dígitos, ej: N0278)");
                    return;
                }

                if (!validarNombre(nombre)) {
                    editNombre.setError("Nombre inválido. Solo letras y espacios (máx. 30 caracteres)");
                    return;
                }

                if (!validarCarrera(carrera)) {
                    editCarrera.setError("Carrera inválida. Solo letras y espacios (máx. 40 caracteres)");
                    return;
                }

                Estudiante estudianteModificado = new Estudiante(codigo, nombre, carrera);
                biblioteca.getEstudiantes().set(indexSeleccionado, estudianteModificado);

                String estudianteStr = "Código: " + codigo + "\nNombre: " + nombre + "\nCarrera: " + carrera;
                listaEstudiantes.set(indexSeleccionado, estudianteStr);
                adapter.notifyDataSetChanged();
                limpiarCampos();
            }
        });

        // Manejar la selección de un item en el ListView
        listViewEstudiantes.setOnItemClickListener((parent, view, position, id) -> {
            String estudianteSeleccionado = listaEstudiantes.get(position);
            String[] partes = estudianteSeleccionado.split("\n");
            String codigo = partes[0].replace("Código: ", "");
            String nombre = partes[1].replace("Nombre: ", "");
            String carrera = partes[2].replace("Carrera: ", "");

            editCodigo.setText(codigo);
            editNombre.setText(nombre);
            editCarrera.setText(carrera);
            indexSeleccionado = position; // Guardamos el índice del estudiante seleccionado
        });

        // Acción del botón Volver: regresa al Activity anterior
        btnVolver.setOnClickListener(v -> finish());
    }

    private void limpiarCampos() {
        editCodigo.setText("");
        editNombre.setText("");
        editCarrera.setText("");
        indexSeleccionado = -1; // Reseteamos la selección
    }

    private boolean validarCodigo(String codigo) {
        return codigo.matches("^[^AEIOUaeiou\\W\\d]{1}\\d{4}$");
    }

    private boolean validarNombre(String nombre) {
        return nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,30}$");
    }

    private boolean validarCarrera(String carrera) {
        return carrera.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,40}$");
    }

}