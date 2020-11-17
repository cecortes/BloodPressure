package com.example.bloodpresure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.*;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    //Globales
    String edoSelect, strSisto, strDiasto;

    //Implementación de objetos de Volley
    RequestQueue rq;

    //Instance activity elements
    TextView txtSisto, txtDiasto;
    Spinner spnrEdo;
    ImageButton btnAdd;
    ListView lvBp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference activity elements
        txtSisto = findViewById(R.id.TxtSisto);
        txtDiasto = findViewById(R.id.TxtDiasto);
        spnrEdo = findViewById(R.id.SpinnerEdo);
        btnAdd = findViewById(R.id.BtnAdd);
        lvBp = findViewById(R.id.LvBp);

        // Instance the RequestQueue.
        rq = Volley.newRequestQueue(this);

        //Adapter para el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combo_edo, R.layout.support_simple_spinner_dropdown_item);
        spnrEdo.setAdapter(adapter);

        //Método para realizar la consulta de la tabla blood
        ConsultarBlood();

        //Listener para Spinner
        spnrEdo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Captura del spinner
                edoSelect = adapterView.getItemAtPosition(i).toString();

                /*Debug
                AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                msg.setMessage("Estado: " + edoSelect).setTitle("Selección").create().show();
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Set estado
                edoSelect = "NA";
            }
        });

        //Listener para el botón
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Debug
                AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                msg.setMessage("Press").setTitle("Botón").create().show();
                 */

                //Método para validar y capturar datos
                Captura();
            }
        });
    }

    //Se encarga de recibir el JSON de la consulta blood y agregar el resultado al ListView
    private void ConsultarBlood() {

        //Construimos la url del servidor con los campos string necesarios
        String url = "http://sylkaventas.ddns.net/get_blood.php";

        //Jason Request
        JsonObjectRequest consultaRq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Locales
                final List<BloodPressure> lista = new ArrayList<>();
                //Control de errores
                try {

                    //JSON array
                    JSONArray jsonArray = response.getJSONArray("bp");

                    //Rutina para recorrer el arreglo
                    for (int i = 0; i < jsonArray.length(); i++) {

                        //Objeto JSON para recibir los key con los datos
                        JSONObject bp = jsonArray.getJSONObject(i);

                        //Captura de datos en variables
                        int id = bp.getInt("blood_id");
                        int sisto = bp.getInt("sisto");
                        int diasto = bp.getInt("diasto");
                        String edo = bp.getString("estado");
                        String created = bp.getString("blood_create");

                        //DEBUG
                        //AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        //alerta.setMessage("Id: " + created).setTitle("Consulta").create().show();

                        //Agregamos a la lista
                        lista.add(new BloodPressure(id,sisto,diasto,edo,created));
                    }

                    //Adaptador para agregar la lista al listview
                    ArrayAdapter<BloodPressure> adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,lista);

                    //Cargamos los datos al listview
                    lvBp.setAdapter(adaptador);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Usr
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                //alerta.setMessage(error.networkResponse.toString()).setTitle("Error").create().show();
                alerta.setMessage(error.getMessage()).setTitle("Error").create().show();
            }
        });
        //Add Jason Rquest to Request Que
        rq.add(consultaRq);
    }

    //Se encarga de validar que los datos no estén vacíos y las opciones sean correctas
    private void Captura() {

        //TextView
        strSisto = txtSisto.getText().toString();
        strDiasto = txtDiasto.getText().toString();

        //Validación
        if(!strSisto.isEmpty() && !strDiasto.isEmpty()) {
            
            //Método para agregar registro
            Agregar();

        } else {

            //Usr
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("Los campos Sistólica y/o Diastólica no pueden estar vacíos.")
                    .setTitle("Error").create().show();
        }

    }

    //Se encarga de agregar un registro nuevo a la tabla
    private void Agregar() {

        //Locales
        int sisto = 0;
        int disto = 0;


        //Control de errores
        try {

            //Parse to int
            sisto = Integer.parseInt(strSisto);
            disto = Integer.parseInt(strDiasto);

        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        //Constructor http get
        String url = "http://sylkaventas.ddns.net/bp_insert_blood.php?sisto=" + strSisto +"&diasto=" + strDiasto + "&edo=" + edoSelect;
        //String url = "http://sylkaventas.ddns.net/tst_insert_blood.php";

        //Jason Request
        JsonObjectRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null,
                this, this);
        //Add Jason Rquest to Request Que
        rq.add(jrq);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //Usr
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        //alerta.setMessage(error.networkResponse.toString()).setTitle("Error").create().show();
        alerta.setMessage("No se pudo registrar").setTitle("Error").create().show();

    }

    @Override
    public void onResponse(JSONObject response) {

        //Usr
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("Nuevo registro correcto").setTitle("Presión Arterial").create().show();

        //Clear Text
        Limpiar();

        //Espera de 2 seg.
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Actualizar listview
        ConsultarBlood();
    }

    //Método para limpiar los cuadros de texto
    private void Limpiar() {
        txtSisto.setText("");
        txtDiasto.setText("");
    }
}