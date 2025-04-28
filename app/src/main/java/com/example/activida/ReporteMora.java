package com.example.activida;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReporteMora extends AppCompatActivity {
    private Button btnVolver;
    private TextView tvReporteMora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reporte_mora);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvReporteMora = findViewById(R.id.tvReporteMora);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtener el reporte
        Biblioteca biblioteca = Biblioteca.getInstance();
        String reporteMora = biblioteca.generarReporteMoraPorEstudiante();

        // Mostrar el reporte en el TextView
        tvReporteMora.setText(reporteMora);
        // Botón para cerrar y volver
        btnVolver.setOnClickListener(v -> finish());
    }
}