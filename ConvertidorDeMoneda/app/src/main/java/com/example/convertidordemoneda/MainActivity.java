package com.example.convertidordemoneda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText txtCantidadMoneda;
    private RadioButton rdioOrigenDollar, rdioOrigenYenes, rdioOrigenPesoMexicano, rdioOrigenSoles;
    private RadioButton rdioDestinoDollar, rdioDestinoYenes, rdioDestinoPesoMexicano, rdioDestinoSoles;
    private Button btnConvertir;
    private TextView lblResultado;
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
        txtCantidadMoneda = findViewById(R.id.txtCantidadMoneda);

        rdioOrigenDollar = findViewById(R.id.rdioOrigenDollar);
        rdioOrigenYenes = findViewById(R.id.rdioOrigenYenes);
        rdioOrigenPesoMexicano = findViewById(R.id.rdioOrigenPesoMexicano);
        rdioOrigenSoles = findViewById(R.id.rdioOrigenSoles);

        rdioDestinoDollar = findViewById(R.id.rdioDestinoDollar);
        rdioDestinoYenes = findViewById(R.id.rdioDestinoYenes);
        rdioDestinoPesoMexicano = findViewById(R.id.rdioDestinoPesoMexicano);
        rdioDestinoSoles = findViewById(R.id.rdioDestinoSoles);

        btnConvertir = findViewById(R.id.btnConvertir);
        lblResultado = findViewById(R.id.lblResultado);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularConversion();
            }
        });
    }
    private void calcularConversion() {
        String valorTexto = txtCantidadMoneda.getText().toString().trim();

        if (valorTexto.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(valorTexto.replace(",", "."));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingresa un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidadEnUSD;

        if (rdioOrigenDollar.isChecked()) {
            cantidadEnUSD = cantidad;
        } else if (rdioOrigenYenes.isChecked()) {
            cantidadEnUSD = cantidad / 150.0;
        } else if (rdioOrigenPesoMexicano.isChecked()) {
            cantidadEnUSD = cantidad / 17.0;
        } else if (rdioOrigenSoles.isChecked()) {
            cantidadEnUSD = cantidad / 3.70;
        } else {
            Toast.makeText(this, "Selecciona la moneda de origen", Toast.LENGTH_SHORT).show();
            return;
        }

        double resultadoFinal;
        if (rdioDestinoDollar.isChecked()) {
            resultadoFinal = cantidadEnUSD;
        } else if (rdioDestinoYenes.isChecked()) {
            resultadoFinal = cantidadEnUSD * 150.0;
        } else if (rdioDestinoPesoMexicano.isChecked()) {
            resultadoFinal = cantidadEnUSD * 17.0;
        } else if (rdioDestinoSoles.isChecked()) {
            resultadoFinal = cantidadEnUSD * 3.70;
        } else {
            Toast.makeText(this, "Selecciona la moneda de destino", Toast.LENGTH_SHORT).show();
            return;
        }
        lblResultado.setText("Resultado: " + String.format(java.util.Locale.US, "%.2f", resultadoFinal));
    }


}