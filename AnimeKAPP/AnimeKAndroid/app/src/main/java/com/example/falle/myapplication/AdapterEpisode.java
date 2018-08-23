package com.example.falle.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class AdapterEpisode extends RecyclerView.Adapter<AdapterEpisode.AnimeViewHolder>{

    private Context context;
    private List<Episode> episodes;



    public AdapterEpisode(Context context,List<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public AdapterEpisode.AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.episode_recycleview,parent,false);
        return new AdapterEpisode.AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEpisode.AnimeViewHolder holder, final int position) {
        final Episode e =episodes.get(position);
        final int season = e.getSeason();
        final int episode = e.getNumber();
        final String synopsis = e.getSynopsis();
        final String en_jp = e.getEn_jp();
        final String thumbnail = e.getThumbnail();
        final String fechaEstreno = e.getFechaEstreno();
        holder.season.setText("Season: " + season);
        holder.episode.setText("Episode: " + episode);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EpisodeInformation.class);
                intent.putExtra("season", season);
                intent.putExtra("episode", episode);
                intent.putExtra("synopsis", synopsis);
                intent.putExtra("en_jp", en_jp);
                intent.putExtra("thumbail", thumbnail);
                intent.putExtra("fechaEstreno", fechaEstreno);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder{
        TextView season;
        TextView episode;
        CardView cv;
        public AnimeViewHolder(View itemView) {
            super(itemView);
            season= itemView.findViewById(R.id.season);
            episode= itemView.findViewById(R.id.episodeAnime);
            cv = itemView.findViewById(R.id.cv_episode);


        }
    }
}
