package com.example.falle.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_figuras extends Fragment {


    private RecyclerView recyclerView;
    private AdapterFigura adapterFigura;
    private ArrayList<Figura> figuraArrayList;
    private RequestQueue requestQueue;
    String id;
    String type;
    double price;
    private MenuItem searchItem;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment__figura, container, false);
        recyclerView = view.findViewById(R.id.store_recycling);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        figuraArrayList = new ArrayList<>();
        this.adapterFigura = new AdapterFigura(getActivity(), figuraArrayList);
        requestQueue = Volley.newRequestQueue(getActivity());
        searchFiguras();
        recyclerView.setAdapter(adapterFigura);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.list_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Searh by... ?");
                final String[] sesion = {"Anime", "Character name"};
                builder.setItems(sesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builderEditex = new AlertDialog.Builder(getActivity());
                        final EditText editText = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        editText.setLayoutParams(lp);
                        builderEditex.setTitle("Put your " + sesion[0]);
                        builderEditex.setView(editText);
                        if(i==0) {

                            builderEditex.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    listFigura(editText.getText().toString());


                                }

                            });

                        }
                        if(i==1){
                                builderEditex.setTitle("Put your " + sesion[1]);
                                builderEditex.setView(editText);
                                builderEditex.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        listName_character(editText.getText().toString());



                                    }
                                });


                            }
                        builderEditex.show();

                        }


                });
                builder.show();

            }
        });


        return view;

    }





    private void searchFiguras() {
        final String url = "http://192.168.1.132:8080/Figura/findAll";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                int id = data.getInt("id");
                                String tipo = data.getString("tipo");
                                double precio = data.getDouble("precio");
                                String nombre_personaje = data.getString("nombre_personaje");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_figura");
                                Figura f = new Figura(id, tipo, nombre_personaje, anime, cantidad,precio, imagen_url);
                                figuraArrayList.add(f);
                            }
                            adapterFigura = new AdapterFigura(getActivity(), figuraArrayList);
                            recyclerView.setAdapter(adapterFigura);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }
    private void listFigura(String anime) {
        final String url = "http://192.168.1.132:8080/Figura/anime/" + anime;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                int id = data.getInt("id");
                                String tipo = data.getString("tipo");
                                double precio = data.getDouble("precio");
                                String nombre_personaje = data.getString("nombre_personaje");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_figura");
                                Figura f = new Figura(id, tipo, nombre_personaje, anime, cantidad,precio, imagen_url);
                                figuraArrayList.add(0,f);
                            }
                            adapterFigura = new AdapterFigura(getActivity(), figuraArrayList);
                            recyclerView.setAdapter(adapterFigura);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }
    private void listName_character(String name) {
        final String url = "http://192.168.1.132:8080/Figura/nombre/" + name;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                int id = data.getInt("id");
                                String tipo = data.getString("tipo");
                                double precio = data.getDouble("precio");
                                String nombre_personaje = data.getString("nombre_personaje");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_figura");
                                Figura f = new Figura(id, tipo, nombre_personaje, anime, cantidad,precio, imagen_url);
                                figuraArrayList.set(0,f);
                            }
                            adapterFigura = new AdapterFigura(getActivity(), figuraArrayList);
                            recyclerView.setAdapter(adapterFigura);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }



}
