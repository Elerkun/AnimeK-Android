package com.example.falle.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;



public class AdapterManga extends RecyclerView.Adapter<AdapterManga.MangaViewHolder>{

    private Context context;
    private ArrayList<Manga> mangas;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference = database.child("Favoritos_Manga");
    public static final String sharedPreferences = "shared_Preferences";


    public AdapterManga(Context context, ArrayList<Manga> mangas) {
        this.context = context;
        this.mangas = mangas;
    }

    @NonNull
    @Override
    public AdapterManga.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new AdapterManga.MangaViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MangaViewHolder holder, final int position) {
        final Manga m = mangas.get(position);
        final String posterImage = m.getPoster();
        final String en_jp = m.getEn_jp();
        final String en = m.getEn();
        final int id = m.getId();
        final String episode = m.getChapterCount();
        final String fechaEstreno = m.getStartDate();
        final String fechaFin = m.getEndDate();


        holder.en_jp.setText(en);
        holder.en.setText("Titulo Japonés " + en_jp);
        Picasso.with(context).load(posterImage).fit().centerInside().into(holder.posterImage);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, MangaInformation.class);
                intent.putExtra("synopsis", m.getSynopsis());
                intent.putExtra("coverImage", m.getCoverImage());
                intent.putExtra("poster", posterImage);
                intent.putExtra("title", en);
                intent.putExtra("id", id);
                intent.putExtra("episode", episode);
                intent.putExtra("fechaEstreno", fechaEstreno);
                intent.putExtra("fechaFin", fechaFin);
                context.startActivity(intent);


            }
        });

        holder.favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Query query = databaseReference.orderByChild("en").equalTo(en);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            databaseReference.push().setValue(m);
                            Toast toast = Toast.makeText(context, "El Manga " + en_jp + " ha sido guardado en favoritos", Toast.LENGTH_SHORT);
                            toast.show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                            Toast toast = Toast.makeText(context, "El Manga " + en_jp + " ha sido eliminado de favoritos", Toast.LENGTH_SHORT);
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
        return mangas.size();
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;
        public TextView en_jp;
        public TextView en;
        CardView cv;
        LikeButton favorite;

        public MangaViewHolder(View itemView ) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage);
            en_jp = itemView.findViewById(R.id.en_jp);
            en = itemView.findViewById(R.id.ja_jp);
            cv = itemView.findViewById(R.id.cv);
            favorite = itemView.findViewById(R.id.toogle);




        }
    }
}
