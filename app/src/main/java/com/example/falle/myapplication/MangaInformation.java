package com.example.falle.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MangaInformation extends AppCompatActivity {

    PullRefreshLayout layout;
    ImageView coverImage;
    ImageView poster;
    TextView synopsis;
    TextView title;
    TextView episodes;
    TextView fechaEstrenos;
    TextView fechaFins;
    private RecyclerView recyclerView;
    private AdapterChapters adapterChapters;
    private List<Chapters> chaptersList;
    private RequestQueue requestQueue;
    int id = 0;
    int page =0;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_information);

        Intent intent = getIntent();
        String synop = intent.getStringExtra("synopsis");
        String cover = intent.getStringExtra("coverImage");
        String post = intent.getStringExtra("poster");
        String titleEn = intent.getStringExtra("title");
        String episode = intent.getStringExtra("episode");
        String fechaEstreno = intent.getStringExtra("fechaEstreno");
        String fechaFin = intent.getStringExtra("fechaFin");

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sunflower-Bold.ttf");
        id = intent.getIntExtra("id", 1);
        episodes = findViewById(R.id.chapterManga);
        fechaEstrenos = findViewById(R.id.fechaEstreno);
        fechaFins= findViewById(R.id.fechaFin);
        episodes.setText("Chapter: " + episode);
        fechaEstrenos.setText("Start Date: " + fechaEstreno);
        fechaFins.setText("End Date: " + fechaFin);
        coverImage = findViewById(R.id.coverImage);
        poster = findViewById(R.id.poster);
        synopsis = findViewById(R.id.synopis);
        title = findViewById(R.id.title);
        layout = findViewById(R.id.swipeRefreshLayout);
        synopsis.setText(synop);
        synopsis.setMovementMethod(new ScrollingMovementMethod());
        title.setText(titleEn);
        title.setTypeface(tf);
        if(cover==null){
            coverImage.setImageResource(R.drawable.animelogo);
            Picasso.with(MangaInformation.this).load(cover).fit().centerInside().into(coverImage);
        }else{
            Picasso.with(MangaInformation.this).load(cover).fit().centerInside().into(coverImage);
        }

        Picasso.with(MangaInformation.this).load(post).fit().centerInside().into(poster);


        recyclerView = findViewById(R.id.chapters_recycling);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        chaptersList = new ArrayList<>();
        adapterChapters = new AdapterChapters(MangaInformation.this, chaptersList);
        requestQueue = Volley.newRequestQueue(MangaInformation.this);
        refresh();
        parseJson(page);
        recyclerView.setAdapter(adapterChapters);


    }

    private void parseJson(int i) {

        url = "https://kitsu.io/api/edge/manga/" + id + "/chapters?page[limit]=20&page[offset]=" + i ;





        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonObject) {
                        try {
                            JSONArray jsonArray = JsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String thumbnail;
                                int id = Integer.valueOf(data.getString("id"));
                                String synopsis = data.getJSONObject("attributes").getString("synopsis");
                                String en_jp = data.getJSONObject("attributes").getString("canonicalTitle");
                                if(en_jp==null){
                                    en_jp="";
                                }
                                JSONObject thumbnailJson = data.getJSONObject("attributes");
                                if(thumbnailJson.isNull("thumbnail")){
                                    thumbnail=null;
                                }else{
                                    thumbnail = thumbnailJson.getJSONObject("thumbnail").getString("original");
                                }
                                int season;
                                JSONObject seasonNumber=  data.getJSONObject("attributes");
                                if(seasonNumber.isNull("volumeNumber")){
                                    season=0;
                                }else{
                                    season = Integer.valueOf(seasonNumber.getString("volumeNumber"));
                                }
                                int number = Integer.parseInt(data.getJSONObject("attributes").getString("number"));

                                final Chapters chapters = new Chapters(season, number,synopsis,en_jp,thumbnail);
                                chaptersList.add(chapters);
                                Collections.sort(chaptersList, new Comparator<Chapters>() {
                                    @Override
                                                public int compare(Chapters c, Chapters t1) {
                                        return Integer.compare(c.getNumber(), t1.getNumber());
                                    }
                                });


                            }
                            adapterChapters = new AdapterChapters(MangaInformation.this, chaptersList);
                            recyclerView.setAdapter(adapterChapters);


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
    private void refresh(){
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onRefresh() {
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {

                        parseJson(page+=20);
                        layout.setRefreshing(false);


                        super.onPostExecute(s);
                    }

                }.execute();
            }
        });
    }

}

