package com.example.activida;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LibroActivity extends AppCompatActivity {

    private EditText editCodigo, editTitulo, editAutor;
    private Button btnAgregar, btnEliminar, btnModificar, btnVolver;
    private ListView listViewLibros;
    private ArrayAdapter<Libro> adapter;
    private int indexSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_libro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        editCodigo = findViewById(R.id.editCodigo);
        editTitulo = findViewById(R.id.editTitulo);
        editAutor = findViewById(R.id.editAutor);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        btnVolver = findViewById(R.id.btnVolver);
        listViewLibros = findViewById(R.id.listViewLibros);

        // Obtener la lista de libros desde la clase global Biblioteca
        ArrayList<Libro> listaLibros = (ArrayList<Libro>) Biblioteca.getInstance().getLibros();

        // Crear el adaptador para el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaLibros);
        listViewLibros.setAdapter(adapter);

        // Agregar Libro
        btnAgregar.setOnClickListener(v -> {
            String codigo = editCodigo.getText().toString();
            String titulo = editTitulo.getText().toString();
            String autor = editAutor.getText().toString();
            // Validar los campos
            if (!validarCodigoLibro(codigo)) {
                editCodigo.setError("Código inválido. Debe empezar con consonante y tener 5 dígitos.");
                return;
            }

            if (!validarTitulo(titulo)) {
                editTitulo.setError("Título inválido. Solo letras, espacios y números. Máx. 30 caracteres.");
                return;
            }

            if (!validarAutor(autor)) {
                editAutor.setError("Autor inválido. Solo letras y espacios. Máx. 30 caracteres.");
                return;
            }

            if (!codigo.isEmpty() && !titulo.isEmpty() && !autor.isEmpty()) {
                // Crear libro con el código, título y autor
                Libro libro = new Libro(codigo, titulo, autor); // El atributo 'disponible' se establece a 'true' por defecto
                Biblioteca.getInstance().agregarLibro(libro);  // Agregar el libro a la lista global
                adapter.notifyDataSetChanged(); // Actualiza el ListView para mostrar el libro
                limpiarCampos();  // Limpiar los campos después de agregar el libro
            } else {
                Toast.makeText(LibroActivity.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Eliminar Libro
        btnEliminar.setOnClickListener(v -> {
            if (indexSeleccionado >= 0) {
                listaLibros.remove(indexSeleccionado);  // Eliminar libro seleccionado
                adapter.notifyDataSetChanged();  // Actualizar la vista
                limpiarCampos();  // Limpiar los campos
            } else {
                Toast.makeText(LibroActivity.this, "Selecciona un libro para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Modificar Libro
        btnModificar.setOnClickListener(v -> {
            if (indexSeleccionado >= 0) {
                String codigo = editCodigo.getText().toString();
                String titulo = editTitulo.getText().toString();
                String autor = editAutor.getText().toString();
                // Validar los campos
                if (!validarCodigoLibro(codigo)) {
                    editCodigo.setError("Código inválido. Debe empezar con consonante y tener 5 dígitos.");
                    return;
                }

                if (!validarTitulo(titulo)) {
                    editTitulo.setError("Título inválido. Solo letras, espacios y números. Máx. 30 caracteres.");
                    return;
                }

                if (!validarAutor(autor)) {
                    editAutor.setError("Autor inválido. Solo letras y espacios. Máx. 30 caracteres.");
                    return;
                }
                // Modificar el libro seleccionado
                Libro libroModificado = new Libro(codigo, titulo, autor);
                listaLibros.set(indexSeleccionado, libroModificado);
                adapter.notifyDataSetChanged();
                limpiarCampos();
            }
        });

        // Manejar la selección de un item en el ListView
        listViewLibros.setOnItemClickListener((parent, view, position, id) -> {
            Libro libroSeleccionado = listaLibros.get(position);
            editCodigo.setText(libroSeleccionado.getCodigo());
            editTitulo.setText(libroSeleccionado.getTitulo());
            editAutor.setText(libroSeleccionado.getAutor());
            indexSeleccionado = position; // Guardamos el índice del libro seleccionado
        });

        // Acción del botón Volver: regresa al Activity anterior
        btnVolver.setOnClickListener(v -> finish());
    }

    private void limpiarCampos() {
        editCodigo.setText("");
        editTitulo.setText("");
        editAutor.setText("");
        indexSeleccionado = -1; // Reseteamos la selección
    }

    // Validación del código (Consonante + 5 dígitos)
    private boolean validarCodigoLibro(String codigo) {
        return codigo.matches("^[^AEIOUaeiou\\W\\d]{1}\\d{5}$");
    }

    // Validación del título (Máximo 30 caracteres, solo letras, espacios y números)
    private boolean validarTitulo(String titulo) {
        return titulo.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]{1,30}$");
    }

    // Validación del autor (Máximo 30 caracteres, solo letras y espacios)
    private boolean validarAutor(String autor) {
        return autor.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,30}$");
    }
}