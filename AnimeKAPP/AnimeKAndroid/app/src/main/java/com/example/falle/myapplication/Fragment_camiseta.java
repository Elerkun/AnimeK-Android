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
import android.widget.Button;
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


public class Fragment_camiseta extends Fragment {
    Button button;
    private RecyclerView recyclerView;
    private AdapterCamiseta adapterCamiseta;
    private ArrayList<Camiseta> camisetaArrayList;
    private RequestQueue requestQueue;
    String id;
    String type;
    double price;
    private MenuItem searchItem;
    private SearchView searchView;
    String size;
    String anime_name;
    String cantidad;
    String imagen;
    String anime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_camiseta, container, false);
        recyclerView = view.findViewById(R.id.store_recycling);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        camisetaArrayList = new ArrayList<>();
        this.adapterCamiseta = new AdapterCamiseta(getActivity(), camisetaArrayList);
        requestQueue = Volley.newRequestQueue(getActivity());
        searchCamisetas();
        recyclerView.setAdapter(adapterCamiseta);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.list_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Searh by... ?");
                final String[] sesion = {"Anime", "Size"};
                builder.setItems(sesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            final AlertDialog.Builder builderEdiex = new AlertDialog.Builder(getActivity());
                            final EditText editText = new EditText(getActivity());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            editText.setLayoutParams(lp);
                            if(i==0){

                                builderEdiex.setTitle("Put your " + sesion[0]);
                                builderEdiex.setView(editText);
                                builderEdiex.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        listCamisetas(editText.getText().toString());

                                    }
                                });
                                builderEdiex.show();

                            }
                            if(i==1){
                                builderEdiex.setTitle("Put your " + sesion[1]);
                                builderEdiex.setView(editText);
                                builderEdiex.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        listSize(editText.getText().toString());

                                    }
                                });
                                builderEdiex.show();

                            }


                        }


                });
                builder.show();

            }
        });


        return view;

    }





    private void searchCamisetas() {
        final String url = "http://192.168.1.130:8080/Camiseta/findAll";
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
                                String talla = data.getString("talla");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_camiseta");
                                Camiseta c = new Camiseta(id, tipo, precio, talla, anime, cantidad, imagen_url);
                                camisetaArrayList.add(c);
                            }
                            adapterCamiseta = new AdapterCamiseta(getActivity(), camisetaArrayList);
                            recyclerView.setAdapter(adapterCamiseta);


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
    private void listCamisetas(String anime) {
        final String url = "http://192.168.1.130:8080/Camiseta/anime/" + anime;
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
                                String talla = data.getString("talla");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_camiseta");
                                Camiseta c = new Camiseta(id, tipo, precio, talla, anime, cantidad, imagen_url);
                                camisetaArrayList.add(0,c);
                            }
                            adapterCamiseta = new AdapterCamiseta(getActivity(), camisetaArrayList);
                            recyclerView.setAdapter(adapterCamiseta);


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
    private void listSize(String size) {
        final String url = "http://192.168.1.130:8080/Camiseta/talla/" + size;
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
                                String talla = data.getString("talla");
                                int cantidad = data.getInt("cantidad");
                                String anime = data.getString("anime");
                                String imagen_url = data.getString("imagen_camiseta");
                                Camiseta c = new Camiseta(id, tipo, precio, talla, anime, cantidad, imagen_url);
                                camisetaArrayList.add(0,c);
                            }
                            adapterCamiseta = new AdapterCamiseta(getActivity(), camisetaArrayList);
                            recyclerView.setAdapter(adapterCamiseta);


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
