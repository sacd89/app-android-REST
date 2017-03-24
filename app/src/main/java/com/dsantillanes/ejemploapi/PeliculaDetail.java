package com.dsantillanes.ejemploapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class PeliculaDetail extends AppCompatActivity{


//    TextView txtNombre, txtDescripcion, txtAnio, txtGenero, txtDirector, txtActor;
    EditText edNombre, edDescripcion, edYear, edGenero, edDirector, edActor;
    Button editarGuardar;
    String nombre, descripcion, genero, director, actor, _id;
    Integer year;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombre = getIntent().getStringExtra("nombre");
        descripcion = getIntent().getStringExtra("descripcion");
        year = getIntent().getIntExtra("year", 0);
        genero = getIntent().getStringExtra("descripcion");
        director = getIntent().getStringExtra("director");
        actor = getIntent().getStringExtra("actor");
        _id = getIntent().getStringExtra("_id");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                txtNombre.setVisibility(View.GONE);
//                edNombre.setVisibility(View.VISIBLE);
                edNombre.setEnabled(true);
//                txtDescripcion.setVisibility(View.GONE);
//                edDescripcion.setVisibility(View.VISIBLE);
                edDescripcion.setEnabled(true);
//                txtAnio.setVisibility(View.GONE);
//                edYear.setVisibility(View.VISIBLE);
                edYear.setEnabled(true);
//                txtGenero.setVisibility(View.GONE);
//                edGenero.setVisibility(View.VISIBLE);
                edGenero.setEnabled(true);
//                txtDirector.setVisibility(View.GONE);
//                edDirector.setVisibility(View.VISIBLE);
                edDirector.setEnabled(true);
//                txtActor.setVisibility(View.GONE);
//                edActor.setVisibility(View.VISIBLE);
                edActor.setEnabled(true);
                fab.setVisibility(View.GONE);
                editarGuardar.setVisibility(View.VISIBLE);

            }
        });

//        txtNombre = (TextView) findViewById(R.id.txtNombre);
//        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
//        txtAnio = (TextView) findViewById(R.id.txtAno);
//        txtGenero = (TextView) findViewById(R.id.txtGenero);
//        txtDirector = (TextView) findViewById(R.id.txtDirector);
//        txtActor = (TextView) findViewById(R.id.txtActor);

        edNombre = (EditText) findViewById(R.id.edNombre);
        edDescripcion = (EditText) findViewById(R.id.edDescripcion);
        edYear = (EditText) findViewById(R.id.edYear);
        edGenero = (EditText) findViewById(R.id.edGenero);
        edDirector = (EditText) findViewById(R.id.edDirector);
        edActor = (EditText) findViewById(R.id.edActor);


//        txtNombre.setText(nombre);
//        txtDescripcion.setText(descripcion);
//        txtAnio.setText(String.valueOf(year));
//        txtGenero.setText(genero);
//        txtDirector.setText(director);
//        txtActor.setText(actor);
        edNombre.setText(nombre);
        edDescripcion.setText(descripcion);
        edYear.setText(String.valueOf(year));
        edGenero.setText(genero);
        edDirector.setText(director);
        edActor.setText(actor);

        editarGuardar = (Button) findViewById(R.id.btnEditarGuardar);
        editarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = edNombre.getText().toString();
                descripcion = edDescripcion.getText().toString();
                year = Integer.parseInt(edYear.getText().toString());
                genero = edGenero.getText().toString();
                director = edDirector.getText().toString();
                actor = edActor.getText().toString();
                modifyPelicula(getBaseContext(), nombre, descripcion, year, genero, director, actor, _id);
            }
        });
    }

    public void modifyPelicula(final Context context, final String nombre, final String descripcion, final int year, final String genero, final String director, final String actor, String idPelicula){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.PUT,"https://lit-bayou-34177.herokuapp.com/modificarPelicula/"+ idPelicula, new Response.Listener<String>() {
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
