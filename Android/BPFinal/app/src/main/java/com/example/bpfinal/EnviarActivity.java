package com.example.bpfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EnviarActivity extends AppCompatActivity {

    //Instance activity elements
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);

        //Reference activity elements
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setSubtitle("César López (C) 2020");

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
                startActivity(new Intent(EnviarActivity.this , MainActivity.class));
                finish();
                break;

            case R.id.Menu_Consulta:
                startActivity(new Intent(EnviarActivity.this, EnviarActivity.class));
                finish();
                break;

            case R.id.Menu_Enviar:
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}