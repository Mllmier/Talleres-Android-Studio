package Taller3.guias.ejemploconexionhttp.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.httpclient.NameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Taller3.guias.ejemploconexionhttp.R;
import Taller3.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import Taller3.guias.ejemploconexionhttp.datos.Usuario;

public class PantallaListado extends AppCompatActivity {

    private ProgressDialog barraProgreso;
    private ConexionHttpPostServer conexionServidor;
    private ListView listaUsuariosView;
    private Button btnGuardar;
    List<Usuario> listaUsuarios;
    ArrayAdapter<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_listado);

        listaUsuariosView = (ListView) findViewById(R.id.listaUsuarios);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        conexionServidor = new ConexionHttpPostServer();
        conexionServidor.direccionDelServidor = "http://10.0.2.2/crudphpjson/crud/operacion.php";
        new TareaListarTodo().execute("", "");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(PantallaListado.this, PantallaCrudUsuario.class);
                startActivity(intento);
            }
        });
    }

    class TareaListarTodo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            barraProgreso = new ProgressDialog(PantallaListado.this);
            barraProgreso.setMessage("Conectando...");
            barraProgreso.setIndeterminate(false);
            barraProgreso.setCancelable(false);
            barraProgreso.show();
        }

        @Override
        protected String doInBackground(String... params) {
            boolean resultado = procesarRespuestaPeticion();
            if (resultado == true) {
                return "OK";
            } else {
                return "NO";
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            barraProgreso.dismiss();
            if (resultado.equals("OK")) {
                mostrarUsuariosEnLista();
            } else {
                Toast.makeText(PantallaListado.this, "Error en la Tarea", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void mostrarUsuariosEnLista() {
        int i = 1;
        for (Usuario alguien : listaUsuarios) {
            i++;
            System.out.println("No. " + i + " " + alguien);
            String elemento = alguien.getEmail() + " - " + alguien.getNombre();
            items.add(elemento);
        }
        listaUsuariosView.setAdapter(items);
    }

    private boolean procesarRespuestaPeticion() {
        ArrayList<NameValuePair> listaParametros = new ArrayList<NameValuePair>();
        NameValuePair parametro = new NameValuePair("accion", "listar");
        listaParametros.add(parametro);
        try {
            String resultadoDelServidor = conexionServidor.conexionConElServidor(listaParametros, conexionServidor.direccionDelServidor);
            if (resultadoDelServidor != null && resultadoDelServidor.length() > 0) {
                System.out.println("JSON: " + resultadoDelServidor);
                Gson json = new Gson();
                Type lista = new TypeToken<List<Usuario>>(){}.getType();
                listaUsuarios = json.fromJson(resultadoDelServidor, lista);
                return true;
            } else {
                return false;
            }
        } catch (Exception error) {
            error.printStackTrace();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}