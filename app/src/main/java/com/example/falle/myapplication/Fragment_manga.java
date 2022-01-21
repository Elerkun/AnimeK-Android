package com.example.falle.myapplication;


import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * Created by Falle on 7/9/2018.
 */

public class Fragment_manga extends Fragment {
    private RecyclerView recyclerView;
    private AdapterManga adapterManga;
    private ArrayList<Manga> mangaArrayList;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_manga, container, false);
        recyclerView = view.findViewById(R.id.recycling_view_manga);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        mangaArrayList = new ArrayList<>();
        adapterManga = new AdapterManga(getActivity(), mangaArrayList);
        firebaseFavoritos();
        recyclerView.setAdapter(adapterManga);


        return view;




    }
    public void firebaseFavoritos(){
        database.child("Favoritos_Manga").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Manga m = dataSnapshot.getValue(Manga.class);
                Log.i("key", m.getEn_jp()+ " " + m.getCoverImage() + " " + m.getEn() + " " + m.getChapterCount() + "" + m.getStartDate() + "" + m.getEndDate() + "" + m.getId() + ""+  m.getPoster() +  "" + m.getSynopsis() );
                Manga manga = new Manga(m.getId(),m.getEn_jp(),m.getEn(),m.getPoster()  , m.getSynopsis() , m.getCoverImage(), m.getChapterCount() ,m.getStartDate() , m.getEndDate() );
                mangaArrayList.add(manga);
                adapterManga = new AdapterManga(getActivity(), mangaArrayList);
                recyclerView.setAdapter(adapterManga);
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
