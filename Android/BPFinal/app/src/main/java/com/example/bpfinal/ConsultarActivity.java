package com.example.bpfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.format;
import static java.text.DateFormat.*;

public class ConsultarActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    //Globales
    String iniPer = "";
    String finPer = "";
    String TAG = "LOG";
    ArrayList<String> listDatos;

    //Instance activity elements
    Toolbar mToolbar;
    Button mBtnPicker;
    RecyclerView recycler;
    FloatingActionButton mBtnSearch;

    //Implementación de objetos de Volley
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        //Reference activity elements
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setSubtitle("César López (C) 2020");
        mBtnPicker = findViewById(R.id.BtnPicker);
        mBtnSearch = findViewById(R.id.BtnSearch);
        recycler = findViewById(R.id.ListaSearch);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Material DatePicker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        //Options Material DatePicker
        builder.setTitleText("Selecciona el Periódo");
        //Instance Material DatePicker
        MaterialDatePicker materialDatePicker = builder.build();

        //Listener Botones
        mBtnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Muestra el Material DatePicker
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Conversión para el rango de fechas, se puede convertir en una función
                String select = String.valueOf(materialDatePicker.getSelection());

                //Método para obtener el rango de fechas
                GetRange(select);

                //Cambia texto del botón
                mBtnPicker.setText(iniPer + " - " + finPer);

                /*Usr
                MaterialAlertDialogBuilder msg = new MaterialAlertDialogBuilder(ConsultarActivity.this);
                msg.setMessage("I: " + iniPer + " F: " + finPer).setTitle("Periódo")
                        .setIcon(R.drawable.ic_info).create().show();
                 */
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Validación
                if (!iniPer.isEmpty() || !finPer.isEmpty()) {

                    //Método para realizar la consulta
                    ConsultarPeriodo(iniPer, finPer);

                } else {

                    //Usr
                    MaterialAlertDialogBuilder msg = new MaterialAlertDialogBuilder(ConsultarActivity.this);
                    msg.setTitle("Error");
                    msg.setIcon(R.drawable.ic_err);
                    msg.setMessage("Debe seleccionar periódo inicial y/o final");
                    msg.create().show();

                }
            }
        });

        // Instance the RequestQueue.
        rq = Volley.newRequestQueue(this);

        //Instancia de los datos
        listDatos = new ArrayList<String>();
    }

    //Se encarga de realizar la consulta JSON en un rango de fechas
    private void ConsultarPeriodo(String i, String f) {

        //Construimos la url del servidor con los campos string necesarios
        String url = "http://sylkaventas.ddns.net/get_bp_period.php?ini=";
        url += i + "&fin=" + f;

        JsonObjectRequest consultaRq = new JsonObjectRequest(Request.Method.GET, url,
                null, this, this);

        //Add Jason Rquest to Request Que
        rq.add(consultaRq);
    }

    //Se encarga de convertir la selección del DatePicker a variables tipo string
    private void GetRange(String select) {

        //Replace de los caracteres no necesarios
        select = select.replace("Pair", "");
        select = select.replace("{", "");
        select = select.replace("}", "");

        String[] periodo = select.split(" ");
        long inicio = Long.parseLong(periodo[0]);
        long fin = Long.parseLong(periodo[1]);
        iniPer = new SimpleDateFormat("yyyy/MM/dd").format(inicio);
        finPer = new SimpleDateFormat("yyyy/MM/dd").format(fin);
        iniPer = iniPer.replace("/", "-");
        finPer = finPer.replace("/", "-");
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
                startActivity(new Intent(ConsultarActivity.this , MainActivity.class));
                finish();
                break;

            case R.id.Menu_Consulta:
                break;

            case R.id.Menu_Enviar:
                startActivity(new Intent(ConsultarActivity.this, EnviarActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //Usuario
        MaterialAlertDialogBuilder err = new MaterialAlertDialogBuilder(ConsultarActivity.this);
        err.setMessage("No se pudo consultar periódo seleccionado").setTitle("MySql Error")
                .setIcon(R.drawable.ic_err).create().show();

    }

    @Override
    public void onResponse(JSONObject response) {

        //JSON array
        try {

            JSONArray jsonArray = response.getJSONArray("p");

            //Rutina para recorrer el arreglo
            for (int i = 0; i < jsonArray.length(); i++) {

                //Objeto JSON para recibir los key con los datos
                JSONObject p = jsonArray.getJSONObject(i);

                //Captura de datos en variables
                int id = p.getInt("blood_id");
                int sisto = p.getInt("sisto");
                int diasto = p.getInt("diasto");
                String edo = p.getString("estado");
                String created = p.getString("blood_create");

                //Concat data
                String data = "          " + created + " | Sistólica : " + sisto
                        + " / Diastólica : " + diasto + " | ID: " + id;

                //Agregamos al Recycler View
                listDatos.add(data);

                /*debug
                MaterialAlertDialogBuilder msg = new MaterialAlertDialogBuilder(ConsultarActivity.this);
                msg.setTitle("Mysql");
                msg.setMessage(data);
                msg.create().show();
                 */
            }

            //Cargamos los datos por medio del adaptador
            AdapterDatos adap = new AdapterDatos(listDatos);
            recycler.setAdapter(adap);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Re iniciar variables
        ReIniciar();
    }

    //Se encarga de reiniciar las variables y textos necesarios
    private void ReIniciar() {

        //Var
        iniPer = "";
        finPer = "";
        mBtnPicker.setText("Seleccionar Periódo");
    }
}