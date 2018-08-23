package com.example.falle.myapplication;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class TrendingSeries extends Fragment {
    private RecyclerView recyclerView;
    private AdapterAnime adapterAnime;
    private ArrayList<Anime> animeArrayList;
    private RequestQueue requestQueue;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trending_series, container, false);
        recyclerView = view.findViewById(R.id.recycling_view);
        recyclerView.setHasFixedSize(true);
        String name=  getArguments().getString("name");


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        animeArrayList = new ArrayList<>();
        adapterAnime = new AdapterAnime(getActivity(), animeArrayList);
        requestQueue = Volley.newRequestQueue(getActivity());

        parseJson();

        recyclerView.setAdapter(adapterAnime);
        return view;

    }

    private void parseJson() {
        String url = "https://kitsu.io/api/edge/trending/anime";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonObject) {
                        try {
                            JSONArray jsonArray = JsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String en_jp = data.getJSONObject("attributes").getJSONObject("titles").getString("en_jp");
                                if(en_jp==null){
                                    en_jp="";
                                }
                                String ja_jp = data.getJSONObject("attributes").getJSONObject("titles").getString("ja_jp");
                                String poster = data.getJSONObject("attributes").getJSONObject("posterImage").getString("large");
                                String synopsis = data.getJSONObject("attributes").getString("synopsis");
                                String coverImage = data.getJSONObject("attributes").getJSONObject("coverImage").getString("large");
                                int id = Integer.parseInt(data.getString("id"));
                                String episode = String.valueOf(data.getJSONObject("attributes").getString("episodeCount"));
                                String fechaEstreno = data.getJSONObject("attributes").getString("startDate");
                                String fechaFin = data.getJSONObject("attributes").getString("endDate");

                                Anime anime = new Anime(id, en_jp, ja_jp, poster, synopsis, coverImage, episode, fechaEstreno, fechaFin,getArguments().getString("name"));
                                animeArrayList.add(anime);
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

}
