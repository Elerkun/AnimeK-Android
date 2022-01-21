package com.example.falle.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class AdapterAnime extends RecyclerView.Adapter<AdapterAnime.AnimeViewHolder> {
    private Context context;
    private ArrayList<Anime> animes;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference = database.child("Users/animes");


    public AdapterAnime(Context context, ArrayList<Anime> animes) {
        this.context = context;
        this.animes = animes;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new AnimeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final AnimeViewHolder holder, final int position) {
        final Anime a = animes.get(position);
        final String posterImage = a.getPoster();
        final String en_jp = a.getEn_jp();
        final String ja_jp = a.getJa_jp();
        final int id = a.getId();
        final String episode = a.getEpisode();
        final String fechaEstreno = a.getFechaEstreno();
        final String fechaFin = a.getFechaFin();
        final String user = a.getUser();
        



        holder.en_jp.setText(en_jp);
        holder.ja_jp.setText("Titulo Japonés " + ja_jp);
        Picasso.with(context).load(posterImage).fit().centerInside().into(holder.posterImage);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(context, AnimeInformation.class);
                    intent.putExtra("synopsis", a.getSynopsis());
                    intent.putExtra("coverImage", a.getCoverImage());
                    intent.putExtra("poster", posterImage);
                    intent.putExtra("title", en_jp);
                    intent.putExtra("id", id);
                    intent.putExtra("episode", episode);
                    intent.putExtra("fechaEstreno", fechaEstreno);
                    intent.putExtra("fechaFin", fechaFin);
                    context.startActivity(intent);


            }
        });

        holder.favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(final LikeButton likeButton) {
                final Query query = databaseReference.orderByChild("en_jp").equalTo(en_jp);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(!dataSnapshot.exists()){
                           databaseReference.push().setValue(a);
                           Toast toast = Toast.makeText(context, "The anime " + en_jp + " has been save on favorites", Toast.LENGTH_SHORT);
                           toast.show();
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                SharedPreferences.Editor editor = context.getSharedPreferences("com.example.falle.myapplication", MODE_PRIVATE).edit();
                editor.putBoolean("favorite_OnOff"+ position, true);
                editor.apply();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Query query = databaseReference.orderByChild("en_jp").equalTo(en_jp);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            dataSnapshot1.getRef().removeValue();
                            Toast toast = Toast.makeText(context, "The anime " + en_jp + " has been remove from favorites", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SharedPreferences.Editor editor = context.getSharedPreferences("com.example.falle.myapplication", MODE_PRIVATE).edit();
                editor.putBoolean("favorite_OnOff"+ position, false);
                editor.apply();


            }
        });

        SharedPreferences sharedPrefs = context.getSharedPreferences("com.example.falle.myapplication", MODE_PRIVATE);
        holder.favorite.setLiked(sharedPrefs.getBoolean("favorite_OnOff" + position, true));



    }
    @Override
    public int getItemCount() {
        return animes.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;
        public TextView en_jp;
        public TextView ja_jp;
        CardView cv;
        LikeButton favorite;

        public AnimeViewHolder(View itemView ) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage);
            en_jp = itemView.findViewById(R.id.en_jp);
            ja_jp = itemView.findViewById(R.id.ja_jp);
            cv = itemView.findViewById(R.id.cv);
            favorite = itemView.findViewById(R.id.toogle);




        }
    }
}

