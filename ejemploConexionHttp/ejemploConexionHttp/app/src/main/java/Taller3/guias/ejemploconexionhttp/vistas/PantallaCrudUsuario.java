package Taller3.guias.ejemploconexionhttp.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.apache.commons.httpclient.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import Taller3.guias.ejemploconexionhttp.R;
import Taller3.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import Taller3.guias.ejemploconexionhttp.datos.Usuario;

public class PantallaCrudUsuario extends AppCompatActivity {

    private EditText campoCodigo;
    private EditText campoPassword;
    private EditText campoNombre;
    private Button botonModificar;
    private Button botonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crud_usuario);

        campoCodigo = (EditText) findViewById(R.id.campoCodigo2);
        campoPassword = (EditText) findViewById(R.id.campoPassword2);
        campoNombre = (EditText) findViewById(R.id.campoNombre);
        botonModificar = (Button) findViewById(R.id.botonModificar);
        botonCancelar = (Button) findViewById(R.id.botonCancelar2);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intento = this.getIntent();
        if (intento.getExtras() != null) {
            Usuario juansito = (Usuario) intento.getExtras().getSerializable("sesion");
            campoNombre.setText(juansito.getNombre());
            campoCodigo.setText(juansito.getEmail());
            campoPassword.setText(juansito.getPassword());
        } else if (PantallaInicial.fulanito != null) {
            campoNombre.setText(PantallaInicial.fulanito.getNombre());
            campoCodigo.setText(PantallaInicial.fulanito.getEmail());
            campoPassword.setText(PantallaInicial.fulanito.getPassword());
        } else {
            Toast.makeText(this, "Debe registrarse para ver el listado de usuarios", Toast.LENGTH_LONG).show();
        }

        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar(null);
            }
        });
    }

    public void guardar(View v) {
        TareaGuardar tarea = new TareaGuardar();
        tarea.execute("");
    }

    public String guardarUsuario() {
        String url = "http://10.0.2.2/crudphpjson/crud/operacion.php";
        ConexionHttpPostServer.direccionDelServidor = url;

        NameValuePair parametroAccion;
        if (PantallaInicial.fulanito == null) {
            parametroAccion = new NameValuePair("accion", "Agregar");
        } else {
            parametroAccion = new NameValuePair("accion", "editar");
        }

        NameValuePair parametroEmail = new NameValuePair("email", campoCodigo.getText().toString());
        NameValuePair parametroClave = new NameValuePair("psw", campoPassword.getText().toString());
        NameValuePair parametroNombre = new NameValuePair("nombre", campoNombre.getText().toString());

        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(parametroAccion);
        parametros.add(parametroEmail);
        parametros.add(parametroClave);
        parametros.add(parametroNombre);

        ConexionHttpPostServer conexionHttp = new ConexionHttpPostServer();
        try {
            String jsonRespuesta = conexionHttp.conexionConElServidor(parametros, url);
            if (jsonRespuesta == null) {
                return "ERROR: respuesta vacía";
            }
            System.out.println("RESPUESTA SERVIDOR: " + jsonRespuesta);
            try {
                Gson json = new Gson();
                Mensaje n = json.fromJson(jsonRespuesta, Mensaje.class);
                if (n == null || n.getMensaje() == null) {
                    return "Respuesta sin mensaje: " + jsonRespuesta;
                }
                return n.getMensaje();
            } catch (Exception ex) {
                return "No es JSON: " + jsonRespuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    class TareaGuardar extends AsyncTask<String, String, String> {
        ProgressDialog barraDeprogreso;
        String codigo, password, nombre, accion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barraDeprogreso = new ProgressDialog(PantallaCrudUsuario.this);
            barraDeprogreso.setMessage("Conectando...");
            barraDeprogreso.setIndeterminate(false);
            barraDeprogreso.setCancelable(false);
            barraDeprogreso.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String respuesta = guardarUsuario();
            return respuesta;
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (resultado.equalsIgnoreCase("OK") == true) {
                Toast.makeText(PantallaCrudUsuario.this, "Usuario Guardado con Exito", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(2000);
                    barraDeprogreso.dismiss();
                    Intent intento = new Intent(PantallaCrudUsuario.this, PantallaInicial.class);
                    startActivity(intento);
                } catch (InterruptedException e) {
                    barraDeprogreso.dismiss();
                }
            } else {
                barraDeprogreso.dismiss();
                Toast.makeText(PantallaCrudUsuario.this, "ERROR: " + resultado, Toast.LENGTH_LONG).show();
            }
        }
    }

    class Mensaje {
        String mensaje;

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}

