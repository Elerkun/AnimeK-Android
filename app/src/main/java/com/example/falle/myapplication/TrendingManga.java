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


public class TrendingManga extends Fragment {
        private RecyclerView recyclerView;
        private AdapterManga adapterManga;
        private ArrayList<Manga> mangaArrayList;
        private RequestQueue requestQueue;


        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_trending_series, container, false);
            recyclerView = view.findViewById(R.id.recycling_view);
            recyclerView.setHasFixedSize(true);


            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            mangaArrayList = new ArrayList<>();
            adapterManga = new AdapterManga(getActivity(), mangaArrayList);
            requestQueue = Volley.newRequestQueue(getActivity());

            parseJson();

            recyclerView.setAdapter(adapterManga);
            return view;

        }

        private void parseJson() {
            String url = "https://kitsu.io/api/edge/trending/manga";

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
                                    if(ja_jp==null){
                                        ja_jp="";
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
                                }
                                adapterManga = new AdapterManga(getActivity(), mangaArrayList);
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

}
