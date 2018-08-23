package com.example.falle.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MangasSearch extends AppCompatActivity {
    PullRefreshLayout layout;
    int page=0;
    private RecyclerView recyclerView;
    private AdapterManga adapterManga;
    private ArrayList<Manga> mangaArrayList;
    private RequestQueue requestQueue;
    String manga;
    private SearchView searchView;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangas_search);
        recyclerView = findViewById(R.id.manga_recycling);
        recyclerView.setHasFixedSize(true);
        layout = findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager llm = new LinearLayoutManager(MangasSearch.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mangaArrayList = new ArrayList<>();
        adapterManga = new AdapterManga(MangasSearch.this, mangaArrayList);
        requestQueue = Volley.newRequestQueue(MangasSearch.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        refresh();
        searchManga(manga,page);
        recyclerView.setAdapter(adapterManga);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manga=query;
                searchManga(manga,page);
                return true;



            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        return true;
    }
    private void searchManga(String manga, int page){
        String url = "https://kitsu.io/api/edge/manga?filter[text]=" + manga + "&page%5Blimit%5D=20&page%5Boffset%5D=" + page;


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonObject) {
                        try {
                            JSONArray jsonArray = JsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String cover;
                                String en_jp = data.getJSONObject("attributes").getJSONObject("titles").getString("en_jp");
                                String ja_jp = data.getJSONObject("attributes").getString("canonicalTitle");
                                if(en_jp==null){
                                    en_jp="No hay titulo en ingles";
                                }
                                String poster = data.getJSONObject("attributes").getJSONObject("posterImage").getString("large");
                                String synopsis = data.getJSONObject("attributes").getString("synopsis");
                                JSONObject coverImage = data.getJSONObject("attributes");
                                if( coverImage.isNull("coverImage")){
                                    cover=null;
                                }else{
                                    cover = coverImage.getJSONObject("coverImage").getString("large");
                                }
                                int id = Integer.parseInt(data.getString("id"));
                                String episode = String.valueOf(data.getJSONObject("attributes").getString("chapterCount"));
                                String fechaEstreno = data.getJSONObject("attributes").getString("startDate");
                                String fechaFin = data.getJSONObject("attributes").getString("endDate");

                                Manga manga = new Manga(id, en_jp, ja_jp, poster, synopsis, cover, episode, fechaEstreno, fechaFin);
                                mangaArrayList.add(manga);
                                Collections.reverse(mangaArrayList);
                            }
                            adapterManga = new AdapterManga(MangasSearch.this, mangaArrayList);
                            recyclerView.setAdapter(adapterManga);


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
                        searchManga(manga, page+=20);
                        layout.setRefreshing(false);


                        super.onPostExecute(s);
                    }

                }.execute();
            }
        });
    }
}
