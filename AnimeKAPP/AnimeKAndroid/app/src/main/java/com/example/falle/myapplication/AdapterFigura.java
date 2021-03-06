package com.example.falle.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterFigura extends  RecyclerView.Adapter<AdapterFigura.FiguraViewHolder>{

    private Context context;
    private ArrayList<Figura> figuras;


    public AdapterFigura(Context context, ArrayList<Figura> figuras) {
        this.context = context;
        this.figuras = figuras;
    }

    @NonNull
    @Override
    public AdapterFigura.FiguraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycling_view_figuras, parent, false);
        return new AdapterFigura.FiguraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterFigura.FiguraViewHolder holder, int position) {
        final Figura f = figuras.get(position);
        final String type = f.getTipo();
        final double price = f.getPrecio();
        final String nombre_personaje = f.getNombre_personaje();
        final String anime_name = f.getAnime();
        final int cantidad = f.getCantidad();
        final String imagen = f.getImagen_figura();
        final int id = f.getId();

        holder.tipo.setText("Type: " + " " + type);
        holder.precio.setText("Price: " + " " + price + " " + "$");
        holder.nombre_personaje.setText("Character name : \n " + nombre_personaje + "");
        holder.anime_name.setText("Anime: " + " " + anime_name);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Sunflower-Bold.ttf");
        holder.anime_name.setTypeface(tf);
        Picasso.with(context).load(imagen).fit().centerInside().into(holder.imagen_url);
        if (cantidad <= 0){
            holder.cantidad.setTypeface(tf);
            holder.cantidad.setTextColor(Color.RED);
            holder.cantidad.setText("Stock: " + " " + cantidad);


        }else{
            holder.cantidad.setTypeface(tf);
            holder.cantidad.setTextColor(Color.GREEN);
            holder.cantidad.setText("Stock: " + " " + cantidad);




        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("how many T-Shirt do you want?");
                final EditText editText = new EditText(context);


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(lp);
                builder.setView(editText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String cant = editText.getText().toString();
                        if(Integer.parseInt(cant )>cantidad){
                            Toast.makeText(context,"Stock not available, soon we´ll get new for you!", Toast.LENGTH_SHORT).show();

                        }else {
                            final RequestQueue requestQueue;
                            requestQueue = Volley.newRequestQueue(context);
                            final JSONObject jsonObject = new JSONObject();
                            try {

                                jsonObject.put("id", id);
                                jsonObject.put("tipo", type);
                                jsonObject.put("precio", price);
                                jsonObject.put("talla", nombre_personaje);
                                jsonObject.put("anime", anime_name);
                                jsonObject.put("cantidad", cantidad- Integer.parseInt(cant));
                                jsonObject.put("imagen_camiseta", imagen);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final String url = "http://192.168.1.132:8080/Camiseta";
                            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }

                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json");
                                    return headers;
                                }
                                @Override
                                public String getBodyContentType() {
                                    return "application/json";
                                }


                            };
                            requestQueue.add(request);

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Do you want your bill?");
                            final String[] sesion = {"Yes", "No"};
                            builder.setItems(sesion, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i == 0) {

                                        String[] TO = {"thejoncelito07.jg@gmail.com"};
                                        String[] CC = {"thejoncelito07.jg@gmail.com"};
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setData(Uri.parse("mailto:"));
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_EMAIL, TO);
                                        intent.putExtra(Intent.EXTRA_CC, CC);
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "Your purchase");
                                        intent.putExtra(Intent.EXTRA_TEXT, " T-Shirt: " + anime_name + " " + "Prize" + " "
                                                + price + " " + "Size" + " " + nombre_personaje);
                                        context.startActivity(Intent.createChooser(intent, "Send mail..."));
                                        Toast.makeText(context, "Sending bill", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                            builder.show();
                        }

                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.setCancelable(true);
                    }
                });


                builder.show();
            }
        });
        }



    }


    @Override
    public int getItemCount() {
        return figuras.size();
    }
    public class FiguraViewHolder extends RecyclerView.ViewHolder{
        TextView tipo;
        TextView precio;
        TextView nombre_personaje;
        TextView anime_name;
        TextView cantidad;
        ImageView imagen_url;
        CardView cv;



        public FiguraViewHolder(View itemView) {
            super(itemView);
            tipo= itemView.findViewById(R.id.tipo_figura);
            precio=itemView.findViewById(R.id.precio);
            nombre_personaje = itemView.findViewById(R.id.nombre_personaje);
            anime_name=itemView.findViewById(R.id.anime_store);
            cv = itemView.findViewById(R.id.cv_store);
            cantidad = itemView.findViewById(R.id.Cantidad);
            imagen_url = itemView.findViewById(R.id.imagen);
        }
    }
}
