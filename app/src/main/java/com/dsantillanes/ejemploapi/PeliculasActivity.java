package com.dsantillanes.ejemploapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dsantillanes.ejemploapi.models.Pelicula;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeliculasActivity extends AppCompatActivity {

    ListView lvPeliculas;
    ArrayList<Pelicula> peliculas = new ArrayList<Pelicula>();
    MyAdapter myAdapter;
    static Pelicula pelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), PeliculaAdd.class);
                startActivity(i);
                finish();
            }
        });

        getPeliculas();

        lvPeliculas = (ListView) findViewById(R.id.listViewPeliculas);
        myAdapter = new MyAdapter();
        lvPeliculas.setAdapter(myAdapter);
        lvPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), PeliculaDetail.class);

                i.putExtra("nombre", peliculas.get(position).getNombre());
                i.putExtra("descripcion", peliculas.get(position).getDescripcion());
                i.putExtra("year", peliculas.get(position).getYear());
                i.putExtra("genero", peliculas.get(position).getGenero());
                i.putExtra("actor", peliculas.get(position).getActor());
                i.putExtra("director", peliculas.get(position).getDirector());
                i.putExtra("_id", peliculas.get(position).get_id());
                startActivity(i);
                finish();
            }
        });
        lvPeliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasActivity.this);
                builder.setMessage("Desea eliminar la pelicula seleccionada?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                eliminarPelicula(peliculas.get(position).get_id());
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });
    }

    public void getPeliculas(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lit-bayou-34177.herokuapp.com/getPeliculas";
        JsonArrayRequest stringRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length();i++){
                            try {
                                JSONObject jsonObject =response.getJSONObject(i);
                                pelicula = new Pelicula();

                                Gson gson = new Gson();
                                pelicula = gson.fromJson(new JsonParser().parse(jsonObject.toString()), Pelicula.class);
                                peliculas.add(pelicula);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("234552341" + error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void eliminarPelicula(String idPelicula){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lit-bayou-34177.herokuapp.com/eliminarPelicula/"+ idPelicula;
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getBaseContext(), PeliculasActivity.class);
                        startActivity(i);
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.

                    }
                }
        );
        queue.add(dr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.peliculas_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reloadPeliculas:
                Intent i = new Intent(getBaseContext(), PeliculasActivity.class);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter(){}

        @Override
        public int getCount() {
            return peliculas.size();
        }

        @Override
        public Object getItem(int position) {
            return peliculas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pelicula contacto = peliculas.get(position);

            convertView = getLayoutInflater().inflate(R.layout.celda, null);


            TextView txtNombre = (TextView) convertView.findViewById(R.id.lblNombre);

            txtNombre.setText(contacto.getNombre());

            return convertView;
        }
    }

}
