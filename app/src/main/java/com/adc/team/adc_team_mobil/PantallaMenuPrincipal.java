package com.adc.team.adc_team_mobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 * Clase que representa la pantalla principal de la aplicación con acceso al menú principal
 *
 * @author Daniela Gutierrez
 */

public class PantallaMenuPrincipal extends AppCompatActivity {

    private TextView tvUsuari, tvPwd, tvId, tvRol;
    private String id_conn;
    private static final String TAG = "Resposta server :";
    private androidx.appcompat.widget.Toolbar toolbar;
    private String colorPantalla[] = {"Claro", "Oscuro"};
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_menu_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tvUsuari = (TextView) findViewById(R.id.tvUsuari);
        tvPwd = (TextView) findViewById(R.id.tvPwd);
        tvId = (TextView) findViewById(R.id.tvId);
        tvRol = (TextView) findViewById(R.id.tvRol);

        String usuari = getIntent().getStringExtra("usuari");
        String pwd = getIntent().getStringExtra("pwd");
        id_conn = getIntent().getStringExtra("id");
        String rol = getIntent().getStringExtra("rol");
       //Todos estos valores permanecen ocultos, pero son necesarios para hacer el logout
        tvUsuari.setText(usuari);
        tvPwd.setText(pwd);
        tvId.setText(id_conn);
        tvRol.setText(rol);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AltaIncidencia:
                Toast.makeText(this, " Alta Incidencia", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ListaIncidencia:
                Toast.makeText(this, "Lista Incidencias", Toast.LENGTH_SHORT).show();
                break;
            case R.id.BajaIncidencia:
                Toast.makeText(this, "Baja de una Incidencia", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Configuracion:
                Toast.makeText(this, "Acceso a configuración", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ColorPantallaClara:
                Intent modoClaro = new Intent(this, PantallaModoClaro.class);
                startActivity(modoClaro);
                break;
            case R.id.ColorPantallaOscura:
                Intent modoOscuro = new Intent(this, PantallaMenuPrincipal.class);
                startActivity(modoOscuro);
                break;
            case R.id.Valoracion:
                Toast.makeText(this, "Valora tu experiencia. ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Contacta:
                Toast.makeText(this, "Contacta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Desconecta:
                Toast.makeText(this, "Desconectar.", Toast.LENGTH_SHORT).show();
                new Task2().execute(tvUsuari.getText().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Método que ejecuta la acción de Logout, asociado a la opción Desconecta del menu Principal
     */
    class Task2 extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {
                //Intentamos establecer conexión con el servidor
                Socket sc;
                sc = new Socket("192.168.0.11", 5000);
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                //Variable que recibirá la respuesta del servidor una vez establecida la conexión
                // Enviament de la clau pública del servidor
                out.writeUTF("Enviament de la clau pública del client");
                // Llegim la clau pública del servidor
                String resposta_svr = in.readUTF();

                Log.i(TAG, resposta_svr);
                Log.i(TAG, "Se desconecta el usuario: " + String.valueOf(String.valueOf(tvUsuari.getText().toString())));

                //Enviamos respuesta al servidor con el usuario, contraseña y valor 0
                //Ejecutamos la consulta de USER_EXIT
                out.writeUTF(id_conn + ",USER_EXIT");

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG).show();
                Log.i(TAG, "ERROR DE CONEXIÓN CON EL SERVIDOR");
                e.printStackTrace();
            }
            return strings[0];
        }

        /**
         * Método que retorna a la pantalla del login
         * una vez ejecutada la acción del logout
         *
         * @param s del método doInBackground
         */

        @Override
        protected void onPostExecute(String s) {

            Intent intent3 = new Intent(PantallaMenuPrincipal.this, MainActivity.class);
            startActivity(intent3);
        }
    }
}
