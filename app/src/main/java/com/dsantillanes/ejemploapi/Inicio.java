package com.dsantillanes.ejemploapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    Button APIPeliculas, APIGenerador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        APIPeliculas = (Button) findViewById(R.id.btnApiPeliculas);
        APIGenerador = (Button) findViewById(R.id.btnApiGenerador);

        APIPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), PeliculasActivity.class);
                startActivity(i);
            }
        });

        APIGenerador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ThingActivity.class);
                startActivity(i);
            }
        });
    }
}
