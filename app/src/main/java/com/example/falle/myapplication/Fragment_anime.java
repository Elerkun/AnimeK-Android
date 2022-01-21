package com.example.falle.myapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class Fragment_anime extends Fragment {
    private RecyclerView recyclerView;
    private AdapterAnime adapterAnime;
    private ArrayList<Anime> animeArrayList;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Anime a;
    String name;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime, container, false);
        recyclerView = view.findViewById(R.id.recycling_view_anime);
        recyclerView.setHasFixedSize(true);
        name = getArguments().getString("name");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);




        animeArrayList = new ArrayList<>();
        adapterAnime = new AdapterAnime(getActivity(), animeArrayList);
        firebaseFavoritos();
        recyclerView.setAdapter(adapterAnime);


        return view;


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void firebaseFavoritos() {
        database.child("Users/animes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                a = dataSnapshot.getValue(Anime.class);
                if(a.getUser().equals(name)){
                    Log.i("key", a.getEn_jp() + " " + a.getCoverImage() + " " + a.getJa_jp() + " " + a.getEpisode() + "" + a.getFechaEstreno() + "" + a.getFechaFin() + "" + a.getId() + "" + a.getPoster() + "" + a.getSynopsis());
                    Anime anime = new Anime(a.getId(), a.getEn_jp(), a.getJa_jp(), a.getPoster(), a.getSynopsis(), a.getCoverImage(), a.getEpisode(), a.getFechaEstreno(), a.getFechaFin(), a.getUser());
                    animeArrayList.add(anime);
                    adapterAnime = new AdapterAnime(getActivity(), animeArrayList);
                    recyclerView.setAdapter(adapterAnime);
                }


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}


