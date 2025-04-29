package com.example.activida;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReporteMorososActivity extends AppCompatActivity {

    private TextView tvReporteMorosos;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reporte_morosos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvReporteMorosos = findViewById(R.id.tvReporteMorosos);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtener el reporte de morosos
        Biblioteca biblioteca = Biblioteca.getInstance();
        String reporteMorosos = biblioteca.generarReporteMorosos();

        // Mostrar el reporte en el TextView
        tvReporteMorosos.setText(reporteMorosos);

        // Botón para cerrar y volver
        btnVolver.setOnClickListener(v -> finish());
    }
}