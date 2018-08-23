package com.example.falle.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class AnimeInformation extends AppCompatActivity {

    PullRefreshLayout layout;
    ImageView coverImage;
    ImageView poster;
    TextView synopsis;
    TextView title;
    TextView episodes;
    TextView fechaEstrenos;
    TextView fechaFins;
    String titleEn;
    String fechaEmision;
    int number;
    private RecyclerView recyclerView;
    private AdapterEpisode episode;
    private List<Episode> episodeList;
    private RequestQueue requestQueue;
    int id = 0;
    int page = 0;
    String url;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_information);

        Intent intent = getIntent();
        String synop = intent.getStringExtra("synopsis");
        String cover = intent.getStringExtra("coverImage");
        String post = intent.getStringExtra("poster");
        titleEn = intent.getStringExtra("title");
        String episode = intent.getStringExtra("episode");
        String fechaEstreno = intent.getStringExtra("fechaEstreno");
        String fechaFin = intent.getStringExtra("fechaFin");

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sunflower-Bold.ttf");
        id = intent.getIntExtra("id", 1);
        episodes = findViewById(R.id.episodeAnime);
        fechaEstrenos = findViewById(R.id.fechaEstreno);
        fechaFins = findViewById(R.id.fechaFin);
        episodes.setText("Episode: " + episode);
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
        if (cover == null) {
            coverImage.setImageResource(R.drawable.animelogo);
            Picasso.with(AnimeInformation.this).load(cover).fit().centerInside().into(coverImage);
        } else {
            Picasso.with(AnimeInformation.this).load(cover).fit().centerInside().into(coverImage);
        }

        Picasso.with(AnimeInformation.this).load(post).fit().centerInside().into(poster);


        recyclerView = findViewById(R.id.episode_recycling);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        episodeList = new ArrayList<>();
        this.episode = new AdapterEpisode(AnimeInformation.this, episodeList);
        requestQueue = Volley.newRequestQueue(AnimeInformation.this);
        refresh();
        parseJson(page);
        recyclerView.setAdapter(this.episode);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseJson(int i) {

        url = "https://kitsu.io/api/edge/anime/" + id + "/episodes?page[limit]=20&page[offset]=" + i;


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
                                if (thumbnailJson.isNull("thumbnail")) {
                                    thumbnail = null;
                                } else {
                                    thumbnail = thumbnailJson.getJSONObject("thumbnail").getString("original");
                                }
                                int season;
                                JSONObject seasonNumber = data.getJSONObject("attributes");
                                if (seasonNumber.isNull("seasonNumber")) {
                                    season = 0;
                                } else {
                                    season = Integer.valueOf(seasonNumber.getString("seasonNumber"));
                                }
                                 number= Integer.parseInt(data.getJSONObject("attributes").getString("number"));
                               fechaEmision = data.getJSONObject("attributes").getString("airdate");

                                final Episode episode = new Episode(season, number, synopsis, en_jp, thumbnail, fechaEmision);
                                episodeList.add(episode);
                                Collections.sort(episodeList, new Comparator<Episode>() {
                                    @Override
                                    public int compare(Episode c, Episode t1) {
                                        return Integer.compare(c.getNumber(), t1.getNumber());
                                    }
                                });

                                LocalDate localDate = LocalDate.now();//For reference
                                Log.i("KEY", localDate+"");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String formattedString = localDate.format(formatter);
                                String date =fechaEmision;
                                if(date.equals(formattedString)){
                                    notification();
                                }
                            }
                            episode = new AdapterEpisode(AnimeInformation.this, episodeList);
                            recyclerView.setAdapter(episode);


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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notification() {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "HOLA";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("New Episode Aired from " + titleEn + "!!")
                .setContentText("Do you want to miss it??" + " " + "Episode" + " " + number)
                .setSmallIcon(R.drawable.animelogo)
                .setChannelId(CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID, notification);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refresh() {
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

                        parseJson(page += 20);
                        layout.setRefreshing(false);


                        super.onPostExecute(s);
                    }

                }.execute();
            }
        });
    }
}

