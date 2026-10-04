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

import Taller3.guias.ejemploconexionhttp.R;
import Taller3.guias.ejemploconexionhttp.controladores.ConexionHttpPostServer;
import Taller3.guias.ejemploconexionhttp.datos.Usuario;

public class PantallaInicial extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoPassword;
    private Button btnLogin;
    private Button botonCancelar;
    private ConexionHttpPostServer conexionServidor;
    public static Usuario fulanito;
    private ProgressDialog barraDeprogreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicial);

        campoEmail = (EditText) findViewById(R.id.campoEmail);
        campoPassword = (EditText) findViewById(R.id.campoClave);
        btnLogin = (Button) findViewById(R.id.buttonIniciarSesion);
        botonCancelar = (Button) findViewById(R.id.buttonCancelar);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(PantallaInicial.this, PantallaCrudUsuario.class);
                startActivity(intento);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarDatos() == true) {
                    String codigo = campoEmail.getText().toString();
                    String password = campoPassword.getText().toString();

                    conexionServidor = new ConexionHttpPostServer();
                    conexionServidor.direccionDelServidor ="http://10.0.2.2/crudphpjson/crud/operacion.php";

                    TareaLoginEnSegunPlano tareaLogin = new TareaLoginEnSegunPlano();
                    tareaLogin.execute(codigo, password);
                }
            }
        });
    }

    private void mostrarDatosDeUsuarioEnOtraActividad() {
        if (fulanito != null) {
            Intent intento = new Intent(PantallaInicial.this, PantallaListado.class);
            intento.putExtra("sesion", fulanito);
            startActivity(intento);
        }
    }

    private Object iniciarSesion(String codigo, String password) {
        fulanito = null;
        ArrayList<NameValuePair> listaParametros = new ArrayList<NameValuePair>();
        listaParametros.add(new NameValuePair("accion", "login"));
        listaParametros.add(new NameValuePair("email", codigo));
        listaParametros.add(new NameValuePair("psw", password));

        String respuestaDelServidorEnJson = null;
        Object resp;
        try {
            respuestaDelServidorEnJson = conexionServidor.conexionConElServidor(listaParametros, conexionServidor.direccionDelServidor);
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }

        if (respuestaDelServidorEnJson != null && respuestaDelServidorEnJson.length() > 0) {
            Gson formatoJson = new Gson();
            try {
                resp = formatoJson.fromJson(respuestaDelServidorEnJson, Usuario.class);
                if (((Usuario) resp).getEmail() == null) {
                    return formatoJson.fromJson(respuestaDelServidorEnJson, Mensaje.class);
                } else {
                    return resp;
                }
            } catch (Exception e) {
                return formatoJson.fromJson(respuestaDelServidorEnJson, Mensaje.class);
            }
        } else {
            return null;
        }
    }

    private boolean verificarDatos() {
        String codigo = campoEmail.getText().toString();
        String password = campoPassword.getText().toString();
        if (codigo == null || codigo.trim().length() <= 0) {
            Toast.makeText(this, "Debe ingresar el Codigo", Toast.LENGTH_LONG).show();
            return false;
        } else if (password == null || password.trim().length() <= 0) {
            Toast.makeText(this, "Debe ingresar el Password", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    class TareaLoginEnSegunPlano extends AsyncTask<String, String, String> {
        String codigo;
        String password;

        @Override
        protected void onPreExecute() {
            barraDeprogreso = new ProgressDialog(PantallaInicial.this);
            barraDeprogreso.setMessage("Conectando...");
            barraDeprogreso.setIndeterminate(false);
            barraDeprogreso.setCancelable(false);
            barraDeprogreso.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            codigo = parametros[0];
            password = parametros[1];
            Object resp = iniciarSesion(codigo, password);
            if (resp != null) {
                if (resp instanceof Usuario) {
                    fulanito = (Usuario) resp;
                    return "OK";
                } else {
                    return ((Mensaje) resp).getMensaje();
                }
            } else {
                return "Acceso Negado, Error en el Servidor";
            }
        }

        @Override
        protected void onPostExecute(String resp) {
            barraDeprogreso.dismiss();
            if (resp.equals("OK") == true) {
                mostrarDatosDeUsuarioEnOtraActividad();
            } else {
                Toast.makeText(PantallaInicial.this, resp, Toast.LENGTH_LONG).show();
                return;
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


