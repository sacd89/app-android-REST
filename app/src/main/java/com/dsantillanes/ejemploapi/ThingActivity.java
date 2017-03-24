package com.dsantillanes.ejemploapi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dsantillanes.ejemploapi.models.Pelicula;
import com.dsantillanes.ejemploapi.models.Thing;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThingActivity extends AppCompatActivity {

    ListView lvThings;
    ArrayList<Thing> things = new ArrayList<Thing>();
    MyAdapter myAdapter;
    Thing thing;
    String newThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ThingActivity.this);
                builder.setTitle("Add Thing");

                final EditText input = new EditText(ThingActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newThing = input.getText().toString();
                        addThing(getBaseContext(),newThing);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        getThings();
        lvThings = (ListView) findViewById(R.id.listViewThings);
        myAdapter = new MyAdapter();
        lvThings.setAdapter(myAdapter);
        lvThings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThingActivity.this);
                builder.setMessage("Desea eliminar el elemento seleccionado?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                eliminarThing(things.get(position).get_id());
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

    public void getThings(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://safe-shore-82586.herokuapp.com/api/things"; //getString(R.string.master)+ getString(R.string.login);
        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("ENTRE AQUI");
                        for (int i=0; i<response.length();i++){
                            System.out.println("ENTRE FOR");
                            try {
                                JSONObject jsonObject =response.getJSONObject(i);
                                System.out.println("111111111111111" + jsonObject);
                                thing = new Thing();

                                Gson gson = new Gson();
                                thing = gson.fromJson(new JsonParser().parse(jsonObject.toString()), Thing.class);
                                things.add(thing);

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

    public void eliminarThing(String idThing){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://safe-shore-82586.herokuapp.com/api/things/delete/"+ idThing;
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getBaseContext(), ThingActivity.class);
                        startActivity(i);
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR"+error);
                        // error.

                    }
                }
        );
        queue.add(dr);
    }

    public void addThing(final Context context, final String thing){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://safe-shore-82586.herokuapp.com/api/things/post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent i = new Intent(context, ThingActivity.class);
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
                params.put("name", thing);

                return params;
            }

        };
        queue.add(sr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.things_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reloadThings:
                Intent i = new Intent(getBaseContext(), ThingActivity.class);
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
            return things.size();
        }

        @Override
        public Object getItem(int position) {
            return things.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Thing thing = things.get(position);

            convertView = getLayoutInflater().inflate(R.layout.celda, null);


            TextView txtNombre = (TextView) convertView.findViewById(R.id.lblNombre);

            txtNombre.setText(thing.getName());

            return convertView;
        }
    }

}
