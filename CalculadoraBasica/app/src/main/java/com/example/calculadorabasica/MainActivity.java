package com.example.calculadorabasica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText txtValorCredito, txtNumCuotas, txtInteresMensual;
    Button btnCalcular;
    TextView lblCuotaMensual, lblTotalCredito, lblGananciaTotal;
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
        txtValorCredito = findViewById(R.id.txtValorCredito);
        txtNumCuotas = findViewById(R.id.txtNumCuotas);
        txtInteresMensual = findViewById(R.id.txtInteresMensual);
        btnCalcular = findViewById(R.id.btnCalcular);
        lblCuotaMensual = findViewById(R.id.lblCuotaMensual);
        lblTotalCredito = findViewById(R.id.lblTotalCredito);
        lblGananciaTotal = findViewById(R.id.lblGananciaTotal);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double p = Double.parseDouble(txtValorCredito.getText().toString());
                int n = Integer.parseInt(txtNumCuotas.getText().toString());
                double interesPorcentaje = Double.parseDouble(txtInteresMensual.getText().toString());

                double cuotaMensual = calcularCuotaMensual(p, n, interesPorcentaje);
                double valorTotalCredito = cuotaMensual * n;
                double gananciaTotal = valorTotalCredito - p;

                lblCuotaMensual.setText(String.format("Valor por cuota: %.2f", cuotaMensual));
                lblTotalCredito.setText(String.format("Valor total del crédito: %.2f", valorTotalCredito));
                lblGananciaTotal.setText(String.format("Ganancia total: %.2f", gananciaTotal));
            }
        });
    }

    private double calcularCuotaMensual(double principal, int cuotas, double porcentajeInteres) {
        double interes = porcentajeInteres / 100.0;
        if (interes == 0) {
            return principal / cuotas;
        } else {
            return principal * (interes* Math.pow(1 + interes, cuotas)) / (Math.pow(1 + interes, cuotas) - 1);
        }
    }
}