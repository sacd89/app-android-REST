package com.dsantillanes.ejemploapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PeliculaAdd extends AppCompatActivity {

    EditText addNombre, addDescripcion, addYear, addGenero, addDirector, addActor;
    String nombre,descripcion,genero,director,actor;
    Integer year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addNombre = (EditText) findViewById(R.id.addNombre);
        addDescripcion = (EditText) findViewById(R.id.addDescripcion);
        addYear = (EditText) findViewById(R.id.addYear);
        addGenero = (EditText) findViewById(R.id.addGenero);
        addActor = (EditText) findViewById(R.id.addActor);
        addDirector = (EditText) findViewById(R.id.addDirector);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = addNombre.getText().toString();
                descripcion = addDescripcion.getText().toString();
                year = Integer.parseInt(addYear.getText().toString());
                genero = addGenero.getText().toString();
                actor = addActor.getText().toString();
                director = addDirector.getText().toString();
                addPelicula(getBaseContext(), nombre, descripcion, year, genero, director, actor);

            }
        });
    }

    public void addPelicula(final Context context, final String nombre, final String descripcion, final int year, final String genero, final String director, final String actor){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://lit-bayou-34177.herokuapp.com/createPeliculas", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent i = new Intent(context, PeliculasActivity.class);
                startActivity(i);
                finish();
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
                Log.d("Error.Response", String.valueOf(error));
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("nombre", nombre);
                params.put("descripcion",descripcion);
                params.put("year", String.valueOf(year));
                params.put("genero",genero);
                params.put("director",director);
                params.put("actor",actor);

                return params;
            }

        };
        queue.add(sr);
    }

}
