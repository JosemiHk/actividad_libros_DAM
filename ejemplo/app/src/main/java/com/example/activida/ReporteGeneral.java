package com.example.activida;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReporteGeneral extends AppCompatActivity {

    private TextView tvReporte;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reporte_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvReporte = findViewById(R.id.tvReporte);
        btnVolver = findViewById(R.id.btnVolver);
        // Obtener instancia del singleton
        Biblioteca biblioteca = Biblioteca.getInstance();

        // Mostrar el reporte en el TextView
        String reporte = biblioteca.generarReporteGeneral();
        tvReporte.setText(reporte);

        // Botón para cerrar y volver
        btnVolver.setOnClickListener(v -> finish());
    }
}