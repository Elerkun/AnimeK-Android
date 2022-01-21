package com.example.falle.myapplication;

import android.annotation.SuppressLint;
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



public class ListManga extends Fragment {
    private PullRefreshLayout layout;
    int page = 0;
    private RecyclerView recyclerView;
    private AdapterManga adapterManga;
    private ArrayList<Manga> mangaArrayList;
    private RequestQueue requestQueue;
    private String url;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_manga, container, false);
        recyclerView = view.findViewById(R.id.recycling_list_anime);
        recyclerView.setHasFixedSize(true);
        layout = view.findViewById(R.id.swipeRefreshLayout);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        mangaArrayList = new ArrayList<>();
        adapterManga = new AdapterManga(getActivity(), mangaArrayList);
        requestQueue = Volley.newRequestQueue(getActivity());
        refresh();
        parseJson(page);

        recyclerView.setAdapter(adapterManga);
        return view;

    }

    private void parseJson(int i) {
        url = "https://kitsu.io/api/edge/manga?page[limit]=20&page[offset]=0" + i;

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
                                Collections.reverse(mangaArrayList);

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
