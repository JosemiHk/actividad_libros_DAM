package com.example.activida;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void irAGestionEstudiantes(View view) {
        Intent intent = new Intent(this, EstudianteActivity.class);
        startActivity(intent);
    }

    public void irAGestionLibros(View view) {
        Intent intent = new Intent(this, LibroActivity.class);
        startActivity(intent);
    }

    public void irAGestionBibliotecarios(View view) {
        Intent intent = new Intent(this, BibliotecarioActivity.class);
        startActivity(intent);
    }

    public void irAGestionPrestamos(View view) {
        Intent intent = new Intent(this, PrestamoActivity.class);
        startActivity(intent);
    }
    public void irAReporteGeneral(View view) {
        Intent intent = new Intent(this, ReporteGeneral.class);
        startActivity(intent);
    }
    public void irAReporteMora(View view){
        Intent intent = new Intent(this, ReporteMora.class);
        startActivity(intent);
    }
}