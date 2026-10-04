package Taller3.guias.ejemploconexionhttp.controladores;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ConexionHttpPostServer {

    public static String direccionDelServidor;
    private String respuesta;
    private InputStream datosEntrada;

    public String conexionConElServidor(List<NameValuePair> parametros, String rutaDeLaAplicacionWeb) throws Exception {
        HttpClient clienteHTTP = new HttpClient();
        clienteHTTP.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        clienteHTTP.getHttpConnectionManager().getParams().setSoTimeout(5000);
        BufferedReader br = null;
        PostMethod peticionPOST = new PostMethod(rutaDeLaAplicacionWeb);
        for (NameValuePair parametro : parametros) {
            peticionPOST.addParameter(parametro);
        }
        try {
            int codigoRespuesta = clienteHTTP.executeMethod(peticionPOST);
            if (codigoRespuesta == HttpStatus.SC_NOT_IMPLEMENTED) {
                throw new Exception("ERROR 1: Parametros mal codificados");
            } else if (codigoRespuesta != HttpStatus.SC_OK) {
                throw new Exception("ERROR 2: URL invalida");
            } else {
                datosEntrada = peticionPOST.getResponseBodyAsStream();
                respuesta = procesarRespuestaDelServidor();
                if (datosEntrada != null) {
                    return respuesta;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new Exception("ERROR 3: Conexion fallida:\n" + e.getMessage());
        } finally {
            peticionPOST.releaseConnection();
            if (br != null) {
                try {
                    br.close();
                } catch (Exception fe) {
                }
            }
        }
    }

    private String procesarRespuestaDelServidor() throws Exception {
        BufferedReader lectorDatos = null;
        try {
            lectorDatos = new BufferedReader(new InputStreamReader(datosEntrada, "iso-8859-1"), 8);
        } catch (UnsupportedEncodingException error) {
            throw new Exception("ERROR 4: Sin respuesta\n" + error.getMessage());
        }
        String linea = null;
        String json2 = "";
        try {
            while (((linea = lectorDatos.readLine()) != null)) {
                if (linea.trim().isEmpty() == false) {
                    json2 += linea;
                }
            }
        } catch (IOException error) {
            throw new Exception("ERROR 5: Sin respuesta\n" + error.getMessage());
        }
        return json2;
    }
}