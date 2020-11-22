package com.example.bpfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bpfinal.R.id;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    //Instance activity elements
    Toolbar mToolbar;
    ArrayList<String> listDatos;
    RecyclerView recycler;
    FloatingActionButton mBtnAdd;
    TextInputEditText mTxtSisto, mTxtDiasto;

    //Implementación de objetos de Volley
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference activity elements
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setSubtitle("César López (C) 2020");
        recycler = findViewById(R.id.ListaReg);
        recycler.setLayoutManager(new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL,false));
        mBtnAdd = findViewById(id.BtnAddReg);
        mTxtSisto = findViewById(id.TxtSisto);
        mTxtDiasto = findViewById(id.TxtDiasto);

        //Listener for buttons
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Captura de datos
                CapturarDatos();

                /*Usuario
                MaterialAlertDialogBuilder msg = new MaterialAlertDialogBuilder
                        (MainActivity.this);
                msg.setMessage("Agregando registro nuevo").setTitle("MySql")
                        .setIcon(R.drawable.ic_info).create().show();
                 */

            }
        });

        // Instance the RequestQueue.
        rq = Volley.newRequestQueue(this);

        //Instancia de los datos
        listDatos = new ArrayList<String>();

        ConsultarRegistro();
    }

    //Método para capturar los datos de los EditText y validarlos
    private void CapturarDatos() {

        //Capturar EditText
        String sisto = mTxtSisto.getText().toString();
        String diasto = mTxtDiasto.getText().toString();

        //Validar
        if (!sisto.isEmpty() && !diasto.isEmpty()) {

            //Agregar registro
            AgregarRegistro(sisto, diasto);

        } else {

            //Usuario
            MaterialAlertDialogBuilder err = new MaterialAlertDialogBuilder(MainActivity.this);
            err.setMessage("Los campos Sistólica y/o Diastólica no pueden estar vacíos.")
                    .setTitle("Error").setIcon(R.drawable.ic_err).create().show();
        }
    }

    //Método para agregar registro por medio de Volley GET
    private void AgregarRegistro(String sisto, String diasto) {

        //Constructor http get
        String edoSelect = "NA";
        String url = "http://sylkaventas.ddns.net/bp_insert_blood.php?sisto="
                + sisto +"&diasto=" + diasto + "&edo=" + edoSelect;

        //Jason Request
        JsonObjectRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null,
                this, this);
        //Add Jason Rquest to Request Que
        rq.add(jrq);

    }

    //Método para realizar la consulta al PHP del servidor
    private void ConsultarRegistro() {

        //Construimos la url del servidor con los campos string necesarios
        String url = "http://sylkaventas.ddns.net/get_blood.php";

        //Jason Request
        JsonObjectRequest consultaRq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

                        //Concat data
                        String data = "          " + created + " | Sistólica : " + sisto
                                + " / Diastólica : " + diasto + " | ID: " + id;

                        //Agregamos al Recycler View
                        listDatos.add(data);
                    }

                    //Cargamos los datos por medio del adaptador
                    AdapterDatos adapter = new AdapterDatos(listDatos);
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Validamos opciones
        switch (item.getItemId()) {
            case R.id.Menu_Registrar:
                break;

            case id.Menu_Consulta:
                startActivity(new Intent(MainActivity.this , ConsultarActivity.class));
                finish();
                break;

            case id.Menu_Enviar:
                startActivity(new Intent(MainActivity.this, EnviarActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //Usuario
        MaterialAlertDialogBuilder err = new MaterialAlertDialogBuilder(MainActivity.this);
        err.setMessage("No se pudo registrar").setTitle("MySql Error")
                .setIcon(R.drawable.ic_err).create().show();

    }

    @Override
    public void onResponse(JSONObject response) {

        //Usuario
        MaterialAlertDialogBuilder msg = new MaterialAlertDialogBuilder
                (MainActivity.this);
        msg.setMessage("Registro Agregado").setTitle("MySql")
                .setIcon(R.drawable.ic_ok).create().show();

        //Clear Text
        Limpiar();

        //Espera de 2 seg.
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Consultar Base de datos
        ConsultarRegistro();
    }

    //Se encarga de limpiar los EditText
    private void Limpiar() {

        //Reset Edit Text
        mTxtSisto.setText("");
        mTxtDiasto.setText("");

        //Limpiar el RecyclerView
        AdapterDatos adapter = new AdapterDatos(listDatos);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);
    }
}