package com.example.falle.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



public class Category extends Fragment {
    private PullRefreshLayout layout;
    int page = 0;
    private RecyclerView recyclerView;
    private AdapterAnime adapterAnime;
    private ArrayList<Anime> animeArrayList;
    private RequestQueue requestQueue;
    private String url;
    String category;
    String name;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.category_recycling);
        recyclerView.setHasFixedSize(true);
        layout = view.findViewById(R.id.swipeRefreshLayout);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        Bundle bundle = this.getArguments();
        name=bundle.getString("name");
        if (bundle != null) {
            category = bundle.getString("category");
        }

        animeArrayList = new ArrayList<>();
        adapterAnime = new AdapterAnime(getActivity(), animeArrayList);
        requestQueue = Volley.newRequestQueue(getActivity());
        refresh();
        parseJson(page, category);

        recyclerView.setAdapter(adapterAnime);
        return view;

    }

    private void parseJson(int i, final String category) {
        url = "https://kitsu.io/api/edge/anime?filter[genres]=" + category + "&page[limit]=20&page[offset]= " + i;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonObject) {
                        try {
                            JSONArray jsonArray = JsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String cover;
                                String en_jp;
                                String ja=null;
                                JSONObject data = jsonArray.getJSONObject(i);
                                JSONObject canonical = data.getJSONObject("attributes");
                                if(canonical.isNull("canonicalTitle")){
                                    en_jp=null;
                                }else{
                                    en_jp = canonical.getString("canonicalTitle");
                                }

                                JSONObject ja_jp = data.getJSONObject("attributes");
                                if(ja_jp.getJSONObject("titles").isNull("ja_jp")){
                                    ja=null;
                                }else{
                                    ja = canonical.getJSONObject("titles").getString("ja_jp");
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
                                String episode = String.valueOf(data.getJSONObject("attributes").getString("episodeCount"));
                                String fechaEstreno = data.getJSONObject("attributes").getString("startDate");
                                String fechaFin = data.getJSONObject("attributes").getString("endDate");


                                Anime anime = new Anime(id, en_jp, ja, poster, synopsis, cover, episode, fechaEstreno, fechaFin, name);
                                animeArrayList.add(anime);
                                Collections.reverse(animeArrayList);

                            }
                            adapterAnime = new AdapterAnime(getActivity(), animeArrayList);
                            recyclerView.setAdapter(adapterAnime);


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

                        parseJson(page += 20,category);
                        layout.setRefreshing(false);


                        super.onPostExecute(s);
                    }

                }.execute();
            }
        });

    }
}
