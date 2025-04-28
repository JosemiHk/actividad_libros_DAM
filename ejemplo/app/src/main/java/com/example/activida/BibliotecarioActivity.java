package com.example.activida;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class BibliotecarioActivity extends AppCompatActivity {

    private EditText editCodigo, editNombre;
    private Button btnAgregarBibliotecario, btnEliminarBibliotecario, btnModificarBibliotecario, btnVolver;
    private ListView listViewBibliotecarios;
    private ArrayAdapter<Bibliotecario> adapter;
    private int indexSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bibliotecario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar los componentes de la UI
        editCodigo = findViewById(R.id.editCodigo);
        editNombre = findViewById(R.id.editNombre);
        btnAgregarBibliotecario = findViewById(R.id.btnAgregarBibliotecario);
        btnEliminarBibliotecario = findViewById(R.id.btnEliminarBibliotecario);
        btnModificarBibliotecario = findViewById(R.id.btnModificarBibliotecario);
        btnVolver = findViewById(R.id.btnVolver);
        listViewBibliotecarios = findViewById(R.id.listViewBibliotecarios);

        // Obtener la lista de bibliotecarios desde la clase global Biblioteca
        ArrayList<Bibliotecario> listaBibliotecarios = (ArrayList<Bibliotecario>) Biblioteca.getInstance().getBibliotecarios();

        // Crear el adaptador para el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaBibliotecarios);
        listViewBibliotecarios.setAdapter(adapter);


        // Agregar bibliotecario
        btnAgregarBibliotecario.setOnClickListener(v ->  {
            String codigo = editCodigo.getText().toString().toString();
            String nombre = editNombre.getText().toString().toString();

            // Validar los campos
            if (!validarCodigoBibliotecario(codigo)) {
                editCodigo.setError("Código inválido. Debe empezar con 'B' y tener 3 dígitos.");
                return;
            }

            if (!validarNombreBibliotecario(nombre)) {
                editNombre.setError("Nombre inválido. Solo letras y espacios.");
                return;
            }

            if (!codigo.isEmpty() && !nombre.isEmpty()) {
                Bibliotecario bibliotecario = new Bibliotecario(codigo, nombre);
                Biblioteca.getInstance().agregarBibliotecario(bibliotecario);  // Agregar bibliotecario a la lista global
                adapter.notifyDataSetChanged();
                limpiarCampos();
            } else {
                Toast.makeText(BibliotecarioActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        // Eliminar bibliotecario
        btnEliminarBibliotecario.setOnClickListener(v ->  {
            if (indexSeleccionado >= 0) {
                listaBibliotecarios.remove(indexSeleccionado);  // Eliminar bibliotecario seleccionado
                adapter.notifyDataSetChanged();  // Actualizar la vista
                limpiarCampos();  // Limpiar los campos
            } else {
                Toast.makeText(BibliotecarioActivity.this, "Selecciona un bibliotecario para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Modificar bibliotecario
        btnModificarBibliotecario.setOnClickListener(v ->  {
            if (indexSeleccionado >= 0) {
                String codigo = editCodigo.getText().toString();
                String nombre = editNombre.getText().toString();
                if (!validarCodigoBibliotecario(codigo)) {
                    editCodigo.setError("Código inválido. Debe empezar con 'B' y tener 3 dígitos.");
                    return;
                }

                if (!validarNombreBibliotecario(nombre)) {
                    editNombre.setError("Nombre inválido. Solo letras y espacios.");
                    return;
                }
                if (!codigo.isEmpty() && !nombre.isEmpty()) {
                    // Modificar bibliotecario seleccionado
                    Bibliotecario bibliotecarioModificado = new Bibliotecario(codigo, nombre);
                    listaBibliotecarios.set(indexSeleccionado, bibliotecarioModificado);
                    adapter.notifyDataSetChanged();
                    limpiarCampos();
                }
            }
        });
        // Manejar la selección de un item en el ListView
        listViewBibliotecarios.setOnItemClickListener((parent, view, position, id) -> {
            Bibliotecario bibliotecarioSeleccionado = listaBibliotecarios.get(position);
            editCodigo.setText(bibliotecarioSeleccionado.getCodigo());
            editNombre.setText(bibliotecarioSeleccionado.getNombre());
            indexSeleccionado = position; // Guardamos el índice del bibliotecario seleccionado
        });

        // Volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void limpiarCampos() {
        editCodigo.setText("");
        editNombre.setText("");
        indexSeleccionado = -1; // Reseteamos la selección
    }

    // Validar el código del bibliotecario (Debe comenzar con 'B' y tener 3 dígitos)
    private boolean validarCodigoBibliotecario(String codigo) {
        return codigo.matches("^B\\d{3}$");
    }

    // Validar el nombre del bibliotecario (Solo letras y espacios, sin caracteres especiales)
    private boolean validarNombreBibliotecario(String nombre) {
        return nombre.matches("^[A-Za-z ]+$");
    }
}